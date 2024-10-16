package edu.carroll.doin_backend.web.model;

import edu.carroll.doin_backend.web.dto.RegisterDTO;
import edu.carroll.doin_backend.web.dto.UserDTO;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "profile_picture_id")
    private Image profilePicture;

    @ManyToOne
    @JoinColumn(name = "security_question_id")
    private SecurityQuestion securityQuestion;

    @Column(name = "security_question_answer_hash")
    private String securityQuestionAnswer;

    public User(UserDTO user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.profilePicture = user.getProfilePicture();
    }

    public User(RegisterDTO registerDTO, String passwordHash, SecurityQuestion securityQuestion) {
        this.username = registerDTO.getUsername();
        this.passwordHash = passwordHash;
        this.securityQuestion = securityQuestion;
        this.securityQuestionAnswer = registerDTO.getSecurityAnswer();
        registerDTO.clearData();
    }

    public User() {
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

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Image getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Image profilePicture) {
        this.profilePicture = profilePicture;
    }

    public SecurityQuestion getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(SecurityQuestion securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityQuestionAnswer() {
        return securityQuestionAnswer;
    }

    public void setSecurityQuestionAnswer(String securityQuestionAnswer) {
        this.securityQuestionAnswer = securityQuestionAnswer;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // No authorities needed, return an empty list
        return List.of();
    }

    @Override
    public String getPassword() {
        return passwordHash; // Return the hashed password
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // User account is not expired
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // User account is not locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // User credentials are not expired
    }

    @Override
    public boolean isEnabled() {
        return true; // User account is enabled
    }
}