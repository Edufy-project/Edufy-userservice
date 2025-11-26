package edufy.userservice.dtos;

public class FeedbackRequestDto {
    private Long mediaId;
    private String mediaType;
    private boolean isPositive;

    public FeedbackRequestDto() {}

    public FeedbackRequestDto(Long mediaId, String mediaType, boolean positive) {
        this.mediaId = mediaId;
        this.mediaType = mediaType;
        this.isPositive = positive;
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

    public boolean isPositive() {
        return isPositive;
    }

    public void setPositive(boolean positive) {
        this.isPositive = positive;
    }
}