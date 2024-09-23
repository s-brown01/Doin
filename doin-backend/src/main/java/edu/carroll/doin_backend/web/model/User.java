package edu.carroll.doin_backend.web.model;

import edu.carroll.doin_backend.web.dto.UserDTO;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String passwordHash;
    @Column(nullable = false)
    private String email;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @OneToOne
    @JoinColumn(name = "profile_picture_id")
    private Image profilePictureId;
    @ManyToOne
    @JoinColumn(name = "security_question_id")
    private SecurityQuestion securityQuestionId;
    @Column(name = "security_question_answer_hash")
    private String securityQuestionAnswer;

    public User(UserDTO user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.profilePictureId = user.getProfilePictureId();
    }

    public User() {
    }

    public User(String username, String hashedPassword) {
        this.username = username;
        this.passwordHash = hashedPassword;
        this.email = "test-email@example.com";
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Image getProfilePictureId() {
        return profilePictureId;
    }

    public void setProfilePictureId(Image profilePictureId) {
        this.profilePictureId = profilePictureId;
    }

    public SecurityQuestion getSecurityQuestionId() {
        return securityQuestionId;
    }

    public void setSecurityQuestionId(SecurityQuestion securityQuestionId) {
        this.securityQuestionId = securityQuestionId;
    }

    public String getSecurityQuestionAnswer() {
        return securityQuestionAnswer;
    }

    public void setSecurityQuestionAnswer(String securityQuestionAnswer) {
        this.securityQuestionAnswer = securityQuestionAnswer;
    }

    /**
     * Getter for the User's Hashed Password.
     *
     * @return - the String containing the hashing of the user's password.
     */
    public String getHashedPassword() {
        return passwordHash;
    }
}
