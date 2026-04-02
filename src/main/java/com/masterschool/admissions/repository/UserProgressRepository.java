package com.masterschool.admissions.repository;

import com.masterschool.admissions.domain.UserProgress;

import java.util.Optional;

/**
 * Repository for managing UserProgress persistence.
 */
public interface UserProgressRepository {

    Optional<UserProgress> findByUserId(String userId);

    void save(UserProgress userProgress);
}