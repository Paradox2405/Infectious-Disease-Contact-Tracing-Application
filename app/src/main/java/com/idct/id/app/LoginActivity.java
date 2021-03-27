package com.idct.id.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.idct.id.sensor.SensorArray;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnnext= findViewById(R.id.btn_next);
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adddata();
            }
        });




    }

public void adddata(){

    EditText n = (EditText) findViewById(R.id.txt_name);
    String name=n.getText().toString();
    EditText a= (EditText) findViewById(R.id.txt_address);
    String address=a.getText().toString();
    EditText nu = (EditText) findViewById(R.id.txt_id);
    String number=nu.getText().toString();
       String identifier = ((SensorArray) AppDelegate.getAppDelegate().sensor()).payloadData().shortName();
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Create a new user with a first and last name
    Map<String, Object> user = new HashMap<>();
    user.put("name", name);
    user.put("address", address);
    user.put("phone", number);
    user.put("Unique Identifier", identifier);

// Add a new document with a generated ID

    db.collection("user").whereEqualTo("Unique Identifier",true).get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()){
                            String dbidentifier= document.getString("Unique Identifier");

                            if(dbidentifier.equalsIgnoreCase(identifier)){

                                Intent local = new Intent(LoginActivity.this,CasesOverview.class);
                                startActivity(local);
                            }

                        }
                    } else {
                        Log.d(null, "Error getting documents: ", task.getException());

                        db.collection("user")
                                .add(user)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.w(null,"DocumentSnapshot added with ID: " + documentReference.getId());
                                        Intent local = new Intent(LoginActivity.this,CasesOverview.class);
                                        startActivity(local);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(null, "Error adding document", e);
                                    }
                                });
                    }
                        }

            });

}

}
