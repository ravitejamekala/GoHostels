package com.gptwgl.gohostel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class HelpActivity extends AppCompatActivity {
    private Button btn;
    private EditText feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        btn = findViewById(R.id.feedbtn);
        feedback= findViewById(R.id.feedbacktxt);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto"));
                String[] to = {"gptwarangal004@gmail.com"};
                intent.putExtra(Intent.EXTRA_SUBJECT,"Feedback Go Hostel");
                intent.putExtra(Intent.EXTRA_EMAIL,to);
                intent.putExtra(Intent.EXTRA_TEXT,feedback.getText().toString());
                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent,"Select an email client to send feedback"));

            }
        });


    }



}
