package com.masterschool.admissions.runtime;

import java.util.ArrayList;
import java.util.List;

public class UserFlow {
    private final List<RuntimeStep> steps;

    public UserFlow(List<RuntimeStep> steps) {
        this.steps = new ArrayList<>(steps);
    }

    public List<RuntimeStep> getSteps() {
        return steps;
    }

    public RuntimeStep getStep(int index) {
        return steps.get(index);
    }

    public int size() {
        return steps.size();
    }

    public void insertStep(int index, RuntimeStep step) {
        steps.add(index, step);
    }
}