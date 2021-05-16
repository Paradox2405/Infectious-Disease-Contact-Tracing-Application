package com.idcta.proj.app;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class CasesOverview extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RequestQueue mRequestQueue;
    private DrawerLayout drawer;
    /// REQUIRED: Unique permission request code, used by requestPermission and onRequestPermissionsResult.
    private final static int permissionRequestCode = 1249951875;
    private final static String tag = CasesOverview.class.getName();

    //Give your SharedPreferences file a name and save it to a static variable
    public static final String PREFS_NAME = "MyPrefsFile";
    final int delay = 60000;//refresh cases interval
    final Handler handler = new Handler();

    //for QR scanner
    private CodeScanner mCodeScanner;
    ImageButton btn_qr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cases_overview);

        Toolbar toolbar= findViewById(R.id.toolbar);
        toolbar.setTitle("Local Statistics");
        setSupportActionBar(toolbar);

        drawer=findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        // REQUIRED : Ensure app has all required permissions
        requestPermissions();


        btn_qr =(ImageButton)findViewById(R.id.btn_qr);
        btn_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CasesOverview.this, QrScanner.class);
                startActivity(intent);
            }
//                CodeScannerView scannerView = findViewById(R.id.scanner_view);
//                mCodeScanner = new CodeScanner(QrScanner, scannerView);
//                mCodeScanner.setDecodeCallback(new DecodeCallback() {
//
//                    @Override
//                    public void onDecoded(@NonNull Result result) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(CasesOverview.this, result.getText(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                    });
//                scannerView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mCodeScanner.startPreview();
//                    }
//                });
//            }

        });

//test of adding header data
//     TextView name= (TextView) findViewById(R.id.txt_name);
//        SharedPreferences prefs= getSharedPreferences(CasesOverview.PREFS_NAME,0);
//        String names= prefs.getString("name", "");
//        name.setText(names);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setCheckedItem(R.id.nav_localcases);

        mLayoutManager= new GridLayoutManager(this,1);
        mRequestQueue= Volley.newRequestQueue(this);
//run cases once
        parseJSON();
        //refresh cases
        handler.postDelayed(new Runnable() {
            public void run() {
                parseJSON();
                System.out.println("Cases Refreshed!"); // Do your work here
                handler.postDelayed(this, delay);
            }
        }, delay);


    }

    private void requestPermissions() {
        // Check and request permissions
        final List<String> requiredPermissions = new ArrayList<>();

        requiredPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        requiredPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        requiredPermissions.add(Manifest.permission.BLUETOOTH);
        requiredPermissions.add(Manifest.permission.BLUETOOTH_ADMIN);
        requiredPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        requiredPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        requiredPermissions.add(Manifest.permission.INTERNET);
        requiredPermissions.add(Manifest.permission.CAMERA);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            requiredPermissions.add(Manifest.permission.FOREGROUND_SERVICE);
        }
        requiredPermissions.add(Manifest.permission.WAKE_LOCK);
        final String[] requiredPermissionsArray = requiredPermissions.toArray(new String[0]);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(requiredPermissionsArray, permissionRequestCode);
        } else {
            ActivityCompat.requestPermissions(this, requiredPermissionsArray, permissionRequestCode);
        }
    }

    /// REQUIRED : Handle permission results.
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == permissionRequestCode) {
            boolean permissionsGranted = true;
            for (int i = 0; i < permissions.length; i++) {
                final String permission = permissions[i];
                if (grantResults[i] != PERMISSION_GRANTED) {
                    Log.e(tag, "Permission denied (permission=" + permission + ")");
                    permissionsGranted = false;
                } else {
                    Log.d(tag, "Permission granted (permission=" + permission + ")");
                }
            }

            if (!permissionsGranted) {
                Log.e(tag, "Application does not have all required permissions to start (permissions=" + Arrays.asList(permissions) + ")");
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()){
            case R.id.nav_localcases:
                Intent local = new Intent(CasesOverview.this,CasesOverview.class);
                startActivity(local);
                CasesOverview.this.finish();
                break;

            case R.id.nav_mapview:
                Intent maps = new Intent(CasesOverview.this,Mapview.class);
                startActivity(maps);
                CasesOverview.this.finish();
                break;
            case R.id.nav_contactlogs:
                Intent contacts = new Intent(CasesOverview.this,Contactlogs.class);
                startActivity(contacts);
                CasesOverview.this.finish();
                break;
            case R.id.nav_share:
                Intent share = new Intent(CasesOverview.this,MainActivity.class);
                startActivity(share);
                CasesOverview.this.finish();
                break;
//                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
//                break;
            case R.id.nav_send:
                Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
        }

    private void parseJSON(){
        String url="https://hpb.health.gov.lk/api/get-current-statistical";
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonobject = response.getJSONObject("data");

                    String updateTime =jsonobject.getString("update_date_time");
                    int deaths= jsonobject.getInt("local_deaths");
                    int recoveries= jsonobject.getInt("local_recovered");
                    int hospitalizedCases= jsonobject.getInt("local_total_number_of_individuals_in_hospitals");
                    int totalCases= jsonobject.getInt("local_total_cases");
                    int newCases= jsonobject.getInt("local_new_cases");
                    int deathstoday=jsonobject.getInt("local_new_deaths");

                    TextView time = findViewById(R.id.update_time);
                    time.setText("Updated at:  "+updateTime);

                    ArrayList<PandemicItem> pandemicItems = new ArrayList<>();
                    pandemicItems.add(new PandemicItem(R.drawable.total_count,"Total Confirmed Cases",totalCases));
                    pandemicItems.add(new PandemicItem(R.drawable.total_recoveries,"Recovered & Discharged",recoveries));
                    pandemicItems.add(new PandemicItem(R.drawable.active_cases,"Suspected & Hospitalized",hospitalizedCases));
                    pandemicItems.add(new PandemicItem(R.drawable.cases_today,"New Cases (Today)",newCases));
                    pandemicItems.add(new PandemicItem(R.drawable.dead,"Total Deaths",deaths));
                    pandemicItems.add(new PandemicItem(R.drawable.dead_today,"Deaths\n(Today)",deathstoday));


                    mRecyclerView=findViewById(R.id.recyclerView);
                    mRecyclerView.setHasFixedSize(true);

                    mAdapter= new PandemicItemAdapter(pandemicItems);

                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);


                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"Error in fetching data",Toast.LENGTH_LONG).show();
                    System.out.println(e);


                }
            }
        }, error -> {
        error.printStackTrace();
            Toast.makeText(getApplicationContext(),"Check Internet Connection",Toast.LENGTH_LONG).show();
        });
        mRequestQueue.add(request);
    }


}
