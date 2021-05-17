package com.idcta.proj.app;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.idcta.proj.sensor.Sensor;
import com.idcta.proj.sensor.SensorArray;
import com.idcta.proj.sensor.SensorDelegate;
import com.idcta.proj.sensor.analysis.SocialDistance;
import com.idcta.proj.sensor.datatype.ImmediateSendData;
import com.idcta.proj.sensor.datatype.Location;
import com.idcta.proj.sensor.datatype.PayloadData;
import com.idcta.proj.sensor.datatype.Proximity;
import com.idcta.proj.sensor.datatype.SensorState;
import com.idcta.proj.sensor.datatype.SensorType;
import com.idcta.proj.sensor.datatype.TargetIdentifier;
import com.idcta.proj.sensor.datatype.TimeInterval;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class Contactlogs extends AppCompatActivity implements SensorDelegate,
        NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemClickListener {
    private DrawerLayout drawer;


    ImageButton btn_share;

    private final static String tag = MainActivity.class.getName();

    /// Test UI specific data, not required for production solution.
    private final static SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
    private boolean foreground = false;

    // MARK:- Events
    private long didDetect = 0, didRead = 0, didMeasure = 0, didShare = 0, didReceive = 0;

    // MARK:- Detected payloads
    private final Map<TargetIdentifier, PayloadData> targetIdentifiers = new ConcurrentHashMap<>();
    private final Map<PayloadData,Target> payloads = new ConcurrentHashMap<>();
    private final List<Target> targets = new ArrayList<>();
    private TargetListAdapter targetListAdapter = null;

    // MARK:- Social mixing
    private final SocialDistance socialMixingScore = new SocialDistance();
    private TimeInterval socialMixingScoreUnit = new TimeInterval(60);



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactlogs);


        //share button event
        btn_share =(ImageButton)findViewById(R.id.btn_sharelogs);
        btn_share.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {


                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                        "Android/media/com.idcta.proj.app/Sensor/detection.csv");

                Uri path = FileProvider.getUriForFile(Contactlogs.this,BuildConfig.APPLICATION_ID+".provider",file);

                    Intent fileIntent = new Intent(Intent.ACTION_SEND);
                    fileIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    fileIntent.setType("text/*");
                    fileIntent.putExtra(Intent.EXTRA_STREAM, path);
                    Toast.makeText(Contactlogs.this," "+path,Toast.LENGTH_LONG).show();
                    startActivity(Intent.createChooser(fileIntent, "Send"));


            }
        });





        // Test UI specific process to gather data from sensor for presentation
        final Sensor sensor = AppDelegate.getAppDelegate().sensor();
        sensor.add(this);
        sensor.add(socialMixingScore);
        ((TextView) findViewById(R.id.payload)).setText("Private Identifier : " + ((SensorArray) AppDelegate.getAppDelegate().sensor()).payloadData().shortName());
        targetListAdapter = new TargetListAdapter(this, targets);
        final ListView targetsListView = ((ListView) findViewById(R.id.targets));
        targetsListView.setAdapter(targetListAdapter);
        targetsListView.setOnItemClickListener(this);


        Toolbar toolbar= findViewById(R.id.toolbar);
        toolbar.setTitle("Persons Contacted");
        setSupportActionBar(toolbar);


        drawer=findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setCheckedItem(R.id.nav_contactlogs);
    }




    private synchronized void updateTargets() {
        // De-duplicate targets based on short name and last updated at time stamp
        final Map<String,Target> shortNames = new HashMap<>(payloads.size());
        for (Map.Entry<PayloadData,Target> entry : payloads.entrySet()) {
            final String shortName = entry.getKey().shortName();
            final Target target = entry.getValue();
            final Target duplicate = shortNames.get(shortName);
            if (duplicate == null || duplicate.lastUpdatedAt().getTime() < target.lastUpdatedAt().getTime()) {
                shortNames.put(shortName, target);
            }
        }
        // Get target list in alphabetical order
        final List<Target> targetList = new ArrayList<>(shortNames.values());
        Collections.sort(targetList, new Comparator<Target>() {
            @Override
            public int compare(Target t0, Target t1) {
                return t0.payloadData().shortName().compareTo(t1.payloadData().shortName());
            }
        });
        // Update UI
        ((TextView) findViewById(R.id.detection)).setText("DETECTION (" + targetListAdapter.getCount() + ")");
        targetListAdapter.clear();
        targetListAdapter.addAll(targetList);
    }
    // Update social distance score
    private void updateSocialDistance(TimeInterval unit) {
        final long millisecondsPerUnit = unit.value * 1000;

        final long epoch = (new Date().getTime() / millisecondsPerUnit) - 11;
        for (int i=0; i<=11; i++) {
            // Compute score for time slot
            final Date start = new Date((epoch + i) * millisecondsPerUnit);
            final Date end = new Date((epoch + i + 1) * millisecondsPerUnit);
            final double score = socialMixingScore.scoreByProximity(start, end, -25, -70);


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        foreground = true;
        Log.d(tag, "app (state=foreground)");
        updateTargets();

    }

    @Override
    protected void onPause() {
        foreground = false;
        Log.d(tag, "app (state=background)");
        super.onPause();
    }

    public void quitApp(View view) {
        finishAndRemoveTask();
    }






    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_localcases:
                Intent local = new Intent(Contactlogs.this,CasesOverview.class);
                startActivity(local);
                Contactlogs.this.finish();
                break;

            case R.id.nav_mapview:
                Intent maps = new Intent(Contactlogs.this,Mapview.class);
                startActivity(maps);
                Contactlogs.this.finish();
                break;
            case R.id.nav_contactlogs:
                Intent contacts = new Intent(Contactlogs.this,Contactlogs.class);
                startActivity(contacts);
                Contactlogs.this.finish();
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
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


    @Override
    public void sensor(SensorType sensor, TargetIdentifier didDetect) {
        this.didDetect++;
        if (foreground) {
            final String text = Long.toString(this.didDetect);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    }

    @Override
    public void sensor(SensorType sensor, PayloadData didRead, TargetIdentifier fromTarget) {
        this.didRead++;
        targetIdentifiers.put(fromTarget, didRead);
        Target target = payloads.get(didRead);
        if (target != null) {
            target.didRead(new Date());
        } else {
            payloads.put(didRead, new Target(fromTarget, didRead));
        }
        if (foreground) {
            final String text = Long.toString(this.didRead);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    updateTargets();
                }
            });
        }
    }

    @Override
    public void sensor(SensorType sensor, List<PayloadData> didShare, TargetIdentifier fromTarget) {

        final Date now = new Date();
        for (PayloadData didRead : didShare) {
            targetIdentifiers.put(fromTarget, didRead);
            Target target = payloads.get(didRead);
            if (target != null) {
                target.didRead(new Date());
            } else {
                payloads.put(didRead, new Target(fromTarget, didRead));
            }
        }
        if (foreground) {
            final String text = Long.toString(this.didShare);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    updateTargets();
                }
            });
        }
    }

    @Override
    public void sensor(SensorType sensor, Proximity didMeasure, TargetIdentifier fromTarget) {
        this.didMeasure++;
        final PayloadData didRead = targetIdentifiers.get(fromTarget);
        if (didRead != null) {
            final Target target = payloads.get(didRead);
            if (target != null) {
                target.targetIdentifier(fromTarget);
                target.proximity(didMeasure);
            }
        }
        if (foreground) {
            final String text = Long.toString(this.didMeasure);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    updateTargets();
                    updateSocialDistance(socialMixingScoreUnit);
                }
            });
        }
    }

    @Override
    public void sensor(SensorType sensor, ImmediateSendData didReceive, TargetIdentifier fromTarget) {
        this.didReceive++;
        final PayloadData didRead = new PayloadData(didReceive.data.value);
        if (didRead != null) {
            final Target target = payloads.get(didRead);
            if (target != null) {
                targetIdentifiers.put(fromTarget, didRead);
                target.targetIdentifier(fromTarget);
                target.received(didReceive);
            }
        }
        if (foreground) {
            final String text = Long.toString(this.didReceive);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    updateTargets();
                }
            });
        }
    }

    @Override
    public void sensor(SensorType sensor, Location didVisit) {
//        Object loc = didVisit.value;
//        Log.e(null,"Loction Result  "+loc);
//        Log.e(null,"Loction Result  "+loc);
//        Log.e(null,"Loction Result  "+loc);
//        Log.e(null,"Loction Result  "+loc);
//        Log.e(null,"Loction Result  "+loc);
//        Log.e(null,"Loction Result  "+loc);
        if (foreground) {
            Object loc = didVisit.value;
            Log.e(null,"Loction Result  "+loc);
            Log.e(null,"Loction Result  "+loc);
            Log.e(null,"Loction Result  "+loc);
            Log.e(null,"Loction Result  "+loc);
            Log.e(null,"Loction Result  "+loc);
            Log.e(null,"Loction Result  "+loc);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    updateTargets();
                }
            });
        }
        // Not used
    }

    @Override
    public void sensor(SensorType sensor, Proximity didMeasure, TargetIdentifier fromTarget, PayloadData withPayload) {
        // High level integration API is not used as the test app is using the low level API to present all the detection events.
    }

    @Override
    public void sensor(SensorType sensor, SensorState didUpdateState) {
        // Sensor state is already presented by the operating system, so not duplicating in the test app.
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final Target target = targetListAdapter.getItem(i);
        final SensorArray sensor = (SensorArray) AppDelegate.getAppDelegate().sensor();
        final PayloadData payloadData = sensor.payloadData();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final boolean result = sensor.immediateSend(payloadData, target.targetIdentifier());
                Log.d(tag, "immediateSend (to=" + target.payloadData().shortName() + ",result=" + result + ")");
            }
        }).start();
    }

}
