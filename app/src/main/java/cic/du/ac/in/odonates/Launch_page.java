package cic.du.ac.in.odonates;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



    public class Launch_page extends AppCompatActivity {
        private static int timeOut = 5000;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_launch_page);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent homeIntent = new Intent(Launch_page.this,Login.class);
                    startActivity(homeIntent);
                    finish();
                }
            },timeOut);
        }
    }
