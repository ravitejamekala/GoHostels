package com.gptwgl.gohostel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private Button signin;
    private TextView fpass;
    private TextView create;
    private EditText musername,mpassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private  FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
      signin = findViewById(R.id.signinbtn);
        fpass = findViewById(R.id.forgotpswd);
      create = findViewById(R.id.createaccount);
      musername = findViewById(R.id.email);
      mpassword = findViewById(R.id.password);
      progressBar=findViewById(R.id.progressBar);
      auth = FirebaseAuth.getInstance();

        InputMethodManager inputMethodManager= (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(signin.getWindowToken(),InputMethodManager.RESULT_UNCHANGED_SHOWN);
     if (auth.getCurrentUser()!= null){
         Intent intent = new Intent(LoginActivity.this,DashboardActivity.class);
         startActivity(intent);
         finish();
     }



      create.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              startActivity(new Intent(LoginActivity.this,SignupActivity.class));

          }
      });
      signin.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              final String email = musername.getText().toString().trim();
              final String password = mpassword.getText().toString().trim();
              if (TextUtils.isEmpty(email)) {
                  musername.setError("enter email address");
                  return;
              }
              if (TextUtils.isEmpty(password)) {
                 mpassword.setError("enter Password");
                   return;
              }
              progressBar.setVisibility(View.VISIBLE);
              auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Invalid Username/Password. Please try again", Toast.LENGTH_SHORT).show();


                        }
                        else {
                            if (auth.getCurrentUser().isEmailVerified()){
                                startActivity(new Intent(LoginActivity.this,DashboardActivity.class));
                                finish();
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "Please Verify your Email", Toast.LENGTH_SHORT).show();
                            }

                        }


                    }
                });


          }

      });

      fpass.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent = new Intent(LoginActivity.this,Fpass.class);
              startActivity(intent);

          }
      });

    }
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }



}
