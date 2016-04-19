package net.zoo9.moti.model;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by sheldon on 16. 4. 10..
 */
public class StickerHistoryManager {
    private static StickerHistoryManager singleInstance;
    private Context mContext;

    private StickerHistoryManager(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static StickerHistoryManager getInstance(Context context) {
        if (singleInstance == null) {
            singleInstance = new StickerHistoryManager(context);
        }

        return singleInstance;
    }



    public void removeLastSticker() {

    }

    public void addNewSticker(int boardId) {
        MySQLiteHandler mySQLiteHandler = MySQLiteHandler.open(mContext);
        String sqlForAddingNewSticker = "insert into sticker_histories (board_id, check_date) values (" +
                boardId +", DATETIME('now'))";
        mySQLiteHandler.executeSQL(sqlForAddingNewSticker);
        mySQLiteHandler.close();
    }

    public List<Date> getStickerHistories(int boardId) throws ParseException {
        MySQLiteHandler mySQLiteHandler = MySQLiteHandler.open(mContext);
        Cursor cursor = mySQLiteHandler.select("select check_date from sticker_histories where board_id = ? order by check_date asc", new String[]{Integer.toString(boardId)});

        List<Date> stickerDates = new ArrayList<Date>();
        while (cursor.moveToNext()) {
            Calendar t = new GregorianCalendar();
            String checked_date = cursor.getString(cursor.getColumnIndex("check_date"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date dt = sdf.parse(checked_date);
            stickerDates.add(dt);
        }

        return  stickerDates;
    }
}
