package com.example.tuan6_bai2;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listview;
    ArrayList<Player> player_arr = new ArrayList<>();
    int index = -1;
    PlayerAdapter player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setEvent();
        setControl();
//        Update();
    }
    public void setEvent(){
        listview = findViewById(R.id.listview);
    }
    public void setControl(){
        init();
        player = new PlayerAdapter(this,R.layout.list_item,player_arr);
        listview.setAdapter(player);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Player p = (Player) parent.getItemAtPosition(position);
                index = position;
                String name = p.getName();
                String star = p.getStar();
                String club = p.getClub();
                int img = p.getImg();
                Intent push = new Intent(MainActivity.this,EditPlayer.class);
                push.putExtra("name",name);
                push.putExtra("star",star);
                push.putExtra("club",club);
                push.putExtra("img",img);
                startActivityForResult(push,0);

            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                Player p = player_arr.get(pos);
                index = pos;
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete !!");
                builder.setMessage("Are you sure ??");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        player_arr.remove(index);
                        player.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this,"Removed",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

                return true;
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case RESULT_OK:
                String new_star = data.getStringExtra("star");
                String new_club = data.getStringExtra("club");
                player_arr.get(index).setClub(new_club);
                player_arr.get(index).setStar(new_star);
                player.notifyDataSetChanged();
        }
    }

    void init() {
        player_arr.add(new Player(R.drawable.ronaldo,"Ronaldo","12","Juventus"));
        player_arr.add(new Player(R.drawable.messi,"Messi","13","Barcelona"));
        player_arr.add(new Player(R.drawable.mbappe,"Mbappe","10","Paris"));
        player_arr.add(new Player(R.drawable.neymar,"Neymar JR","12","Barcelona"));
    }
}
