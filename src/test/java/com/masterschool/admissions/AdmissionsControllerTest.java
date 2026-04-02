package com.masterschool.admissions;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.masterschool.admissions.builder.AdmissionsSystemBuilder;
import com.masterschool.admissions.controller.AdmissionsController;
import com.masterschool.admissions.controller.TaskController;
import com.masterschool.admissions.domain.UserStatus;
import com.masterschool.admissions.dto.*;
import com.masterschool.admissions.exception.UserNotFoundException;
import com.masterschool.admissions.facade.AdmissionsFacade;
import com.masterschool.admissions.flow.StepName;
import com.masterschool.admissions.task.TaskName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterschool.admissions.service.TaskRequestMapper;

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
    private ObjectMapper objectMapper;
    private TaskRequestMapper taskRequestMapper;

    /**
     * Initializes the system using the production-like builder.
     * Ensures all components are wired consistently.
     */
    @BeforeEach
    void setUp() {
        facade = AdmissionsSystemBuilder.build();

        admissionsController = new AdmissionsController(facade);

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        taskRequestMapper = new TaskRequestMapper(objectMapper);
        taskController = new TaskController(facade, taskRequestMapper);
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

        completeTask(userId, StepName.PERSONAL_DETAILS, TaskName.PERSONAL_DETAILS, validPersonal());

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
        completeTask(userId, StepName.PERSONAL_DETAILS, TaskName.PERSONAL_DETAILS, validPersonal());
        completeTask(userId, StepName.IQ_TEST, TaskName.IQ_TEST, validIQ(90));
        completeTask(userId, StepName.INTERVIEW, TaskName.SCHEDULE_INTERVIEW, validSchedule());
        completeTask(userId, StepName.INTERVIEW, TaskName.PERFORM_INTERVIEW, passedInterview());
        completeTask(userId, StepName.SIGN_CONTRACT, TaskName.UPLOAD_IDENTIFICATION, validUpload());
        completeTask(userId, StepName.SIGN_CONTRACT, TaskName.SIGN_CONTRACT, validSign());
        completeTask(userId, StepName.PAYMENT, TaskName.PAYMENT, validPayment());
        completeTask(userId, StepName.JOIN_SLACK, TaskName.JOIN_SLACK, validSlack());
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

    private void completeTask(String userId, StepName stepName, TaskName taskName, Object payload) {
        CompleteTaskRequest request =
                new CompleteTaskRequest(stepName, objectMapper.valueToTree(payload));

        taskController.completeTask(userId, taskName, request);
    }
}