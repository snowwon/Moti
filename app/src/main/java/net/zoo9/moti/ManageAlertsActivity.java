package net.zoo9.moti;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CursorJoiner;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import net.zoo9.moti.model.Board;
import net.zoo9.moti.model.BoardManager;
import net.zoo9.moti.model.StickerHistoryManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ManageAlertsActivity extends AppCompatActivity {
    public static int REMINDER_HOUR = 0;
    public static int REMINDER_MINUTES = 0;

    public final static String KEY_ALERT_HOUR = "alert_hour";
    public final static String KEY_ALERT_MINUTES = "alert_minutes";


    public boolean isAlertEnabled = false;

    private static SharedPreferences alertsSharedPreference;

    public SharedPreferences getStorage() {
        return this.getSharedPreferences("alert", MODE_PRIVATE);
    }

    private void setReminderHourAndMinutes(int hour, int minutes) {
        ManageAlertsActivity.REMINDER_HOUR = hour;
        ManageAlertsActivity.REMINDER_MINUTES = minutes;

        Log.d("unja66", "set minutes (setReminderHourAndMinutes):"+REMINDER_MINUTES);

        alertsSharedPreference = getStorage();
        SharedPreferences.Editor editor = alertsSharedPreference.edit();
        editor.putInt(KEY_ALERT_HOUR, REMINDER_HOUR);
        editor.putInt(KEY_ALERT_MINUTES, REMINDER_MINUTES);
        editor.commit();
        setScheduledReminder();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_alerts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("알림 설정");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        TimePicker timePicker = (TimePicker) findViewById(R.id.timepicker_for_reminder);
        timePicker.setCurrentHour(REMINDER_HOUR);
        timePicker.setCurrentMinute(REMINDER_MINUTES);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener(){
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                if (((CheckBox) findViewById(R.id.check_whether_use_reminder)).isChecked())
                setReminderHourAndMinutes(hourOfDay, minute);
            }
        });

        CheckBox checkBox = (CheckBox) findViewById(R.id.check_whether_use_reminder);
        showTimerZone(checkBox.isChecked());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showTimerZone(isChecked);
                SharedPreferences alertsSharedPreference = getStorage();
                SharedPreferences.Editor editor = alertsSharedPreference.edit();
                editor.putBoolean("isAlertEnabled", isChecked);
                editor.commit();
                setScheduledReminder();
            }
        });

        Button confirmButton = (Button)findViewById(R.id.button_confirm_for_alert);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                setScheduledReminder();
            }
        });
    }

    public void setScheduledReminder() {
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        Intent myIntent = new Intent(ManageAlertsActivity.this, MyRemindMessageReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ManageAlertsActivity.this, 0, myIntent,0);

        if (isAlertEnabled(this) == false) {
            alarmManager.cancel(pendingIntent);
            return;
        }else {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, REMINDER_HOUR); // For 1 PM or 2 PM
            calendar.set(Calendar.MINUTE, REMINDER_MINUTES);
            calendar.set(Calendar.SECOND, 0);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }


    @Override
    public void onResume(){
        super.onResume();
        alertsSharedPreference = getStorage();

        isAlertEnabled = alertsSharedPreference.getBoolean("isAlertEnabled", false);
        ((CheckBox)findViewById(R.id.check_whether_use_reminder)).setChecked(isAlertEnabled);

        int loadedHour = alertsSharedPreference.getInt(KEY_ALERT_HOUR, 22);
        int loadedMinutes = alertsSharedPreference.getInt(KEY_ALERT_MINUTES, 0);

        Log.d("unja66", "load time in on resume: "+REMINDER_MINUTES);

        showTimerZone(isAlertEnabled);
        setReminderHourAndMinutes(loadedHour, loadedMinutes);
        ((TimePicker)findViewById(R.id.timepicker_for_reminder)).setCurrentHour(loadedHour);
        ((TimePicker)findViewById(R.id.timepicker_for_reminder)).setCurrentMinute(loadedMinutes);

    }


    public void showTimerZone(boolean isChecked) {
        TimePicker timePicker = (TimePicker) findViewById(R.id.timepicker_for_reminder);
        if (isChecked) {
            timePicker.setCurrentHour(REMINDER_HOUR);
            Log.d("unja66", "set reminder minutes (showTimerZone): "+REMINDER_MINUTES);
            timePicker.setCurrentMinute(REMINDER_MINUTES);

            findViewById(R.id.panel_for_setting_the_reminder).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.panel_for_setting_the_reminder).setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        setScheduledReminder();
        return super.onOptionsItemSelected(item);
    }

    public boolean isAlertEnabled(Context context) {
        alertsSharedPreference = context.getSharedPreferences("alert", MODE_PRIVATE);
        boolean isAlertEnabled = alertsSharedPreference.getBoolean("isAlertEnabled", false);
        return isAlertEnabled;
    }

}