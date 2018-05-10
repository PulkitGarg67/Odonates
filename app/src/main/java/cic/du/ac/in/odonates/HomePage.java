package cic.du.ac.in.odonates;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class HomePage extends AppCompatActivity {


    Toolbar toolbar;
    FirebaseAuth mAuth;
    ImageView img;
    ProgressBar p;
    TextView t;

    void set(String Sname){
//        Toast.makeText(this, Sname, Toast.LENGTH_SHORT).show();
        t.setText(Sname);
        StorageReference storageReference =  FirebaseStorage.getInstance().getReference().child(Sname+"/img_1.JPG");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(img);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HomePage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                p.setVisibility(View.INVISIBLE);
            }
        },2000);
    }

    public void location(View view  ){
        Intent homeIntent = new Intent(HomePage.this,Checklist_user.class);
        startActivity(homeIntent);
    }
    public void list1(View view) {
        Intent homeIntent = new Intent(HomePage.this,List.class);
        homeIntent.putExtra("species",1);
        startActivity(homeIntent);
    }

    public void list2(View view) {
        Intent homeIntent = new Intent(HomePage.this,List.class);
        homeIntent.putExtra("species",2);
        startActivity(homeIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mAuth = FirebaseAuth.getInstance();
        img= findViewById(R.id.topOdonate);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ODONATA");

        p = findViewById(R.id.main_p);
        p.setVisibility(View.VISIBLE);
        t = findViewById(R.id.text);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("Odonates").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();

                int c = (int) count;
//                    int randomNumber = new Random().nextInt(c);
//                    while(randomNumber==0)
//                        int randomNumber = new Random().nextInt(c);
                    int randomNumber =0;
                    int i=0;
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        if(i == randomNumber) {
                           set(snap.child("Sname").getValue().toString());
                            break;
                        }
                        i++;
                    }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HomePage.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                p.setVisibility(View.INVISIBLE);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.item1_id) {
            Toast.makeText(this, "About Odonates", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,About.class));
        }else if(id == R.id.search_id) {
            Intent homeIntent = new Intent(HomePage.this,Search.class);
            startActivity(homeIntent);
        }else if(id==R.id.item2_id){
            mAuth.signOut();
            startActivity(new Intent(this,Login.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

