package com.fidenz.dev.crmdemo.Actvities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fidenz.dev.crmdemo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView Signup;
    private FirebaseAuth mAuth;
    private EditText editTextEmail;
    private EditText editTextPassword;

    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.textViewSignup).setOnClickListener(this);
        findViewById(R.id.buttonLogin).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.textViewSignup:


                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
                break;


            case R.id.buttonLogin:

                loginUser();


        }

    }

    private void loginUser() {


        String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Minimum lenght of password should be 6");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if (task.isSuccessful()) {

                    progressBar.setVisibility(View.GONE);

                    finish();
                    Intent intent = new Intent(MainActivity.this, ContactList.class);
                    startActivity(intent);

                    Toast.makeText(getApplicationContext(), "Successfully loggedIN", Toast.LENGTH_LONG).show();


                } else {

                    Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_LONG).show();

                }


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();


        if (mAuth.getCurrentUser() != null) {


            finish();
            Intent intent = new Intent(MainActivity.this, ContactList.class);

            String email = mAuth.getCurrentUser().getEmail();
            Bundle bundle = new Bundle();
            bundle.putString("email", email);

            startActivity(intent);


        }

    }
}
