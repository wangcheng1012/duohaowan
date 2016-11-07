package com.hd.wlj.duohaowan.ui.home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.Urls;
import com.hd.wlj.duohaowan.ui.home.classify.work.WorkDetailsActivity;
import com.hd.wlj.duohaowan.ui.home.classify.ClassifyListActivity;
import com.hd.wlj.duohaowan.ui.home.view.KamHorizontalScrollView;
import com.wlj.base.bean.Base;
import com.wlj.base.ui.BaseFragment;
import com.wlj.base.util.GoToHelp;
import com.wlj.base.util.StringUtils;
import com.wlj.base.util.UIHelper;
import com.wlj.base.util.img.LoadImage;
import com.wlj.base.util.statusbar.StatusBarUtil;
import com.wlj.base.widget.SwitchViewPager;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends BaseFragment implements HomeView {

    @BindView(R.id.home_recyclerView)
    RecyclerView homeRecyclerView;

    private OnFragmentInteractionListener mListener;
    private HomePresenterImpl homePresenter;
    private FrameLayout bannerViewGroup;
    private List<Base> mData;
    private View part_home_head;
    private TextView header_new;
    private HeaderAndFooterWrapper<Base> mHeaderAndFooterWrapper;
    private LinearLayout mGallery;
    private List<Base> hostList;
    private KamHorizontalScrollView mHorizontalScrollView;

    public HomeFragment() {
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
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getlayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this, view);
        init();
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

        initClassify();
        initHot();

//        honmeClassifyViewPager =  (ViewPager) part_home_head.findViewById(R.id.honme_classify_viewPager);
//        //设置Page间间距
//        honmeClassifyViewPager.setPageMargin(20);
//        //设置缓存的页面数量
//        honmeClassifyViewPager.setOffscreenPageLimit(4);
    }

    private void initClassify() {

        part_home_head.findViewById(R.id.home_artist).setOnClickListener(new ClassifyOnClickListener());
        part_home_head.findViewById(R.id.home_artgallery).setOnClickListener(new ClassifyOnClickListener());
        part_home_head.findViewById(R.id.home_workofart).setOnClickListener(new ClassifyOnClickListener());
        part_home_head.findViewById(R.id.home_artview).setOnClickListener(new ClassifyOnClickListener());
    }

    private void initHot() {

//        mHorizontalScrollView = (HorizontalScrollView) part_home_head.findViewById(R.id.id_horizontalScrollView);
        mGallery = (LinearLayout) part_home_head.findViewById(R.id.id_gallery);
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
                holder.getConvertView().setTag(R.id.tag_first, o);

                JSONObject resultJsonObject = o.getResultJsonObject();
                final ImageView view = holder.getView(R.id.item_home_recyclerView_image);
                holder.setImageResource(R.id.item_home_recyclerView_image, R.drawable.project_bg);
                Glide.with(HomeFragment.this)
                        .load(Urls.HOST + resultJsonObject.optString("pic"))
                        .placeholder(R.drawable.project_bg)
                        .into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {

                                int intrinsicWidth = resource.getIntrinsicWidth();
                                int height = view.getWidth() * resource.getIntrinsicHeight() / intrinsicWidth;

                                view.setMinimumWidth(intrinsicWidth);
                                view.setMinimumHeight(height);

                                view.setImageDrawable(resource);
                            }
                        });


//               LoadImage.getinstall().addTask(Urls.HOST + resultJsonObject.optString("pic"), view).doTask();

                //
                final HomeModelImpl homemodelimpl = (HomeModelImpl) o;
                final ImageButton fat_in = holder.getView(R.id.home_fat_in);
                final LinearLayout fat_out = holder.getView(R.id.home_fat_out);
                if (homemodelimpl.getTag() == HomeModelImpl.Tag.in) {

                    fat_out.setVisibility(View.GONE);
                    fat_in.setVisibility(View.VISIBLE);
                } else {
                    fat_out.setVisibility(View.VISIBLE);
                    fat_in.setVisibility(View.GONE);
                }

                holder.setText(R.id.item_home_rv_name, resultJsonObject.optString("name") + "  " + resultJsonObject.optString("zuozhe"));

                String years = resultJsonObject.optString("years");
                String cicun = resultJsonObject.optString("cicun");
                if (StringUtils.isEmpty(cicun) || StringUtils.isEmpty(years)) {
                    //其中一个为空 不加"／"
                    holder.setText(R.id.item_home_rv_year, cicun + "" + years);
                } else {
                    holder.setText(R.id.item_home_rv_year, cicun + "/" + years);
                }

                fat_in.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setVisibility(View.GONE);
                        fat_out.setVisibility(View.VISIBLE);
                        homemodelimpl.setTag(HomeModelImpl.Tag.out);
                    }
                });

                fat_out.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setVisibility(View.GONE);
                        fat_in.setVisibility(View.VISIBLE);
                        homemodelimpl.setTag(HomeModelImpl.Tag.in);
                    }
                });

            }
        };

        homeRecyclerView.setAdapter(commonAdapter);
        //item点击
        commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Object tag = view.getTag(R.id.tag_first);

                if (tag != null) {
                    Base b = (Base) tag;

                    Bundle bundle = new Bundle();
                    JSONObject jo = b.getResultJsonObject();
                    bundle.putString("id", jo.optString("pub_id"));
                    GoToHelp.go(getActivity(), WorkDetailsActivity.class, bundle);
                }

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
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
        header_new.setText(jsonObject.optString("name"));
    }

    @Override
    public void showHot(List<Base> list) {
        hostList = list;
        for (int i = 0; i < list.size(); i++) {
            mGallery.addView(createHotItem(i));
        }
        for (int i = list.size() - 1; i >= 0; i--) {

            mHorizontalScrollView.addLeft(createHotItem(i));
        }
    }

    private View createHotItem(int index) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home_hot, null);
        ImageView mImg = (ImageView) view.findViewById(R.id.home_hot_img);
        int width = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width / 2 - width / 10, LinearLayout.LayoutParams.MATCH_PARENT);
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

    private class ClassifyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            Bundle bundle = new Bundle();
            switch (v.getId()) {

                case R.id.home_artist:
                    bundle.putString("title", "艺术家列表");
                    bundle.putInt("classify", R.id.home_artist);
                    break;
                case R.id.home_artgallery:
                    bundle.putString("title", "艺术馆列表");
                    bundle.putInt("classify", R.id.home_artgallery);
                    break;
                case R.id.home_workofart:
                    bundle.putString("title", "艺术品列表");
                    bundle.putInt("classify", R.id.home_workofart);
                    break;
                case R.id.home_artview:
                    bundle.putString("title", "艺术观列表");
                    bundle.putInt("classify", R.id.home_artview);
                    break;
            }

            GoToHelp.go(getActivity(), ClassifyListActivity.class, bundle);

            part_home_head.findViewById(R.id.home_artist).setOnClickListener(new ClassifyOnClickListener());
            part_home_head.findViewById(R.id.home_artgallery).setOnClickListener(new ClassifyOnClickListener());
            part_home_head.findViewById(R.id.home_workofart).setOnClickListener(new ClassifyOnClickListener());
            part_home_head.findViewById(R.id.home_artview).setOnClickListener(new ClassifyOnClickListener());
        }
    }
}
