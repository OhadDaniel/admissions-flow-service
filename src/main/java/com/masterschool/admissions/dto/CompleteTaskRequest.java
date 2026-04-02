package com.masterschool.admissions.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.masterschool.admissions.flow.StepName;
import com.masterschool.admissions.service.TaskRequestMapper;

/**
 * Request body for the generic task-completion endpoint.
 *
 * This wrapper contains:
 * - the step the client expects the task to belong to
 * - the raw JSON payload of the task itself
 *
 * The payload is later converted into a typed DTO by {@link TaskRequestMapper}
 * according to the provided task name from the request path.
 */
public class CompleteTaskRequest {
    private StepName stepName;
    private JsonNode payload;

    /**
     * Creates a task-completion request.
     *
     * @param stepName step the task is expected to belong to
     * @param payload raw task payload
     */
    public CompleteTaskRequest(StepName stepName, JsonNode payload) {
        this.stepName = stepName;
        this.payload = payload;
    }

    /**
     * @return the step the client expects the task to belong to
     */
    public StepName getStepName() {
        return stepName;
    }

    /**
     * @return raw JSON payload to be mapped into the task-specific DTO
     */
    public JsonNode getPayload() {
        return payload;
    }
}