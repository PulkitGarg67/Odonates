package cic.du.ac.in.odonates;

import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class my_checklist extends AppCompatActivity {

    ProgressBar p;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    ListView lst;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_checklist);

        p = findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();
        p.setVisibility(View.VISIBLE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ArrayList<String> chklist = new ArrayList<String>();
        lst = findViewById(R.id.my_chklst);

        database = FirebaseDatabase.getInstance();

        getSupportActionBar().setTitle("My_Checklist");
        DatabaseReference myRef = database.getReference("Users").child(mAuth.getCurrentUser().getUid());

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chklist.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String name = ds.getKey().toString().trim();


                    chklist.add(name);

                }
                adapter = new ArrayAdapter(my_checklist.this, android.R.layout.simple_list_item_1, chklist);
                p.setVisibility(View.INVISIBLE);
                lst.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        myRef.addValueEventListener(eventListener);
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent damselIntent = new Intent(my_checklist.this,Specific.class);
//                damselIntent.putExtra("checklist",firebaseListAdapter.getItem(i).toString());
//                startActivity(damselIntent);

                Toast.makeText(my_checklist.this, adapter.getItem(i).toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
