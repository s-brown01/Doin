package edu.carroll.doin_backend.web.model;

import jakarta.persistence.*;

/**
 * Represents a security question used for user authentication.
 * <p>
 * This entity contains a security question text, which can be used to verify the identity of a user during the account recovery process.
 * </p>
 */
@Entity
@Table(name = "security_questions")
public class SecurityQuestion {

    /**
     * The unique identifier for the security question.
     *
     * @return the ID of the security question.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The text of the security question.
     * <p>
     * This is the question that will be asked for user authentication.
     * </p>
     *
     * @return the security question text.
     */
    @Column(nullable = false)
    private String question;

    /**
     * Default constructor for the {@link SecurityQuestion} class.
     * Initializes an empty {@link SecurityQuestion} object.
     */
    public SecurityQuestion() {
    }

    /**
     * Constructor to create a {@link SecurityQuestion} with a specific question.
     *
     * @param question the question text for the security question.
     */
    public SecurityQuestion(String question) {
        this.question = question;
    }

    /**
     * Gets the unique identifier for the security question.
     *
     * @return the ID of the security question.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Gets the text of the security question.
     *
     * @return the security question text.
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Sets the text of the security question.
     *
     * @param question the question text to set.
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Compares this {@link SecurityQuestion} with another object for equality.
     * Two {@link SecurityQuestion} objects are considered equal if their question text is the same, ignoring case.
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
        SecurityQuestion that = (SecurityQuestion) o;
        // question should be the same to be equal
        return this.question.equalsIgnoreCase(that.question);
    }

    /**
     * Returns a string representation of the {@link SecurityQuestion} object.
     * <p>
     * This includes the ID and the text of the security question.
     * </p>
     *
     * @return a string representation of the security question.
     */
    @Override
    public String toString() {
        return "SecurityQuestion{" +
                "id=" + id +
                ", question='" + question + '\'' +
                '}';
    }
}
