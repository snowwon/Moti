package net.zoo9.moti;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import net.zoo9.moti.model.BoardManager;

public class SplashActivity extends Activity {

    private final int SPLASH_TIME_OUT_IN_SEC = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final int board_id_of_activated_board = BoardManager.getInstance(getApplicationContext()).getCurrentBoardId();
        Log.d("unja", "activated board id: "+board_id_of_activated_board);

        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {
                if (board_id_of_activated_board < 0) {
                    Intent intent = new Intent(SplashActivity.this, GuideForCreationActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("board_id", board_id_of_activated_board);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT_IN_SEC * 1000);
    }
}
