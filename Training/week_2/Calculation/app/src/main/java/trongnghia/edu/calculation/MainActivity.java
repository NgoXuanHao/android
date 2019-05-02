package trongnghia.edu.calculation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText edtA, edtB, edtkq;
    Button btnCo, btnTr, btnNh, btnCh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setControl();
        setEvent();
    }

    private void setControl(){
        edtA = findViewById(R.id.edta);
        edtB = findViewById(R.id.edtb);
        edtkq= findViewById(R.id.edtR);

        btnCo = findViewById(R.id.btnC);
        btnTr = findViewById(R.id.btnT);
        btnNh = findViewById(R.id.btnN);
        btnCh = findViewById(R.id.btnCH);
    }

    private void setEvent(){
        btnCo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pheptinh("+");
            }
        });

        btnTr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pheptinh("-");
            }
        });

        btnNh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pheptinh("*");
            }
        });

        btnCh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pheptinh("/");
            }
        });

    }

    private void pheptinh(String pt){
        int soA = Integer.parseInt((edtA.getText().toString()));
        int soB = Integer.parseInt((edtB.getText().toString()));
        int kq = 0;
        switch (pt){
            case "+":
                kq = soA + soB;
                break;
            case "-":
                kq = soA - soB;
                break;
            case "*":
                kq = soA * soB;
                break;
            case "/":
                kq = soA / soB;
                break;
        }

        edtkq.setText(String.valueOf(kq));

    }

}
