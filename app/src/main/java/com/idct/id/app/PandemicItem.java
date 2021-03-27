package com.idct.id.app;

public class PandemicItem {
    private int mImageResource;
    private String mTxt1;
    private int mTxt2;

    public PandemicItem(int imageResource, String txt1, int txt2){

        mImageResource= imageResource;
        mTxt1=txt1;
        mTxt2=txt2;
    }

    public int getimageResource(){
        return mImageResource;
    }
    public String gettxt1(){
        return mTxt1;
    }
    public int gettxt2(){
        return mTxt2;
    }



}
