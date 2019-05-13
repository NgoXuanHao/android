package com.example.quanlytour;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.quanlytour.khach_hang.MainKH;
import com.example.quanlytour.phieu.MainPhieu;
import com.example.quanlytour.tour.MainTour;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private BoomMenuButton bmb;
    ArrayList<String> titleList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setConTrol();
        setEvents();
    }

    public void setConTrol(){
        bmb = findViewById(R.id.boom_menu);
    }

    public void setEvents() {
        boom_menu();
        ActionBar actionBar =getSupportActionBar();
        actionBar.hide();
    }

    public void boom_menu(){
        setInitialData();
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            HamButton.Builder builder = new HamButton.Builder()
                    .normalText(titleList.get(i))
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            if(index == 2){
                                Intent intent = new Intent(MainActivity.this, MainTour.class);
                                startActivity(intent);
                            }
                            if(index == 1){
                                Intent intent = new Intent(MainActivity.this, MainPhieu.class);
                                startActivity(intent);
                            }
                            if(index == 0){
                                Intent intent = new Intent(MainActivity.this, MainKH.class);
                                startActivity(intent);
                            }
                        }
                    });
            bmb.addBuilder(builder);
        }
    }

    private void setInitialData() {
        titleList.add("Quản lý khách hàng");
        titleList.add("Quản lý phiếu đăng ký");
        titleList.add("Quản lý Tour");
    }
}
