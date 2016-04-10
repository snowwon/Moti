package net.zoo9.moti.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import net.zoo9.moti.R;

/**
 * Created by sheldon on 16. 4. 3..
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION = 4;
    private static final String DATABASE_NAME = "moti.db";
    private Context context;

    public MySQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        Log.d("unja", "MySQLiteOpenHelper is created.");
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("unja", "onCreate is invoked."+context.getString(R.string.moti_db_create));
        db.execSQL(context.getString(R.string.moti_db_create));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("unja", "onUpgrae is invoked.");
        db.execSQL(context.getString(R.string.moti_db_drop_tables));
        onCreate(db);
    }
}
