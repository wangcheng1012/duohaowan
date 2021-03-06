package com.hd.wlj.duohaowan;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hd.wlj.duohaowan.been.Load;
import com.wlj.base.bean.Base;
import com.wlj.base.util.AppConfig;
import com.wlj.base.util.GoToHelp;
import com.wlj.base.util.StringUtils;
import com.wlj.base.web.asyn.AsyncCall;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoadingActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private ArrayList<String> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String firstlogin = AppConfig.getAppConfig().get(AppConfig.CONF_FIRSRLOGIN);
        if (!StringUtils.isEmpty(firstlogin)) {
            GoToHelp.go(this,MainActivity.class);
            finish();
        }
        AppConfig.getAppConfig().set(AppConfig.CONF_FIRSRLOGIN,AppConfig.CONF_FIRSRLOGIN );

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_loading);
        mData = new ArrayList<>();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),mData);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        Load load = new Load(this);

        load.Request().setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> list, Base base, int requestType) {
                for (Base base1 : list) {
                    JSONObject jsonObject = base1.getResultJsonObject();
                    String pic = jsonObject.optString("pic");
                    mData.add(Urls.HOST+pic);
                }
                mSectionsPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void fail(Exception paramException) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_loading, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String IMAGEURL = "url";
        private static final String POSTION = "position";
        private   Activity mActivity;

        public PlaceholderFragment() {

        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance( String url, int position, ArrayList<String>  list) {
            PlaceholderFragment fragment = new PlaceholderFragment( );
            Bundle args = new Bundle();
            args.putInt(POSTION,position);
            args.putString(IMAGEURL, url);
            args.putStringArrayList("list", list);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            String url = getArguments().getString(IMAGEURL);
            int position = getArguments().getInt(POSTION);
            List<String> list = getArguments().getStringArrayList("list");

            View inflate = inflater.inflate(R.layout.item_loading, container, false);
            ImageView viewById = (ImageView) inflate.findViewById(R.id.item_loading_imaeview);
            View buttom = inflate.findViewById(R.id.item_loading_button);
            buttom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoToHelp.go(getActivity(),MainActivity.class);
                    getActivity().finish();
                }
            });
            if(position == list.size()-1){
                buttom.setVisibility(View.VISIBLE);
            }else {
                buttom.setVisibility(View.GONE);
            }

            Glide.with(this).
                    load(url)
                    .placeholder(R.drawable.project_bg)
                    .into(viewById);

            return inflate;

        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final ArrayList<String> list;

        public SectionsPagerAdapter(FragmentManager fm, ArrayList<String> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(list.get(position),position, list);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return list.size();
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return "SECTION 1";
//                case 1:
//                    return "SECTION 2";
//                case 2:
//                    return "SECTION 3";
//            }
//            return null;
//        }
    }
}
