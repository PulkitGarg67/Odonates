package cic.du.ac.in.odonates;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Checklist_user extends AppCompatActivity {

    ProgressBar p;
    FirebaseAuth mAuth;
    double lat,lng;
    Dialog Checkname;
    String name;
    EditText check,comment;
    DatabaseReference user;
    FirebaseListAdapter<dataFetch> firebaseListAdapter;
    ArrayList<String> checkedItems;
    private FusedLocationProviderClient client;

    public void SharedPrefesSAVE(String Name){
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("NAME", 0);
        SharedPreferences.Editor prefEDIT = prefs.edit();
        prefEDIT.putString("Name", Name);
        prefEDIT.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist_user);

        getLocation();

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Checklist");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        checkedItems = new ArrayList<>();

        Checkname = new Dialog(Checklist_user.this);
        Checkname.setContentView(R.layout.dialog_template);
        Checkname.setTitle("Checklist Name");
        Checkname.setCancelable(false);
        Checkname.show();
        check = (EditText)Checkname.findViewById(R.id.cn);
        Button s = Checkname.findViewById(R.id.sv);
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check.getText().toString().length() > 0) {
                    SharedPrefesSAVE(check.getText().toString());
                    name = check.getText().toString();
                    Checkname.cancel();
                }
                if (check.getText().toString().isEmpty()) {
                    check.setError("Email is Required");
                    check.requestFocus();
                    return;
                }

            }
        });
        Button c = Checkname.findViewById(R.id.ca);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checkname.cancel();
                Intent back = new Intent(Checklist_user.this, HomePage.class);
                startActivity(back);
            }
        });

        p = findViewById(R.id.progressbar);
        p.setVisibility(View.VISIBLE);
        final ListView lst = findViewById(R.id.checklist);
        lst.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        comment = findViewById(R.id.comment);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Odonate");
        final DatabaseReference uref = database.getReference();
        user = uref.child("Users").child(mAuth.getCurrentUser().getUid());
        Query select = myRef.orderByChild("cname");
        myRef.keepSynced(true);
        firebaseListAdapter = new FirebaseListAdapter<dataFetch>(Checklist_user.this, dataFetch.class, R.layout.activity_design_checklist, select) {
            @Override
            protected void populateView(View v, dataFetch model, int position) {
                String Cname = "<big>"+model.getCname()+"</big>";
                ((TextView) v.findViewById(R.id.chechbox)).setText(Html.fromHtml(Cname));
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
        }, 500);
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences SP = getApplicationContext().getSharedPreferences("Name",0);
                addToList(i);
            }
        });

    }

    private void getLocation() {
        requestPermission();

        client = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        client.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                }

            }
        });
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    private void addToList(int i) {
        //
        if (checkedItems.contains(firebaseListAdapter.getItem(i).getSname()))
            checkedItems.remove(firebaseListAdapter.getItem(i).getSname());
        else
            checkedItems.add(firebaseListAdapter.getItem(i).getSname());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.checklist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            Intent back = new Intent(Checklist_user.this,HomePage.class);
            finish();
            startActivity(back);
        } else if (id == R.id.submit) {
            if (checkedItems.size() > 0) {
                String com = comment.getText().toString().trim();
                final Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_SUBJECT, "List of Odonates : "+name);
                StringBuilder sb = new StringBuilder();
                Date c = Calendar.getInstance().getTime();

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                sb.append("Date : "+df.format(c)+"\n\n");
                sb.append("Following is the list of the Selected Odonates : \n\n");
                sb.append(name+"\n\n") ;
                sb.append("Latitude : "+lat+"\n");
                sb.append("Longitude : "+lng+"\n\n");
                for (String s : checkedItems) {
                    sb.append(s);
                    sb.append("\n");
                }
                if(com.length()!=0)
                    sb.append("\nComments : "+com);
                intent.putExtra(Intent.EXTRA_TEXT, sb.toString());
                DatabaseReference temp = user.child(name);
                for (int i = 0; i < checkedItems.size(); i++)
                    temp.push().child("sname").setValue(checkedItems.get(i));
                temp.child("Comments").setValue(com);
                temp.child("Lat").setValue(lat);
                temp.child("Lng").setValue(lng).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Checklist_user.this, "Submitted!!!", Toast.LENGTH_SHORT).show();
                        Intent back = new Intent(Checklist_user.this, HomePage.class);
                        finish();
                        startActivity(back);
                        startActivity(intent) ;

                    }
                });


            }
        }
        return super.onOptionsItemSelected(item);

    }
}
