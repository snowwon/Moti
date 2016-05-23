package net.zoo9.moti;

import android.content.Intent;
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
import android.widget.TextView;

import net.zoo9.moti.model.Board;
import net.zoo9.moti.model.BoardManager;
import net.zoo9.moti.model.StickerHistoryManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ManageBoardsActivity extends AppCompatActivity {

    PastBoardsRecylerAdapter pastBoardsRecylerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_boards);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("지난 스티커판 보기");


//        TextView noPreviousBoardMsg = (TextView) findViewById(R.id.msg_for_no_previous_board);
//
//
//        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.list_of_past_boards);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ManageBoardsActivity.this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(linearLayoutManager);
//
//        List<Board> boards = null;
//        boards = BoardManager.getInstance(ManageBoardsActivity.this).getPastBoards();
//
//        if (boards.size() == 0) {
//            recyclerView.setVisibility(View.GONE);
//            noPreviousBoardMsg.setVisibility(View.VISIBLE);
//        } else {
//            recyclerView.setVisibility(View.VISIBLE);
//            noPreviousBoardMsg.setVisibility(View.GONE);
//        }
//
//        pastBoardsRecylerAdapter = new PastBoardsRecylerAdapter (boards, R.layout.past_boards_item_layout);
//        recyclerView.setAdapter(pastBoardsRecylerAdapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        TextView noPreviousBoardMsg = (TextView) findViewById(R.id.msg_for_no_previous_board);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.list_of_past_boards);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ManageBoardsActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<Board> boards = null;
        boards = BoardManager.getInstance(ManageBoardsActivity.this).getPastBoards();

        Log.d("unja", "boards length: "+boards.size());
        if (boards.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            noPreviousBoardMsg.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noPreviousBoardMsg.setVisibility(View.GONE);
        }

        pastBoardsRecylerAdapter = new PastBoardsRecylerAdapter (boards, R.layout.past_boards_item_layout);
        recyclerView.setAdapter(pastBoardsRecylerAdapter);
    }



    public void removePastBoard(int boardId) {
        // delete the target board from database.
        BoardManager.getInstance(ManageBoardsActivity.this).removeBoard(boardId);
        // update the recylerview.
        pastBoardsRecylerAdapter.removePastBoard(boardId);

        List<Board> boards = null;
        boards = BoardManager.getInstance(ManageBoardsActivity.this).getPastBoards();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.list_of_past_boards);
        TextView noPreviousBoardMsg = (TextView) findViewById(R.id.msg_for_no_previous_board);
        if (boards.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            noPreviousBoardMsg.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noPreviousBoardMsg.setVisibility(View.GONE);
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

        public void removePastBoard(int boardId) {
            Board targetBoard = null;
            for (Board board : boards) {
                if (board.boardId == boardId) {
                    targetBoard = board;
                }
            }
            boards.remove(targetBoard);
            notifyDataSetChanged();
        }

        @Override
        public PastBoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View boardView = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
            return new PastBoardViewHolder(boardView);
        }

        @Override
        public void onBindViewHolder(PastBoardViewHolder holder, int position) {
            Board board = boards.get(position);
            String userName = board.userName;
            int boardId = board.boardId;
            String period = board.startDate + " ~ " + board.endDate;

            String finalStatus = "획득 성공: "+board.prize;
            if (board.stickerSize != board.stickerPos) {
                finalStatus = "획득 실패: "+board.prize;
            }

            holder.period.setText(period);
            holder.userName.setText(userName);
            holder.boardId.setText(Integer.toString(boardId));
            //holder.finalStatus.setText(finalStatus);

        }

        @Override
        public int getItemCount() {
            return boards.size();
        }
    }

    private class PastBoardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView userName;
        public TextView period;
        public TextView boardId;
        // public TextView finalStatus;


        public PastBoardViewHolder(View itemView) {
            super(itemView);

            boardId = (TextView) itemView.findViewById(R.id.board_id_text);
            userName = (TextView) itemView.findViewById(R.id.user_id_text);
            period = (TextView) itemView.findViewById(R.id.period_text);
            //finalStatus = (TextView) itemView.findViewById(R.id.final_status);

            itemView.findViewById(R.id.btn_view_details).setOnClickListener(this);
            itemView.findViewById(R.id.btn_delete).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int board_id = Integer.parseInt(boardId.getText().toString());

            if (v.getId() == R.id.btn_delete) {
                Log.d("unja", "delete legacy board : "+board_id);
                removePastBoard(board_id);
            } else {
                Intent intent_I_want_to_load_board = new Intent(ManageBoardsActivity.this, MainActivity.class);
//                intent_I_want_to_load_board.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent_I_want_to_load_board.putExtra("board_id", board_id);
                intent_I_want_to_load_board.putExtra("mode", "r");
                startActivity(intent_I_want_to_load_board);
            }
        }
    }
}