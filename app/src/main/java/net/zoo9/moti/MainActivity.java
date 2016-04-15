package net.zoo9.moti;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
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

import net.zoo9.moti.model.Board;
import net.zoo9.moti.model.BoardManager;
import net.zoo9.moti.model.StickerHistoryManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    private Board board = null;
    private  StickerRecycleAdapter stickerRecyclerAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int board_id = -1;
        if (intent != null) {
            board_id = intent.getIntExtra("board_id", -1);
        }

        if (board_id >= 1) {
            board = BoardManager.getInstance(getApplicationContext()).getBoard(board_id);
        }
        // FYI, the id of dummy board is -1.
        if (board == null) board = getDummyBoard();
        Log.d("unja", "Board: " + board.boardId + ":" + board.listOfGoals + ":" + board.prize + ":" + board.userName + ":" + board.stickerSize);


        // initialBoardUI()
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String titleWithNameAndStatus = board.userName + " ( "+board.stickerPos + " / "+board.stickerSize+" )";
        getSupportActionBar().setTitle(titleWithNameAndStatus);

        ((TextView)findViewById(R.id.textview_prize)).setText(board.prize);
        Log.d("unja", "Goals: "+board.listOfGoals );
        ((TextView)findViewById(R.id.textview_goals)).setText(board.listOfGoals);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.achieve_board_container);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.scrollToPosition(board.stickerPos);
        recyclerView.setLayoutManager(gridLayoutManager);

        List<Sticker> stickers = null;

        if (board.boardId == -1) {
            // dummy case
            stickers = getStickersWithDates(null, board.stickerSize);
        } else {
            List<Date> checkedDates = null;
            try {
                checkedDates = StickerHistoryManager.getInstance(getApplicationContext()).getStickerHistories(board.boardId);
                stickers = getStickersWithDates(checkedDates, board.stickerSize);
            } catch (ParseException e) {
                e.printStackTrace();
                stickers = getStickersWithDates(null, board.stickerSize);
            }
        }

        StickerRecycleAdapter stickerRecyclerAdapter = new StickerRecycleAdapter(stickers, R.layout.sticker_item_layout);

        recyclerView.setAdapter(stickerRecyclerAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("unja", "add new sticker!!");
            }
        });


    }

    private String loadGoals() {
        return board.listOfGoals;
    }

    private Board getDummyBoard() {
        Board dummyBoard = new Board();
        dummyBoard.boardId = -1;
        dummyBoard.prize = "터닝매카드 3개 사주기";
        dummyBoard.stickerSize = 23;
        dummyBoard.userName = "교남이";
        dummyBoard.listOfGoals = "양치 잘 하기\n엄마 심부름 하루에 한 번\n숙제는 저녁 식사 전에 마치기";
        dummyBoard.stickerPos = 4;
        return dummyBoard;
    }


    private List<Sticker> getStickersWithDates(List<Date> dates, int stickerSize) {
        LinkedList<Sticker> stickers = new LinkedList<>();

        if (dates == null || dates.size() == 0) {
            stickers.add(new Sticker(createDate(2015, 12, 20)));
            stickers.add(new Sticker(createDate(2015, 12, 21)));
            stickers.add(new Sticker(createDate(2015, 12, 22)));
            stickers.add(new Sticker(createDate(2015, 12, 23)));
            stickers.add(new Sticker(createDate(2015, 12, 24)));
        } else {
            for (Date checkedDate : dates) {
                Log.d("unja", "date :"+checkedDate);
                stickers.add(new Sticker(checkedDate));
            }
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

    private final static class StickerViewHolder extends RecyclerView.ViewHolder {
        public TextView label;

        public StickerViewHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.txt_label_item);
        }
    }

    private class StickerRecycleAdapter extends RecyclerView.Adapter<StickerViewHolder>{
        private List<Sticker> stickers;
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
                SimpleDateFormat sm = new SimpleDateFormat("mm/dd");
                label.append(sm.format(item.checkedDate));
//                label.append(Integer.toString(position+1));
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

            startActivity(new Intent(this, CreateBoardActivity.class));
            return true;
        } else if (id == R.id.action_delete_board) {

            startActivity(new Intent(this, DeleteBoardActivity.class));
            return true;
        } else if (id == R.id.action_manage_boards) {

            startActivity(new Intent(this, ManageBoardsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
