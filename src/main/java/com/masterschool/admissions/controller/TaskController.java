package com.masterschool.admissions.controller;

import com.masterschool.admissions.dto.CompleteTaskRequest;
import com.masterschool.admissions.facade.AdmissionsFacade;
import com.masterschool.admissions.service.TaskRequestMapper;
import com.masterschool.admissions.task.TaskName;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller responsible for completing admissions tasks.
 *
 * This controller exposes a single generic endpoint for all task types.
 * It delegates:
 * - payload conversion to {@link TaskRequestMapper}
 * - business orchestration to {@link AdmissionsFacade}
 *
 * Using one generic endpoint keeps the external API stable and avoids
 * creating a separate controller method for every task in the system.
 */
@RestController
@RequestMapping("/admissions/users/{userId}")
public class TaskController {

    private final AdmissionsFacade admissionsFacade;
    private final TaskRequestMapper taskRequestMapper;

    /**
     * Creates the task controller.
     *
     * @param admissionsFacade main admissions orchestration layer
     * @param taskRequestMapper maps raw JSON payloads into typed DTOs
     */
    public TaskController(AdmissionsFacade admissionsFacade,
                          TaskRequestMapper taskRequestMapper) {
        this.admissionsFacade = admissionsFacade;
        this.taskRequestMapper = taskRequestMapper;
    }

    /**
     * Completes a task for the given user.
     *
     * The endpoint is generic:
     * - the task type is provided in the path
     * - the request body contains the step name and raw payload
     *
     * The raw payload is converted into the correct DTO using
     * {@link TaskRequestMapper}, then passed to {@link AdmissionsFacade}
     * for validation and execution.
     *
     * @param userId user identifier
     * @param taskName task to execute
     * @param request wrapper containing step name and raw payload
     * @return HTTP 200 if the task was processed successfully
     */
    @PutMapping("/tasks/{taskName}")
    public ResponseEntity<Void> completeTask(
            @PathVariable String userId,
            @PathVariable TaskName taskName,
            @RequestBody CompleteTaskRequest request
    ) {
        Object typedRequest = taskRequestMapper.map(taskName, request.getPayload());
        admissionsFacade.handleTask(userId, request.getStepName(), taskName, typedRequest);
        return ResponseEntity.ok().build();
    }
}