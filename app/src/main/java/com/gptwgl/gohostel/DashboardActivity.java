package com.gptwgl.gohostel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashboardActivity extends AppCompatActivity {
    private CardView search;
    private CardView addhostel;
    private CardView settings;
    private CardView signout;
    private FirebaseAuth auth;

    private TextView helloUser;
    private FirebaseDatabase fb;
    private DatabaseReference databaseReference;
    private  String userid;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        search = findViewById(R.id.dsearch);
        addhostel= findViewById(R.id.addhostel);
        settings= findViewById(R.id.dsettings);
        signout= findViewById(R.id.dsignout);
       helloUser= findViewById(R.id.welcome);
        user = auth.getInstance().getCurrentUser();
        userid = user.getUid();
       databaseReference = FirebaseDatabase.getInstance().getReference().child("User");



        auth= FirebaseAuth.getInstance();
       search.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(DashboardActivity.this,MainActivity.class));

           }
       });
       addhostel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(DashboardActivity.this,AddHostel.class));

           }
       });
       signout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(DashboardActivity.this,LoginActivity.class);
               auth.getInstance().signOut();
               startActivity(intent);
               finish();
           }
       });
    }



}
