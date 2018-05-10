package cic.du.ac.in.odonates;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Scanner;


public class Search extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference mref;
    ListView result;

    EditText Sname;
    public void search(View view){
        final ProgressBar p = findViewById(R.id.p);
        p.setVisibility(View.VISIBLE);
        String s = Sname.getText().toString();
        String upper_case_line = "";
        Scanner lineScan = new Scanner(s);
        while(lineScan.hasNext()) {
            String word = lineScan.next();
            upper_case_line += Character.toUpperCase(word.charAt(0)) + word.substring(1) + " ";
        }
        System.out.println(upper_case_line.trim());
        Query select =mref.orderByChild("Sname").startAt(s);
        mref.keepSynced(true);
        final FirebaseListAdapter<dataFetch> firebaseListAdapter = new FirebaseListAdapter<dataFetch>(Search.this,dataFetch.class, android.R.layout.simple_list_item_2, select) {
            @Override
            protected void populateView(View v, dataFetch model, int position) {
                String Cname = "<big>"+model.getCname()+"</big>";
                String Sname = "<i>"+model.getSname()+"</i>";
                ((TextView)v.findViewById(android.R.id.text1)).setText(Html.fromHtml(Cname));
                ((TextView)v.findViewById(android.R.id.text2)).setText(Html.fromHtml(Sname));
            }
        };
        result.setAdapter(firebaseListAdapter);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                p.setVisibility(View.INVISIBLE);
                // Actions to do after 10 seconds
                if(!(firebaseListAdapter.getCount()>0)){
                    Toast.makeText(Search.this, "Sorry Result not Found", Toast.LENGTH_SHORT).show();
                    return;
                }
                result.setAdapter(firebaseListAdapter);
            }
        }, 5000);



        result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent damselIntent = new Intent(Search.this,Specific.class);
                damselIntent.putExtra("Sname",firebaseListAdapter.getItem(i).getSname());
                startActivity(damselIntent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ODONATA");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Sname = findViewById(R.id.editText);
        result = findViewById(R.id.result);

        database = FirebaseDatabase.getInstance();
        mref = database.getReference("Odonates");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            Intent back = new Intent(Search.this,HomePage.class);
            startActivity(back);
        }
        return super.onOptionsItemSelected(item);
    }
}