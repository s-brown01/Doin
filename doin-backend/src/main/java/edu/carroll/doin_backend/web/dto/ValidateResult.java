package edu.carroll.doin_backend.web.dto;

/**
 * Represents the result of a validation process.
 *
 * <p>
 * This class encapsulates the outcome of a validation operation,
 * indicating whether the validation was successful and providing
 * an associated message to describe the result.
 * </p>
 *
 * <p>Structure of the class and javadoc was helped by StackOverflow</p>
 */
public class ValidateResult {
    private final boolean valid;
    private final String message;
    private final Object content;

    /**
     * Constructs a new instance of {@link ValidateResult}.
     *
     * @param valid   a boolean indicating whether the validation was successful
     * @param message a string providing additional information about the validation result
     */
    public ValidateResult(boolean valid, String message, Object content) {
        this.valid = valid;
        this.message = message;
        this.content = content;
    }

    public ValidateResult(boolean valid, String message) {
        this(valid, message, null);
    }

    /**
     * Returns whether the validation was successful.
     *
     * @return true if the validation passed; false otherwise
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Returns the message associated with the validation result.
     *
     * @return a string containing the message that describes the result of the validation
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the content in the validate result
     *
     * @return a Java Object containing the content of the validate result (can be null)
     */
    public Object getContent() {
        return content;
    }
}
