package org.researchstack.sampleapp.bridge;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.sqlcipher.database.SQLiteDatabase;

import org.researchstack.backbone.result.StepResult;
import org.researchstack.backbone.result.TaskResult;
import org.researchstack.backbone.storage.database.StepRecord;
import org.researchstack.backbone.storage.database.sqlite.SqlCipherDatabaseHelper;
import org.researchstack.backbone.storage.database.sqlite.UpdatablePassphraseProvider;
import org.researchstack.backbone.utils.FormatHelper;
import org.researchstack.backbone.utils.LogExt;
import org.researchstack.sampleapp.User;
import org.researchstack.sampleapp.UserDataManager;

import java.sql.SQLException;
import java.util.List;

import co.touchlab.squeaky.db.sqlcipher.SQLiteDatabaseImpl;
import co.touchlab.squeaky.table.TableUtils;


public class BridgeEncryptedDatabase extends SqlCipherDatabaseHelper implements UploadQueue
{
    private static final String TAG = "BridgeEncryptedDatabase";

    public BridgeEncryptedDatabase(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version, UpdatablePassphraseProvider passphraseProvider)
    {
        super(context, name, cursorFactory, version, passphraseProvider);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        super.onCreate(sqLiteDatabase);
        try
        {
            TableUtils.createTables(new SQLiteDatabaseImpl(sqLiteDatabase), UploadRequest.class);
        }
        catch(SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
        super.onUpgrade(sqLiteDatabase, oldVersion, newVersion);
        // handle future db upgrades here
    }

    public void saveUploadRequest(UploadRequest uploadRequest)
    {
        LogExt.d(this.getClass(), "saveUploadRequest() id: " + uploadRequest.id);

        try
        {
            this.getDao(UploadRequest.class).createOrUpdate(uploadRequest);
        }
        catch(SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public List<UploadRequest> loadUploadRequests()
    {
        try
        {
            return this.getDao(UploadRequest.class).queryForAll().orderBy("id DESC").list();
        }
        catch(SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void deleteUploadRequest(UploadRequest request)
    {

        LogExt.d(this.getClass(), "deleteUploadRequest() id: " + request.id);

        try
        {
            this.getDao(UploadRequest.class).delete(request);
        }
        catch(SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveTaskResult(TaskResult taskResult){
        super.saveTaskResult(taskResult);

        Gson gson = new GsonBuilder().setDateFormat(FormatHelper.DATE_FORMAT_ISO_8601).create();

        for (StepResult stepResult : taskResult.getResults().values()) {
            Log.i(TAG, "saving task result");
            if (stepResult != null) {
                StepRecord stepRecord = new StepRecord();
                stepRecord.taskId = taskResult.getIdentifier();
                stepRecord.stepId = stepResult.getIdentifier();
                stepRecord.completed = stepResult.getEndDate();
                if (!stepResult.getResults().isEmpty()) {
                    stepRecord.result = gson.toJson(stepResult.getResults());
                }

                if (stepResult.getIdentifier().equals("distress_level")) {
                    User current = UserDataManager.getInstance().getCurrentUser();
                    Log.i(TAG, "saveTaskResult: updating distress streak of " + current.getName());

                    if (Integer.parseInt(stepRecord.result.replaceAll("\\D", "")) >= 5) {
                        current.setDistressStreak(current.getDistressStreak() + 1);
                    } else {
                        current.setDistressStreak(0);
                    }

                    Log.i(TAG, "saveTaskResult: new distress streak: " + current.getDistressStreak());
                }
                Log.i(TAG, "saveTaskResult: ID: " + stepRecord.stepId + " result: " + stepRecord.result.toString());
            }
        }
    }
}
