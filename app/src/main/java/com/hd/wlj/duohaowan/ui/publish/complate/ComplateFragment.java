package com.hd.wlj.duohaowan.ui.publish.complate;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;

import com.hd.wlj.duohaowan.App;
import com.hd.wlj.duohaowan.MainActivity;
import com.hd.wlj.duohaowan.R;
import com.hd.wlj.duohaowan.ui.LoginActivity;
import com.hd.wlj.duohaowan.ui.publish.ImageAdjustmentActivity;
import com.hd.wlj.duohaowan.ui.publish.MergeBitmap;
import com.hd.wlj.duohaowan.ui.publish.PublishModel;
import com.hd.wlj.duohaowan.util.UploadChucks;
import com.wlj.base.bean.Base;
import com.wlj.base.ui.BaseFragment;
import com.wlj.base.util.GoToHelp;
import com.wlj.base.util.StringUtils;
import com.wlj.base.util.UIHelper;
import com.wlj.base.web.asyn.AsyncCall;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComplateFragment extends BaseFragment {

    @BindView(R.id.complate_name)
    EditText complateName;
    @BindView(R.id.complate_year)
    EditText complateYear;
    @BindView(R.id.complate_money)
    EditText complateMoney;
    @BindView(R.id.complate_intro)
    EditText complateIntro;
    private ComplatePresenter presenter;
    private MergeBitmap mergeBitmap;

    @Override
    protected int getlayout() {
        return R.layout.fragment_complate;
    }

    @Override
    protected void initView() {
        view.setMinimumHeight(0);
        ButterKnife.bind(this, view);
        ImageAdjustmentActivity activity = (ImageAdjustmentActivity) getActivity();
        mergeBitmap = activity.getMergeBitmap();

        presenter = new ComplatePresenter(getActivity());
    }

    @OnClick({R.id.complate_save, R.id.complate_backgound})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.complate_save:

                for ( MergeBitmap tmp : ImageAdjustmentActivity.mergeBitmaps) {

                    try {

                        uploadChucks( tmp);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.complate_backgound:

                Bundle bundle = new Bundle();
                bundle.putInt("from", ImageAdjustmentActivity.from_border_sece);
                Intent intent = new Intent(getActivity(), ImageAdjustmentActivity.class);
                intent.putExtras(bundle);

                startActivity(intent);
                break;
        }
    }

    private void save(MergeBitmap tmp) {

        String name = complateName.getText() + "";
        String year = complateYear.getText() + "";
        String money = complateMoney.getText() + "";
        String intro = complateIntro.getText() + "";

        PublishModel model = new PublishModel(getActivity());

        model.setHavebachground(false);

        model.setName(name);
        model.setYears(year);
        model.setPrice(money);
        ArrayList<MergeBitmap> objects = new ArrayList<>();
        objects.add(tmp);
        model.setMergeBitmaps(objects);

        model.Request().setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
            @Override
            public void OnAsyncBack(List<Base> list, Base base, int requestType) {

                UIHelper.loadingClose();
                UIHelper.toastMessage(getContext(), "发布成功");
                GoToHelp.go(getActivity(), MainActivity.class);
            }

            @Override
            public void fail(Exception paramException) {

            }
        });
    }


    /**
     * 分段上传文件
     *
     * @return
     * @throws Exception
     * @param tmp
     */
    public  void uploadChucks(final MergeBitmap tmp) throws Exception {

        App context = (App) getActivity().getApplicationContext();
        if(!context.islogin()){
            UIHelper.toastMessage(context,"请先登录");
            GoToHelp.go(getActivity(), LoginActivity.class);
            return;
        }

        UIHelper.loading("图片上传中，请耐心等候……",getActivity());

        Integer file_size = 1024 * 200;

        File file_input = new File(tmp.getWorkPath());

        FileInputStream fis = new FileInputStream(file_input);

        byte[] file_bytes = new byte[file_size];

        Long chunks = file_input.length() / file_size;
        if (file_input.length() % file_size != 0) {
            chunks += 1;
        }

        Long chunk = 0l;
        String name = file_input.getName();


        Long had_upload = 0l;
        while (had_upload < file_input.length()) {
            fis.read(file_bytes);

            final UploadChucks uploadChucks = new UploadChucks(getActivity());
            uploadChucks.setFile_bytes(file_bytes);
            uploadChucks.setChunks(chunks);
            uploadChucks.setChunk(chunk);

            AsyncCall request = uploadChucks.Request();
            request.setOnAsyncBackListener(new AsyncCall.OnAsyncBackListener() {
                @Override
                public void OnAsyncBack(List<Base> list, Base base, int requestType) {
                    JSONObject jsonObject = base.getResultJsonObject();
                    String fileName = jsonObject.optString("fileName");
                    if(!StringUtils.isEmpty(fileName)){
                        tmp.setWorkPath(fileName);

                        save(tmp);
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
