package com.hd.wlj.duohaowan.ui.publish.adjustmentmore.sencemore;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.widget.TextView;

import com.hd.wlj.duohaowan.MainActivity;
import com.hd.wlj.duohaowan.ui.mvp.BasePresenter;
import com.hd.wlj.duohaowan.ui.publish.MergeBitmap;
import com.hd.wlj.duohaowan.ui.publish.PublishModel;
import com.hd.wlj.duohaowan.ui.publish.border.BorderModel;
import com.hd.wlj.duohaowan.util.UploadChucks;
import com.wlj.base.bean.Base;
import com.wlj.base.util.GoToHelp;
import com.wlj.base.util.StringUtils;
import com.wlj.base.util.UIHelper;
import com.wlj.base.web.asyn.AsyncCall;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * Created by wlj on 2016/11/10.
 */
public class SenceMorePresenter extends BasePresenter<SenceMoreView> {


    private final BorderModel borderModel;
    private final UploadChucks uploadChucks;
    private Activity activity;
    private AsyncCall asyncCall;
    private List<Base> mDate;
    private LoadMoreWrapper loadMoreWrapper;

    public SenceMorePresenter(Activity activity) {
        this.activity = activity;
        borderModel = new BorderModel();
        uploadChucks = new UploadChucks(activity);
    }

    public void load() {
        loadSenceData(1);
    }

    private void loadSenceData(int page) {
        borderModel.setPage(page);
        borderModel.setPageSize(5);
        asyncCall = borderModel.Request(BorderModel.type_sence);
        loadBack();
    }

    public void LoadMoreWrapper(RecyclerView.Adapter adapter, RecyclerView recyclerview, List<Base> mDate) {
        this.mDate = mDate;
        TextView loadmoretext = new TextView(activity);
//        loadmoretext.setText(" ");
        loadmoretext.setTextColor(Color.BLACK);
        loadmoretext.setGravity(Gravity.CENTER);
        loadmoretext.setLayoutParams(new RecyclerView.LayoutParams(1, 1));
        loadMoreWrapper = new LoadMoreWrapper(adapter, recyclerview);
        loadMoreWrapper.setLoadMoreView(loadmoretext);
        loadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

                if (!asyncCall.isComplate()) {
                    loadMore();
                }
            }
        });
        recyclerview.setAdapter(loadMoreWrapper);
        loadMoreWrapper.notifyDataSetChanged();
    }

    private void loadMore() {

        loadSenceData(asyncCall.getPageIndex() + 1);
    }


    private void loadBack() {
        asyncCall.setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> paramList, Base paramBase, int requesttype) {
                //以此判定为刷新 就清除原油数据
                if (asyncCall.getPageIndex() == 1) {
                    mDate.clear();
                }
                mDate.addAll(paramList);
                loadMoreWrapper.notifyDataSetChanged();

//                if(asyncCall.getPageIndex() == 1 && paramList.size() == 0){
//                    //数据为空
//                    loadmoretext.setText("数据为空");
//                }

//                if (asyncCall.isComplate() && datas.size() != 0) {
//                    //加载完
//                    loadmoretext.setText("已经到底了");
//                }
            }

            @Override
            public void fail(Exception paramException) {


            }
        });
    }


//    /**
//     *
//     * @param file
//     * @param i
//     * @throws Exception
//     */
//    public void uploadChucks( File file, int i) throws Exception {
//
//        uploadChucks.setView(new UploadChucks.UploadChucksView() {
//            @Override
//            public void uploadComplte(String fileName, int i) {
//                if(view != null){
//                    view.uploadComplte(fileName,i);
//                }
//            }
//
//            @Override
//            public void proess(long chunks, long chunk) {
//                if(view != null){
//                    view.proess(chunks,chunk);
//                }
//            }
//        });
//
//        uploadChucks(file,i);
//    }

    public void save(PublishModel publishModel) {
        publishModel.setHavebachground(true);
        publishModel.Request().setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> list, Base base, int requestType) {

                UIHelper.loadingClose();
                UIHelper.toastMessage(activity, "发布成功");
                GoToHelp.go(activity, MainActivity.class);
            }

            @Override
            public void fail(Exception paramException) {

            }
        });

    }


    /**
     * 分段上传文件
     *
     * @param file_input
     * @param i
     * @return
     * @throws Exception
     */
    public void uploadChucks(File file_input, final int i) throws Exception {


        Integer file_size = 1024 * 100;

        FileInputStream fis = new FileInputStream(file_input);

        byte[] file_bytes = new byte[file_size];

        Long chunks = file_input.length() / file_size;
        if (file_input.length() % file_size != 0) {
            chunks += 1;
        }

        Long chunk = 0l;
        String name = (System.currentTimeMillis()+"").substring(3)+i;

        Long had_upload = 0l;
        while (had_upload < file_input.length()) {
            fis.read(file_bytes);

            final UploadChucks uploadChucks = new UploadChucks(activity);
            uploadChucks.setFile_bytes(file_bytes);
            uploadChucks.setName(name);
            uploadChucks.setChunks(chunks);
            uploadChucks.setChunk(chunk);

            AsyncCall request = uploadChucks.Request();

            final Long finalChunks = chunks;
            final Long finalChunk = chunk;
            request.setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
                @Override
                public void OnAsyncBack(List<Base> list, Base base, int requestType) {
                    JSONObject jsonObject = base.getResultJsonObject();
                    String fileName = jsonObject.optString("fileName");

                    if(view != null){
                        view.proess(finalChunks, finalChunk);

                        if (!StringUtils.isEmpty(fileName)) {

                            view.uploadComplte(fileName,i);
                        }
                    }

                }

                @Override
                public void fail(Exception paramException) {
                    uploadChucks.Request();
                }
            });
//            String uploadChuckOne = uploadChuckOne(file_bytes, name, chunk, chunks);
            /**
             * {"statusCode":"200","message":"上传成功!"}	上传单片
             {"data":{"fileName":"attachFiles/temp/bbd093b570e1201aa17b9f0b3be7960a"},"statusCode":"200","message":"上传成功!"}//上传完成
             */
//            System.out.println(uploadChuckOne);

            had_upload += file_size;
            chunk++;
        }

        fis.close();
    }
}
