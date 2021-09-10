package com.gptwgl.gohostel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class AddHostel extends AppCompatActivity implements AdapterView.OnItemSelectedListener, LocationListener {
   private DatabaseReference ref;
    private ImageView imageView;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    ArrayList<String> selection=new ArrayList<String>();
   private CheckBox hwifi,cctv,newspaper,lift,tv,hotwater;
   private EditText hname,haddress,hostelfee,locationdetails;
   private Button getlocation,photoupload,certifiacteupload,submit;
   private RadioGroup radioGroup;
   private RadioButton male,female,mGenderOptions;
   String Gender;
    private DatabaseReference ref1,ref2;
    FirebaseStorage storage;
    StorageReference storageReference;
    public String Facilities="";
    private Spinner s1;
    private LocationManager locationManager;
        private Double Lat,lang;
        private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hostel);
        hostelfee = findViewById(R.id.hostelfee);
        final Spinner s1 = findViewById(R.id.s1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.numbers, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        s1.setAdapter(adapter);

        s1.setOnItemSelectedListener(this);

        hname = findViewById(R.id.hname);
        haddress = findViewById(R.id.haddress);


        getlocation = findViewById(R.id.getlocation);
        photoupload = findViewById(R.id.upload);
        certifiacteupload = findViewById(R.id.certificatebutton);
        submit = findViewById(R.id.hstlSubmit);
        locationdetails = findViewById(R.id.locationdetails);
        radioGroup = findViewById(R.id.hosteltype);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        hwifi = findViewById(R.id.wifi);
        tv = findViewById(R.id.tv);
        lift = findViewById(R.id.Lift);
        cctv = findViewById(R.id.cctv);
        hotwater = findViewById(R.id.hotwater);
        newspaper = findViewById(R.id.newspaper);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                mGenderOptions = radioGroup.findViewById(i);
                switch (i) {
                    case R.id.male:
                        Gender = mGenderOptions.getText().toString().trim();
                        break;
                    case R.id.female:
                        Gender = mGenderOptions.getText().toString().trim();
                        break;
                    default:
                }

            }
        });
        getlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    @SuppressLint("MissingPermission") Location gps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


                if (location != null) {
                    Lat = location.getLatitude();
                    lang = location.getLongitude();
                    String latid = Lat.toString();
                    String langitd = lang.toString();
                    locationdetails.setText(latid+" "+langitd);

                }




            }
        });
        final Hostel Hostel = new Hostel();
        final HostelLocation HostelLocation = new HostelLocation();
        ref1 = FirebaseDatabase.getInstance().getReference().child("Hostel");
        ref2 = FirebaseDatabase.getInstance().getReference().child("HostelLocation");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                String Facilities = "";
                String text = s1.getSelectedItem().toString();
                String et1 = hname.getText().toString().trim();
                String et2 = haddress.getText().toString().trim();
                String et3 = hostelfee.getText().toString().trim();
                String et4 = locationdetails.getText().toString().trim();
                Double lati = Lat;
                Double langi = lang;
                for(String selections :selection){
                    Facilities=Facilities+selections+",";

                }

                Hostel.setName(et1);
                Hostel.setLocation(et2);
                Hostel.setRent(et3);
                Hostel.setLocationid(et4);
                Hostel.setRoomsharing(text);
                Hostel.setFacilites(Facilities);
                Hostel.setHostel_type(Gender);
                HostelLocation.setHostelName(et1);
                HostelLocation.setLatitude(lati);
                HostelLocation.setLongtitude(langi);

                Hostel.setLatitude(lati);
                Hostel.setLongitude(langi);

                ref1.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(Hostel);
                ref2.push().setValue(HostelLocation);
                if (TextUtils.isEmpty(et1)) {
                    hname.setError("Enter Hostel name");
                    return;
                }
                if (TextUtils.isEmpty(et2)) {
                    locationdetails.setError("Enter Hostel Address");
                    return;
                }
                if (TextUtils.isEmpty(et3)) {
                    hostelfee.setError("Enter Hostel Fee");
                    return;
                }
                if (TextUtils.isEmpty(et4)) {
                   // locationdetails.setError("Press get location button ");
                    //return;
                }
                Toast.makeText(AddHostel.this, "Hostel registered succesfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddHostel.this,DashboardActivity.class));
            }
        });


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        photoupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
                uploadImage();
            }
        });
        certifiacteupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
                uploadImage();
            }
        });
    }
        public void select(View view)
        {
            boolean checked=((CheckBox) view).isChecked();
            switch (view.getId())
            {
                case R.id.hotwater:
                    if(checked)
                        selection.add("Hot_water");

                    break;
                case R.id.cctv:
                    if(checked)
                        selection.add("CCtv");

                case R.id.wifi:
                    if(checked)
                        selection.add("Wifi");
                    else
                        selection.remove("Wifi");
                    break;
                case R.id.tv:
                    if(checked)
                        selection.add("Tv");
                    else
                        selection.remove("Tv");
                    break;
                case R.id.newspaper:
                    if(checked)
                        selection.add("Newspaper");
                    else
                        selection.remove("Newspaper");
                    break;
                case R.id.Lift :
                    if(checked)
                        selection.add("Lift");
                    else
                        selection.remove("Lift");
                    break;

                    default:


            }
            for (String selections : selection) {
                Facilities = Facilities+selections+"\n";

            }
        }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    private void uploadImage() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AddHostel.this, "Uploaded", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddHostel.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }






    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner s1=(Spinner)findViewById(R.id.s1);
        s1.setOnItemSelectedListener(this);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public void onLocationChanged(Location location) {


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
