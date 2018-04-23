package cic.du.ac.in.odonates;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class List extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        int id = intent.getIntExtra("species",1);

        final ListView lst = findViewById(R.id.lst);
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        if(id==1)   {
            getSupportActionBar().setTitle("Dragon Flies");
            DatabaseReference myRef = database.getReference("Drag");
            Query sort = myRef.orderByChild("Cname");
            myRef.keepSynced(true);
            FirebaseListAdapter<dataFetch> firebaseListAdapter = new FirebaseListAdapter<dataFetch>(List.this,dataFetch.class, android.R.layout.simple_list_item_1, sort) {
                @Override
                protected void populateView(View v, dataFetch model, int position) {
                    String sourceString = model.getCname()+" - "+"<i>" + model.getSname() + "</i> ";
                    ((TextView)v.findViewById(android.R.id.text1)).setText(Html.fromHtml(sourceString));
                }
            };
            lst.setAdapter(firebaseListAdapter);
            lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    int[] id = new int[]{1,i};
                    Intent dragIntent = new Intent(List.this,Specific.class);
                    dragIntent.putExtra("main",id);
                    startActivity(dragIntent);
                }
            });
        } else
        if(id==2)   {
            getSupportActionBar().setTitle("Damsel Flies");
            DatabaseReference myRef = database.getReference("Drag");
            Query select = myRef.orderByChild("Type").equalTo("D");
//            Query sort = select.orderByChild("Cname")
//                .startAt("as")
//                .endAt("as"+"\uf8ff");
            myRef.keepSynced(true);
            FirebaseListAdapter<dataFetch> firebaseListAdapter = new FirebaseListAdapter<dataFetch>(List.this,dataFetch.class, android.R.layout.simple_list_item_2, select) {
                @Override
                protected void populateView(View v, dataFetch model, int position) {
                    String Cname = "<big>"+model.getCname()+"</big>";
                    String Sname = "<i>"+model.getCname()+"</i>";
                    ((TextView)v.findViewById(android.R.id.text1)).setText(Html.fromHtml(Cname));
                    ((TextView)v.findViewById(android.R.id.text2)).setText(Html.fromHtml(Sname));
                }
            };
            lst.setAdapter(firebaseListAdapter);
            lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    int id[] = {2,i};
                    Intent damselIntent = new Intent(List.this,Specific.class);
                    damselIntent.putExtra("main",id);
                    startActivity(damselIntent);
                }
            });
        }
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
            Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.item1_id)   {
            Toast.makeText(this, "About Odonates", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
