package com.example.tuan6_bai2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EditPlayer extends AppCompatActivity {
    private Button btn_ok;
    private Button btn_cancel;
    private TextView editName;
    private EditText editStar;
    private EditText editClub;
    private ImageView editImgPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_player);
        setControl();
        setEvent();

    }
    public void setControl(){
        btn_ok = findViewById(R.id.btn_ok);
        btn_cancel = findViewById(R.id.btn_cancel);
        editName = findViewById(R.id.editName);
        editClub = findViewById(R.id.editClub);
        editStar = findViewById(R.id.editStar);
        editImgPlayer = findViewById(R.id.editImgPlayer);

    }
    public void setEvent(){
        Intent get = getIntent();
        int img = get.getIntExtra("img",0);
        String name = get.getStringExtra("name");
        String star = get.getStringExtra("star");
        String club = get.getStringExtra("club");
        editImgPlayer.setImageResource(img);
        editName.setText(name);
        editClub.setText(club);
        editStar.setText(star);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent push = new Intent(EditPlayer.this,MainActivity.class);
                String star = editStar.getText().toString();
                String club = editClub.getText().toString();
                push.putExtra("star",star);
                push.putExtra("club",club);
                setResult(RESULT_OK,push);
                finish();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
