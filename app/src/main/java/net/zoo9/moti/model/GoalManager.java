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
        MySQLiteHandler mySQLiteHandler = MySQLiteHandler.open(mContext);
        Cursor cursor = mySQLiteHandler.select("select * from goals order by _id desc", null);
        while (cursor.moveToNext()) {
            int goalId = cursor.getInt(0);
            String goalDesc = cursor.getString(1);
            boolean is_default = false;
            if (cursor.getInt(2) >= 1) is_default = true;

            defaultGoals.add(new Goal(goalId, goalDesc, is_default));
        }

        mySQLiteHandler.close();
        cursor.close();
        return defaultGoals;
    }

    public Goal addNewGoal(String newGoalText) {
        MySQLiteHandler mySQLiteHandler = MySQLiteHandler.open(mContext);
        String sql = "insert into goals(goal) values (\'" + newGoalText + "\');";
        mySQLiteHandler.executeSQL(sql);

        Cursor cursor = mySQLiteHandler.select("select * from goals where goal = ?", new String[]{newGoalText});
        Goal newGoal = null;
        while (cursor.moveToNext()) {
            int goalId = cursor.getInt(0);
            String goalDesc = cursor.getString(1);
            boolean is_default = false;

            newGoal = new Goal(goalId, goalDesc, is_default);
        }
        return newGoal;
    }

    public void removeGoal(int goal_id) {
        MySQLiteHandler mySQLiteHandler = MySQLiteHandler.open(mContext);
        String sql = "delete from goals where _id = "+goal_id+ ";";
        mySQLiteHandler.executeSQL(sql);
    }
}
