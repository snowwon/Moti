package net.zoo9.moti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GuideForCreationActivity extends AppCompatActivity {

    final GuideForCreationActivity self = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_for_creation);

        this.getWindow().setTitle("환영합니다.");

        ((Button) findViewById(R.id.create_board_button_at_intro)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(self, CreateBoardActivity.class));
                finish();
            }
        });

    }
}
