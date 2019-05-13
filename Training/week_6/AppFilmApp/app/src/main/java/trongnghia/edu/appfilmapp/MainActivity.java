package trongnghia.edu.appfilmapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    ArrayList<Film> data = new ArrayList<>();
    int index = -1;
    private FilmAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setEvent();
        setControl();
    }

    public void setEvent()
    {
        listView = findViewById(R.id.list_film);
    }

    public void setControl()
    {
        KhoiTao();
        adapter = new FilmAdapter(this,R.layout.list_item_row,data);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Film f = data.get(position);
                index = position;
                int img_film = f.getImg_film();
                String name_film_en = f.getName_film_en();
                String name_film_vn = f.getName_film_vn();
                int rating_star = f.getStar();
                Intent push = new Intent(MainActivity.this, Edit_Fim.class);
                push.putExtra("ImgFilm", img_film);
                push.putExtra("name_film_en", name_film_en);
                push.putExtra("name_film_vn", name_film_vn);
                push.putExtra("rating_star", rating_star);
                startActivityForResult(push,0);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Xóa phim");
                alert.setMessage("Bạn có chắc chắn xóa phim này không?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(position>=0)
                        {
                            data.remove(position);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(MainActivity.this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.create().show();
                return true;
            }
        });

    }

    public void onActivityResult (int requestCode, int resultCode, Intent datal)
    {
        super.onActivityResult(requestCode, resultCode, datal);
        switch (requestCode)
        {
            case 0:
                if(resultCode == RESULT_OK)
                {
                    String get_name_film_en = datal.getStringExtra("name_film_en");
                    String get_name_film_vn = datal.getStringExtra("name_film_vn");
                    int get_rating_star = datal.getIntExtra("rating_star",0);
                    data.get(index).setName_film_en(get_name_film_en);
                    data.get(index).setName_film_vn(get_name_film_vn);
                    data.get(index).setStar(get_rating_star);
                    Log.d("Rating star recv", String.valueOf(get_rating_star));
                    Toast.makeText(MainActivity.this,"Chỉnh sửa thành công", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                }
        }
    }
    void KhoiTao()
    {
        data.add(new Film(R.drawable.phim_logo1, "ĐoReMon", "Chú mèo máy đến từ tương lai", 2));
        data.add(new Film(R.drawable.phim_logo2, "Pikachu", "Cuộc phiêu lưu của PiKaChu", 3));
        data.add(new Film(R.drawable.phim_logo3, "Gao Superman", "Siêu nhân Gao", 4));
    }
}
