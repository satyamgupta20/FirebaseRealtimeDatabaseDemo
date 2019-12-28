package app.track.firebaserealtimedatabasedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ListViewAutoScrollHelper;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String ARTIST_NAME = "artistname";
    public static final String ARTIST_ID = "artistid";

    EditText editTextName;
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextConfirmPassword;
    EditText editTextMobile;
    Button buttonAdd;
    Button buttonShowDetails;
    Spinner spinnerGenres;
    Spinner spinnerGender;


    ListView listViewArtists;
    List<Artist> artistList;
    DatabaseReference databaseArtists;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseArtists = FirebaseDatabase.getInstance().getReference("artists");

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);
        editTextMobile = (EditText) findViewById(R.id.editTextMobile);
        buttonAdd = (Button) findViewById(R.id.buttonAddArtist);

        spinnerGenres = (Spinner) findViewById(R.id.spinnerGenres);
        spinnerGender = (Spinner) findViewById(R.id.spinnerGender);

        listViewArtists = (ListView) findViewById(R.id.listViewArtists);

        artistList = new ArrayList<>();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addArtistDetails();
                Toast.makeText(MainActivity.this, "Swipe up to see the Details of the artist", Toast.LENGTH_SHORT).show();
            }
        });

        listViewArtists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Artist artist = artistList.get(i);

                Intent intent = new Intent(getApplicationContext(), AddTrackActivity.class);

                intent.putExtra(ARTIST_ID,artist.getArtistId());
                intent.putExtra(ARTIST_NAME, artist.getArtistName());

                startActivity(intent);
            }
        });

        listViewArtists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long id) {

                Artist artist = artistList.get(i);
                showUpdateDialog(artist.getArtistId(),artist.getArtistName());
                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        ValueEventListener addValueEventListener = databaseArtists.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot artistSnapshot : dataSnapshot.getChildren()) {
                    Artist artist = artistSnapshot.getValue(Artist.class);
                    artistList.clear();
                    artistList.add(artist);
                }

                ArtistList adapter = new ArtistList(MainActivity.this, artistList);
                listViewArtists.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showUpdateDialog(final String artistId, String artistName){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView =  inflater.inflate(R.layout.update_dialog, null);

        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdate);
        final Spinner spinnerGenres = (Spinner) dialogView.findViewById(R.id.spinnerGenres);

        dialogBuilder.setTitle("Updating Artist"+artistName);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String genre = spinnerGenres.getSelectedItem().toString();

                if(TextUtils.isEmpty(name))
                {
                    editTextName.setError("Name Required");
                    return;
                }
                updateArtist(artistId, name, genre);
                alertDialog.dismiss();

            }
        });
    }

    private boolean updateArtist(String id, String name,String  genre){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("artists").child(id);

        Artist artist = new Artist(id, name, genre);

        databaseReference.setValue(artist);

        Toast.makeText(this, "Artist Updated Successfully", Toast.LENGTH_SHORT).show();
        return true;

    }




    private void addArtistDetails(){
        String name= editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmpassword = editTextConfirmPassword.getText().toString().trim();
        String mobile = editTextMobile.getText().toString().trim();

        String genre = spinnerGenres.getSelectedItem().toString();
        String gender = spinnerGender.getSelectedItem().toString();

        if(!TextUtils.isEmpty(name) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password) || !TextUtils.isEmpty(confirmpassword) || !TextUtils.isEmpty(mobile)){

            String id = databaseArtists.push().getKey();
            Artist artist = new Artist(id, name, email, password, confirmpassword, mobile, genre, gender);
            databaseArtists.child(id).setValue(artist);
            Toast.makeText(this, "Artist details have been saved successfully", Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(this, "Please fill all the details....", Toast.LENGTH_LONG).show();
        }

    }

}
