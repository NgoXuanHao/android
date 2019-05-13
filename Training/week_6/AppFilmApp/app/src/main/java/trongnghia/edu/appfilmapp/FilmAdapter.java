package trongnghia.edu.appfilmapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;


public class FilmAdapter extends ArrayAdapter<Film> {
    public Context context;
    public int layoutResourceId;
    public ArrayList<Film> data = null;

    public FilmAdapter(Context context, int layoutResourceId, ArrayList<Film> data)
    {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        FilmHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new FilmHolder();
            holder.img_film =  row.findViewById(R.id.img_film);
            holder.txtName_film_en =  row.findViewById(R.id.name_film_en);
            holder.txtName_film_vn =  row.findViewById(R.id.name_film_vn);
            holder.rating_star = row.findViewById(R.id.rating_star);
            row.setTag(holder);

        }
        else
        {
            holder = (FilmHolder) row.getTag();

        }

        Film item = data.get(position);
        holder.img_film.setImageResource(item.img_film);
        holder.txtName_film_en.setText(item.name_film_en);
        holder.txtName_film_vn.setText("("+item.name_film_vn+")");
        holder.rating_star.setRating(item.star);

        return row;
    }
    static class FilmHolder
    {
        ImageView img_film;
        TextView txtName_film_en;
        TextView txtName_film_vn;
        RatingBar rating_star;
    }
}
