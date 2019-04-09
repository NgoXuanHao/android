package com.example.tuan2_bai3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.content.*;

public class MainActivity extends AppCompatActivity {

    private RadioGroup RadioGroupPhotos;
    private ImageView ImageViewPhotos;
    private Integer []photos = { R.drawable.meo, R.drawable.cho, R.drawable.rong, R.drawable.bike };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.ImageViewPhotos = (ImageView) findViewById(R.id.ImageViewPhotos);
        this.RadioGroupPhotos = (RadioGroup) findViewById(R.id.RadioGroupPhotos);
        this.RadioGroupPhotos.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = (RadioButton) radioGroup.findViewById(i);
                int index = radioGroup.indexOfChild(radioButton);
                ImageViewPhotos.setImageResource(photos[index]);
            }
        });
    }
}
