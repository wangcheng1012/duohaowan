package com.hd.wlj.duohaowan.ui.home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.Urls;
import com.hd.wlj.duohaowan.ui.home.contract.HomeContract;
import com.hd.wlj.duohaowan.ui.home.presenter.HomePresenterImpl;
import com.hd.wlj.duohaowan.ui.home.view.KamHorizontalScrollView;
import com.wlj.base.bean.Base;
import com.wlj.base.util.UIHelper;
import com.wlj.base.util.img.LoadImage;
import com.wlj.base.util.statusbar.StatusBarUtil;
import com.wlj.base.widget.SwitchViewPager;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements HomeContract.View {

    @BindView(R.id.home_recyclerView)
    RecyclerView homeRecyclerView;
//    @BindView(R.id.fab)
//    FloatingActionButton fab;

    private OnFragmentInteractionListener mListener;
    private HomePresenterImpl homePresenter;
    //    private ViewPager honmeClassifyViewPager;
    private FrameLayout bannerViewGroup;
    private List<Base> mData;
    private View part_home_head;
    private TextView header_new;
    private HeaderAndFooterWrapper<Base> mHeaderAndFooterWrapper;
//    private HorizontalScrollView mHorizontalScrollView;
//    private HorizontalScrollViewAdapter mAdapter;
    private LinearLayout mGallery;
    private List<Base> hostList;
    private KamHorizontalScrollView mHorizontalScrollView;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        //沉浸式状态栏
        StatusBarUtil.transparentFlags(getActivity());

        initHead();

        initRecyclerView();

        //Presenter
        homePresenter = new HomePresenterImpl(getActivity());
        homePresenter.attachView(this);

        //初始化 网络请求
        homePresenter.loadBannerData();
        homePresenter.loadNews();
        homePresenter.loadHot();
        homePresenter.loadNewest();
    }


    private void initHead() {

        part_home_head = LayoutInflater.from(getContext()).inflate(R.layout.part_home_head, null);

        bannerViewGroup = (FrameLayout) part_home_head.findViewById(R.id.home_autoScrollViewPager);
        header_new = (TextView) part_home_head.findViewById(R.id.home_header_new);

        initHot();

//        honmeClassifyViewPager =  (ViewPager) part_home_head.findViewById(R.id.honme_classify_viewPager);
//        //设置Page间间距
//        honmeClassifyViewPager.setPageMargin(20);
//        //设置缓存的页面数量
//        honmeClassifyViewPager.setOffscreenPageLimit(4);

    }

    private void initHot() {

//        mHorizontalScrollView = (HorizontalScrollView) part_home_head.findViewById(R.id.id_horizontalScrollView);
        mGallery = (LinearLayout)part_home_head.findViewById(R.id.id_gallery);
        mHorizontalScrollView = (KamHorizontalScrollView) part_home_head.findViewById(R.id.id_horizontalScrollView);

        mHorizontalScrollView.setCreatItem(new KamHorizontalScrollView.CreatItem() {
            @Override
            public View getView(int index) {

                return createHotItem(index);
            }
        });
    }


    private void initRecyclerView() {
        // RecyclerView
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mData = new ArrayList<Base>();

        CommonAdapter<Base> commonAdapter = new CommonAdapter<Base>(getContext(), R.layout.item_home_recyclerview, mData) {
            @Override
            protected void convert(ViewHolder holder, Base o, int position) {

                JSONObject resultJsonObject = o.getResultJsonObject();
                ImageView view = holder.getView(R.id.item_home_recyclerView_image);
                holder.setImageResource(R.id.item_home_recyclerView_image,R.drawable.project_bg);
                // Glide.with(HomeFragment.this).load(Urls.HOST + resultJsonObject.optString("pic")).crossFade().placeholder(R.drawable.project_bg).into(view);
                LoadImage.getinstall().addTask(Urls.HOST + resultJsonObject.optString("pic"), view);
                LoadImage.getinstall().doTask();

                final ImageButton fat_in = holder.getView(R.id.home_fat_in);
                final LinearLayout fat_out = holder.getView(R.id.home_fat_out);
                holder.setText(R.id.item_home_rv_name,resultJsonObject.optString("") + " " + resultJsonObject.optString(""));
                holder.setText(R.id.item_home_rv_year,resultJsonObject.optString("") + " " + resultJsonObject.optString(""));

                fat_in.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setVisibility(View.GONE);
                        fat_out.setVisibility(View.VISIBLE);
                    }
                });

                fat_out. setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setVisibility(View.GONE);
                        fat_in.setVisibility(View.VISIBLE);
                    }
                });


            }
        };

        homeRecyclerView.setAdapter(commonAdapter);

        //添加header
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper<Base>(commonAdapter);
        mHeaderAndFooterWrapper.addHeaderView(part_home_head);
        homeRecyclerView.setAdapter(mHeaderAndFooterWrapper);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        homePresenter.detachView();
        super.onDetach();
    }

    @Override
    public void showBanner(List<Base> list) {

        SwitchViewPager<Base> switchViewPager = new SwitchViewPager<Base>(getContext(), list);
        View createview = switchViewPager.createview();
        if (createview != null) {
            bannerViewGroup.addView(createview);
        } else {
            UIHelper.toastMessage(getContext(), "banner图片为空");
        }
    }

    @Override
    public void showNews(Base base) {
        JSONObject jsonObject = base.getResultJsonObject();
//        Html.fromHtml("s");
        header_new.setText( jsonObject.optString("name") );
    }

    @Override
    public void showHot(List<Base> list ) {
        hostList = list;
        for (int i = 0; i < list.size(); i++) {
            mGallery.addView(createHotItem(i));
        }
        for (int i = list.size()-1; i >= 0; i--) {

            mHorizontalScrollView.addLeft(createHotItem(i));
        }
    }

    private View createHotItem(int  index) {
        
        View view =  LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home_hot, null);
        ImageView mImg = (ImageView) view.findViewById(R.id.home_hot_img);
        int width = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width / 2 - width/10, LinearLayout.LayoutParams.MATCH_PARENT);
        //
        JSONObject resultJsonObject = hostList.get(index).getResultJsonObject();

        String pics = resultJsonObject.optString("pic");
        if (pics != null && pics.length() > 0) {
//                Glide.with(HomeFragment.this).load(Urls.HOST +pics).crossFade().placeholder(R.drawable.project_bg).into(mImg);
            LoadImage.getinstall().addTask(Urls.HOST + pics, mImg);
            LoadImage.getinstall().doTask();
        }
        view.setLayoutParams(layoutParams);
        return view;

    }

    @Override
    public void showNewest(List<Base> paramList) {

        mData.addAll(paramList);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }


public interface OnFragmentInteractionListener {
    void onFragmentInteraction(Uri uri);
}
}
