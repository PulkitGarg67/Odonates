package cic.du.ac.in.odonates;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

public class Login extends AppCompatActivity {

    EditText email,password;
    String emailaddress,passwordtext;
    ProgressBar progressBar;
    Toolbar toolbar;
    private FirebaseAuth mAuth;

    public void newUser (View view){
        Intent intent = new Intent(Login.this,SignUp.class);
        finish();
        startActivity(intent);
    }

    public void login(View view){
        emailaddress = email.getText().toString().trim();
        passwordtext = password.getText().toString().trim();
        if(emailaddress.isEmpty()) {
            email.setError("Email is Required");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailaddress).matches()) {
            email.setError("Please enter a valid Email");
            email.requestFocus();
            return;
        }
        if(passwordtext.isEmpty())  {
            password.setError("Password is Requires");
            password.requestFocus();
            return;
        }
        if(passwordtext.length()<6) {
            password.setError("Minimum length of password should be 6");
            password.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(emailaddress,passwordtext).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.INVISIBLE);
                if(task.isSuccessful()) {
                    if(!(mAuth.getCurrentUser().isEmailVerified())){
                        mAuth.getCurrentUser().sendEmailVerification();
                        View v = findViewById(R.id.login);
                        Snackbar.make(v, "Please Verify your Email !", Snackbar.LENGTH_LONG).show();
                        mAuth.signOut();
                        return;
                    }
                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent homeIntent = new Intent(Login.this,HomePage.class);
                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homeIntent);
                    finish();
                }   else {
                    if(task.getException() instanceof FirebaseAuthInvalidUserException) {
                        Toast.makeText(Login.this, "User Not Registered", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        progressBar = findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!= null)    {
            FirebaseUser user = mAuth.getCurrentUser(); // mAuth is your current firebase auth instance
            user.getToken(true).addOnCompleteListener(this, new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        Log.i("TOKEN","token=" + task.getResult().getToken());
                    } else {
                        Log.i("Token", "exception=" +task.getException().toString());
                    }
                }
            });
            finish();
            startActivity(new Intent(this,HomePage.class));
        }
    }
}
