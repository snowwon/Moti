package net.zoo9.moti.model;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sheldon on 16. 4. 10..
 */
public class GoalManager {
    private static GoalManager singleInstance;
    private Context mContext;

    private GoalManager(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static GoalManager getInstance(Context context) {
        if (singleInstance == null) {
            singleInstance = new GoalManager(context);
        }

        return  singleInstance;
    }

    public List<Goal> getDefaultGoals() {
        ArrayList<Goal> defaultGoals = new ArrayList<Goal>();
        Log.d("unja", "getDefaultGoals .................");
        MySQLiteHandler mySQLiteHandler = MySQLiteHandler.open(mContext);
        Cursor cursor = mySQLiteHandler.select("select * from goals", null);
        while (cursor.moveToNext()) {
            int goalId = cursor.getInt(0);
            String goalDesc = cursor.getString(1);
            defaultGoals.add(new Goal(goalId, goalDesc));
        }

        mySQLiteHandler.close();
        cursor.close();
        return defaultGoals;
    }
}