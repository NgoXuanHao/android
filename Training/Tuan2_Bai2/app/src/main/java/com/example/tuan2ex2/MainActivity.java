package com.example.tuan2ex2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.*;

public class MainActivity extends AppCompatActivity {

    private CheckBox checkBoxK, checkBoxE, checkBoxN, checkBoxN2;
    private ImageView Image1, Image2, Image3, Image4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Controls();
    }

    private void Controls() {
        checkBoxK = (CheckBox) findViewById(R.id.checkBoxK);
        checkBoxE = (CheckBox) findViewById(R.id.checkBoxE);
        checkBoxN = (CheckBox) findViewById(R.id.checkBoxN);
        checkBoxN2 = (CheckBox) findViewById(R.id.checkBoxN2);
        Image1 = (ImageView) findViewById(R.id.Image1);
        Image2 = (ImageView) findViewById(R.id.Image2);
        Image3 = (ImageView) findViewById(R.id.Image3);
        Image4 = (ImageView) findViewById(R.id.Image4);

        checkBoxK.setOnClickListener(new View.OnClickListener() {

                                         @Override
                                         public void onClick(View a) {
                                             if (((CheckBox) a).isChecked()) {
                                                 Image1.setImageResource(R.drawable.k);
                                                 Image1.setVisibility(View.VISIBLE);
                                             } else
                                                 Image1.setVisibility(View.INVISIBLE);

                                         }
                                     }
        );


        checkBoxE.setOnClickListener(new View.OnClickListener() {

                                         @Override
                                         public void onClick(View b) {
                                             if (((CheckBox) b).isChecked()) {
                                                 Image2.setImageResource(R.drawable.e);
                                                 Image2.setVisibility(View.VISIBLE);
                                             } else
                                                 Image2.setVisibility(View.INVISIBLE);

                                         }
                                     }

        );

        checkBoxN.setOnClickListener(new View.OnClickListener() {

                                         @Override
                                         public void onClick(View c) {
                                             if (((CheckBox) c).isChecked()) {
                                                 Image3.setImageResource(R.drawable.n);
                                                 Image3.setVisibility(View.VISIBLE);
                                             } else
                                                 Image3.setVisibility(View.INVISIBLE);

                                         }
                                     }

        );

        checkBoxN2.setOnClickListener(new View.OnClickListener() {

                                          @Override
                                          public void onClick(View d) {
                                              if (((CheckBox) d).isChecked()) {
                                                  Image4.setImageResource(R.drawable.n2);
                                                  Image4.setVisibility(View.VISIBLE);
                                              } else
                                                  Image4.setVisibility(View.INVISIBLE);

                                          }
                                      }

        );

    }
}




