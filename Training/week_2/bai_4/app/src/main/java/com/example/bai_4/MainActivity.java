package com.example.bai_4;

import android.graphics.Color;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity {
    TextView result;
    EditText input;
    Button btnSubmit;
    CheckBox cbBackGround, cbTextColor, cbCenter;
    RadioButton rbOdd, rbEven, rbBoth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setControl();
        setEvent();
    }
    private void setControl(){
        cbBackGround = findViewById(R.id.backGround);
        cbTextColor = findViewById(R.id.textColor);
        cbCenter = findViewById(R.id.center);
        btnSubmit = findViewById(R.id.submit);
        input = findViewById(R.id.input);
        rbBoth =findViewById(R.id.both);
        rbOdd = findViewById(R.id.odd);
        rbEven =findViewById(R.id.even);
        result = findViewById(R.id.result);

    }

    private void setEvent() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value= input.getText().toString();
                String[] number = value.split(" ");
                int len = number.length;
                String even ="";
                String odd ="";
                String s = " ";
                for(int i=0; i<len; i++ )
                {
                    String temp = number[i];
                    int n = Integer.parseInt(temp);
                    if((n%2)==0){

                    even = even.concat(temp);
                    even = even.concat(s);

                    }
                    if((n%2)==1){
                    odd = odd.concat(temp);
                    odd = odd.concat(s);
                    }
                }

                String ret = "";
                if (rbOdd.isChecked()){
                    ret = odd;
                }

                if(rbEven.isChecked()){
                    ret = even;

                }
                if(rbBoth.isChecked()){

                    ret = value;

                }


                if(cbBackGround.isChecked())
                {
                    result.setBackgroundColor(Color.parseColor("#2847F7A5"));
                    result.setText(ret);
                }
                else {
                    result.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    result.setText(ret);
                }
                if(cbCenter.isChecked())
                {
                    result.setGravity(Gravity.CENTER);
                    result.setText(ret);
                }
                if(cbTextColor.isChecked())
                {
                    result.setTextColor(Color.parseColor("#5521FF"));
                    result.setText(ret);
                }
                else {
                    result.setTextColor(Color.parseColor("#000000"));
                    result.setText(ret);
                }

            }

        });



    }


}
