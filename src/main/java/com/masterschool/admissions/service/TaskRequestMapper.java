package com.masterschool.admissions.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterschool.admissions.task.TaskName;
import com.masterschool.admissions.dto.*;

/**
 * Maps a generic JSON payload into the typed request DTO
 * expected by a specific admissions task.
 *
 * This class keeps controller code generic while preserving
 * typed task execution inside the business layer.
 *
 * Responsibility:
 * - receive a {@link TaskName}
 * - convert the incoming JSON payload into the matching DTO
 *
 * This makes it possible to expose a single generic task-completion
 * endpoint without pushing task-specific parsing logic into the controller.
 */
public class TaskRequestMapper {

    private final ObjectMapper objectMapper;

    /**
     * Creates a mapper backed by Jackson's {@link ObjectMapper}.
     *
     * @param objectMapper mapper used to convert raw JSON payloads into DTOs
     */
    public TaskRequestMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Converts the given JSON payload into the DTO required by the given task.
     *
     * @param taskName the task being executed
     * @param payload raw JSON payload received from the API
     * @return the typed DTO expected by the resolved task
     *
     * @throws IllegalArgumentException if the payload cannot be converted
     *                                  to the expected DTO shape
     */
    public Object map(TaskName taskName, JsonNode payload) {
        return switch (taskName) {
            case PERSONAL_DETAILS ->
                    objectMapper.convertValue(payload, PersonalDetailsRequest.class);
            case IQ_TEST ->
                    objectMapper.convertValue(payload, IQRequest.class);
            case SCHEDULE_INTERVIEW ->
                    objectMapper.convertValue(payload, ScheduleInterviewRequest.class);
            case PERFORM_INTERVIEW ->
                    objectMapper.convertValue(payload, PerformInterviewRequest.class);
            case UPLOAD_IDENTIFICATION ->
                    objectMapper.convertValue(payload, UploadIdentificationRequest.class);
            case SIGN_CONTRACT ->
                    objectMapper.convertValue(payload, SignContractRequest.class);
            case PAYMENT ->
                    objectMapper.convertValue(payload, PaymentRequest.class);
            case JOIN_SLACK ->
                    objectMapper.convertValue(payload, JoinSlackRequest.class);
        };
    }
}