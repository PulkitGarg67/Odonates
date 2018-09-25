package cic.du.ac.in.odonates;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class NewOdonate extends AppCompatActivity {

    private FusedLocationProviderClient client;
    double lat, lng;
    EditText sn, cn, cnt;
    String sntext, cntext, cnttext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_odonate);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Odonate");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getLocation();

        sn = findViewById(R.id.sn);
        cn = findViewById(R.id.cn);
        cnt = findViewById(R.id.cnt);


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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.checklist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent back = new Intent(NewOdonate.this, HomePage.class);
            finish();
            startActivity(back);
        } else if (id == R.id.submit) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");

            String[] addresses = {getString(R.string.company_mail)};
            cnttext = cnt.getText().toString().trim();
            sntext = sn.getText().toString().trim();
            cntext = cn.getText().toString().trim();
            String body = "Scientific Name - " + sntext + "\nCommon Name - " + cntext +
                    "\nLatitude - " + lat + "\nLongitude - " + lng + "\n\n" + cnttext;
            intent.putExtra(Intent.EXTRA_EMAIL, addresses);
            intent.putExtra(Intent.EXTRA_SUBJECT, "New Odonate");
            intent.putExtra(Intent.EXTRA_TEXT, body);
            if (intent.resolveActivity(getPackageManager()) != null) {
                finish();
                startActivity(new Intent(this, HomePage.class));
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);

    }
}
