package trongnghia.edu.appfilmapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

public class Edit_Fim extends AppCompatActivity {
    private ImageView imgFilmEdit;
    private EditText name_film_en_edit;
    private EditText name_film_vn_edit;
    private RatingBar rating_star_edit;
    private Button btn_ok;
    private Button btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit__film);
        setControl();
        setEvent();
    }
    public void setControl(){
        imgFilmEdit = findViewById(R.id.img_film_edit);
        name_film_en_edit = findViewById(R.id.name_film_en_edit);
        name_film_vn_edit = findViewById(R.id.name_film_vn_edit);
        rating_star_edit = findViewById(R.id.rating_star_edit);
        btn_ok = findViewById(R.id.btn_ok);
        btn_cancel = findViewById(R.id.btn_cancel);
    }

    public void setEvent()
    {
        Intent get = this.getIntent();
        final int imgFilm = get.getIntExtra("ImgFilm",0);
        String name_film_en = get.getStringExtra("name_film_en");
        String name_film_vn = get.getStringExtra("name_film_vn");
        final int rating_star = get.getIntExtra("rating_star",0);
        imgFilmEdit.setImageResource(imgFilm);
        name_film_en_edit.setText(name_film_en);
        name_film_vn_edit.setText(name_film_vn);
        rating_star_edit.setRating(rating_star);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent push = new Intent(Edit_Fim.this, MainActivity.class);
                push.putExtra("name_film_en",name_film_en_edit.getText().toString());
                push.putExtra("name_film_vn", name_film_vn_edit.getText().toString());
                push.putExtra("rating_star", (int) rating_star_edit.getRating());
                setResult(RESULT_OK, push);
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
