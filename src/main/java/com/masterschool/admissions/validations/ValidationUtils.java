package com.masterschool.admissions.validations;

/**
 * Utility class providing common validation methods for request data.
 *
 * Designed to centralize simple validation logic and avoid duplication
 * across DTOs and other layers.
 *
 * Usage:
 * - Called from DTO (record) constructors to validate incoming data
 * - Throws IllegalArgumentException when validation fails
 *
 * Note:
 * This class is stateless and contains only static helper methods.
 */
public class ValidationUtils {

    /**
     * Validates that a string value is not null or blank.
     *
     * @param value     the string to validate
     * @param fieldName the name of the field (used in error messages)
     * @throws IllegalArgumentException if the value is null or blank
     */
    public static void requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is invalid");
        }
    }

    /**
     * Validates that a value is not null.
     *
     * @param value     the object to validate
     * @param fieldName the name of the field (used in error messages)
     * @throws IllegalArgumentException if the value is null
     */
    public static void requireNonNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
    }
}
