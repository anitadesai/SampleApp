package org.researchstack.sampleapp;

import android.content.Context;

import org.researchstack.backbone.result.TaskResult;
import org.researchstack.backbone.step.Step;
import org.researchstack.backbone.task.Task;
import org.researchstack.skin.model.TaskModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Anita on 6/13/2016.
 */
public class InterventionTask extends Task {

    private HashMap<String, Step> steps;
    private HashMap<String, List<TaskModel.RuleModel>> rules;

    private List<String> staticStepIdentifiers;
    private List<String> dynamicStepIdentifiers;

    /**
     * Class constructor specifying a unique identifier.
     *
     * @param context context for fetching any resources needed
     * @param taskModel Java representation of the task json
     */
    public InterventionTask(Context context, TaskModel taskModel) {
        super(taskModel.identifier);

        steps = new HashMap<>(taskModel.elements.size());
        rules = new HashMap<>();
        staticStepIdentifiers = new ArrayList<>(taskModel.elements.size());
        for(TaskModel.StepModel stepModel : taskModel.elements) {
            // Add if statements helping initialization for each type of step
            if (stepModel.type.equals("PictureIntervention")){
                PictureStep pictureStep = new PictureStep(stepModel.identifier);
                // TODO: do stuff to properly initialize PictureStep
                steps.put(stepModel.identifier, pictureStep);
            } else if (stepModel.type.equals("")){
                // other types
            } else {
                throw new UnsupportedOperationException("Wasn't an intervention step");
            }
        }
    }

    // PUT OPERATOR VALUES FOR RULES HERE
    //private static final String OPERATOR_ = "";

    private String checkRule(TaskModel.RuleModel stepRule, Object answer){

        // skipTo is misleading in this case
        // dictates next step instead of just moving down list to this step
        // can we dynamically create the steps for this task?
        // how does that work with results/proliferation?
        String operator = stepRule.operator;
        String skipTo = stepRule.skipTo;
        Object value = stepRule.value;

        if (operator.equals("")){

        }
        if (answer.equals("")){

        }
        // etc.

        return null;
    }

    @Override
    public Step getStepAfterStep(Step step, TaskResult result) {
        return null;
    }

    @Override
    public Step getStepBeforeStep(Step step, TaskResult result) {
        return null;
    }

    @Override
    public Step getStepWithIdentifier(String identifier) {
        return null;
    }

    @Override
    public TaskProgress getProgressOfCurrentStep(Step step, TaskResult result) {
        return null;
    }

    @Override
    public void validateParameters() {

    }
}
