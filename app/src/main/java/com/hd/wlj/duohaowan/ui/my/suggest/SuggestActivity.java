package com.hd.wlj.duohaowan.ui.my.suggest;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hd.wlj.duohaowan.R;
import com.wlj.base.ui.BaseFragmentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SuggestActivity extends BaseFragmentActivity implements SuggestView {

    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(R.id.title_title)
    TextView titleTitle;
    @BindView(R.id.suggest_problem)
    EditText problem;
    @BindView(R.id.suggest_content)
    EditText problemContent;
    @BindView(R.id.suggest_numberfilter)
    TextView numberfilter;

    private int textlength = 50;
    private SuggestPresenter suggestPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        ButterKnife.bind(this);

        initTitle();

        initView();

        suggestPresenter = new SuggestPresenter(this);
        suggestPresenter.attachView(this);
    }

    private void initView() {

        // 问题描述字数限制
        problemContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                String str = s + "";

                if (length > textlength) {
                    str = str.toString().substring(0, textlength);
                    problemContent.setText(str);
                    length = textlength;
                }
                numberfilter.setText(length + "/" + textlength);
            }
        });

    }

    private void initTitle() {
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTitle.setText("意见反馈");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        suggestPresenter.detachView();
    }

    @OnClick(R.id.suggest_save)
    public void onClick() {
        String title = problem.getText()+"";
        String content = problemContent.getText() + "";

        suggestPresenter.loadData(title,content);


    }
}
