package com.gptwgl.gohostel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText fullname;
    private FirebaseAuth.AuthStateListener mauthstatelistener;
    RadioGroup mGender;
    private EditText signupemail;
   private  EditText password;
    private  EditText confirmpass;
    private Button signup;
    String Gender;
    RadioButton mGenderoption;
    private DatabaseReference ref1;
    private ProgressBar progressBar;
    private ProgressDialog PD;
    private FirebaseAuth fbauth;
    private TextView error;
    private RadioButton male;
    private RadioButton female;
   private String email;
   private String spassword;
   private  TextView reginfo;
   private  Spinner s1;
   private  static  final int SELECT_PICTURE=0;
   private ImageView imageView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        fbauth = FirebaseAuth.getInstance();
        signupemail = findViewById(R.id.signupemail);
        fullname = findViewById(R.id.username);
        password = findViewById(R.id.signuppass);
        reginfo= findViewById(R.id.reginfo);
        s1 = findViewById(R.id.usertype);
        imageView=findViewById(R.id.log);

        confirmpass= findViewById(R.id.confirmpass);
        confirmpass.setTransformationMethod(null);
        final Spinner s1 = findViewById(R.id.usertype);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.utype, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        s1.setAdapter(adapter);

        s1.setOnItemSelectedListener(this);

        final String authId= FirebaseAuth.getInstance().getUid();
        Toast.makeText(this, authId, Toast.LENGTH_SHORT).show();



        signup = findViewById(R.id.createaccount);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);

        InputMethodManager inputMethodManager= (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(signup.getWindowToken(),InputMethodManager.RESULT_UNCHANGED_SHOWN);


        mGender = findViewById(R.id.Gender);
        mGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                mGenderoption = mGender.findViewById(i);
                switch (i) {
                    case R.id.male:
                        Gender = mGenderoption.getText().toString().trim();
                        break;
                    case R.id.female:
                        Gender = mGenderoption.getText().toString().trim();
                        break;
                    default:
                }
            }
        });

        final User User=new User();


        ref1= FirebaseDatabase.getInstance().getReference().child("User");
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String et1 =fullname.getText().toString().trim();
                String text = s1.getSelectedItem().toString();
                User.setUsername(et1);
                User.setGender(Gender);
                User.setUsertype(text);
                //ref1.setValue(User);
                String confpswd = password.getText().toString();
                email = signupemail.getText().toString().trim();
                spassword = confirmpass.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    signupemail.setError("Enter email address");
                    return;
                }
                if (TextUtils.isEmpty(spassword)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!confpswd.equals(spassword))
                {

                    confirmpass.setError("Password did not match");
                   return;
                }
                progressBar.setVisibility(View.VISIBLE);
                fbauth.fetchSignInMethodsForEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                boolean check =!task.getResult().getSignInMethods().isEmpty();
                                if (check){
                                    Toast.makeText(SignupActivity.this, "Email Already registered. Please Signin", Toast.LENGTH_LONG).show();
                                }
                        else{

                            //create user
                             fbauth.createUserWithEmailAndPassword(email, spassword)
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    fbauth.getCurrentUser().sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        ref1.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(User);
                                                        progressBar.setVisibility(View.GONE);

                                                        reginfo.setText("Account created Successfully. Please verify your email through the link sent to your email(check spam) to continue login");
/*

                                                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                                                finish();*/




                                                    }
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(SignupActivity.this, "Failed",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                                }
                                            });
                                }


                            }
                        });

                     }
                        }
                });
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner s1=(Spinner)findViewById(R.id.usertype);
        s1.setOnItemSelectedListener(this);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void selectImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select picture"),SELECT_PICTURE);
    }
}




