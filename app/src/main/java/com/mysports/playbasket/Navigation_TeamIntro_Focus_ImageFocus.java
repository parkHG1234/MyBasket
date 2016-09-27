package com.mysports.playbasket;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by 박효근 on 2016-06-29.
 */
public class Navigation_TeamIntro_Focus_ImageFocus extends FragmentActivity {
    static String ImageUrl1="";
    static String ImageUrl2="";
    static String ImageUrl3="";
    static String Choice;
    private Navigation_TeamIntro_Focus_ImageFocus.SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_courtinfo_imagefocus_main);
        //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        Intent intent1 = getIntent();
        ImageUrl1 = intent1.getStringExtra("ImageUrl1");
        ImageUrl2 = intent1.getStringExtra("ImageUrl2");
        ImageUrl3 = intent1.getStringExtra("ImageUrl3");
        Choice = intent1.getStringExtra("Choice");

        mSectionsPagerAdapter = new Navigation_TeamIntro_Focus_ImageFocus.SectionsPagerAdapter(getSupportFragmentManager());



        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //뷰페이지를 중간부터 실행되게 하는 메소드
        mViewPager.setCurrentItem(Integer.parseInt(Choice));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            // return PlaceholderFragment.newInstance(position + 1);
            Fragment fragment = null;
            Bundle args = null;
            switch (position) {
                case 0:
                    fragment = new SectionsFragment1();
                    args = new Bundle();
                    break;
                case 1:
                    fragment = new SectionsFragment2();
                    args = new Bundle();
                    break;
                case 2:
                    fragment = new SectionsFragment3();
                    args = new Bundle();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
    public static class SectionsFragment1 extends Fragment {
        PhotoView Navigation_TeamIntro_Focus_ImageFocus_ImageView_Image1;
        Bitmap bmImg;
        PhotoViewAttacher mAttacher;
        public SectionsFragment1() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.layout_navi_teamintro_focus_imagefocus1, container, false);
            if(ImageUrl1.isEmpty()){

            }
            else {
                Navigation_TeamIntro_Focus_ImageFocus_ImageView_Image1 = (PhotoView) rootView.findViewById(R.id.Navigation_TeamIntro_Focus_ImageFocus_ImageView_Image1);
                back1 task1 = new back1();
                task1.execute(ImageUrl1);
                mAttacher = new PhotoViewAttacher(Navigation_TeamIntro_Focus_ImageFocus_ImageView_Image1);
                mAttacher.setScaleType(PhotoView.ScaleType.FIT_XY);
            }
            return rootView;
        }

        private class back1 extends AsyncTask<String, Integer,Bitmap> {

            @Override
            protected Bitmap doInBackground(String... urls) {
                // TODO Auto-generated method stub
                try{
                    URL myFileUrl = new URL(urls[0]);
                    HttpURLConnection conn = (HttpURLConnection)myFileUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();

                    bmImg = BitmapFactory.decodeStream(is);


                }catch(IOException e){
                    e.printStackTrace();
                }
                return bmImg;
            }

            protected void onPostExecute(Bitmap img){
                Navigation_TeamIntro_Focus_ImageFocus_ImageView_Image1.setImageBitmap(bmImg);
            }

        }
    }

    public static class SectionsFragment2 extends Fragment {
        PhotoView Navigation_TeamIntro_Focus_ImageFocus_ImageView_Image2;
        Bitmap bmImg;
        PhotoViewAttacher mAttacher;
        public SectionsFragment2() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.layout_navi_teamintro_focus_imagefocus2, container, false);
            Navigation_TeamIntro_Focus_ImageFocus_ImageView_Image2 = (PhotoView)rootView.findViewById(R.id.Navigation_TeamIntro_Focus_ImageFocus_ImageView_Image2);
            if(ImageUrl2.isEmpty()){

            }
            else {
                back1 task1 = new back1();
                task1.execute(ImageUrl2);
                mAttacher = new PhotoViewAttacher(Navigation_TeamIntro_Focus_ImageFocus_ImageView_Image2);
                mAttacher.setScaleType(PhotoView.ScaleType.FIT_XY);
            }
            return rootView;
        }
        private class back1 extends AsyncTask<String, Integer,Bitmap> {

            @Override
            protected Bitmap doInBackground(String... urls) {
                // TODO Auto-generated method stub
                try{
                    URL myFileUrl = new URL(urls[0]);
                    HttpURLConnection conn = (HttpURLConnection)myFileUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();

                    bmImg = BitmapFactory.decodeStream(is);


                }catch(IOException e){
                    e.printStackTrace();
                }
                return bmImg;
            }

            protected void onPostExecute(Bitmap img){
                Navigation_TeamIntro_Focus_ImageFocus_ImageView_Image2.setImageBitmap(bmImg);
            }

        }
    }
    public static class SectionsFragment3 extends Fragment {
        PhotoView Navigation_TeamIntro_Focus_ImageFocus_ImageView_Image3;
        Bitmap bmImg;
        PhotoViewAttacher mAttacher;
        public SectionsFragment3() {
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.layout_navi_teamintro_focus_imagefocus3, container, false);
            Navigation_TeamIntro_Focus_ImageFocus_ImageView_Image3 = (PhotoView) rootView.findViewById(R.id.Navigation_TeamIntro_Focus_ImageFocus_ImageView_Image3);
            if(ImageUrl3.isEmpty()){

            }
            else {
                back1 task1 = new back1();
                task1.execute(ImageUrl3);
                mAttacher = new PhotoViewAttacher(Navigation_TeamIntro_Focus_ImageFocus_ImageView_Image3);
                mAttacher.setScaleType(PhotoView.ScaleType.FIT_XY);
            }
            return rootView;
        }
        private class back1 extends AsyncTask<String, Integer,Bitmap> {

            @Override
            protected Bitmap doInBackground(String... urls) {
                // TODO Auto-generated method stub
                try{
                    URL myFileUrl = new URL(urls[0]);
                    HttpURLConnection conn = (HttpURLConnection)myFileUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();

                    bmImg = BitmapFactory.decodeStream(is);


                }catch(IOException e){
                    e.printStackTrace();
                }
                return bmImg;
            }

            protected void onPostExecute(Bitmap img){
                Navigation_TeamIntro_Focus_ImageFocus_ImageView_Image3.setImageBitmap(bmImg);
            }

        }
    }
}
