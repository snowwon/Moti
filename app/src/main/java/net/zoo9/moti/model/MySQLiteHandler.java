package net.zoo9.moti.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MySQLiteHandler  {
    MySQLiteOpenHelper helper;
    SQLiteDatabase db;

    // 초기화 작업
    public MySQLiteHandler(Context context) {
        helper = new MySQLiteOpenHelper(context);
    }

    //open
    public static MySQLiteHandler open(Context context) {
        return new MySQLiteHandler(context);
    }


    //close
    public void close() {
        db.close();
    }

    //저장
    public void executeSQL(String sql) {
        db = helper.getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }//end insert

    //검색
    public Cursor select(String sql, String[] selectionArgs) {
        db = helper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, selectionArgs);
        return c;
    }//end select
}//end class