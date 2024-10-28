package edu.carroll.doin_backend.web.dto;

import edu.carroll.doin_backend.web.model.SecurityQuestion;

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

    /**
     * Constructs a new instance of {@link ValidateResult}.
     *
     * @param valid   a boolean indicating whether the validation was successful
     * @param message a string providing additional information about the validation result
     */
    public ValidateResult(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
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
}
