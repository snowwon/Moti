package net.zoo9.moti.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.zoo9.moti.R;

/**
 * Created by sheldon on 16. 4. 3..
 */
public class MotiDBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "moti.db";

    public MotiDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.valueOf(R.string.moti_db_create));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.valueOf(R.string.moti_db_drop_tables));
        onCreate(db);
    }
}
