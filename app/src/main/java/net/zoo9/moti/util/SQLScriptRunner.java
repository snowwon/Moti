package net.zoo9.moti.util;

import android.database.sqlite.SQLiteDatabase;

import java.util.StringTokenizer;

/**
 * Created by sheldon on 16. 4. 10..
 */
public class SQLScriptRunner {
    public static void runMultiStatement(SQLiteDatabase db, String statementsWithSemiColons) {
        StringTokenizer tokenizer = new StringTokenizer(statementsWithSemiColons, ";");
        while (tokenizer.hasMoreTokens()) {
            db.execSQL(tokenizer.nextToken());
        }
    }
}
