package edufy.userservice.repositories;

import edufy.userservice.entities.UserFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<UserFeedback, Long> {

    List<UserFeedback> findByUserId(Long userId);

    List<UserFeedback> findByUserIdAndFeedbackType(Long userId, String feedbackType);

    Optional<UserFeedback> findByUserIdAndMediaId(Long userId, Long mediaId);

    boolean existsByUserIdAndMediaId(Long userId, Long mediaId);
}