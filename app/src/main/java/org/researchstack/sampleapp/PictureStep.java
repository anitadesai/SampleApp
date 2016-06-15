package org.researchstack.sampleapp;

import org.researchstack.backbone.step.Step;

/**
 * Created by Anita on 6/13/2016.
 */
public class PictureStep extends Step {
    public PictureStep(String identifier) {
        super(identifier);
    }

    @Override
    public Class getStepLayoutClass() {
        return PictureStepLayout.class;
    }
}
