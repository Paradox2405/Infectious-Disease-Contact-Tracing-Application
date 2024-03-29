package com.idcta.proj.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

public class Mapview extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    private DrawerLayout drawer;
   MapView mapView;
   GoogleMap map;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapviewcases);



        mapView=findViewById(R.id.mapView);
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);

        Toolbar toolbar= findViewById(R.id.toolbar);
        toolbar.setTitle("Map Data");
        setSupportActionBar(toolbar);


        drawer=findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setCheckedItem(R.id.nav_mapview);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_localcases:
                Intent local = new Intent(Mapview.this,CasesOverview.class);
                startActivity(local);
                Mapview.this.finish();
                break;

            case R.id.nav_mapview:
                Intent maps = new Intent(Mapview.this,Mapview.class);
                startActivity(maps);
                Mapview.this.finish();
                break;
            case R.id.nav_contactlogs:
                Intent contacts = new Intent(Mapview.this,Contactlogs.class);
                startActivity(contacts);
                Mapview.this.finish();
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
    public void onMapReady(GoogleMap googleMap) {

        Marker m1 = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(6.9167, 79.8333))
                .anchor(0.5f, 0.5f)
                .title("Western Province")
                .snippet("Total Confirmed: 112,000\n"+"Deaths: 479"));


        Marker m2 = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(7.7170,81.7000))
                .anchor(0.5f, 0.5f)
                .title("Eastern Province")
                .snippet("Total Confirmed: 190\n"+"Deaths: 479"));

        Marker m3 = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(8.3350,80.4108))
                .anchor(0.5f, 0.5f)
                .title("North Central Province")
                .snippet("Total Confirmed: 3800\n"+"Deaths: 479")
                // .icon(BitmapDescriptorFactory.fromResource(R.drawable.logo3))
        );
        Marker m4 = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(7.2970,80.6385))
                .anchor(0.5f, 0.5f)
                .title("Central Province")
                .snippet("Total Confirmed: 87,000\n"+"Deaths: 479"));

        Marker m5 = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(6.0395,80.2194))
                .anchor(0.5f, 0.5f)
                .title("Southern Province")
                .snippet("Total Confirmed: 896\n"+"Deaths: 479"));

        Marker m6 = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(9.6647,80.0167))
                .anchor(0.5f, 0.5f)
                .title("Northern Province")
                .snippet("Total Confirmed: 68\n"+"Deaths: 479"));

        Marker m7 = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(6.6930,80.3860))
                .anchor(0.5f, 0.5f)
                .title("Sabaragamuwa Province")
                .snippet("Total Confirmed: 700\n"+"Deaths: 479"));

        Marker m8 = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(8.0330,79.8260))
                .anchor(0.5f, 0.5f)
                .title("North Western Province")
                .snippet("Total Confirmed: 15,000\n"+"Deaths: 479"));

        Marker m9 = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(6.9847,81.0564))
                .anchor(0.5f, 0.5f)
                .title("Uva Province")
                .snippet("Total Confirmed: 53\n"+"Deaths: 479"));


//        map = googleMap;
       LatLng SriLanka = new LatLng(7.8731, 80.7718);
//        map.addMarker(new MarkerOptions()
//                .position(SriLanka)
//                .title("Sri Lanka"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(SriLanka));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(7.5f));
    }
    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
