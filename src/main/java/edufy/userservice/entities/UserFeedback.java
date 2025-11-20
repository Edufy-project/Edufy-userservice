package edufy.userservice.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_feedback",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "media_id"}))
public class UserFeedback {

    public static final String THUMBS_UP = "THUMBS_UP";
    public static final String THUMBS_DOWN = "THUMBS_DOWN";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "media_id", nullable = false)
    private Long mediaId;

    @Column(name = "media_type", nullable = false)
    private String mediaType; // "music", "pod", "video"

    @Column(name = "feedback_type", nullable = false)
    private String feedbackType; // THUMBS_UP eller THUMBS_DOWN

    @Column
    private LocalDateTime timestamp;


    public UserFeedback() {
        this.timestamp = LocalDateTime.now();
    }

    public UserFeedback(Long userId, Long mediaId, String mediaType, String feedbackType) {
        if (!feedbackType.equals(THUMBS_UP) && !feedbackType.equals(THUMBS_DOWN)) {
            throw new IllegalArgumentException("Feedback type måste vara THUMBS_UP eller THUMBS_DOWN");
        }
        this.userId = userId;
        this.mediaId = mediaId;
        this.mediaType = mediaType;
        this.feedbackType = feedbackType;
        this.timestamp = LocalDateTime.now();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}