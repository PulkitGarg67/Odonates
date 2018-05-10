package cic.du.ac.in.odonates;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Checklist_user extends AppCompatActivity {

    ProgressBar p;
    FirebaseAuth mAuth;
    LocationManager locationManager;
    LocationListener locationListener;
    double lat,lng;
    Dialog Checkname;
    String name;
    EditText check;
//
//    void saveCheckName (View view){
//        name = check.getText().toString().trim();
//        Checkname.dismiss();
//    }

    public void SharedPrefesSAVE(String Name){

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("NAME", 0);

        SharedPreferences.Editor prefEDIT = prefs.edit();

        prefEDIT.putString("Name", Name);

        prefEDIT.commit();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER , 0 ,1000 , locationListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist_user);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Location",location.toString());
                lat = location.getLatitude();
                lng =  location.getLongitude();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }

        };
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new  String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER , 0 ,1000 , locationListener);
        }
        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Checklist");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Checkname = new Dialog(Checklist_user.this);
        Checkname.setContentView(R.layout.dialog_template);
        Checkname.setTitle("Checklist Name");
        Checkname.show();
        check = (EditText)Checkname.findViewById(R.id.cn);
        Button s = Checkname.findViewById(R.id.sv);
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefesSAVE(check.getText().toString());
                name = check.getText().toString();
                Checkname.cancel();

            }
        });

        p = findViewById(R.id.progressbar);
        p.setVisibility(View.VISIBLE);
        final ListView lst = (ListView) findViewById(R.id.checklist);
        lst.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Odonates");
        final DatabaseReference uref = database.getReference();
        final DatabaseReference user =  uref.child("Users").child(mAuth.getCurrentUser().getUid());
        Query select = myRef.orderByChild("Type");
        myRef.keepSynced(true);
        final FirebaseListAdapter<dataFetch> firebaseListAdapter = new FirebaseListAdapter<dataFetch>(Checklist_user.this,dataFetch.class, R.layout.activity_design_checklist, select) {
            @Override
            protected void populateView(View v, dataFetch model, int position) {
                String Cname = "<big>"+model.getCname()+"</big>";
                ((TextView)v.findViewById(R.id.imageView)).setText(Html.fromHtml(Cname));
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                p.setVisibility(View.INVISIBLE);
                // Actions to do after 10 seconds
                if(!(firebaseListAdapter.getCount()>0)){
                    Toast.makeText(Checklist_user.this, "Sorry Results not Found", Toast.LENGTH_SHORT).show();
                    return;
                }
                lst.setAdapter(firebaseListAdapter);
            }
        }, 5000);

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent damselIntent = new Intent(Checklist_user.this,Specific.class);
//                damselIntent.putExtra("Sname",firebaseListAdapter.getItem(i).getSname());
//                startActivity(damselIntent);
                SharedPreferences SP = getApplicationContext().getSharedPreferences("Name",0);
                DatabaseReference temp = user.child(name).child(firebaseListAdapter.getItem(i).getSname());
                temp.child("Sname").setValue(firebaseListAdapter.getItem(i).getSname());
                temp.child("Lat").setValue(lat);
                temp.child("Lng").setValue(lng);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            Intent back = new Intent(Checklist_user.this,HomePage.class);
            startActivity(back);
        }
        return super.onOptionsItemSelected(item);
    }
}
