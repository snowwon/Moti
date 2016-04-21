package net.zoo9.moti;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    private static Board board = null;
    private StickerRecycleAdapter stickerRecyclerAdapter = null;
    private int board_id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        board_id = -1;
        if (intent != null) {
            board_id = intent.getIntExtra("board_id", -1);
        }

        if (board_id >= 1) {
            board = BoardManager.getInstance(getApplicationContext()).getBoard(board_id);
        } else {
            // invalid case. Let the user see the board creation page.
            Intent iWannaGoToCreateActivity = new Intent(MainActivity.this, GuideForCreationActivity.class);
            startActivity(iWannaGoToCreateActivity);
            finish();
        }

        // initialBoardUI()
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        updateTitleBasedOnCurrentBoard();

        // set prize and list of goals in the above of the board.
        ((TextView)findViewById(R.id.textview_prize)).setText(board.prize);
        ((TextView)findViewById(R.id.textview_goals)).setText(board.listOfGoals);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.achieve_board_container);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.scrollToPosition(board.stickerPos);
        recyclerView.setLayoutManager(gridLayoutManager);

        List<Sticker> stickers = null;


        List<Date> checkedDates = null;
        try {
            checkedDates = StickerHistoryManager.getInstance(getApplicationContext()).getStickerHistories(board.boardId);
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
                addNewSticker(board_id);
            }
        });


    }

    private void addNewSticker(final int board_id) {
        if (board_id >= 0) {
            stickerRecyclerAdapter.addNewSticker(board_id);
            Log.d("unja", "sticker pos vs size: "+board.stickerPos+" - "+board.stickerSize);
            if (board.stickerPos == board.stickerSize) {
                StringBuffer message = new StringBuffer();
                message.append("축하합니다!\n").append(board.userName).append(" 님의 스티커판이 완성되었습니다.\n");
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("스티커 완성")
                        .setMessage(message.toString())
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BoardManager.getInstance(MainActivity.this).getBoardEnded(board_id);
                                Intent iWannaGoToCreateActivity = new Intent(MainActivity.this, GuideForCreationActivity.class);
                                startActivity(iWannaGoToCreateActivity);
                                finish();
                            }
                        })
                        .show();
            } else {
                Toast.makeText(MainActivity.this, "참! 잘했어요.\n" + "스티커 1개가 추가되었습니다.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("unja", "invalid board id : " + board_id);
        }
    }

    private void removeTheLastStikcer() {
        board.stickerPos = board.stickerPos - 1;
        StickerHistoryManager.getInstance(MainActivity.this).removeLastSticker(board_id);
        BoardManager.getInstance(MainActivity.this).updateStickerPosition(board_id, board.stickerPos);
        stickerRecyclerAdapter.stickers.set(board.stickerPos, new Sticker());
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
            Log.d("unja", "date :"+checkedDate);
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
            if ((getAdapterPosition() + 1) == board.stickerPos) {
                processRemoveTheLastSticker();
            }
        }
    }

    public void processRemoveTheLastSticker() {
        new AlertDialog.Builder(MainActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("스티커 제거")
                .setMessage("붙였던 스티커를 제거하시겠습니까?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeTheLastStikcer();
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

        @Override
        public StickerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case 0:
                    View clickedView = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
                    return new StickerViewHolder(clickedView);
                case 1:
                    View unclickedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_item_layout_unclicked, parent, false);
                    return new StickerViewHolder(unclickedView);
                case 2:
                    View lastItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_item_layout_last, parent, false);
                    return new StickerViewHolder(lastItemView);
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
            if (position == board.stickerPos - 1) {
                return 2;
            } else {
                if (stickers.get(position).checkedDate != null) {
                    // clicked sticker type
                    return 0;
                } else {
                    // un-clicked sticker type
                    return 1;
                }
            }
        }

        public void addNewSticker(int board_id) {
            StickerHistoryManager.getInstance(MainActivity.this).addNewSticker(board_id);
            board.stickerPos = board.stickerPos + 1;
            BoardManager.getInstance(MainActivity.this).updateStickerPosition(board_id, board.stickerPos);
            Log.d("unja", "Board's StickerPos: "+board.stickerPos);
            stickers.set(board.stickerPos - 1, new Sticker(new Date()));
            updateTitleBasedOnCurrentBoard();
            notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_board) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("새 스티커판 만들기")
                    .setMessage("현재 진행 중인 스티커 판은 중도 포기하고, 새로 만들어지게 됩니다. 새로 만드시겠습니까?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            BoardManager.getInstance(MainActivity.this).getBoardEnded(board_id);
                            startActivity(new Intent(MainActivity.this, CreateBoardActivity.class));
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();

        } else if (id == R.id.action_delete_board) {
            // show the confirm dialog to check whether user really wanna delete the current board.
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("스티커판 제거")
                    .setMessage("현재 진행 중인 스티커 판을 제거하시겠습니까?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            BoardManager.getInstance(MainActivity.this).removeBoard(board_id);
                            Intent iWannaGoToCreateActivity = new Intent(MainActivity.this, GuideForCreationActivity.class);
                            startActivity(iWannaGoToCreateActivity);
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();

        } else if (id == R.id.action_manage_boards) {

            startActivity(new Intent(this, ManageBoardsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
