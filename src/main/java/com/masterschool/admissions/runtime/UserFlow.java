package com.masterschool.admissions.runtime;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the runtime admissions flow assigned to a specific user.
 *
 * Unlike a single global static flow, this class stores the ordered
 * runtime steps that belong to one user instance.
 *
 * This design makes it possible to keep a per-user flow model and
 * supports future scenarios such as inserting user-specific steps.
 */
public class UserFlow {
    private final List<RuntimeStep> steps;

    /**
     * Creates a runtime flow for a user.
     *
     * A defensive copy is created to avoid external modification of
     * the original steps list passed into the constructor.
     *
     * @param steps ordered runtime steps assigned to the user
     */
    public UserFlow(List<RuntimeStep> steps) {
        this.steps = new ArrayList<>(steps);
    }

    /**
     * Returns all runtime steps in their current order.
     *
     * @return ordered list of runtime steps
     */
    public List<RuntimeStep> getSteps() {
        return steps;
    }

    /**
     * Returns the runtime step at the given index.
     *
     * @param index zero-based index of the requested step
     * @return runtime step at the given index
     */
    public RuntimeStep getStep(int index) {
        return steps.get(index);
    }

    /**
     * Returns the number of steps in the runtime flow.
     *
     * @return number of runtime steps
     */
    public int size() {
        return steps.size();
    }

    /**
     * Inserts a new step into the runtime flow at the given position.
     *
     * This supports future product scenarios where the flow may need
     * to change for a specific user.
     *
     * @param index position where the step should be inserted
     * @param step runtime step to insert
     */
    public void insertStep(int index, RuntimeStep step) {
        steps.add(index, step);
    }
}
