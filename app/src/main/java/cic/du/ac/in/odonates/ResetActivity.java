package cic.du.ac.in.odonates;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class ResetActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText email;
    TextView reset;
    String emailaddr;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        toolbar = findViewById(R.id.toolbar);
        email = findViewById(R.id.editText);
        reset = findViewById(R.id.resetPass);
        mAuth = FirebaseAuth.getInstance();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailaddr = email.getText().toString().trim();
                if (emailaddr.isEmpty()) {
                    email.setError("Email field can't be empty");
                    email.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailaddr).matches()) {
                    email.setError("Please enter a valid Email");
                    email.requestFocus();
                } else {
                    mAuth.sendPasswordResetEmail(emailaddr).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ResetActivity.this, "Reset e-mail has been sent", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ResetActivity.this, Login.class));
                            } else {

                                Toast.makeText(ResetActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });


    }
}
