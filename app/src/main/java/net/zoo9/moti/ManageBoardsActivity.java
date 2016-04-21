package net.zoo9.moti;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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
import java.util.List;

public class ManageBoardsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_boards);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("지난 스티커판 보기");


        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.list_of_past_boards);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ManageBoardsActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<Board> boards = null;
        boards = BoardManager.getInstance(ManageBoardsActivity.this).getPastBoards();

        PastBoardsRecylerAdapter pastBoardsRecylerAdapter = new PastBoardsRecylerAdapter (boards, R.layout.past_boards_item_layout);
        recyclerView.setAdapter(pastBoardsRecylerAdapter);


//        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.achieve_board_container);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
//        gridLayoutManager.scrollToPosition(board.stickerPos);
//        recyclerView.setLayoutManager(gridLayoutManager);
//
//        List<Sticker> stickers = null;
//
//
//        List<Date> checkedDates = null;
//        try {
//            checkedDates = StickerHistoryManager.getInstance(getApplicationContext()).getStickerHistories(board.boardId);
//            stickers = getStickersWithDates(checkedDates, board.stickerSize);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//
//        stickerRecyclerAdapter = new StickerRecycleAdapter(stickers, R.layout.sticker_item_layout);
//        recyclerView.setAdapter(stickerRecyclerAdapter);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private class PastBoardsRecylerAdapter extends RecyclerView.Adapter<PastBoardViewHolder>{
        public List<Board> boards;
        private int itemLayout;

        public PastBoardsRecylerAdapter(List<Board> boards, int itemLayout) {
            this.boards = boards;
            this.itemLayout = itemLayout;
        }

        @Override
        public PastBoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            switch (viewType) {
//                case 0:
//                    View clickedView = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
//                    return new StickerViewHolder(clickedView);
//                case 1:
//                    View unclickedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_item_layout_unclicked, parent, false);
//                    return new StickerViewHolder(unclickedView);
//                case 2:
//                    View lastItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_item_layout_last, parent, false);
//                    return new StickerViewHolder(lastItemView);
//            }
//
//            View defaultView = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
//            return new StickerViewHolder(defaultView);
            View boardView = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
            return new PastBoardViewHolder(boardView);
        }

        @Override
        public void onBindViewHolder(PastBoardViewHolder holder, int position) {
//            Sticker item = stickers.get(position);
//            StringBuffer label = new StringBuffer();
//            if (item.checkedDate != null) {
//                label.append("Good").append("\n");
//                SimpleDateFormat sm = new SimpleDateFormat("MM/dd");
//                label.append(sm.format(item.checkedDate));
//            } else {
//                label.append(Integer.toString(position+1));
//            }
//
//            holder.label.setText(label.toString());
        }

        @Override
        public int getItemCount() {
            return boards.size();
        }
    }

    private class PastBoardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView userName;
        public TextView period;


        public PastBoardViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            if ((getAdapterPosition() + 1) == board.stickerPos) {
//                processRemoveTheLastSticker();
//            }
        }
    }
}