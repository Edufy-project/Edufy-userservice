package edufy.userservice.controllers;

import edufy.userservice.entities.MediaReference;
import edufy.userservice.entities.User;
import edufy.userservice.exceptions.InvalidUserException;
import edufy.userservice.exceptions.ResourceNotFoundException;
import edufy.userservice.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("edufy/api")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registeruser")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User createdUser = userService.registerUser(user);
                    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (InvalidUserException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.getUserByUsername(username);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with username ' " + username + "' not found");
        }
    }

    @GetMapping("/listusers")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/updateuser/{id}")
    public ResponseEntity<?> updateUser( @PathVariable Long id, @RequestBody User user) {
        try {
            user.setId(id);
            User updatedUser = userService.updateUser(user);
            return ResponseEntity.ok(updatedUser);
        } catch (InvalidUserException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteuser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try{
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
        //endpoint för att visa ökning av antal spelningar
    @PatchMapping("/user/{username}/increment-playcount")
    public ResponseEntity<?> incrementPlayCount(@PathVariable String username, @RequestParam(defaultValue = "1") Long count) {
        try {
            userService.incrementPlayCount(username, count);
            return ResponseEntity.ok("Play count updated for user : " + username);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/usermediahistory/{userid}")
    public List<MediaReference> getUserMediaHistory(@PathVariable Long userid){
        return userService.getUserMediaHistory(userid);
    }

    @PutMapping("/setusermediahistory/{userid}")
    public void setUserMediaHistory(@PathVariable Long userid, @Valid @RequestBody List<MediaReference> mediaHistory){
        userService.setUserMediaHistory(userid, mediaHistory);
    }

    @PutMapping("/addtousermediahistory/{userid}/{mediatype}/{mediaid}")
    public void addToUserMediaHistory(@PathVariable Long userid, @PathVariable String mediatype, @PathVariable Long mediaid){
        userService.addMediaToUserMediaHistory(userid, mediatype, mediaid);
    }

}
