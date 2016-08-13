package net.zoo9.moti;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import net.zoo9.moti.model.Board;
import net.zoo9.moti.model.BoardManager;

import java.util.List;

public class GuideForCreationActivity extends AppCompatActivity {

    final GuideForCreationActivity self = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_for_creation);

        this.getWindow().setTitle("환영합니다.");

        ((Button) findViewById(R.id.create_board_button_at_intro)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(self, CreateBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        ((Button) findViewById(R.id.go_to_previous_boards_activity)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(self, ManageBoardsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();

        List<Board> previousBoards = BoardManager.getInstance(GuideForCreationActivity.this).getPastBoards();

        Button button = (Button) findViewById(R.id.go_to_previous_boards_activity);
        button.setPaintFlags(button.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        if (previousBoards != null && previousBoards.size() > 0) {
            button.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.GONE);
        }

    }
}
