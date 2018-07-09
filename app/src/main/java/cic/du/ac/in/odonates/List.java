package cic.du.ac.in.odonates;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class List extends AppCompatActivity {

    ProgressBar p;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    ListView lst;
    FirebaseListAdapter<dataFetch> firebaseListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        p = findViewById(R.id.progressbar);
        p.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        int id = intent.getIntExtra("species",1);

        lst = findViewById(R.id.lst);
        database = FirebaseDatabase.getInstance();

        switch (id){
            case 1:setList("Dragonfly");
                    break;
            case 2:setList("Damselfly");
                    break;
            default:Log.i("Intent Extra","Wrong Input");
                    break;
        }
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent damselIntent = new Intent(List.this,Specific.class);
                damselIntent.putExtra("Sname",firebaseListAdapter.getItem(i).getSname());
                startActivity(damselIntent);
            }
        });
    }

    private void setList(String Odonates) {
        getSupportActionBar().setTitle(Odonates);
        DatabaseReference myRef = database.getReference(Odonates);
        Query sort = myRef.orderByChild("Cname");
        myRef.keepSynced(true);
        firebaseListAdapter  = new FirebaseListAdapter<dataFetch>(List.this,dataFetch.class, android.R.layout.simple_list_item_2, sort) {
            @Override
            protected void populateView(View v, dataFetch model, int position) {
                String Cname = "<big>"+model.getCname()+"</big>";
                String Sname = "<i>"+model.getSname()+"</i>";
                ((TextView)v.findViewById(android.R.id.text1)).setText(Html.fromHtml(Cname));
                ((TextView)v.findViewById(android.R.id.text2)).setText(Html.fromHtml(Sname));
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                p.setVisibility(View.INVISIBLE);
                // Actions to do after 10 seconds
                if(!(firebaseListAdapter.getCount()>0)){
                    Toast.makeText(List.this, "Sorry Results not Found", Toast.LENGTH_SHORT).show();
                    return;
                }

                lst.setAdapter(firebaseListAdapter);
            }
        }, 5000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home) {
            Intent back = new Intent(List.this,HomePage.class);
            startActivity(back);
        }else if(id == R.id.search_id) {
            Intent homeIntent = new Intent(List.this,Search.class);
            startActivity(homeIntent);
        }else if(id == R.id.item1_id)   {
            Toast.makeText(this, "About Odonates", Toast.LENGTH_SHORT).show();
        }else if(id==R.id.item2_id){
            mAuth.signOut();
            startActivity(new Intent(this,Login.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
