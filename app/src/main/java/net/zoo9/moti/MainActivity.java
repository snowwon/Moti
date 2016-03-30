package net.zoo9.moti;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("환영합니다");



        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.achieve_board_container);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(gridLayoutManager);

        List<Sticker> stickers = getStikcers();
        StickerRecycleAdapter stickerRecyclerAdapter = new StickerRecycleAdapter(stickers, R.layout.sticker_item_layout);

        recyclerView.setAdapter(stickerRecyclerAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String goals = "이를 잘 닦기\n엄마 심부름 잘 하기\n5시 전에 집에 오기";
                Snackbar snackbar = Snackbar.make(view, goals, Snackbar.LENGTH_LONG)
                        .setAction("Action", null);

                View snackbarView = snackbar.getView();
                TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setMaxLines(3);  // show multiple line

                snackbar.show();
            }
        });
    }

    private List<Sticker> getStikcers() {
        LinkedList<Sticker> stickers = new LinkedList<>();
        stickers.add(new Sticker(createDate(2015, 12, 20) ));
        stickers.add(new Sticker(createDate(2015, 12, 21)));
        stickers.add(new Sticker(createDate(2015, 12, 22)));
        stickers.add(new Sticker(createDate(2015, 12, 23) ));
        stickers.add(new Sticker(createDate(2015, 12, 24)));
        stickers.add(new Sticker());
        stickers.add(new Sticker());
        stickers.add(new Sticker());
        stickers.add(new Sticker());
        stickers.add(new Sticker());
        stickers.add(new Sticker());
        stickers.add(new Sticker());
        stickers.add(new Sticker());
        stickers.add(new Sticker());
        stickers.add(new Sticker());
        stickers.add(new Sticker());
        stickers.add(new Sticker());
        stickers.add(new Sticker());
        stickers.add(new Sticker());
        stickers.add(new Sticker());
        stickers.add(new Sticker());
        stickers.add(new Sticker());
        stickers.add(new Sticker());
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
                label.append("Great !!").append("\n");
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
