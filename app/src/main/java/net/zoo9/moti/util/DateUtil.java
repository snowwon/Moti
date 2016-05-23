package net.zoo9.moti.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sheldon on 16. 5. 24..
 */
public class DateUtil {
    public static Date getCurrentDate(){
        long now = System.currentTimeMillis();
        return new Date(now);
    }

    public static String getDateFormatedStringForSqlite(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }
}
