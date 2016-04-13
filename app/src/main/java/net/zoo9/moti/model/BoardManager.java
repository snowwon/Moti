package net.zoo9.moti.model;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * Created by sheldon on 16. 4. 10..
 */
public class BoardManager {
    private static BoardManager singleInstance;
    private Context mContext;

    private BoardManager(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static BoardManager getInstance(Context context) {
        if (singleInstance == null) {
            singleInstance = new BoardManager(context);
        }

        return singleInstance;
    }


    public int createBoard(String userName, Integer stickerSize, String listOfGoals, String prize) {
        MySQLiteHandler mySQLiteHandler = MySQLiteHandler.open(mContext);

        String sql = "insert into boards(assignee, goals, prize, number_of_stickers, current_pos_of_sticker) values (\'"
                + userName + "\', \'"+listOfGoals+"\', \'"+prize+"\', "+stickerSize+", 0);";
        Log.d("unja", "sql: "+sql);
        mySQLiteHandler.executeSQL(sql);

        Cursor cursor = mySQLiteHandler.select("select _id from boards where assignee = ? and goals = ?", new String[]{userName, listOfGoals});
        int boardId = -1;
        while (cursor.moveToNext()) {
            boardId = cursor.getInt(0);
        }
        return boardId;
    }

    public Board getBoard(int boardId) {
        MySQLiteHandler mySQLiteHandler = MySQLiteHandler.open(mContext);
        Cursor cursor = mySQLiteHandler.select("select _id, assignee, goals, prize, number_of_stickers, current_pos_of_sticker from boards where _id = ?", new String[]{Integer.toString(boardId)});

        Board board = new Board();
        while (cursor.moveToNext()) {
            board.boardId = cursor.getInt(cursor.getColumnIndex("_id"));
            board.userName = cursor.getString(cursor.getColumnIndex("assignee"));
            board.prize = cursor.getString(cursor.getColumnIndex("prize"));
            board.stickerPos = cursor.getInt(cursor.getColumnIndex("current_pos_of_sticker"));
            board.stickerSize = cursor.getInt(cursor.getColumnIndex("number_of_stickers"));
        }

        return  board;
    }
}
