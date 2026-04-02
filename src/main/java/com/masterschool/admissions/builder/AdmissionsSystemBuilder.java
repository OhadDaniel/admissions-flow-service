package com.masterschool.admissions.builder;

import com.masterschool.admissions.facade.AdmissionsFacade;
import com.masterschool.admissions.repository.InMemoryUserProgressRepository;
import com.masterschool.admissions.task.TaskFactory;


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
        InMemoryUserProgressRepository repository = new InMemoryUserProgressRepository();
        TaskFactory taskFactory = TaskFactoryBuilder.build();
        return new AdmissionsFacade(repository,taskFactory);
    }
}
