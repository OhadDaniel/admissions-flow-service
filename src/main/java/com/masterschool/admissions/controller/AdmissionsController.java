package com.masterschool.admissions.controller;

import com.masterschool.admissions.domain.UserProgress;
import com.masterschool.admissions.domain.UserStatus;
import com.masterschool.admissions.dto.CurrentStateResponse;
import com.masterschool.admissions.dto.FlowResponse;
import com.masterschool.admissions.facade.AdmissionsFacade;
import com.masterschool.admissions.flow.FlowDefinition;
import com.masterschool.admissions.flow.Step;
import com.masterschool.admissions.flow.StepName;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Controller responsible for user lifecycle and flow state.
 *
 * Handles:
 * - User creation
 * - Flow retrieval
 * - Current state
 * - Status
 */
@RestController
@RequestMapping("/admissions")
@RequiredArgsConstructor
public class AdmissionsController {

    private final AdmissionsFacade facade;
    private final FlowDefinition flow;

    /**
     * Creates a new user and returns a unique identifier.
     */
    @PostMapping("/users")
    public String createUser(@RequestParam String email) {
        return facade.createUser(email);
    }


    /**
     * Retrieves the full admissions flow along with the user's current progress.
     *.
     * The response combines:
     * - The complete list of steps in the flow (structure)
     * - The user's current position within the flow (index)
     * - The current task the user is expected to complete
     * - The overall user status (IN_PROGRESS / ACCEPTED / REJECTED)
     * @param userId the unique identifier of the user
     * @return a FlowResponse containing flow structure and user state
     *
     * @throws IllegalArgumentException if the user does not exist
     */
    @GetMapping("/users/{userId}/flow")
    public FlowResponse getUserFlow(@PathVariable String userId) {

        UserProgress progress = facade.getProgress(userId);
        List<Step> steps = flow.getSteps();

        StepName currentStepName = progress.getCurrentStep();

        int currentIndex = -1;

        if (currentStepName != null) {
            for (int i = 0; i < steps.size(); i++) {
                if (steps.get(i).getName() == currentStepName) {
                    currentIndex = i;
                    break;
                }
            }
        }

        return new FlowResponse(
                steps,
                currentIndex,
                progress.getCurrentTask() != null
                        ? progress.getCurrentTask().name()
                        : null,
                progress.getStatus()
        );
    }

    /**
     * Returns the current step and task of a user.
     */
    @GetMapping("/users/{userId}/current-step")
    public CurrentStateResponse getCurrentState(@PathVariable String userId) {

        UserProgress progress = facade.getProgress(userId);

        if (progress.getCurrentStep() == null) {
            return new CurrentStateResponse(null, null);
        }

        return new CurrentStateResponse(
                flow.getStep(progress.getCurrentStep()),
                progress.getCurrentTask() != null
                        ? progress.getCurrentTask().name()
                        : null
        );
    }

    /**
     * Returns the user's current status.
     */
    @GetMapping("/users/{userId}/status")
    public UserStatus getStatus(@PathVariable String userId) {
        return facade.getProgress(userId).getStatus();
    }
}