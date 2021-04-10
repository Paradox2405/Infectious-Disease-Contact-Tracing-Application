package com.idct.id.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.idct.id.sensor.SensorArray;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity  {
    String identifier = ((SensorArray) AppDelegate.getAppDelegate().sensor()).payloadData().shortName();

    //Give your SharedPreferences file a name and save it to a static variable
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        onStartup();

        Button btnnext= findViewById(R.id.btn_next);
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                existingUser();

            }
        });




    }
public void onStartup(){
    SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
//Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
    boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);

    if(hasLoggedIn)
    {
        Intent local = new Intent(LoginActivity.this, CasesOverview.class);
        startActivity(local);
        LoginActivity.this.finish();

    }
}

public void existingUser() {

    EditText n = (EditText) findViewById(R.id.txt_name);
    String name = n.getText().toString();
    EditText i = (EditText) findViewById(R.id.txt_id);
    String id = i.getText().toString();
    EditText a = (EditText) findViewById(R.id.txt_address);
    String address = a.getText().toString();
    EditText nu = (EditText) findViewById(R.id.txt_number);
    String number = nu.getText().toString();

    // Create a new user with a first and last name
    Map<String, Object> user = new HashMap<>();
    user.put("name", name);
    user.put("ID", id);
    user.put("address", address);
    user.put("phone", number);
    user.put("Unique Identifier", identifier);

// Add a new document with a generated ID
    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    CollectionReference allUsersRef = rootRef.collection("user");
    Query uIDQuery = allUsersRef.whereEqualTo("Unique Identifier", identifier);

       uIDQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
           @Override
           public void onComplete(@NonNull Task<QuerySnapshot> task) {
               if (task.isSuccessful()) {
                   if (task.getResult().isEmpty()){
                       newUser();

               } else{
                       for (DocumentSnapshot document : task.getResult()) {
                           if (document.exists()) {
                               Toast.makeText(getApplicationContext(), "You are already Registe" +
                                       "red!", Toast.LENGTH_LONG).show();
                               checkLogin();
                               Intent local = new Intent(LoginActivity.this, CasesOverview.class);
                               startActivity(local);
                               LoginActivity.this.finish();

                           } else {
                               Log.e(null, "Id does not exists");
                           }

                       }
                   }
               } else {
                   Log.e(null, "Database Error ");
               }
           }
       });





    }
    public void newUser(){
        EditText n = (EditText) findViewById(R.id.txt_name);
        String name = n.getText().toString();
        EditText i = (EditText) findViewById(R.id.txt_id);
        String id = i.getText().toString();
        EditText a = (EditText) findViewById(R.id.txt_address);
        String address = a.getText().toString();
        EditText nu = (EditText) findViewById(R.id.txt_number);
        String number = nu.getText().toString();

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("ID", id);
        user.put("address", address);
        user.put("phone", number);
        user.put("Unique Identifier", identifier);

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference allUsersRef = rootRef.collection("user");
        Query uIDQueryF =allUsersRef.whereNotEqualTo("Unique Identifier", identifier);
        uIDQueryF.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (DocumentSnapshot document: task.getResult()) {
                        if (document.exists()) {
                            allUsersRef.add(user);
                            Log.w(null, "DocumentSnapshot added with ID: " + identifier);
                            Toast.makeText(getApplicationContext(), "User Successfully" +
                                    " Added!", Toast.LENGTH_LONG).show();
                            checkLogin();
                            Intent local = new Intent(LoginActivity.this, CasesOverview.class);
                            startActivity(local);
                            LoginActivity.this.finish();

                        }

                    }

                } else {

                    Log.e(null, "Id does not exists");

                }
            }
        });
    }
public void checkLogin(){
 //User has successfully logged in, save this information
// We need an Editor object to make preference changes.
SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0); // 0 - for private mode
SharedPreferences.Editor editor = settings.edit();
//Set "hasLoggedIn" to true
editor.putBoolean("hasLoggedIn", true);
// Commit the edits!
editor.apply();
    }
}
