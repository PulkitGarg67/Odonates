package cic.du.ac.in.odonates;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import me.relex.circleindicator.CircleIndicator;

public class Specific extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef,mDatabase,dref;
    String key;
    ProgressBar p;
    CircleIndicator circleIndicator;

    private boolean mProcesslike = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific);
        p  = findViewById(R.id.p);
        p.setVisibility(View.VISIBLE);

        Toolbar toolbar = findViewById(R.id.toolbarspe);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        String Sname = getIntent().getStringExtra("Sname");
        getSupportActionBar().setTitle(Sname);

        final ListView lst = findViewById(R.id.content);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Odonate");
        Query select = myRef.orderByChild("sname").equalTo(Sname).limitToFirst(1);

        myRef.orderByChild("sname").equalTo(Sname).limitToFirst(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    key=datas.getKey();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Specific.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        myRef.keepSynced(true);
        final FirebaseListAdapter<dataFetch> firebaseListAdapter = new FirebaseListAdapter<dataFetch>(Specific.this,dataFetch.class, android.R.layout.simple_list_item_2, select) {
            @Override
            protected void populateView(View v, dataFetch model, int position) {
                ((TextView)v.findViewById(android.R.id.text1)).setText(model.getContent());
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                p.setVisibility(View.INVISIBLE);
                // Actions to do after 10 seconds
                if(!(firebaseListAdapter.getCount()>0)){
                    Toast.makeText(Specific.this, "Sorry Results not Found", Toast.LENGTH_SHORT).show();
                    return;
                }
                lst.setAdapter(firebaseListAdapter);
            }
        }, 500);
        ViewPager viewPager = findViewById(R.id.viewPager);
        circleIndicator = findViewById(R.id.indicator);
        slideshowAdapter adapter = new slideshowAdapter(this,Sname);
        viewPager.setAdapter(adapter);
        circleIndicator.setViewPager(viewPager);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home) {
            Intent back = new Intent(Specific.this,HomePage.class);
            startActivity(back);
        }
        return super.onOptionsItemSelected(item);
    }
}
