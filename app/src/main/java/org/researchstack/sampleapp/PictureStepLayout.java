package org.researchstack.sampleapp;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import org.researchstack.backbone.result.StepResult;
import org.researchstack.backbone.step.Step;
import org.researchstack.backbone.ui.callbacks.StepCallbacks;
import org.researchstack.backbone.ui.step.body.StepBody;
import org.researchstack.backbone.ui.step.layout.StepLayout;

/**
 * Created by Anita on 6/13/2016.
 */
public class PictureStepLayout extends FrameLayout implements StepLayout {

    // find a suitable view to extend

    public static final String TAG = PictureStepLayout.class.getSimpleName();

    private PictureStep pictureStep;
    private StepResult stepResult;

    private StepCallbacks callbacks;

    private StepBody stepBody;

    public PictureStepLayout(Context context) {
        super(context);
    }

    public PictureStepLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PictureStepLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void initialize(Step step, StepResult result) {

        if ( ! (step instanceof PictureStep)){
            throw  new RuntimeException("Step being used is not a PictureStep");
        }

        this.pictureStep = (PictureStep) step;
        this.stepResult = result;

        initializeStep();
    }

    public void initializeStep(){
        initStepLayout();
        initStepBody();
    }

    public void initStepLayout(){
        // Put code here to set up all of the elements of the layout

        if (pictureStep != null){
            // set up stuff that involves picture step attributes
        }

        // put navigation information here, what do the buttons do as a result?
        // EG skip buttons, like/dislike buttons
    }

    public void initStepBody(){
        // put code here that sets layout for body of step
    }
    @Override
    public View getLayout() {
        return this;
    }

    @Override
    public boolean isBackEventConsumed() {
        callbacks.onSaveStep(StepCallbacks.ACTION_PREV, getStep(), stepBody.getStepResult(false));
        return false;
    }

    @Override
    public void setCallbacks(StepCallbacks callbacks){
        this.callbacks = callbacks;
    }

    @Override
    public Parcelable onSaveInstanceState(){
        callbacks.onSaveStep(StepCallbacks.ACTION_NONE, getStep(), stepBody.getStepResult(false));
        return super.onSaveInstanceState();
    }

    public Step getStep(){
        return pictureStep;
    }

    // set for layout resource file
    /*@Override
    public int getContentResourceId(){
        return null;
    }*/

    public String getString(@StringRes int stringResId) {
        return getResources().getString(stringResId);
    }
}
