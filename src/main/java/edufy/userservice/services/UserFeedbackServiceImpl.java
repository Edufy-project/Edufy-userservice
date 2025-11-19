package edufy.userservice.services;

import edufy.userservice.entities.UserFeedback;
import edufy.userservice.exceptions.InvalidUserException;
import edufy.userservice.exceptions.ResourceNotFoundException;
import edufy.userservice.repositories.FeedbackRepository;
import edufy.userservice.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserFeedbackServiceImpl implements UserFeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    public UserFeedbackServiceImpl(FeedbackRepository feedbackRepository, UserRepository userRepository) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserFeedback submitFeedback(Long userId, Long mediaId, String mediaType, boolean isPositive) {

        if (userId == null || mediaId == null || mediaType == null) {
            throw new IllegalArgumentException("UserId, mediaId and mediaType cannot be null");
        }


        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));


        if (feedbackRepository.existsByUserIdAndMediaId(userId, mediaId)) {
            throw new InvalidUserException("User has already rated this media");
        }

        String feedbackType = isPositive ? UserFeedback.THUMBS_UP : UserFeedback.THUMBS_DOWN;
        UserFeedback feedback = new UserFeedback(userId, mediaId, mediaType, feedbackType);

        return feedbackRepository.save(feedback);
    }

    @Override
    public List<UserFeedback> getUserFeedback(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }


        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return feedbackRepository.findByUserId(userId);
    }

    @Override
    public List<UserFeedback> getUserLikes(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }

        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return feedbackRepository.findByUserIdAndFeedbackType(userId, UserFeedback.THUMBS_UP);
    }

    @Override
    public List<UserFeedback> getUserDislikes(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }

        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return feedbackRepository.findByUserIdAndFeedbackType(userId, UserFeedback.THUMBS_DOWN);
    }

    @Override
    public List<Long> getUserLikedMediaIds(Long userId) {
        return getUserLikes(userId).stream()
                .map(UserFeedback::getMediaId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getUserDislikedMediaIds(Long userId) {
        return getUserDislikes(userId).stream()
                .map(UserFeedback::getMediaId)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void removeFeedback(Long userId, Long mediaId) {
        if (userId == null || mediaId == null) {
            throw new IllegalArgumentException("UserId and mediaId cannot be null");
        }

        feedbackRepository.findByUserIdAndMediaId(userId, mediaId)
                .ifPresentOrElse(
                        feedbackRepository::delete,
                        () -> { throw new ResourceNotFoundException("Feedback", "userId and mediaId", userId + " and " + mediaId); }
                );
    }

    @Override
    public boolean hasUserRatedMedia(Long userId, Long mediaId) {
        if (userId == null || mediaId == null) {
            throw new IllegalArgumentException("UserId and mediaId cannot be null");
        }

        return feedbackRepository.existsByUserIdAndMediaId(userId, mediaId);
    }
}