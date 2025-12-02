package edufy.userservice.services;

import edufy.userservice.entities.MediaReference;
import edufy.userservice.entities.User;
import edufy.userservice.exceptions.InvalidUserException;
import edufy.userservice.exceptions.ResourceNotFoundException;
import edufy.userservice.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceImplUnitTest {


    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void registerUser_WithValidUser_ShouldReturnSavedUser() {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        SecurityContextHolder.setContext(securityContext);

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setPreferredGenres("Rock,Pop");
        user.setTotalPlayCount(0L);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);


        User result = userService.registerUser(user);


        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("Rock,Pop", result.getPreferredGenres());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void registerUser_WithEmptyUsername_ShouldThrowInvalidUserException() {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        SecurityContextHolder.setContext(securityContext);

        User user = new User();
        user.setUsername("");
        user.setPassword("password123");
        user.setPreferredGenres("Rock,Pop");


        InvalidUserException exception = assertThrows(InvalidUserException.class, () -> {
            userService.registerUser(user);
        });
        assertEquals("Username is empty", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerUser_WithEmptyPassword_ShouldThrowInvalidUserException() {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        SecurityContextHolder.setContext(securityContext);

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("");
        user.setPreferredGenres("Rock,Pop");


        InvalidUserException exception = assertThrows(InvalidUserException.class, () -> {
            userService.registerUser(user);
        });
        assertEquals("Password is empty", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerUser_WithEmptyPreferredGenres_ShouldThrowInvalidUserException() {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        SecurityContextHolder.setContext(securityContext);

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setPreferredGenres("");


        InvalidUserException exception = assertThrows(InvalidUserException.class, () -> {
            userService.registerUser(user);
        });
        assertEquals("Preferred genres is empty", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerUser_WithExistingUsername_ShouldThrowInvalidUserException() {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        SecurityContextHolder.setContext(securityContext);

        User existingUser = new User();
        existingUser.setUsername("testuser");

        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setPassword("password123");
        newUser.setPreferredGenres("Rock,Pop");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(existingUser));


        InvalidUserException exception = assertThrows(InvalidUserException.class, () -> {
            userService.registerUser(newUser);
        });
        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerUser_WithNullTotalPlayCount_ShouldSetToZero() {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        SecurityContextHolder.setContext(securityContext);

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setPreferredGenres("Rock,Pop");
        user.setTotalPlayCount(null);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);


        User result = userService.registerUser(user);


        assertEquals(0L, result.getTotalPlayCount());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getUserByUsername_WithExistingUser_ShouldReturnUser() {

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setPreferredGenres("Rock,Pop");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));


        Optional<User> result = userService.getUserByUsername("testuser");


        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void getUserByUsername_WithNonExistingUser_ShouldReturnEmpty() {

        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.getUserByUsername("nonexistent");

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByUsername("nonexistent");
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {

        User user1 = new User();
        user1.setUsername("user1");
        User user2 = new User();
        user2.setUsername("user2");

        when(userRepository.findAll()).thenReturn(java.util.Arrays.asList(user1, user2));


        java.util.List<User> result = userService.getAllUsers();


        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals("user2", result.get(1).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void updateUser_WithValidUser_ShouldReturnUpdatedUser() {

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("oldusername");
        existingUser.setPreferredGenres("Rock");
        existingUser.setPassword("oldpassword");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("newusername");
        updatedUser.setPreferredGenres("Pop,Jazz");
        updatedUser.setPassword("newpassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUsername("newusername")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(existingUser);


        User result = userService.updateUser(updatedUser);


        assertEquals("newusername", result.getUsername());
        assertEquals("Pop,Jazz", result.getPreferredGenres());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void updateUser_WithNullId_ShouldThrowInvalidUserException() {

        User user = new User();
        user.setId(null);
        user.setUsername("testuser");
        user.setPreferredGenres("Rock");


        InvalidUserException exception = assertThrows(InvalidUserException.class, () -> {
            userService.updateUser(user);
        });
        assertEquals("User ID is required for update", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_WithNonExistingUser_ShouldThrowResourceNotFoundException() {

        User user = new User();
        user.setId(999L);
        user.setUsername("testuser");
        user.setPreferredGenres("Rock");

        when(userRepository.findById(999L)).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateUser(user);
        });
        assertTrue(exception.getMessage().contains("User"));
        assertTrue(exception.getMessage().contains("999"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_WithEmptyUsername_ShouldThrowInvalidUserException() {

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("oldusername");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("");
        updatedUser.setPreferredGenres("Rock");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        // Act & Assert
        InvalidUserException exception = assertThrows(InvalidUserException.class, () -> {
            userService.updateUser(updatedUser);
        });
        assertEquals("Username cannot be empty", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_WithEmptyPreferredGenres_ShouldThrowInvalidUserException() {

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("testuser");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("testuser");
        updatedUser.setPreferredGenres("");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));


        InvalidUserException exception = assertThrows(InvalidUserException.class, () -> {
            userService.updateUser(updatedUser);
        });
        assertEquals("Preferred genres cannot be empty", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_WithUsernameTakenByAnotherUser_ShouldThrowInvalidUserException() {
        // Arrange
        User existingUser1 = new User();
        existingUser1.setId(1L);
        existingUser1.setUsername("user1");

        User existingUser2 = new User();
        existingUser2.setId(2L);
        existingUser2.setUsername("user2");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("user2");
        updatedUser.setPreferredGenres("Rock");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser1));
        when(userRepository.findByUsername("user2")).thenReturn(Optional.of(existingUser2));


        InvalidUserException exception = assertThrows(InvalidUserException.class, () -> {
            userService.updateUser(updatedUser);
        });
        assertEquals("Username already taken by another user", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_WithExistingUser_ShouldDeleteSuccessfully() {

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));


        userService.deleteUser(1L);


        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void deleteUser_WithNonExistingUser_ShouldThrowResourceNotFoundException() {

        when(userRepository.findById(999L)).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteUser(999L);
        });
        assertTrue(exception.getMessage().contains("User"));
        assertTrue(exception.getMessage().contains("999"));
        verify(userRepository, never()).delete(any());
    }

    @Test
    void incrementPlayCount_WithExistingUser_ShouldIncrementByCount() {

        User user = new User();
        user.setUsername("testuser");
        user.setTotalPlayCount(5L);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);


        userService.incrementPlayCount("testuser", 3L);


        assertEquals(8L, user.getTotalPlayCount());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void incrementPlayCount_WithNonExistingUser_ShouldThrowResourceNotFoundException() {

        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.incrementPlayCount("nonexistent", 1L);
        });
        assertTrue(exception.getMessage().contains("User"));
        assertTrue(exception.getMessage().contains("nonexistent"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void getUserMediaHistory_WithExistingUser_ShouldReturnMediaHistory() {

        User user = new User();
        user.setId(1L);
        user.addToMediaHistory("music", 101L);
        user.addToMediaHistory("video", 202L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));


        java.util.List<MediaReference> result = userService.getUserMediaHistory(1L);


        assertEquals(2, result.size());
        assertEquals("music", result.get(0).getMediaType());
        assertEquals(101L, result.get(0).getMediaId());
        assertEquals("video", result.get(1).getMediaType());
        assertEquals(202L, result.get(1).getMediaId());
    }

    @Test
    void getUserMediaHistory_WithNonExistingUser_ShouldThrowResourceNotFoundException() {

        when(userRepository.findById(999L)).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserMediaHistory(999L);
        });
        assertTrue(exception.getMessage().contains("User"));
        assertTrue(exception.getMessage().contains("999"));
    }

    @Test
    void setUserMediaHistory_WithValidList_ShouldSetMediaHistory() {

        User user = new User();
        user.setId(1L);

        java.util.List<MediaReference> mediaHistory = new java.util.ArrayList<>();
        mediaHistory.add(new MediaReference("music", 101L));
        mediaHistory.add(new MediaReference("video", 202L));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));


        userService.setUserMediaHistory(1L, mediaHistory);


        assertEquals(2, user.getMediaHistory().size());
    }

    @Test
    void setUserMediaHistory_WithNullList_ShouldThrowIllegalArgumentException() {

        User user = new User();
        user.setId(1L);




        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.setUserMediaHistory(1L, null);
        });
        assertEquals("Can not add null Object to User MediaHistory.", exception.getMessage());
    }

    @Test
    void addMediaToUserMediaHistory_WithValidParams_ShouldAddMedia() {

        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));


        userService.addMediaToUserMediaHistory(1L, "music", 101L);


        assertEquals(1, user.getMediaHistory().size());
        assertEquals("music", user.getMediaHistory().get(0).getMediaType());
        assertEquals(101L, user.getMediaHistory().get(0).getMediaId());
    }

    @Test
    void addMediaToUserMediaHistory_WithNullUserId_ShouldThrowIllegalArgumentException() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.addMediaToUserMediaHistory(null, "music", 101L);
        });
        assertEquals("Can not add to User MediaHistory with null in params.", exception.getMessage());
    }

    @Test
    void addMediaToUserMediaHistory_WithNullMediaType_ShouldThrowIllegalArgumentException() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.addMediaToUserMediaHistory(1L, null, 101L);
        });
        assertEquals("Can not add to User MediaHistory with null in params.", exception.getMessage());
    }

    @Test
    void addMediaToUserMediaHistory_WithNullMediaId_ShouldThrowIllegalArgumentException() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.addMediaToUserMediaHistory(1L, "music", null);
        });
        assertEquals("Can not add to User MediaHistory with null in params.", exception.getMessage());
    }
}