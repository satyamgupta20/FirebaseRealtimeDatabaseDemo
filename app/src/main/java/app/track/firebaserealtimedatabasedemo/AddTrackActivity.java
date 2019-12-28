package app.track.firebaserealtimedatabasedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Snapshot;

import android.app.backup.BackupHelper;
import android.content.Intent;
import android.database.DefaultDatabaseErrorHandler;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddTrackActivity extends AppCompatActivity {

    TextView textViewArtistName, textViewArtistId;
    EditText editTextTrackName;
    SeekBar seekBarRating;
    Button buttonAddTrack;
    ListView listViewTracks;

    DatabaseReference databaseTracks;

    List<Track> tracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track);

        textViewArtistName = (TextView) findViewById(R.id.textViewArtistName);
        textViewArtistId = (TextView) findViewById(R.id.textViewArtistId);
        editTextTrackName = (EditText) findViewById(R.id.editTextTrackName);
        seekBarRating = (SeekBar) findViewById(R.id.seekBarRating);
        listViewTracks = (ListView) findViewById(R.id.listViewTracks);
        buttonAddTrack = (Button) findViewById(R.id.buttonAddTrack);

        Intent intent = getIntent();

        tracks = new ArrayList<>();

        String id = intent.getStringExtra(MainActivity.ARTIST_ID);
        String name = intent.getStringExtra(MainActivity.ARTIST_NAME);

        textViewArtistName.setText(name);
        textViewArtistId.setText(id);

        databaseTracks = FirebaseDatabase.getInstance().getReference("tracks").child(id);

        buttonAddTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTrack();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseTracks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                tracks.clear();

                for (DataSnapshot trackSnapshot : dataSnapshot.getChildren())
                {
                    Track track = trackSnapshot.getValue(Track.class);
                    tracks.add(track);
                }

                TrackList trackListAdapter = new TrackList(AddTrackActivity.this, tracks);
                listViewTracks.setAdapter(trackListAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveTrack() {
        String trackName = editTextTrackName.getText().toString().trim();
        int rating = seekBarRating.getProgress();

        if(!TextUtils.isEmpty(trackName)){
            String id = databaseTracks.push().getKey();

            Track track = new Track(id, trackName, rating);

            databaseTracks.child(id).setValue(track);

            Toast.makeText(this, "Track saved successfully", Toast.LENGTH_SHORT).show();
            
        }else
        {
            Toast.makeText(this, "Track name should not be empty", Toast.LENGTH_SHORT).show();
        }

    }
}