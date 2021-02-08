package com.vmware.herald.app;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CasesOverview extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RequestQueue mRequestQueue;
    private DrawerLayout drawer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cases_overview);

        Toolbar toolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        drawer=findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, drawer,
                toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        mLayoutManager= new GridLayoutManager(this,2);
        mRequestQueue= Volley.newRequestQueue(this);
        parseJSON();


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
                    int newCases= jsonobject.getInt("local_new_cases");
                    int deaths= jsonobject.getInt("local_deaths");
                    int recoveries= jsonobject.getInt("local_recovered");
                    int activeCases= jsonobject.getInt("local_active_cases");
                    int totalCases= jsonobject.getInt("local_total_cases");

                    TextView time = findViewById(R.id.update_time);
                    time.setText("Updated at:  "+updateTime);

                    ArrayList<PandemicItem> pandemicItems = new ArrayList<>();
                    pandemicItems.add(new PandemicItem(R.drawable.total_count,"Total Cases to Date",totalCases));
                    pandemicItems.add(new PandemicItem(R.drawable.cases_today,"New Cases Today",newCases));
                    pandemicItems.add(new PandemicItem(R.drawable.active_cases,"Cases Under Treatment",activeCases));
                    pandemicItems.add(new PandemicItem(R.drawable.dead,"Deaths",deaths));
                    pandemicItems.add(new PandemicItem(R.drawable.total_recoveries,"Total Recoveries",recoveries));

                    mRecyclerView=findViewById(R.id.recyclerView);
                    mRecyclerView.setHasFixedSize(true);

                    mAdapter= new PandemicItemAdapter(pandemicItems);

                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);


                } catch (JSONException e) {
                    System.out.println(e);


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
                Toast.makeText(getApplicationContext(),"Check Internet Connection",Toast.LENGTH_LONG).show();
            }
        });
        mRequestQueue.add(request);
    }



    public void refreshClicked(View view) {

        parseJSON();

    }
}
