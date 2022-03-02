package com.example.intelligenttourist;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Agency extends AppCompatActivity {
    String[] items =  {"Cultural Tourism","Leisure Tourism","Religious Tourism","Medical Tourism"};
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterItems;
    EditText triptilte,tripdescription,starttime,endtime,Price,place,address,dropoffpickup;
    Button addimage,add_location,dropoffpickuplocation,add_place,add_trip;
    TextView triptype;
    ImageView imageView;
    int photo = 200;
    FusedLocationProviderClient fusedLocationProviderClient;
    ArrayList<String> Items;
    ArrayAdapter<String>adapter;
    ListView l;
    String addr;
    String addr2;
    FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agency);
        mAuth = FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        database=FirebaseDatabase.getInstance();
        DatabaseReference ref=database.getReference("Trips");
        triptilte=findViewById(R.id.title);
        imageView=findViewById(R.id.imageView);
        addimage=findViewById(R.id.add);
        tripdescription=findViewById(R.id.description);
        address=findViewById(R.id.address);
        add_location=findViewById(R.id.addlocation);
        starttime=findViewById(R.id.from);
        endtime=findViewById(R.id.to);
        dropoffpickup=findViewById(R.id.pickupdropoff);
        dropoffpickuplocation=findViewById(R.id.picklocation);
        Price=findViewById(R.id.price);
        add_place=findViewById(R.id.addplace);
        place=findViewById(R.id.places);
        triptype=findViewById(R.id.type);
        l=findViewById(R.id.list);
        add_trip=findViewById(R.id.addtrip);
        autoCompleteTxt = findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<String>(this,R.layout.list_item,items);
        autoCompleteTxt.setAdapter(adapterItems);
        Items=new ArrayList<String>();
        adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,Items);
        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),"Item: "+item,Toast.LENGTH_SHORT).show();
                triptype.setText(item +" is checked");
            }
        });
        add_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=triptilte.getText().toString();
                String description=tripdescription.getText().toString();
                String addres=address.getText().toString();
                String start=starttime.getText().toString();
                String end=endtime.getText().toString();
                String drop_pick=dropoffpickup.getText().toString();
                String price=Price.getText().toString();
                String Triptype=triptype.getText().toString();
                String places=place.getText().toString();
                imageView.buildDrawingCache();
                Bitmap bmap = imageView.getDrawingCache();
                ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                bmap.compress(Bitmap.CompressFormat.PNG,100, baos);
                byte [] b=baos.toByteArray();
                String temp=Base64.encodeToString(b, Base64.DEFAULT);
                if(title.isEmpty()&&description.isEmpty()&&addres.isEmpty()&&start.isEmpty()&&end.isEmpty()&&drop_pick.isEmpty()&&price.isEmpty()&&Triptype.isEmpty()&&places.isEmpty()&&temp.isEmpty())
                {
                    Toast.makeText(Agency.this, "Please Fill All Data!!!", Toast.LENGTH_SHORT).show();
                }
                if(title.isEmpty())
                {
                    triptilte.setError("Trip title is invalid!");
                }
                else
                if(temp.isEmpty())
                {
                    Toast.makeText(Agency.this,"image is invalid",Toast.LENGTH_LONG).show();
                }
                else
                if(description.isEmpty())
                {
                    tripdescription.setError("Trip description is invalid!");
                }
                else
                if(addres.isEmpty())
                {
                    address.setError("Address is invalid!");
                }
                else
                if(start.isEmpty())
                {
                    starttime.setError("Start time is invalid!");
                }
                else
                if(end.isEmpty())
                {
                    endtime.setError("End time is invalid!");
                }
                else
                if(drop_pick.isEmpty())
                {
                    dropoffpickup.setError("Pick up & Drop off location is invalid!");
                }
                else
                if(price.isEmpty())
                {
                    Price.setError("Price is invalid!");
                }
                else
                if(items.equals(" "))
                {
                    place.setError("Places is invalid!");
                }
                else
                if(Triptype.isEmpty())
                {
                    triptype.setError("Trip Type is invalid!");
                }
                TripsHelper trip=new TripsHelper(title,description,addres,start,end,drop_pick,price,Triptype,Items,temp);
                ref.child(title).setValue(trip);
                ref.setValue(trip);
            }
        });
        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picture();
            }
        });
        l.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.remove(adapter.getItem(position));
                return false;
            }
        });
        add_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(Agency.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    getLocation();
                }
                else {
                    ActivityCompat.requestPermissions(Agency.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
                Toast.makeText(getApplicationContext(), "Address: " + addr, Toast.LENGTH_LONG).show();
                address.setText("Address :"+ addr);
            }
            private void getLocation() {
                if (ActivityCompat.checkSelfPermission(Agency.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Agency.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location != null) {
                            try {
                                Geocoder geocoder = new Geocoder(Agency.this, Locale.getDefault());
                                List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                addr = addressList.get(0).getAddressLine(0);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
            }
        });
        dropoffpickuplocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(Agency.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(Agency.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);

                }
                Toast.makeText(getApplicationContext(), "Address: " + addr2, Toast.LENGTH_LONG).show();
                dropoffpickup.setText("Address :"+ addr2);
            }
            private void getLocation() {
                if (ActivityCompat.checkSelfPermission(Agency.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Agency.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location != null) {
                            try {
                                Geocoder geocoder = new Geocoder(Agency.this, Locale.getDefault());
                                List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                addr2 = addressList.get(0).getAddressLine(0);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
            }
        });
        add_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String placess=place.getText().toString();
                if(placess.isEmpty())
                {
                    Toast.makeText(Agency.this, "No place Added!!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Items.add(placess);
                    place.setText(" ");
                    l.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
    void picture() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select image"), photo);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == photo) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    imageView.setImageURI(selectedImageUri);
                }
            }
        }
    }

}