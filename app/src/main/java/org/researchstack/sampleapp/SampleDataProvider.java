package org.researchstack.sampleapp;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;
import org.researchstack.backbone.ResourcePathManager;
import org.researchstack.backbone.StorageAccess;
import org.researchstack.backbone.result.TaskResult;
import org.researchstack.backbone.storage.database.AppDatabase;
import org.researchstack.backbone.task.Task;
import org.researchstack.backbone.utils.LogExt;
import org.researchstack.sampleapp.bridge.BridgeDataProvider;
import org.researchstack.skin.DataProvider;
import org.researchstack.skin.DataResponse;
import org.researchstack.skin.ResearchStack;
import org.researchstack.skin.ResourceManager;
import org.researchstack.skin.TaskProvider;
import org.researchstack.skin.model.SchedulesAndTasksModel;
import org.researchstack.skin.model.TaskModel;
import org.researchstack.skin.model.User;
import org.researchstack.skin.schedule.ScheduleHelper;
import org.researchstack.skin.task.SmartSurveyTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscriber;


public class SampleDataProvider extends DataProvider {
    Observable<DataResponse> obs;

    @Override
    public Observable<DataResponse> initialize(Context context) {
        obs = Observable.create(new Observable.OnSubscribe<DataResponse>() {

            @Override
            public void call(Subscriber<? super DataResponse> subscriber) {
                subscriber.onNext(new DataResponse(true, null));
            }
        });

        return obs;
    }

    @Override
    public Observable<DataResponse> signUp(Context context, String s, String s1, String s2) {
        LogExt.i(SampleDataProvider.class, "User signing up");
        return obs;
    }

    @Override
    public Observable<DataResponse> signIn(Context context, String s, String s1) {
        LogExt.i(SampleDataProvider.class, "User signing in");
        return obs;
    }

    @Override
    public Observable<DataResponse> signOut(Context context) {
        LogExt.i(SampleDataProvider.class, "User signing out");
        return obs;
    }

    @Override
    public Observable<DataResponse> resendEmailVerification(Context context, String s) {
        LogExt.i(SampleDataProvider.class, "User asking to resend email");
        return obs;
    }

    @Override
    public boolean isSignedUp(Context context) {
        return false;
    }

    @Override
    public boolean isSignedIn(Context context) {
        return false;
    }

    @Override
    public boolean isConsented(Context context) {
        return false;
    }

    @Override
    public Observable<DataResponse> withdrawConsent(Context context, String reason) {
        LogExt.i(SampleDataProvider.class, "Withdrawing consent");
        return obs;
    }

    @Override
    public void uploadConsent(Context context, TaskResult consentResult) {
        LogExt.i(SampleDataProvider.class, "Consent uploaded");
    }

    @Override
    public void saveConsent(Context context, TaskResult consentResult) {
        LogExt.i(SampleDataProvider.class, "Consent saved");
    }

    @Override
    public User getUser(Context context) {
        return UserDataManager.getInstance().getCurrentUser();
    }

    @Override
    public String getUserSharingScope(Context context) {
        return "here";
    }

    @Override
    public void setUserSharingScope(Context context, String s) {
    }

    @Override
    public String getUserEmail(Context context) {
        return UserDataManager.getInstance().getCurrentUser().getEmail();
    }

    @Override
    public void uploadTaskResult(Context context, TaskResult taskResult) {

    }

    @Override
    public SchedulesAndTasksModel loadTasksAndSchedules(Context context) {
        SchedulesAndTasksModel schedulesAndTasksModel = ResourceManager.getInstance().getTasksAndSchedules().create(context);

        AppDatabase db = StorageAccess.getInstance().getAppDatabase();

        List<SchedulesAndTasksModel.ScheduleModel> schedules = new ArrayList<>();
        for (SchedulesAndTasksModel.ScheduleModel schedule : schedulesAndTasksModel.schedules) {

            SchedulesAndTasksModel.TaskScheduleModel task = schedule.tasks.get(0);
            LogExt.d(SampleDataProvider.class, "Loading task " + task.taskClassName);

            String resultstaskid = null;

            if (task.taskClassName != null && task.taskClassName.equalsIgnoreCase("SmartSurveyTask")) {
                TaskModel taskModel = ResourceManager.getInstance().getTask(task.taskFileName).create(context);
                resultstaskid = taskModel.identifier;
            } else if (task.taskClassName != null && task.taskClassName.equalsIgnoreCase("InterventionTask")) {
                //InterventionTask interventionTask = ResourceManager.getInstance().getTask(task.taskFileName).create(context);
                schedules.add(schedule);

            } else if (task.taskClassName != null && task.taskClassName.equalsIgnoreCase("OrderedTask")) {

            } else resultstaskid = task.taskID;

            if (resultstaskid != null) {
                TaskResult result = db.loadLatestTaskResult(resultstaskid);

                 if (schedule.scheduleType.equalsIgnoreCase("DistressBased")) {
                    if (Integer.parseInt(schedule.scheduleString) <= UserDataManager.getInstance().getCurrentUser().getDistressStreak()) {
                        schedules.add(schedule);
                    }
                } else if (result == null) {
                    schedules.add(schedule);
                } else if (StringUtils.isNotEmpty(schedule.scheduleString)) {
                        Date date = ScheduleHelper.nextSchedule(schedule.scheduleString, result.getEndDate());
                        if (date.before(new Date())) {
                            schedules.add(schedule);
                        }
                    }
                }
            }

            schedulesAndTasksModel.schedules = schedules;
            return schedulesAndTasksModel;
        }


        @Override
        public Task loadTask (Context context, SchedulesAndTasksModel.TaskScheduleModel task){
        if(task.taskClassName.equalsIgnoreCase("SmartSurveyTask")){
            TaskModel taskModel = ResourceManager.getInstance().getTask(task.taskFileName).create(context);
            SmartSurveyTask smartSurveyTask = new SmartSurveyTask(context, taskModel);
            return smartSurveyTask;
        } else if (task.taskClassName.equalsIgnoreCase("InterventionTask")) {
            TaskModel interventionTaskModel = ResourceManager.getInstance().getTask(task.taskFileName).create(context);
            return new InterventionTask(context, interventionTaskModel);
        } else {
            LogExt.e(SampleDataProvider.class, "Task with classname "+task.taskClassName+"cannot be found");
            return null;
        }
        }


        @Override
        public void processInitialTaskResult (Context context, TaskResult taskResult){

        }

        @Override
        public Observable<DataResponse> forgotPassword (Context context, String email){
            LogExt.i(SampleDataProvider.class, "Forgot password");
            return obs;
        }

    }
