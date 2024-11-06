package edu.carroll.doin_backend.web.model;

import edu.carroll.doin_backend.web.dto.RegisterDTO;
import edu.carroll.doin_backend.web.dto.UserDTO;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Represents a user in the system, implementing {@link UserDetails} for Spring Security integration.
 * <p>
 * This entity stores user information such as username, password hash, profile picture, security question, and security question answer hash.
 * </p>
 */
@Entity
@Table(name = "users")
public class User implements UserDetails {

    /**
     * The unique identifier for the user.
     *
     * @return the ID of the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The username of the user.
     * <p>
     * This is a unique identifier for the user within the system.
     * </p>
     *
     * @return the username of the user.
     */
    @Column(nullable = false)
    private String username;

    /**
     * The hashed password of the user.
     * <p>
     * This is used for user authentication, stored securely as a hashed value.
     * </p>
     *
     * @return the hashed password.
     */
    @Column(nullable = false)
    private String passwordHash;

    /**
     * The date and time when the user was created in the system.
     *
     * @return the creation timestamp of the user account.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * The user's profile picture, if any.
     * <p>
     * The profile picture is an image object that is associated with the user.
     * </p>
     *
     * @return the user's profile picture.
     */
    @OneToOne
    @JoinColumn(name = "profile_picture_id")
    private Image profilePicture;

    /**
     * The security question associated with the user.
     * <p>
     * This question is used for password recovery or identity verification.
     * </p>
     *
     * @return the security question.
     */
    @ManyToOne
    @JoinColumn(name = "security_question_id")
    private SecurityQuestion securityQuestion;

    /**
     * The hashed answer to the security question.
     * <p>
     * This is used to validate the user's identity during account recovery.
     * </p>
     *
     * @return the hashed security question answer.
     */
    @Column(name = "security_question_answer_hash")
    private String securityQuestionAnswer;

    /**
     * Constructs a {@link User} object from a {@link UserDTO}.
     *
     * @param user the data transfer object containing user information.
     */
    public User(UserDTO user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.profilePicture = user.getProfilePicture();
    }

    /**
     * Constructs a {@link User} object from a {@link RegisterDTO}, password hash, and a security question.
     *
     * @param registerDTO      the data transfer object containing user registration information.
     * @param passwordHash     the hashed password for the user.
     * @param securityQuestion the security question chosen by the user.
     */
    public User(RegisterDTO registerDTO, String passwordHash, SecurityQuestion securityQuestion) {
        this.username = registerDTO.getUsername();
        this.passwordHash = passwordHash;
        this.securityQuestion = securityQuestion;
        this.securityQuestionAnswer = registerDTO.getSecurityAnswer();
        registerDTO.clearData();
    }

    /**
     * Default constructor for the {@link User} class.
     * Initializes an empty {@link User} object.
     */
    public User() {
    }

    /**
     * Called before persisting the entity to set the creation timestamp.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    /**
     * Gets the unique identifier for the user.
     *
     * @return the ID of the user.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the user.
     *
     * @param id the ID to set for the user.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the username of the user.
     *
     * @return the username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for the user.
     *
     * @param username the username to set for the user.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the hashed password for the user.
     *
     * @param passwordHash the password hash to set.
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Gets the creation timestamp of the user account.
     *
     * @return the creation timestamp of the user.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp of the user account.
     *
     * @param createdAt the timestamp to set for the user account creation.
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the user's profile picture.
     *
     * @return the user's profile picture.
     */
    public Image getProfilePicture() {
        return profilePicture;
    }

    /**
     * Sets the user's profile picture.
     *
     * @param profilePicture the profile picture to set for the user.
     */
    public void setProfilePicture(Image profilePicture) {
        this.profilePicture = profilePicture;
    }

    /**
     * Gets the security question associated with the user.
     *
     * @return the user's security question.
     */
    public SecurityQuestion getSecurityQuestion() {
        return securityQuestion;
    }

    /**
     * Sets the security question for the user.
     *
     * @param securityQuestion the security question to set for the user.
     */
    public void setSecurityQuestion(SecurityQuestion securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    /**
     * Gets the hashed answer to the security question.
     *
     * @return the hashed security question answer.
     */
    public String getSecurityQuestionAnswer() {
        return securityQuestionAnswer;
    }

    /**
     * Sets the hashed answer to the security question.
     *
     * @param securityQuestionAnswer the hashed answer to set.
     */
    public void setSecurityQuestionAnswer(String securityQuestionAnswer) {
        this.securityQuestionAnswer = securityQuestionAnswer;
    }

    /**
     * Returns a collection of granted authorities for the user.
     * <p>
     * Since there are no specific authorities for this user class, an empty list is returned.
     * </p>
     *
     * @return an empty list of authorities.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(); // No authorities needed, return an empty list
    }

    /**
     * Gets the user's password hash.
     *
     * @return the hashed password.
     */
    @Override
    public String getPassword() {
        return passwordHash; // Return the hashed password
    }

    /**
     * Checks if the user's account is non-expired.
     *
     * @return {@code true} if the account is not expired.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true; // User account is not expired
    }

    /**
     * Checks if the user's account is non-locked.
     *
     * @return {@code true} if the account is not locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true; // User account is not locked
    }

    /**
     * Checks if the user's credentials are non-expired.
     *
     * @return {@code true} if the credentials are not expired.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // User credentials are not expired
    }

    /**
     * Checks if the user's account is enabled.
     *
     * @return {@code true} if the account is enabled.
     */
    @Override
    public boolean isEnabled() {
        return true; // User account is enabled
    }

    /**
     * Compares this {@link User} object to another for equality.
     * <p>
     * Two users are considered equal if their username, password hash, security question, security question answer, and creation timestamp are the same.
     * </p>
     *
     * @param o the object to compare with.
     * @return {@code true} if the objects are equal, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User otherUser = (User) o;
        // all data should be the same, except profile pic & id
        return (
                this.username.equals(otherUser.getUsername()) &&
                        this.passwordHash.equals(otherUser.passwordHash) &&
                        this.securityQuestion.equals(otherUser.securityQuestion) &&
                        this.securityQuestionAnswer.equals(otherUser.securityQuestionAnswer) &&
                        this.createdAt.equals(otherUser.createdAt)
        );
    }

    /**
     * Returns a string representation of the {@link User} object.
     * <p>
     * This includes the ID, username, and account creation timestamp.
     * </p>
     *
     * @return a string representation of the user.
     */
    @Override
    public String toString() {
        return "ID #" + id + ", Username: " + username + " created at " + createdAt;
    }
}
