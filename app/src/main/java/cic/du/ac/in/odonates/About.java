package cic.du.ac.in.odonates;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class About extends AppCompatActivity {

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about_page,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home) {
            Intent back = new Intent(this,HomePage.class);
            startActivity(back);
        }else if(id == R.id.search_id) {
            Intent homeIntent = new Intent(this,Search.class);
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
