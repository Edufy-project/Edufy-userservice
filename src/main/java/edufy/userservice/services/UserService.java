package edufy.userservice.services;

import edufy.userservice.entities.MediaReference;
import edufy.userservice.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User registerUser(User user);
    Optional<User> getUserByUsername(String username);
    List<User> getAllUsers();
    User updateUser(User user);
    void deleteUser(Long id);
    void incrementPlayCount(String username, Long count);

    void setUserMediaHistory(Long userId, List<MediaReference> mediaHistory);
    void addMediaToUserMediaHistory(Long userId, String mediaType, Long mediaId);
    List<MediaReference> getUserMediaHistory(Long userId);

}
