package com.masterschool.admissions.domain;

import lombok.Getter;

/**
 * Represents a system user.
 * A user is identified by a unique ID and an email address.
 * This class is immutable and does not contain any process-related data.
 */

public class User {
    private final String id;
    private final String email;

    public User(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public String getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
}
