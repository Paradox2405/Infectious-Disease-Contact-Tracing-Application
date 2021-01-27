package com.vmware.herald.app;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CasesOverview extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    //private ArrayList<PandemicItem> mPandemicItem;
    private RequestQueue mRequestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cases_overview);


//        ArrayList<PandemicItem> pandemicItems = new ArrayList<>();
//        pandemicItems.add(new PandemicItem(R.drawable.total_count,22,"Line 2"));
//        pandemicItems.add(new PandemicItem(R.drawable.cases_today,12,"Line 4"));
//        pandemicItems.add(new PandemicItem(R.drawable.active_cases,122,"Line 6"));
//
//        mRecyclerView=findViewById(R.id.recyclerView);
//        mRecyclerView.setHasFixedSize(true);
       mLayoutManager= new LinearLayoutManager(this);
//        mAdapter= new PandemicItemAdapter(pandemicItems);
//
//       mRecyclerView.setLayoutManager(mLayoutManager);
//       mRecyclerView.setAdapter(mAdapter);

       //mPandemicItem= new ArrayList<>();
        mRequestQueue= Volley.newRequestQueue(this);
        parseJSON();


    }

    private void parseJSON(){
        String url="https://hpb.health.gov.lk/api/get-current-statistical";
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonobject = response.getJSONObject("data");

                    int newCases= jsonobject.getInt("local_new_cases");
                    int deaths= jsonobject.getInt("local_deaths");
                    int recoveries= jsonobject.getInt("local_recovered");
                    int activeCases= jsonobject.getInt("local_active_cases");
                    int totalCases= jsonobject.getInt("local_total_cases");


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
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }


    public void refreshClicked(View view) {
        parseJSON();
    }
}
