package edufy.userservice.services;

import edufy.userservice.entities.UserFeedback;
import java.util.List;

public interface UserFeedbackService {


    UserFeedback submitFeedback(Long userId, Long mediaId, String mediaType, boolean isPositive);


    List<UserFeedback> getUserFeedback(Long userId);


    List<UserFeedback> getUserLikes(Long userId);


    List<UserFeedback> getUserDislikes(Long userId);


    List<Long> getUserLikedMediaIds(Long userId);


    List<Long> getUserDislikedMediaIds(Long userId);


    void removeFeedback(Long userId, Long mediaId);


    boolean hasUserRatedMedia(Long userId, Long mediaId);
}