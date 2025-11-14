package edufy.userservice.controllers;

import edufy.userservice.dtos.FeedbackRequestDto;
import edufy.userservice.entities.UserFeedback;
import edufy.userservice.exceptions.InvalidUserException;
import edufy.userservice.exceptions.ResourceNotFoundException;
import edufy.userservice.services.UserFeedbackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/edufy")
public class UserFeedbackController {

    private final UserFeedbackService userFeedbackService;

    public UserFeedbackController(UserFeedbackService userFeedbackService) {
        this.userFeedbackService = userFeedbackService;
    }


    @PostMapping("/user/{userId}/feedback")
    public ResponseEntity<?> submitFeedback(
            @PathVariable Long userId,
            @RequestBody FeedbackRequestDto request) {
        try {
            UserFeedback feedback = userFeedbackService.submitFeedback(
                    userId,
                    request.getMediaId(),
                    request.getMediaType(),
                    request.isPositive()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(feedback);
        } catch (InvalidUserException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @GetMapping("/user/{userId}/feedback")
    public ResponseEntity<?> getUserFeedback(@PathVariable Long userId) {
        try {
            List<UserFeedback> feedbacks = userFeedbackService.getUserFeedback(userId);
            return ResponseEntity.ok(feedbacks);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @GetMapping("/user/{userId}/feedback/likes")
    public ResponseEntity<?> getUserLikes(@PathVariable Long userId) {
        try {
            List<UserFeedback> likes = userFeedbackService.getUserLikes(userId);
            return ResponseEntity.ok(likes);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @GetMapping("/user/{userId}/feedback/dislikes")
    public ResponseEntity<?> getUserDislikes(@PathVariable Long userId) {
        try {
            List<UserFeedback> dislikes = userFeedbackService.getUserDislikes(userId);
            return ResponseEntity.ok(dislikes);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @GetMapping("/user/{userId}/feedback/liked-media-ids")
    public ResponseEntity<?> getUserLikedMediaIds(@PathVariable Long userId) {
        try {
            List<Long> mediaIds = userFeedbackService.getUserLikedMediaIds(userId);
            return ResponseEntity.ok(mediaIds);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @GetMapping("/user/{userId}/feedback/disliked-media-ids")
    public ResponseEntity<?> getUserDislikedMediaIds(@PathVariable Long userId) {
        try {
            List<Long> mediaIds = userFeedbackService.getUserDislikedMediaIds(userId);
            return ResponseEntity.ok(mediaIds);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @DeleteMapping("/user/{userId}/feedback/{mediaId}")
    public ResponseEntity<?> removeFeedback(
            @PathVariable Long userId,
            @PathVariable Long mediaId) {
        try {
            userFeedbackService.removeFeedback(userId, mediaId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @GetMapping("/user/{userId}/feedback/check/{mediaId}")
    public ResponseEntity<?> hasUserRated(
            @PathVariable Long userId,
            @PathVariable Long mediaId) {
        try {
            boolean hasRated = userFeedbackService.hasUserRatedMedia(userId, mediaId);
            return ResponseEntity.ok(hasRated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}