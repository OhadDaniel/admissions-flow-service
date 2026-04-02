package com.masterschool.admissions.builder;

import com.masterschool.admissions.facade.AdmissionsFacade;
import com.masterschool.admissions.flow.FlowConfig;
import com.masterschool.admissions.flow.FlowDefinition;
import com.masterschool.admissions.repository.InMemoryUserProgressRepository;
import com.masterschool.admissions.task.TaskFactory;
import com.masterschool.admissions.task.TaskName;

/**
 * Responsible for constructing the full admissions system.
 *.
 * This class wires together all core components:
 * - Repository
 * - TaskFactory
 * - FlowDefinition
 * - AdmissionsFacade
 *.
 * Design notes:
 * - Acts as the system composition root.
 * - Ensures all dependencies are initialized in a consistent way.
 * - Simplifies test setup and improves maintainability.
 *.
 * Usage:
 * AdmissionsFacade facade = AdmissionsSystemBuilder.build();
 */
public class AdmissionsSystemBuilder {

    public static AdmissionsFacade build() {

        // 1. Create repository
        InMemoryUserProgressRepository repository = new InMemoryUserProgressRepository();

        // 2. Create task factory
        TaskFactory taskFactory = TaskFactoryBuilder.build();

        // 3. Build flow using tasks from factory
        FlowDefinition flow = FlowConfig.build(
                taskFactory.get(TaskName.PERSONAL_DETAILS),
                taskFactory.get(TaskName.IQ_TEST),
                taskFactory.get(TaskName.SCHEDULE_INTERVIEW),
                taskFactory.get(TaskName.PERFORM_INTERVIEW),
                taskFactory.get(TaskName.UPLOAD_IDENTIFICATION),
                taskFactory.get(TaskName.SIGN_CONTRACT),
                taskFactory.get(TaskName.PAYMENT),
                taskFactory.get(TaskName.JOIN_SLACK)
        );

        // 4. Create facade
        return new AdmissionsFacade(repository, flow, taskFactory);
    }
}
