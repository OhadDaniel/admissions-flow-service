package com.masterschool.admissions.flow;

import java.util.List;
import java.util.Optional;

/**
 * Defines the ordered steps of the system.
 * This class is intentionally simple and does not contain logic.
 */
public class FlowDefinition {

    private final List<Step> steps;

    public FlowDefinition(List<Step> steps) {
        this.steps = steps;
    }

    public Step getFirstStep() {
        return steps.get(0);
    }

    public Step getStep(StepName name) {
        return steps.stream()
                .filter(step -> step.getName() == name)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException("Step not found in flow: " + name));
    }

    public Optional<Step> getNextStep(StepName current) {
        for (int i = 0; i < steps.size(); i++) {
            if (steps.get(i).getName() == current) {
                return (i + 1 < steps.size())
                        ? Optional.of(steps.get(i + 1))
                        : Optional.empty();
            }
        }
        throw new IllegalStateException("Step not found in flow: " + current);
    }

    public List<Step> getSteps() {
        return steps;
    }
}