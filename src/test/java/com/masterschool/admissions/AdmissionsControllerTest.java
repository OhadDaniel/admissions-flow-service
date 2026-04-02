package com.masterschool.admissions;

import com.masterschool.admissions.builder.AdmissionsSystemBuilder;
import com.masterschool.admissions.controller.AdmissionsController;
import com.masterschool.admissions.controller.TaskController;
import com.masterschool.admissions.domain.UserStatus;
import com.masterschool.admissions.dto.*;
import com.masterschool.admissions.exception.UserNotFoundException;
import com.masterschool.admissions.facade.AdmissionsFacade;
import com.masterschool.admissions.flow.FlowDefinition;
import com.masterschool.admissions.repository.InMemoryUserProgressRepository;
import com.masterschool.admissions.task.TaskFactory;
import com.masterschool.admissions.task.TaskName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests controller layer behavior without HTTP.
 *
 * Verifies that controllers correctly delegate to the facade
 * and return consistent system state.
 */
class AdmissionsControllerTest {

    private AdmissionsFacade facade;
    private AdmissionsController admissionsController;
    private TaskController taskController;

    /**
     * Initializes the system using the production-like builder.
     * Ensures all components are wired consistently.
     */
    @BeforeEach
    void setUp() {

        facade = AdmissionsSystemBuilder.build();

        // חשוב: controller עדיין צריך flow
        FlowDefinition flow = facade.getFlow(); // 👈 תוסיף getter אם אין

        admissionsController = new AdmissionsController(facade, flow);
        taskController = new TaskController(facade);
    }

    // ======================
    // TESTS
    // ======================

    /**
     * Verifies user creation.
     */
    @Test
    void createUser_shouldReturnId() {

        String userId = admissionsController.createUser("test@mail.com");

        assertNotNull(userId);
        assertFalse(userId.isBlank());
    }

    /**
     * Verifies flow retrieval.
     */
    @Test
    void getFlow_shouldReturnUserProgress() {

        String userId = admissionsController.createUser("test@mail.com");

        FlowResponse response = admissionsController.getUserFlow(userId);

        assertNotNull(response);
        assertEquals(UserStatus.IN_PROGRESS, response.status());
    }

    /**
     * Verifies current step retrieval.
     */
    @Test
    void getCurrentStep_shouldReturnCorrectStep() {

        String userId = admissionsController.createUser("test@mail.com");

        taskController.personalDetails(userId, validPersonal());

        CurrentStateResponse response = admissionsController.getCurrentState(userId);

        assertNotNull(response);
    }

    /**
     * Verifies status endpoint after completing full flow.
     */
    @Test
    void getStatus_shouldReturnAcceptedAfterFullFlow() {

        String userId = admissionsController.createUser("test@mail.com");

        completeFullFlow(userId);

        UserStatus status = admissionsController.getStatus(userId);

        assertEquals(UserStatus.ACCEPTED, status);
    }

    // ======================
    // EDGE CASES
    // ======================

    /**
     * Verifies that requesting flow for a non-existing user throws an exception.
     */
    @Test
    void getFlow_nonExistingUser_shouldThrowException() {

        assertThrows(
                UserNotFoundException.class,
                () -> admissionsController.getUserFlow("invalid-id")
        );
    }

    /**
     * Verifies initial state before user starts the flow.
     */
    @Test
    void getFlow_beforeStart_shouldReturnInitialState() {

        String userId = admissionsController.createUser("test@mail.com");

        FlowResponse response = admissionsController.getUserFlow(userId);

        assertEquals(-1, response.currentStepIndex());
        assertEquals(UserStatus.IN_PROGRESS, response.status());
        assertNull(response.currentTask());
    }

    /**
     * Verifies current step endpoint before flow starts.
     */
    @Test
    void getCurrentState_beforeStart_shouldReturnNullStep() {

        String userId = admissionsController.createUser("test@mail.com");

        CurrentStateResponse response = admissionsController.getCurrentState(userId);

        assertNull(response.step());
        assertNull(response.currentTask());
    }

    // ======================
    // HELPERS
    // ======================

    /**
     * Executes the full flow for a user.
     */
    private void completeFullFlow(String userId) {
        taskController.personalDetails(userId, validPersonal());
        taskController.iqTest(userId, validIQ(90));
        taskController.scheduleInterview(userId, validSchedule());
        taskController.performInterview(userId, passedInterview());
        taskController.uploadIdentification(userId, validUpload());
        taskController.signContract(userId, validSign());
        taskController.payment(userId, validPayment());
        taskController.joinSlack(userId, validSlack());
    }

    private PersonalDetailsRequest validPersonal() {
        return new PersonalDetailsRequest("Ohad", "Daniel", "test@mail.com", java.time.Instant.now());
    }

    private IQRequest validIQ(double score) {
        return new IQRequest("test1", score, java.time.Instant.now());
    }

    private ScheduleInterviewRequest validSchedule() {
        return new ScheduleInterviewRequest(java.time.Instant.now());
    }

    private PerformInterviewRequest passedInterview() {
        return new PerformInterviewRequest(
                java.time.Instant.now(),
                "interviewer1",
                "passed_interview"
        );
    }

    private UploadIdentificationRequest validUpload() {
        return new UploadIdentificationRequest("A12345678", java.time.Instant.now());
    }

    private SignContractRequest validSign() {
        return new SignContractRequest(java.time.Instant.now());
    }

    private PaymentRequest validPayment() {
        return new PaymentRequest("payment123", java.time.Instant.now());
    }

    private JoinSlackRequest validSlack() {
        return new JoinSlackRequest("test@mail.com", java.time.Instant.now());
    }
}