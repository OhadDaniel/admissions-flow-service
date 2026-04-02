package com.masterschool.admissions.controller;

import com.masterschool.admissions.domain.TaskStatus;
import com.masterschool.admissions.dto.*;
import com.masterschool.admissions.facade.AdmissionsFacade;
import com.masterschool.admissions.task.TaskName;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for executing tasks in the admissions flow.
 *.
 * Each endpoint represents a specific task.
 */
@RestController
@RequestMapping("/admissions/users/{userId}")
@RequiredArgsConstructor
public class TaskController {

    private final AdmissionsFacade facade;

    @PutMapping("/{userId}/personal-details")
    public TaskResponse personalDetails(
            @PathVariable String userId,
            @RequestBody PersonalDetailsRequest request
    ) {
        TaskStatus status = facade.handleTask(userId, TaskName.PERSONAL_DETAILS, request);
        return new TaskResponse(status);
    }

    @PutMapping("/{userId}/iq-test")
    public TaskResponse iqTest(
            @PathVariable String userId,
            @RequestBody IQRequest request
    ) {
        TaskStatus status = facade.handleTask(userId, TaskName.IQ_TEST, request);
        return new TaskResponse(status);
    }

    @PutMapping("/{userId}/schedule-interview")
    public TaskResponse scheduleInterview(
            @PathVariable String userId,
            @RequestBody ScheduleInterviewRequest request
    ) {
        TaskStatus status = facade.handleTask(userId, TaskName.SCHEDULE_INTERVIEW, request);
        return new TaskResponse(status);
    }

    @PutMapping("/{userId}/perform-interview")
    public TaskResponse performInterview(
            @PathVariable String userId,
            @RequestBody PerformInterviewRequest request
    ) {
        TaskStatus status = facade.handleTask(userId, TaskName.PERFORM_INTERVIEW, request);
        return new TaskResponse(status);
    }

    @PutMapping("/{userId}/upload-identification")
    public TaskResponse uploadIdentification(
            @PathVariable String userId,
            @RequestBody UploadIdentificationRequest request
    ) {
        TaskStatus status = facade.handleTask(userId, TaskName.UPLOAD_IDENTIFICATION, request);
        return new TaskResponse(status);
    }

    @PutMapping("/{userId}/sign-contract")
    public TaskResponse signContract(
            @PathVariable String userId,
            @RequestBody SignContractRequest request
    ) {
        TaskStatus status = facade.handleTask(userId, TaskName.SIGN_CONTRACT, request);
        return new TaskResponse(status);
    }

    @PutMapping("/{userId}/payment")
    public TaskResponse payment(
            @PathVariable String userId,
            @RequestBody PaymentRequest request
    ) {
        TaskStatus status = facade.handleTask(userId, TaskName.PAYMENT, request);
        return new TaskResponse(status);
    }

    @PutMapping("/{userId}/join-slack")
    public TaskResponse joinSlack(
            @PathVariable String userId,
            @RequestBody JoinSlackRequest request
    ) {
        TaskStatus status = facade.handleTask(userId, TaskName.JOIN_SLACK, request);
        return new TaskResponse(status);
    }

    @PutMapping("/{userId}/tasks/{taskName}")
    public TaskResponse executeTask(
            @PathVariable String userId,
            @PathVariable TaskName taskName,
            @RequestBody Object request
    ) {

        TaskStatus status = facade.handleTask(userId, taskName, request);

        return new TaskResponse(status);
    }
}