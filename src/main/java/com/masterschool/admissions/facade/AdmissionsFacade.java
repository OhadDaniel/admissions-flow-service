package com.masterschool.admissions.facade;

import com.masterschool.admissions.domain.TaskInstance;
import com.masterschool.admissions.domain.TaskStatus;
import com.masterschool.admissions.domain.UserProgress;
import com.masterschool.admissions.domain.UserStatus;
import com.masterschool.admissions.exception.InvalidTaskOrderException;
import com.masterschool.admissions.exception.TaskNotAllowedException;
import com.masterschool.admissions.exception.UserNotFoundException;
import com.masterschool.admissions.flow.FlowConfig;
import com.masterschool.admissions.flow.StepName;
import com.masterschool.admissions.repository.UserProgressRepository;
import com.masterschool.admissions.runtime.RuntimeStep;
import com.masterschool.admissions.runtime.UserFlow;
import com.masterschool.admissions.task.Task;
import com.masterschool.admissions.task.TaskFactory;
import com.masterschool.admissions.task.TaskName;

import java.time.Instant;
import java.util.UUID;

/**
 * Main orchestration layer for the admissions process.
 *
 * Responsibilities:
 * - Load and persist user progress
 * - Validate task execution against the user's current runtime flow
 * - Execute task logic through the task factory
 * - Advance, accept, or reject the user based on task results
 *
 * Notes:
 * - Task-specific business rules remain inside task implementations
 * - Each user owns a personal runtime flow (UserFlow)
 */
public class AdmissionsFacade {

    private final UserProgressRepository repository;
    private final TaskFactory taskFactory;

    public AdmissionsFacade(UserProgressRepository repository,TaskFactory taskFactory) {
        this.repository = repository;
        this.taskFactory = taskFactory;
    }

    /**
     * Executes a task for a user inside the user's current runtime flow.
     *
     * @param userId   unique user identifier
     * @param stepName step the client expects the task to belong to
     * @param taskName task to execute
     * @param request  task payload DTO
     * @param <T>      payload type
     * @return task execution result
     */
    public <T> TaskStatus handleTask(String userId, StepName stepName, TaskName taskName, T request) {

        UserProgress progress = repository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));

        if (progress.getStatus() == UserStatus.REJECTED ||
                progress.getStatus() == UserStatus.ACCEPTED) {
            throw new IllegalStateException("User flow already completed");
        }

        if (progress.getCurrentStep() == null) {
            RuntimeStep firstStep = progress.getUserFlow().getStep(0);
            progress.advance(firstStep.getName(), null);
        }

        if (progress.getCurrentStep() != stepName) {
            throw new TaskNotAllowedException("Task does not belong to the current step");
        }

        Task<T> task = taskFactory.get(taskName);
        RuntimeStep currentStep = getCurrentRuntimeStep(progress);

        boolean taskExists = currentStep.getTasks().stream()
                .anyMatch(name -> name == task.getName());

        if (!taskExists) {
            throw new TaskNotAllowedException("Task does not belong to current step");
        }

        if (progress.getTasks().containsKey(task.getName())) {
            throw new TaskNotAllowedException("Task already executed");
        }

        if (!isPreviousTasksCompleted(progress, currentStep, task)) {
            throw new InvalidTaskOrderException("Previous tasks must be completed first");
        }

        TaskStatus status = task.process(request);

        progress.addTaskInstance(
                new TaskInstance(task.getName(), status, request, Instant.now())
        );

        updateProgress(progress, currentStep, status);

        repository.save(progress);
        return status;
    }

    /**
     * Ensures all tasks before the current task inside the same step
     * were already completed successfully.
     */
    private boolean isPreviousTasksCompleted(UserProgress progress,
                                             RuntimeStep step,
                                             Task<?> currentTask) {

        for (TaskName taskName : step.getTasks()) {
            if (taskName == currentTask.getName()) {
                return true;
            }

            TaskInstance instance = progress.getTasks().get(taskName);
            if (instance == null || instance.getStatus() != TaskStatus.PASSED) {
                return false;
            }
        }
        return true;
    }

    /**
     * A step is complete only when all its tasks passed.
     */
    private boolean isStepCompleted(UserProgress progress, RuntimeStep step) {
        for (TaskName taskName : step.getTasks()) {
            TaskInstance instance = progress.getTasks().get(taskName);
            if (instance == null || instance.getStatus() != TaskStatus.PASSED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Advances the user to the next runtime step, accepts them,
     * or rejects them if the task failed.
     */
    private void updateProgress(UserProgress progress, RuntimeStep currentStep, TaskStatus status) {
        if (status == TaskStatus.FAILED) {
            progress.markRejected();
            return;
        }

        if (isStepCompleted(progress, currentStep)) {
            int currentIndex = getCurrentStepIndex(progress);

            if (currentIndex + 1 < progress.getUserFlow().size()) {
                RuntimeStep nextStep = progress.getUserFlow().getStep(currentIndex + 1);
                progress.advance(nextStep.getName(), null);
            } else {
                progress.markAccepted();
            }
        }
    }

    /**
     * Returns the runtime step matching the user's current step.
     */
    private RuntimeStep getCurrentRuntimeStep(UserProgress progress) {
        for (RuntimeStep step : progress.getUserFlow().getSteps()) {
            if (step.getName() == progress.getCurrentStep()) {
                return step;
            }
        }
        throw new IllegalStateException("Current step not found in user flow");
    }

    /**
     * Returns the index of the user's current step in the runtime flow.
     */
    private int getCurrentStepIndex(UserProgress progress) {
        for (int i = 0; i < progress.getUserFlow().getSteps().size(); i++) {
            if (progress.getUserFlow().getSteps().get(i).getName() == progress.getCurrentStep()) {
                return i;
            }
        }
        throw new IllegalStateException("Current step index not found in user flow");
    }

    /**
     * Returns the persisted progress of the requested user.
     *
     * @param userId unique user identifier
     * @return the current progress state of the user
     *
     * @throws UserNotFoundException if no user exists for the given identifier
     */
    public UserProgress getProgress(String userId) {
        return repository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
    }

    /**
     * Creates a new user and initializes a personal runtime admissions flow for them.
     *
     * The user starts with:
     * - a unique generated identifier
     * - the provided email
     * - a fresh {@link UserFlow} created from the current flow configuration
     * - initial status of IN_PROGRESS
     *
     * @param email user email
     * @return the unique identifier of the newly created user
     */
    public String createUser(String email) {
        String userId = UUID.randomUUID().toString();
        UserFlow userFlow = FlowConfig.createUserFlow();
        repository.save(new UserProgress(userId, email, userFlow));
        return userId;
    }


}