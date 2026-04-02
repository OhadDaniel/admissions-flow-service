package com.masterschool.admissions.repository;

import com.masterschool.admissions.domain.UserProgress;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Optional;

/**
 * In-memory implementation of UserProgressRepository.
 */
public class InMemoryUserProgressRepository implements UserProgressRepository {

    private final Map<String, UserProgress> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<UserProgress> findByUserId(String userId) {
        return Optional.ofNullable(storage.get(userId));
    }

    @Override
    public void save(UserProgress userProgress) {
        storage.put(userProgress.getUserId(), userProgress);
    }
}