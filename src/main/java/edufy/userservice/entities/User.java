package edufy.userservice.entities;

import jakarta.persistence.*;

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
    private Long totalPlayCount = 0L;

@Column(nullable = false)
private String roles = "USER";

    public User() {
    }

    public User(Long id, String username, String password, String preferredGenres, Long totalPlayCount) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.preferredGenres = preferredGenres;
        this.totalPlayCount = totalPlayCount;
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
}
