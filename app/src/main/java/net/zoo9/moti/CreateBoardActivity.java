package net.zoo9.moti;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.zoo9.moti.model.BoardManager;

import java.util.List;

public class CreateBoardActivity extends AppCompatActivity implements SetGoalsDialgFragment.SetGoalDialogFragmentInterface {
    public static final int MAX_SIZE_OF_STICKERS = 100;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("스티커판 만들기");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Button setGoalsButton = (Button)findViewById(R.id.btn_set_goals);
        setGoalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                SetGoalsDialgFragment setGoalsDialgFragment = new SetGoalsDialgFragment();
                setGoalsDialgFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                setGoalsDialgFragment.show(fragmentManager, "goal_dialog");
            }
        });

        this.context = getApplicationContext();

        ((EditText)findViewById(R.id.edit_text_sticker_size)).addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if ( s == null || s.toString().trim().equals("")) {return;}
                int sizeOfBoard = Integer.parseInt(s.toString());
                if (sizeOfBoard > MAX_SIZE_OF_STICKERS) {
                    Toast.makeText(CreateBoardActivity.this, "스티커 개수는 최대 "+MAX_SIZE_OF_STICKERS+"개 입니다.", Toast.LENGTH_SHORT).show();
                    s.replace(0, s.length(), Integer.toString(MAX_SIZE_OF_STICKERS));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        });

        ((Button)findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, GuideForCreationActivity.class));
                finish();
            }
        });

        ((Button)findViewById(R.id.btn_create_board)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int boardId = createBoard();

                if (boardId >= 1) {
                    Intent intent_I_want_to_load_board = new Intent(context, MainActivity.class);
//                    intent_I_want_to_load_board.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent_I_want_to_load_board.putExtra("board_id", boardId);
                    startActivity(intent_I_want_to_load_board);
                    finish();
                } else {
                    Log.d("unja", "failed at creating the board. current board id: "+boardId);
                }
            }
        });
    }

    private int createBoard() {
        String userName = ((EditText)findViewById(R.id.edit_text_user_name)).getText().toString();
        String stickerSizeString = ((EditText)findViewById(R.id.edit_text_sticker_size)).getText().toString();
        Integer stickerSize = new Integer(0);
//        Log.d("unja", "sticker Size: "+stickerSizeString);
        if (TextUtils.isEmpty(stickerSizeString) == false) {
            stickerSize = new Integer(stickerSizeString);
        }
        String listOfGoals = ((TextView)findViewById(R.id.list_of_goals)).getText().toString();
        String prize = ((EditText)findViewById(R.id.edit_text_prize)).getText().toString();

        int boardId = -1; // -1 means not-initialized.
        if (validateInputs(userName, stickerSize, listOfGoals, prize) == true) {
            boardId = BoardManager.getInstance(context).createBoard(userName, stickerSize, listOfGoals, prize);
        }

        return boardId;
    }

    private boolean validateInputs(String userName, Integer stickerSize, String listOfGoals, String prize) {
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(context, "칭찬 받을 아이의 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (stickerSize <= 0) {
            Toast.makeText(context, "스티커 개수를 입력해주세요. (최대 "+MAX_SIZE_OF_STICKERS+"개)", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(listOfGoals)) {
            Toast.makeText(context, "칭찬 주제를 추가해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(prize)) {
            Toast.makeText(context, "스티커판 완성 선물을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setGoals(List<String> goals) {
        ((TextView)findViewById(R.id.list_of_goals)).setText(TextUtils.join("\n", goals.toArray()));
    }
}
