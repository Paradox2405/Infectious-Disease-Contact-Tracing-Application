package com.vmware.herald.app;

public class pandemicitem {
    private int mImageResource;
    private String mTxt1;
    private String mTxt2;

    public pandemicitem(int imageResource,String Txt1, String Txt2){

        mImageResource= imageResource;
        mTxt1=Txt1;
        mTxt2=Txt2;
    }

    public int getmImageResource(){
        return mImageResource;
    }

    public String getTxt1(){

        return mTxt1;
    }

    public String getTxt2(){
    return mTxt2;
    }




}
