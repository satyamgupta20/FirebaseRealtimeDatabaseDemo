package app.track.firebaserealtimedatabasedemo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.concurrent.TimeoutException;

public class ArtistList extends ArrayAdapter<Artist> {

    private Activity context;
    private List<Artist> artistList;


    public ArtistList(Activity context, List<Artist> artistList) {
        super(context, R.layout.list_layout, artistList);
        this.context = context;
        this.artistList = artistList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout,null,true);

        TextView textViewName =(TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewEmail =(TextView) listViewItem.findViewById(R.id.textViewEmail);
        TextView textViewPassword =(TextView) listViewItem.findViewById(R.id.textViewPassword);
        TextView textViewMobile =(TextView) listViewItem.findViewById(R.id.textViewMobile);
        TextView textViewGenre =(TextView) listViewItem.findViewById(R.id.textViewGenre);
        TextView textViewGender =(TextView) listViewItem.findViewById(R.id.textViewGender);

        Artist artist = artistList.get(position);

        textViewName.setText(artist.getArtistName());
        textViewEmail.setText(artist.getArtistEmail());
        textViewPassword.setText(artist.getArtistPassword());
        textViewMobile.setText(artist.getArtistMobile());
        textViewGenre.setText(artist.getArtistGenre());
        textViewGender.setText(artist.getArtistGender());

        return listViewItem;

    }
}
