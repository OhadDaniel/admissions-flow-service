package com.masterschool.admissions.facade;

import com.masterschool.admissions.domain.*;
import com.masterschool.admissions.exception.InvalidTaskOrderException;
import com.masterschool.admissions.exception.TaskNotAllowedException;
import com.masterschool.admissions.exception.UserNotFoundException;
import com.masterschool.admissions.flow.FlowDefinition;
import com.masterschool.admissions.flow.Step;
import com.masterschool.admissions.repository.UserProgressRepository;
import com.masterschool.admissions.task.Task;
import com.masterschool.admissions.task.TaskFactory;
import com.masterschool.admissions.task.TaskName;

import java.time.Instant;
import java.util.UUID;

/**
 * Facade responsible for orchestrating the admissions flow execution.
 *.
 * This class acts as the main entry point for processing user actions.
 * It coordinates between:
 * - Repository (for loading and saving user state)
 * - FlowDefinition (for determining progression)
 * - TaskFactory (for resolving task implementations)
 *.
 * Responsibilities:
 * - Load or initialize user progress
 * - Execute the requested task
 * - Validate task belongs to current step
 * - Update user progression (next step / accepted / rejected)
 * - Persist the updated state
 *.
 * Note:
 * - Does not contain business logic of tasks
 * - Does not define the flow itself
 */

public class AdmissionsFacade {

    private final UserProgressRepository repository;
    private final FlowDefinition flow;
    private final TaskFactory taskFactory;

    public AdmissionsFacade(UserProgressRepository repository, FlowDefinition flow, TaskFactory taskFactory){
        this.repository = repository;
        this.flow = flow;
        this.taskFactory = taskFactory;
    }

    /**
     * Executes a task for a given user within the admissions flow.
     *.
     * The method performs the following steps:
     * 1. Retrieves or initializes the user's progress
     * 2. Resolves the task using TaskFactory
     * 3. Validates that the task belongs to the current step
     * 4. Executes the task logic
     * 5. Records the result of the execution
     * 6. Updates the user's progression:
     *    - FAILED → user is rejected and flow stops
     *    - PASSED → user advances to next step or is accepted if flow is complete
     * 7. Persists the updated user state
     *.
     * @param userId   the unique identifier of the user
     * @param taskName the task to execute
     * @param request  the input DTO required by the task
     * @param <T>      the type of the request object
     * @return the result of the task execution (PASSED / FAILED)
     *.
     * @throws IllegalStateException if the task does not belong to the current step
     * @throws IllegalArgumentException if the task is not registered in the factory
     */
    public <T> TaskStatus handleTask(String userId, TaskName taskName, T request) {

        // 1. Load user state
        UserProgress progress = repository
                .findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
        //Guard: flow already completed
        if (progress.getStatus() == UserStatus.REJECTED ||
                progress.getStatus() == UserStatus.ACCEPTED) {
            throw new IllegalStateException("User flow already completed");
        }
        // Initialize first step if needed
        if (progress.getCurrentStep() == null) {
            progress.advance(flow.getFirstStep().getName(), null);
        }

        // 2. Resolve task from factory
        Task<T> task = taskFactory.get(taskName);

        // 3. Validate task belongs to current step
        Step currentStep = flow.getStep(progress.getCurrentStep());

        boolean taskExists = currentStep.getTasks().stream()
                .anyMatch(t -> t.getName().equals(task.getName()));

        if (!taskExists) {
            throw new TaskNotAllowedException("Task does not belong to current step");
        }
        if (progress.getTasks().containsKey(task.getName())) {
            throw new TaskNotAllowedException("Task already executed");
        }

        if (!isPreviousTasksCompleted(progress, currentStep, task)) {
            throw new InvalidTaskOrderException("Previous tasks must be completed first");
        }


        // 4. Execute task
        TaskStatus status = task.process(request);

        // 5. Record result
        progress.addTaskInstance(
                new TaskInstance(task.getName(), status, request, Instant.now())
        );

        // 6. Advance flow only if step is fully completed
        updateProgress(progress, currentStep, status);

        // 7. Persist state
        repository.save(progress);
        return status;
    }

    /**
     * Validates that all tasks preceding the current task in the step
     * have been successfully completed.
     *.
     * Enforces execution order within a step.
     *.
     * @return true if all previous tasks are PASSED, false otherwise
     */
    private boolean isPreviousTasksCompleted(UserProgress progress, Step step, Task<?> currentTask) {

        for (Task<?> task : step.getTasks()) {

            if (task.getName().equals(currentTask.getName())) return true;

            TaskInstance instance = progress.getTasks().get(task.getName());

            if (instance == null || instance.getStatus() != TaskStatus.PASSED) return false;
        }
        return true;
    }

    /**
     * Checks whether all tasks in the given step have been completed successfully.
     *.
     * A step is considered complete only if every task has a PASSED status.
     *.
     * @return true if the step is fully completed, false otherwise
     */
    private boolean isStepCompleted(UserProgress progress, Step step) {

        for (Task<?> task : step.getTasks()) {

            TaskInstance instance = progress.getTasks().get(task.getName());

            if (instance == null || instance.getStatus() != TaskStatus.PASSED) {
                return false;
            }
        }

        return true;
    }


    /**
     * Updates the user's progression in the flow based on the task result.
     *.
     * - FAILED → marks user as rejected and stops the flow
     * - PASSED → advances to the next step if current step is complete,
     *            otherwise remains in the same step
     * - If no next step exists → marks user as accepted
     */
    private void updateProgress(UserProgress progress, Step currentStep, TaskStatus status) {

        if (status == TaskStatus.FAILED) {
            progress.markRejected();
            return;
        }

        if (isStepCompleted(progress, currentStep)) {
            flow.getNextStep(currentStep.getName())
                    .ifPresentOrElse(
                            step -> progress.advance(step.getName(), null),
                            progress::markAccepted
                    );
        }
    }

    public UserProgress getProgress(String userId) {
        return repository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found" + userId));
    }

    public String createUser(String email) {
        String userId = UUID.randomUUID().toString();
        repository.save(new UserProgress(userId));
        return userId;
    }

    public FlowDefinition getFlow() {
        return flow;
    }
}