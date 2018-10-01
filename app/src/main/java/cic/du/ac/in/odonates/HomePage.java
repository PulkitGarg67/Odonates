package cic.du.ac.in.odonates;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Random;

public class HomePage extends AppCompatActivity {


    Toolbar toolbar;
    FirebaseAuth mAuth;
    ImageView img;
    ProgressBar p;
    TextView t;
    FirebaseDatabase database;
    String snames;
    long elapsedDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mAuth = FirebaseAuth.getInstance();
        img = findViewById(R.id.topOdonate);
        t = findViewById(R.id.text);
        toolbar = findViewById(R.id.toolbar);
        p = findViewById(R.id.main_p);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        p.setVisibility(View.VISIBLE);

        database = FirebaseDatabase.getInstance();
        set_daily_image();

    }

    private void set_daily_image() {

        if (!(isNetworkAvailable())) {
            Toast.makeText(this, "Check Your Connection And retry.", Toast.LENGTH_SHORT).show();
        } else {
            final long current = Calendar.getInstance().getTimeInMillis();
            getLastInstance("time", current);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    if (elapsedDays != 0) {
                        Log.i("passed","passed");
                        database.getReference("time").setValue(current);
                        database.getReference("Odonate").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists())
                                    Log.e("snap: ", "not exist");
                                long count = dataSnapshot.getChildrenCount();

                                int c = (int) count;
                                int randomNumber = new Random().nextInt(c);
                                while (randomNumber == 0)
                                    randomNumber = new Random().nextInt(c);
                                updatePos(randomNumber);
                                int i = 0;
                                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                    if (i == randomNumber) {
                                        snames = snap.child("sname").getValue().toString();
                                        set(snap.child("sname").getValue().toString());
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
                    } else {

                        database.getReference("pos").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Long randomNumber = (Long) (dataSnapshot.getValue());
                                setpic(randomNumber);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(HomePage.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                                p.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }
            }, 300);
        }
    }

    private void updatePos(int randomNumber) {
        database.getReference("pos").setValue(randomNumber);
    }

    private void setpic(final long random) {
        database.getReference("Odonate").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists())
                    Log.e("snap: ", "not exist");
                long count = dataSnapshot.getChildrenCount();
                long i = 0;
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    if (i == random) {
                        snames = snap.child("sname").getValue().toString();
                        set(snap.child("sname").getValue().toString());
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


    private void getLastInstance(String time, final long curTime) {

        database.getReference(time).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long Value = (Long) dataSnapshot.getValue();
                difference(Value, curTime);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void difference(long value, long curtime) {
        long different = curtime - value;
        elapsedDays = different / (60000 * 24);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    void set(String Sname) {
        t.setText(Sname);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(Sname + "/img_1.JPG");
        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Picasso.get().load(task.getResult()).into(img);
                p.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void location(View view) {
        Intent homeIntent = new Intent(HomePage.this, Checklist_user.class);
        startActivity(homeIntent);
    }

    public void new_odonate(View  view){
        startActivity(new Intent(this,NewOdonate.class));
    }
    public void list1(View view) {
        Intent homeIntent = new Intent(HomePage.this, List.class);
        homeIntent.putExtra("species", 1);
        startActivity(homeIntent);
    }

    public void list2(View view) {
        Intent homeIntent = new Intent(HomePage.this, List.class);
        homeIntent.putExtra("species", 2);
        startActivity(homeIntent);
    }

    public void details(View view) {
        Intent damselIntent = new Intent(this, Specific.class);
        damselIntent.putExtra("Sname", snames);
        startActivity(damselIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.item1_id) {
            startActivity(new Intent(this, About.class));
        } else if (id == R.id.search_id) {
            Intent homeIntent = new Intent(HomePage.this, Search.class);
            startActivity(homeIntent);
        } else if (id == R.id.item2_id) {
            mAuth.signOut();
            startActivity(new Intent(this, Login.class));
            finish();
        }else if (id == R.id.download){
            Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(getString(R.string.pdfFile)));
            startActivity(intent);
        }else if (id == R.id.chklist){
            startActivity(new Intent(HomePage.this,my_checklist.class));
        }
        return super.onOptionsItemSelected(item);
    }


}

