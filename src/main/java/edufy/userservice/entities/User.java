package edufy.userservice.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "users")
public class User {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

@Column(nullable = false, unique = true)
    private String username;

@Column(nullable = false)
    private String password; // kör en bcrypt för lösen(hashat)

@Column(nullable = false)
    private String preferredGenres;

@Column(nullable = false)
    private Long totalPlayCount = (Long)0L;

@Column(nullable = false)
private String roles = "USER";

@ElementCollection
private List<MediaReference> mediaHistory = new ArrayList<>();


    public User() {
    }

    public User(Long id, String username, String password, String preferredGenres, Long totalPlayCount, List<MediaReference> mediaHistory) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.preferredGenres = preferredGenres;
        this.totalPlayCount = totalPlayCount;
        this.mediaHistory = mediaHistory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPreferredGenres() {
        return preferredGenres;
    }

    public void setPreferredGenres(String preferredGenres) {
        this.preferredGenres = preferredGenres;
    }

    public Long getTotalPlayCount() {
        return totalPlayCount;
    }

    public void setTotalPlayCount(Long totalPlayCount) {
        this.totalPlayCount = totalPlayCount;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }


    private final static int maxMediaHistoryArraySize = 100;

    public void setMediaHistory(List<MediaReference> mediaHistory){
        if (mediaHistory.size() > maxMediaHistoryArraySize){
            throw new IllegalArgumentException("Media History is not allowed to surpass the max length set within Edufy-userservice/User");
        }
        else {
            this.mediaHistory = mediaHistory;
        }

    }

    public void addToMediaHistory(String mediaType, Long mediaId){
        if (this.mediaHistory.size() >= maxMediaHistoryArraySize){
            this.mediaHistory.remove(0);
        }
        MediaReference mediaReference = new MediaReference(mediaType, mediaId);
        this.mediaHistory.add(mediaReference);
    }

    public List<MediaReference> getMediaHistory(){
        return this.mediaHistory;
    }

}