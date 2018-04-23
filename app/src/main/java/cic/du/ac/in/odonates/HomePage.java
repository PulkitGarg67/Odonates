package cic.du.ac.in.odonates;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class HomePage extends AppCompatActivity {

    Toolbar toolbar;

    public void location(View view  ){
        Intent homeIntent = new Intent(HomePage.this,MapsActivity.class);
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

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ODONATA");
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
        }else if(id == R.id.search_id) {
            Intent homeIntent = new Intent(HomePage.this,Search.class);
            startActivity(homeIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}

