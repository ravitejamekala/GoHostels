package com.gptwgl.gohostel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {
    private Button signOut;
    private TextView tvName,tvGender,tvUType;
    private FirebaseAuth auth;

    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        signOut = findViewById(R.id.psignout);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this,LoginActivity.class);
                auth.getInstance().signOut();
                startActivity(intent);
                finish();
            }
        });

        tvName = findViewById(R.id.pusername);
        tvGender = findViewById(R.id.pgender);
        tvUType = findViewById(R.id.pusertype);

        dbRef = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tvName.setText(String.valueOf(dataSnapshot.child("username").getValue()));
                tvGender.setText(String.valueOf(dataSnapshot.child("gender").getValue()));
                tvUType.setText(String.valueOf(dataSnapshot.child("usertype").getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
