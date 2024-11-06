package edu.carroll.doin_backend.web.model;

import edu.carroll.doin_backend.web.enums.FriendshipStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Represents a friendship relationship between two users in the system.
 * <p>
 * This entity maps to the "friendships" table in the database and stores the details about a friendship
 * between two users, including the status of the friendship and timestamps for its creation and confirmation.
 * </p>
 */
@Entity
@Table(name = "friendships")
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_id")
    private User friend;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    /**
     * Creates a new friendship between the given user and friend with the specified status.
     * The creation timestamp is automatically set to the current time.
     *
     * @param user   the user initiating the friendship
     * @param friend the friend involved in the friendship
     * @param status the status of the friendship
     */
    public Friendship(User user, User friend, FriendshipStatus status) {
        this.user = user;
        this.friend = friend;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Default constructor for the Friendship class.
     */
    public Friendship() {
    }

    /**
     * Gets the unique identifier of the friendship.
     *
     * @return the ID of the friendship
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the user who initiated the friendship.
     *
     * @return the user in the friendship
     */
    public User getUser() {
        return user;
    }

    /**
     * Gets the friend in the friendship.
     *
     * @return the friend in the friendship
     */
    public User getFriend() {
        return friend;
    }

    /**
     * Gets the status of the friendship.
     *
     * @return the current status of the friendship
     */
    public FriendshipStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of the friendship.
     *
     * @param status the status to set for the friendship
     */
    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }

    /**
     * Gets the creation timestamp of the friendship.
     *
     * @return the timestamp when the friendship was created
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Gets the confirmation timestamp of the friendship (if applicable).
     *
     * @return the timestamp when the friendship was confirmed, or {@code null} if not confirmed
     */
    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }

    /**
     * Sets the confirmation timestamp of the friendship.
     *
     * @param confirmedAt the timestamp when the friendship was confirmed
     */
    public void setConfirmedAt(LocalDateTime confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    /**
     * Compares this friendship to another object for equality.
     * <p>
     * Two friendships are considered equal if they have the same user, friend, and created timestamp.
     * </p>
     *
     * @param o the object to compare this friendship with
     * @return {@code true} if the objects are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Friendship that = (Friendship) o;
        return (
                this.user.equals(that.user) &&
                        this.friend.equals(that.friend) &&
                        this.createdAt.equals(that.createdAt)
        );
    }

    /**
     * Returns a string representation of the friendship.
     *
     * @return a string describing the friendship, including the user, friend, status, and creation timestamp
     */
    @Override
    public String toString() {
        return "Friendship between user " + user + " and friend " + friend + " with status " + status + ", created at " + createdAt;
    }
}
