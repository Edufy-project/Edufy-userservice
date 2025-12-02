package edufy.userservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edufy.userservice.entities.MediaReference;
import edufy.userservice.entities.User;
import edufy.userservice.exceptions.InvalidUserException;
import edufy.userservice.exceptions.ResourceNotFoundException;
import edufy.userservice.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
        import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerUnitTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper = new ObjectMapper();
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        user = new User(1L, "simon", "password", "rock", 0L, new ArrayList<>());
    }

    @Test
    void registerUser_Success() throws Exception {
        when(userService.registerUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/edufy/api/users/registeruser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("simon"));

        verify(userService).registerUser(any(User.class));
    }

    @Test
    void registerUser_InvalidUser() throws Exception {
        when(userService.registerUser(any(User.class))).thenThrow(new InvalidUserException("Invalid user"));

        mockMvc.perform(post("/edufy/api/users/registeruser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid user"));
    }

    @Test
    void getUserByUsername_Found() throws Exception {
        when(userService.getUserByUsername("simon")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/edufy/api/users/user/simon"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("simon"));
    }

    @Test
    void getUserByUsername_NotFound() throws Exception {
        when(userService.getUserByUsername("simon")).thenReturn(Optional.empty());

        mockMvc.perform(get("/edufy/api/users/user/simon"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User with username ' simon' not found"));
    }

    @Test
    void getAllUsers_Success() throws Exception {
        List<User> users = Arrays.asList(user);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/edufy/api/users/listusers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("simon"));
    }

    @Test
    void updateUser_Success() throws Exception {
        when(userService.updateUser(any(User.class))).thenReturn(user);

        mockMvc.perform(put("/edufy/api/users/updateuser/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("simon"));
    }

    @Test
    void updateUser_InvalidUser() throws Exception {
        when(userService.updateUser(any(User.class))).thenThrow(new InvalidUserException("Invalid user"));

        mockMvc.perform(put("/edufy/api/users/updateuser/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid user"));
    }

    @Test
    void updateUser_NotFound() throws Exception {
        when(userService.updateUser(any(User.class))).thenThrow(new ResourceNotFoundException("User", "id", 1L));

        mockMvc.perform(put("/edufy/api/users/updateuser/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User with id '1' not found"));
    }

    @Test
    void deleteUser_Success() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/edufy/api/users/deleteuser/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("User", "id", 1L)).when(userService).deleteUser(1L);

        mockMvc.perform(delete("/edufy/api/users/deleteuser/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User with id '1' not found"));
    }

    @Test
    void incrementPlayCount_Success() throws Exception {
        doNothing().when(userService).incrementPlayCount("simon", 1L);

        mockMvc.perform(patch("/edufy/api/users/user/simon/increment-playcount"))
                .andExpect(status().isOk())
                .andExpect(content().string("Play count updated for user : simon"));
    }

    @Test
    void incrementPlayCount_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("User", "username", "simon"))
                .when(userService).incrementPlayCount("simon", 1L);

        mockMvc.perform(patch("/edufy/api/users/user/simon/increment-playcount"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User with username 'simon' not found"));
    }

    @Test
    void getUserMediaHistory_Success() throws Exception {
        List<MediaReference> history = Arrays.asList(new MediaReference("song", 1L));
        when(userService.getUserMediaHistory(1L)).thenReturn(history);

        mockMvc.perform(get("/edufy/api/users/usermediahistory/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].mediaType").value("song"));
    }

    @Test
    void setUserMediaHistory_Success() throws Exception {
        doNothing().when(userService).setUserMediaHistory(eq(1L), anyList());

        mockMvc.perform(put("/edufy/api/users/setusermediahistory/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ArrayList<MediaReference>())))
                .andExpect(status().isOk());
    }

    @Test
    void addToUserMediaHistory_Success() throws Exception {
        doNothing().when(userService).addMediaToUserMediaHistory(1L, "song", 1L);

        mockMvc.perform(put("/edufy/api/users/addtousermediahistory/1/song/1"))
                .andExpect(status().isOk());
    }
}
