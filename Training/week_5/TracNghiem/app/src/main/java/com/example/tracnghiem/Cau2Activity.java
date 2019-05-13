package com.example.tracnghiem;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class Cau2Activity extends AppCompatActivity {
    Button btright, btnleft, btnKQ;
    TextView txtdapan1,txtdapan2;
    CheckBox checked1,checked2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cau2);
        setControl();
        setEvent();
    }
    public void setControl(){
        btright = findViewById(R.id.right);
        btnleft = findViewById(R.id.left);
        btnKQ = findViewById(R.id.KQ);
        checked1 = findViewById(R.id.dapan1);
        checked2 = findViewById(R.id.dapan2);
        txtdapan1 = findViewById(R.id.dapan1);
        txtdapan2 = findViewById(R.id.dapan2);
    }
    public void setEvent(){
        KetQua.lsKQ.add(1,"Câu 2: Đáp án sai");
        btright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cau2Activity.this, Cau3Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);

                if (checked1.isChecked()&checked2.isChecked()){
                    KetQua.lsKQ.set(1,"Câu 2: Đáp án đúng");

                } else
                {
                    KetQua.lsKQ.set(1,"Câu 2: Đáp án sai");
                }
            }
        });
        btnleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cau2Activity.this, Cau1Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
        btnKQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cau2Activity.this, ListView.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                if (checked1.isChecked()&checked2.isChecked()){
                    KetQua.lsKQ.set(1,"Câu 2: Đáp án đúng");

                } else
                {
                    KetQua.lsKQ.set(1,"Câu 2: Đáp án sai");
                }
                intent.putExtra("Cau1", KetQua.lsKQ.get(0));
                intent.putExtra("Cau2", KetQua.lsKQ.get(1));
                intent.putExtra("Cau3", KetQua.lsKQ.get(2));
                intent.putExtra("Cau4", KetQua.lsKQ.get(3));
                intent.putExtra("Cau5", KetQua.lsKQ.get(4));
                startActivity(intent);
            }
        });

    }
}
