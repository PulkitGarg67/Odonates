package cic.du.ac.in.odonates;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
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

    public String refineString(String s){
        String upper_case_line = "";
        Scanner lineScan = new Scanner(s);
        while(lineScan.hasNext()) {
            String word = lineScan.next();
            upper_case_line += Character.toUpperCase(word.charAt(0)) + word.substring(1) + " ";
        }
        return (upper_case_line.trim());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ODONATA");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        result = findViewById(R.id.result);

        database = FirebaseDatabase.getInstance();
        mref = database.getReference("Odonate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setIconified(false);
        searchView.setQueryHint("Enter Scientific Name.....");
        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(Search.this,HomePage.class);
                finish();
                startActivity(back);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                final ProgressBar p = findViewById(R.id.p);
                p.setVisibility(View.VISIBLE);
                String s = refineString(query);
                Query select =mref.orderByChild("sname").startAt(s).endAt(s+"\uf8ff");
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
                }, 300);

                result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent damselIntent = new Intent(Search.this,Specific.class);
                        damselIntent.putExtra("Sname",firebaseListAdapter.getItem(i).getSname());
                        startActivity(damselIntent);
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
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