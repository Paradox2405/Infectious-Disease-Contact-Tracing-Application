package com.vmware.herald.app;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PandemicItemAdapter extends RecyclerView.Adapter<PandemicItemAdapter.PandemicViewHolder> {
    private ArrayList<PandemicItem> mPandemicList;

    public static class PandemicViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;

        public PandemicViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView=itemView.findViewById(R.id.imageView);
            mTextView1=itemView.findViewById(R.id.txt1);
            mTextView2=itemView.findViewById(R.id.txt2);
        }
    }

    public  PandemicItemAdapter (ArrayList<PandemicItem> pandemicList){
        mPandemicList=pandemicList;
    }




    @NonNull
    @Override
    public PandemicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pandemic_item,parent,false);
      PandemicViewHolder pvh = new PandemicViewHolder(v);
      return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull PandemicViewHolder holder, int position) {
        PandemicItem currentItem = mPandemicList.get(position);
        holder.mImageView.setImageResource(currentItem.getimageResource());
        holder.mTextView1.setText(currentItem.gettxt1());
        holder.mTextView2.setText(""+currentItem.gettxt2());
        if(position == 1){
            holder.mTextView1.setTextColor(Color.GREEN);
            holder.mTextView2.setTextColor(Color.GREEN);
        }
        if(position == 2){
            holder.mTextView1.setTextColor(Color.MAGENTA);
            holder.mTextView2.setTextColor(Color.MAGENTA);
        }
        if(position == 3){
            holder.mTextView1.setTextColor(Color.BLUE);
            holder.mTextView2.setTextColor(Color.BLUE);
        }
        if(position == 4){
            holder.mTextView1.setTextColor(Color.RED);
            holder.mTextView2.setTextColor(Color.RED);
        }
        if(position == 5){
            holder.mTextView1.setTextColor(Color.RED);
            holder.mTextView2.setTextColor(Color.RED);
        }


    }

    @Override
    public int getItemCount() {
        return mPandemicList.size();
    }
}
