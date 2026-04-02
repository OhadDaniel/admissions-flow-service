package com.masterschool.admissions;

import com.masterschool.admissions.builder.AdmissionsSystemBuilder;
import com.masterschool.admissions.domain.UserStatus;
import com.masterschool.admissions.dto.*;
import com.masterschool.admissions.exception.InvalidTaskOrderException;
import com.masterschool.admissions.exception.TaskNotAllowedException;
import com.masterschool.admissions.facade.AdmissionsFacade;
import com.masterschool.admissions.flow.StepName;
import com.masterschool.admissions.task.TaskName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AdmissionsFacade.
 *
 * This test suite verifies:
 * - User creation
 * - Task execution and validation
 * - Step transitions
 * - Failure handling
 * - Full flow completion
 * - Edge cases and invalid scenarios
 *
 * The system is initialized using AdmissionsSystemBuilder,
 * ensuring consistency with production wiring.
 */
public class AdmissionsFacadeTest {

    private AdmissionsFacade facade;

    /**
     * Initializes the system under test using the system builder.
     *
     * This ensures:
     * - No duplication of setup logic
     * - Consistent wiring of dependencies
     * - Production-like configuration
     */
    @BeforeEach
    void setUp() {
        facade = AdmissionsSystemBuilder.build();
    }

    // ======================
    // CORE TESTS
    // ======================

    /**
     * Verifies that a new user can be created successfully.
     */
    @Test
    void createUser_shouldReturnValidUserId() {

        String userId = facade.createUser("test@mail.com");

        assertNotNull(userId);
        assertFalse(userId.isBlank());
    }

    /**
     * Verifies that completing the first task advances the user to the next step.
     */
    @Test
    void firstTask_shouldAdvanceToNextStep() {

        String userId = facade.createUser("test@mail.com");

        facade.handleTask(userId, TaskName.PERSONAL_DETAILS, validPersonal());

        assertEquals(
                StepName.IQ_TEST,
                facade.getProgress(userId).getCurrentStep()
        );
    }

    /**
     * Verifies that tasks must be executed in correct order within a step.
     */
    @Test
    void performInterview_beforeSchedule_shouldThrowException() {

        String userId = facade.createUser("test@mail.com");

        advanceToInterviewStep(userId);

        assertThrows(
                InvalidTaskOrderException.class,
                () -> facade.handleTask(userId, TaskName.PERFORM_INTERVIEW, passedInterview())
        );
    }

    /**
     * Verifies that a failing task results in user rejection.
     */
    @Test
    void failingTask_shouldMarkUserAsRejected() {

        String userId = facade.createUser("test@mail.com");

        facade.handleTask(userId, TaskName.PERSONAL_DETAILS, validPersonal());
        facade.handleTask(userId, TaskName.IQ_TEST, validIQ(50));

        assertEquals(
                UserStatus.REJECTED,
                facade.getProgress(userId).getStatus()
        );
    }

    /**
     * Verifies that completing the full flow results in user acceptance.
     */
    @Test
    void fullFlow_shouldMarkUserAsAccepted() {

        String userId = facade.createUser("test@mail.com");

        completeFullFlow(userId);

        assertEquals(
                UserStatus.ACCEPTED,
                facade.getProgress(userId).getStatus()
        );
    }

    // ======================
    // EDGE CASES
    // ======================

    /**
     * Verifies that executing a task not belonging to current step fails.
     */
    @Test
    void invalidTaskForStep_shouldThrowException() {

        String userId = facade.createUser("test@mail.com");

        assertThrows(
                TaskNotAllowedException.class,
                () -> facade.handleTask(userId, TaskName.IQ_TEST, validIQ(90))
        );
    }

    /**
     * Verifies that rejected users cannot continue the flow.
     */
    @Test
    void rejectedUser_shouldNotContinueFlow() {

        String userId = facade.createUser("test@mail.com");

        facade.handleTask(userId, TaskName.PERSONAL_DETAILS, validPersonal());
        facade.handleTask(userId, TaskName.IQ_TEST, validIQ(50));

        assertThrows(
                IllegalStateException.class,
                () -> facade.handleTask(userId, TaskName.SCHEDULE_INTERVIEW, validSchedule())
        );
    }

    /**
     * Verifies that accepted users cannot execute additional tasks.
     */
    @Test
    void acceptedUser_shouldNotExecuteMoreTasks() {

        String userId = facade.createUser("test@mail.com");

        completeFullFlow(userId);

        assertThrows(
                IllegalStateException.class,
                () -> facade.handleTask(userId, TaskName.PERSONAL_DETAILS, validPersonal())
        );
    }

