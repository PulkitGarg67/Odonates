package cic.du.ac.in.odonates;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class specific_checklist extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    ListView lst;
    ArrayAdapter<String> adapter;

    TextView list,comment,loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_checklist);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list = findViewById(R.id.txt2);
        comment = findViewById(R.id.txt3);
        loc = findViewById(R.id.txt4);
        String name = getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(name);

        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users").child(mAuth.getCurrentUser().getUid()).child(name);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String lst ="a\n";
                StringBuilder stringBuilder = new StringBuilder();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    lst = (String) ds.child("sname").getValue();
                    if(!TextUtils.isEmpty(lst)) {
                        stringBuilder.append(lst);
                        stringBuilder.append("\n");
                    }
                }
                list.setText(stringBuilder.toString());
                StringBuilder builder = new StringBuilder();
                builder.append("Lat : ");
                builder.append(dataSnapshot.child("Lat").getValue().toString());
                builder.append("\nLng : ");
                builder.append(dataSnapshot.child("Lng").getValue().toString());

                String comm =TextUtils.isEmpty(dataSnapshot.child("Comments").getValue().toString())?"-------------":dataSnapshot.child("Comments").getValue().toString();

                comment.setText("Comments : "+ comm);
                loc.setText(builder.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        myRef.addValueEventListener(eventListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            Intent back = new Intent(specific_checklist.this,my_checklist.class);
            startActivity(back);
        }
        return super.onOptionsItemSelected(item);
    }
}
