package net.zoo9.moti;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.zoo9.moti.model.Board;
import net.zoo9.moti.model.BoardManager;
import net.zoo9.moti.model.StickerHistoryManager;
import net.zoo9.moti.util.DateUtil;
import net.zoo9.moti.util.WrapGridLayoutManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    private static Board board = null;
    private StickerRecycleAdapter stickerRecyclerAdapter = null;
    private boolean isReadOnlyMode = false;

    private final static int STICKER_WIDTH_IN_DP = 90;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        board = null;

        Intent intent = getIntent();
        int board_id = -1;
        if (intent != null) {
            board_id = intent.getIntExtra("board_id", -1);
        }

        if (board_id >= 1) {
            board = BoardManager.getInstance(getApplicationContext()).getBoard(board_id);
        } else {
            Intent iWannaGoToCreateActivity = new Intent(MainActivity.this, GuideForCreationActivity.class);
            startActivity(iWannaGoToCreateActivity);
            finish();
            return;
        }

        String mode = intent.getStringExtra("mode");
        if (mode != null && (mode.equalsIgnoreCase("r") == true)) {
            isReadOnlyMode = true;
        }

        // initialBoardUI()
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        updateTitleBasedOnCurrentBoard();

        if (getSupportActionBar() != null && isReadOnlyMode == true) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // set prize and list of goals in the above of the board.
        ((TextView)findViewById(R.id.textview_prize)).setText(board.prize);
        ((TextView)findViewById(R.id.textview_goals)).setText(board.listOfGoals);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.achieve_board_container);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, getProperGridNumber());
        recyclerView.setLayoutManager(gridLayoutManager);

        List<Sticker> stickers = null;
        List<Date> checkedDates = null;
        try {
            Log.d("unja66", "board:"+board);
            Log.d("unja66", "board.boardId:"+board.boardId);
            checkedDates = StickerHistoryManager.getInstance(getApplicationContext()).getStickerHistories(board.boardId);
            Log.d("unja66", "checkedDates:"+checkedDates);
            stickers = getStickersWithDates(checkedDates, board.stickerSize);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        stickerRecyclerAdapter = new StickerRecycleAdapter(stickers, R.layout.sticker_item_layout);
        recyclerView.setAdapter(stickerRecyclerAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewSticker();
            }
        });

        if (isReadOnlyMode == true) {
            fab.setVisibility(View.INVISIBLE);
        } else {
            fab.setVisibility(View.VISIBLE);
        }
    }

    private int getProperGridNumber() {
        final DisplayMetrics displayMetrics=getResources().getDisplayMetrics();
        final float device_screen_width_in_dp=displayMetrics.widthPixels/displayMetrics.density;
//        Log.d("unja", "divide: "+device_screen_width_in_dp+"%"+STICKER_WIDTH_IN_DP+"="+(device_screen_width_in_dp / STICKER_WIDTH_IN_DP));
        int suggestedGridNumber = (int)(Math.floor(device_screen_width_in_dp / STICKER_WIDTH_IN_DP));
//        Log.d("unja", "suggested grid number: "+suggestedGridNumber);
        return suggestedGridNumber;

    }

    private void loadCurrentActivatedBoard(int board_id_of_activated_board) {
        isReadOnlyMode = false;
        board = BoardManager.getInstance(getApplicationContext()).getBoard(board_id_of_activated_board);
//        Log.d("unja", "loadCurrentActivatedBoard: "+board);
        updateTitleBasedOnCurrentBoard();
        ((TextView)findViewById(R.id.textview_prize)).setText(board.prize);
        ((TextView)findViewById(R.id.textview_goals)).setText(board.listOfGoals);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.achieve_board_container);
        List<Sticker> stickers = null;
        List<Date> checkedDates = null;
        try {
            checkedDates = StickerHistoryManager.getInstance(getApplicationContext()).getStickerHistories(board.boardId);
            stickers = getStickersWithDates(checkedDates, board.stickerSize);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ((StickerRecycleAdapter)recyclerView.getAdapter()).stickers = stickers;
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    public void saveTheLastActivityTimeIntoSharedPreference() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("alert", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("last_update_time_in_mills", System.currentTimeMillis());
        editor.commit();
    }

    private void addNewSticker() {


        int board_id = board.boardId;
//        Log.d("unja", "addNewSticker to "+board_id);
        if (isReadOnlyMode) {
            notifyReadMode();
            return;
        }

        if (board_id >= 0) {
            stickerRecyclerAdapter.addNewSticker(board_id);

            if (board.stickerPos == board.stickerSize) {
                showMeesageForCompletion();
            } else {

                Toast.makeText(MainActivity.this, "참! 잘했어요.\n" + "스티커 1개가 추가되었습니다.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("unja", "invalid board id : " + board_id);
        }
    }

    private void showMeesageForCompletion() {
        StringBuffer message = new StringBuffer();
        message.append("축하합니다!\n\n").append(board.userName).append(" 님의 칭찬 스티커판이 완성되었습니다.\n\n")
            .append("상으로 \'").append(board.prize).append("\' 선물을 받을 수 있습니다.\n");
        new AlertDialog.Builder(MainActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("칭찬 스티커판 완성")
                .setMessage(message.toString())
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String toastMessage = "축하합니. 완성한 스티커판은 ‘지난 칭찬 스티커판 보기’ 메뉴에서 확인할 수 있습니다";
                        Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT).show();

                        BoardManager.getInstance(MainActivity.this).getBoardEnded(board.boardId);
                        Intent iWannaGoToCreateActivity = new Intent(MainActivity.this, GuideForCreationActivity.class);
                        startActivity(iWannaGoToCreateActivity);
                        finish();
                    }
                })
                .show();
    }

    private void notifyReadMode() {
        Toast.makeText(MainActivity.this, "읽기 전용 모드입니다.", Toast.LENGTH_SHORT).show();
    }

    private void removeTheSelectedStikcer(int pos_in_adapter) {
        if (isReadOnlyMode) {
            notifyReadMode();
            return;
        }


        String deletedItemDate = StickerHistoryManager.getInstance(MainActivity.this).removeStickerAtAndReturnDateString(board.boardId, pos_in_adapter);
        Log.d("unja", "deleted item's date: "+deletedItemDate);

        board.stickerPos = board.stickerPos - 1;
        BoardManager.getInstance(MainActivity.this).updateStickerPosition(board.boardId, board.stickerPos);
        // First of all, I need to remove the selected sticker from stickers.
        Log.d("unja66", "before: "+stickerRecyclerAdapter.stickers);
        Log.d("unja66", "length: "+stickerRecyclerAdapter.stickers.size());
        stickerRecyclerAdapter.stickers.remove(pos_in_adapter);
        stickerRecyclerAdapter.stickers.add(new Sticker());
        Log.d("unja66", "length: "+stickerRecyclerAdapter.stickers.size());
        Log.d("unja66", "after: "+stickerRecyclerAdapter.stickers);


        updateTitleBasedOnCurrentBoard();
        stickerRecyclerAdapter.notifyDataSetChanged();
        Toast.makeText(MainActivity.this, "저런 ...\n" + "스티커 1개가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
    }

    private void updateTitleBasedOnCurrentBoard() {
        String titleWithNameAndStatus = board.userName + " ( "+board.stickerPos + " / "+board.stickerSize+" )";
        getSupportActionBar().setTitle(titleWithNameAndStatus);
    }

    private String loadGoals() {
        return board.listOfGoals;
    }

    private List<Sticker> getStickersWithDates(List<Date> dates, int stickerSize) {
        LinkedList<Sticker> stickers = new LinkedList<>();

        for (Date checkedDate : dates) {
            stickers.add(new Sticker(checkedDate));
        }

        int uncheckedItemLength = stickerSize - stickers.size();
        for (int i = 0; i < uncheckedItemLength; i++) {
            stickers.add(new Sticker());
        }
        return stickers;
    }

    private Date createDate(int year, int month, int day) {
        String date = Integer.toString(year)+"/"+Integer.toString(month)+"/"+Integer.toString(day);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private final class StickerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView label;

        public StickerViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            label = (TextView) itemView.findViewById(R.id.txt_label_item);
        }

        @Override
        public void onClick(View v) {
            if (isReadOnlyMode == true) {
                return;
            }

            if ((getAdapterPosition() + 1) <= board.stickerPos) {
                showDialogForConfirmingOfRemoval(getAdapterPosition());
            } else {
                runNewStickerProcess();
            }
        }
    }

    public void runNewStickerProcess() {
        new AlertDialog.Builder(MainActivity.this)
                .setIcon(null)
                .setTitle("스티커 추가")
                .setMessage("스트커를 추가하시겠습니까?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addNewSticker();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    public void showDialogForConfirmingOfRemoval(int positionOfclickedItem) {
        final int pos_in_adapter = positionOfclickedItem;

        Sticker targetSticker = stickerRecyclerAdapter.getStickerAt(positionOfclickedItem);
        if (targetSticker.checkedDate == null) return;

        SimpleDateFormat sm = new SimpleDateFormat("MM/dd");
        String checkedDateString = sm.format(targetSticker.checkedDate);
        Log.d("unja66", "selected pos: "+pos_in_adapter);

        new AlertDialog.Builder(MainActivity.this)
                .setIcon(null)
                .setTitle("스티커 삭제")
                .setMessage("붙였던 "+checkedDateString+"일 스티커를 삭제하시겠습니까?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeTheSelectedStikcer(pos_in_adapter);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }



    private class StickerRecycleAdapter extends RecyclerView.Adapter<StickerViewHolder>{
        public List<Sticker> stickers;
        private int itemLayout;

        public StickerRecycleAdapter(List<Sticker> stickers, int itemLayout) {
            this.stickers = stickers;
            this.itemLayout = itemLayout;
        }

        public Sticker getStickerAt(int position) {
            return stickers.get(position);
        }

        @Override
        public StickerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case 0:
                    View clickedView = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
                    return new StickerViewHolder(clickedView);
                case 1:
                    View unclickedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_item_layout_unclicked, parent, false);
                    return new StickerViewHolder(unclickedView);
            }

            View defaultView = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
            return new StickerViewHolder(defaultView);
        }

        @Override
        public void onBindViewHolder(StickerViewHolder holder, int position) {
            Sticker item = stickers.get(position);
            StringBuffer label = new StringBuffer();
            if (item.checkedDate != null) {
                label.append("Good").append("\n");
                SimpleDateFormat sm = new SimpleDateFormat("MM/dd");
                label.append(sm.format(item.checkedDate));
            } else {
                label.append(Integer.toString(position+1));
            }

            holder.label.setText(label.toString());
        }

        @Override
        public int getItemCount() {
            return stickers.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (stickers.get(position).checkedDate != null) {
                // clicked sticker type
                return 0;
            } else {
                // un-clicked sticker type
                return 1;
            }
        }

        public void addNewSticker(int board_id) {
            Log.d("unja66", "added new sticker to : "+board.toString());
            board.stickerPos = board.stickerPos + 1;
            if (stickers.size() <= (board.stickerPos - 1)) {
                board.stickerPos = stickers.size();
            } else {
                Date currentDate = DateUtil.getCurrentDate();
                StickerHistoryManager.getInstance(MainActivity.this).addNewSticker(board_id, currentDate);
                BoardManager.getInstance(MainActivity.this).updateStickerPosition(board_id, board.stickerPos);

                stickers.set(board.stickerPos - 1, new Sticker(currentDate));

                saveTheLastActivityTimeIntoSharedPreference();
                updateTitleBasedOnCurrentBoard();
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (isReadOnlyMode == false) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_delete_board) {
            // show the confirm dialog to check whether user really wanna delete the current board.
            new AlertDialog.Builder(this)
                    .setIcon(null)
                    .setTitle("스티커판 삭제")
                    .setMessage("진행 중인 "+board.userName+"님 칭찬스티커 판을 삭제하시겠습니까?")
                    .setPositiveButton("삭제", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            BoardManager.getInstance(MainActivity.this).removeBoard(board.boardId);
                            Intent iWannaGoToCreateActivity = new Intent(MainActivity.this, GuideForCreationActivity.class);
                            startActivity(iWannaGoToCreateActivity);
                        }

                    })
                    .setNegativeButton("취소", null)
                    .show();

        } else if (id == R.id.action_manage_boards) {
            startActivity(new Intent(this, ManageBoardsActivity.class));
            return true;
        } else if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.tips) {
            startActivity(new Intent(this, TipActivity.class));
            return true;
        } else if (id == R.id.action_manange_alerts)
            startActivity(new Intent(this, ManageAlertsActivity.class));

        return super.onOptionsItemSelected(item);
    }



}