    /**
     * Verifies that executing the same task twice is not allowed.
     */
    @Test
    void sameTaskTwice_shouldFail() {

        String userId = facade.createUser("test@mail.com");

        facade.handleTask(userId, TaskName.PERSONAL_DETAILS, validPersonal());

        assertThrows(
                TaskNotAllowedException.class,
                () -> facade.handleTask(userId, TaskName.PERSONAL_DETAILS, validPersonal())
        );
    }

    /**
     * Verifies that task execution is recorded.
     */
    @Test
    void taskExecution_shouldBeRecorded() {

        String userId = facade.createUser("test@mail.com");;

        facade.handleTask(userId, TaskName.PERSONAL_DETAILS, validPersonal());

        assertFalse(facade.getProgress(userId).getTasks().isEmpty());
    }

    /**
     * Verifies user isolation.
     */
    @Test
    void multipleUsers_shouldNotAffectEachOther() {

        String user1 = facade.createUser("test1@mail.com");
        String user2 = facade.createUser("test2@mail.com");

        facade.handleTask(user1, TaskName.PERSONAL_DETAILS, validPersonal());

        assertNotNull(facade.getProgress(user1).getCurrentStep());
        assertNull(facade.getProgress(user2).getCurrentStep());
    }

    /**
     * Verifies partial flow remains IN_PROGRESS.
     */
    @Test
    void partialFlow_shouldStayInProgress() {

        String userId = facade.createUser("test@mail.com");

        facade.handleTask(userId, TaskName.PERSONAL_DETAILS, validPersonal());

        assertEquals(
                UserStatus.IN_PROGRESS,
                facade.getProgress(userId).getStatus()
        );
    }

    /**
     * Verifies that multi_task step requires all tasks to complete.
     */
    @Test
    void interviewStep_shouldRequireAllTasksBeforeAdvancing() {

        String userId = facade.createUser("test@mail.com");

        advanceToInterviewStep(userId);

        facade.handleTask(userId, TaskName.SCHEDULE_INTERVIEW, validSchedule());

        assertEquals(
                StepName.INTERVIEW,
                facade.getProgress(userId).getCurrentStep()
        );

        facade.handleTask(userId, TaskName.PERFORM_INTERVIEW, passedInterview());

        assertNotEquals(
                StepName.INTERVIEW,
                facade.getProgress(userId).getCurrentStep()
        );
    }

    // ======================
    // HELPERS
    // ======================

    private void advanceToInterviewStep(String userId) {
        facade.handleTask(userId, TaskName.PERSONAL_DETAILS, validPersonal());
        facade.handleTask(userId, TaskName.IQ_TEST, validIQ(80));
    }

    private void completeFullFlow(String userId) {
        facade.handleTask(userId, TaskName.PERSONAL_DETAILS, validPersonal());
        facade.handleTask(userId, TaskName.IQ_TEST, validIQ(90));
        facade.handleTask(userId, TaskName.SCHEDULE_INTERVIEW, validSchedule());
        facade.handleTask(userId, TaskName.PERFORM_INTERVIEW, passedInterview());
        facade.handleTask(userId, TaskName.UPLOAD_IDENTIFICATION, validUpload());
        facade.handleTask(userId, TaskName.SIGN_CONTRACT, validSign());
        facade.handleTask(userId, TaskName.PAYMENT, validPayment());
        facade.handleTask(userId, TaskName.JOIN_SLACK, validSlack());
    }

    private PersonalDetailsRequest validPersonal() {
        return new PersonalDetailsRequest("Ohad", "Daniel", "test@mail.com", Instant.now());
    }

    private IQRequest validIQ(double score) {
        return new IQRequest("test1", score, Instant.now());
    }

    private ScheduleInterviewRequest validSchedule() {
        return new ScheduleInterviewRequest(Instant.now());
    }

    private PerformInterviewRequest passedInterview() {
        return new PerformInterviewRequest(Instant.now(), "interviewer1", "passed_interview");
    }

    private UploadIdentificationRequest validUpload() {
        return new UploadIdentificationRequest("A12345678", Instant.now());
    }

    private SignContractRequest validSign() {
        return new SignContractRequest(Instant.now());
    }

    private PaymentRequest validPayment() {
        return new PaymentRequest("payment123", Instant.now());
    }

    private JoinSlackRequest validSlack() {
        return new JoinSlackRequest("test@mail.com", Instant.now());
    }
}