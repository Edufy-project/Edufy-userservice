package edufy.userservice.services;

import edufy.userservice.entities.User;
import edufy.userservice.exceptions.InvalidUserException;
import edufy.userservice.exceptions.ResourceNotFoundException;
import edufy.userservice.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    //private static final Logger = LoggerFactory.getLogger(UserUserServiceImpl.class.getName());

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new InvalidUserException("Username is empty");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new InvalidUserException("Password is empty");
        }
        if (user.getPreferredGenres() == null || user.getPreferredGenres().isEmpty()) {
            throw new InvalidUserException("Preferred genres is empty");
        }
        if (user.getTotalPlayCount() == null ) {
            user.setTotalPlayCount((Long)0L);
        }
        userRepository.findByUsername(user.getUsername()).ifPresent(c ->{
            throw new InvalidUserException("Username already exists");
        });

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User user) {

        if (user.getId() == null) {
            throw new InvalidUserException("User ID is required for update");
        }
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", user.getId()));
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new InvalidUserException("Username cannot be empty");
        }
        if (user.getPreferredGenres() == null || user.getPreferredGenres().trim().isEmpty()) {
            throw new InvalidUserException("Preferred genres cannot be empty");
        }
        // Kontrollera om nytt användarnamn redan finns (och inte tillhör samma användare)
        userRepository.findByUsername(user.getUsername())
                .filter(u -> !u.getId().equals(user.getId()))
                .ifPresent(u -> {
                    throw new InvalidUserException("Username already taken by another user");
                });

        existingUser.setUsername(user.getUsername());
        existingUser.setPreferredGenres(user.getPreferredGenres());
        // Uppdatera lösenord om nytt skickas in
        if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
//         Uppdatera roller (valfritt – beroende på säkerhetsnivå)
//        if (user.getRoles() != null && !user.getRoles().trim().isEmpty()) {
//            existingUser.setRoles(user.getRoles());
//        }
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() ->
                new ResourceNotFoundException("User", "id", id));
        userRepository.delete(existingUser);
    }

    @Override
    public void incrementPlayCount(String username, Long count) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        user.setTotalPlayCount(Long.valueOf(user.getTotalPlayCount() + count));
        userRepository.save(user);

    }

    public User getUserOrThrow(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "UserId", userId));
    }

    @Override
    public void setUserMediaHistory(Long userId, List<Object> mediaHistory) {

        if (mediaHistory == null){
            throw new IllegalArgumentException("Can not add null Object to User MediaHistory.");
        }
        getUserOrThrow(userId).setMediaHistory(mediaHistory);
    }

    @Override
    public void addMediaToUserMediaHistory(Long userId, Object media) {
        if (media == null){
            throw new IllegalArgumentException("Can not add null Object to User MediaHistory.");
        }
        else {
            getUserOrThrow(userId).addToMediaHistory(media);
        }
    }

    @Override
    public List<Object> getUserMediaHistory(Long userId) {

        return getUserOrThrow(userId).getMediaHistory();
    }


}
