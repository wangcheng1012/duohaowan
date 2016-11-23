package com.hd.wlj.duohaowan.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hd.wlj.duohaowan.R;
import com.wlj.base.util.DpAndPx;
import com.wlj.base.widget.IconfontTextview;

/**
 *  iconfont EditText iconfont组合
 *
 */
public class ImgInpImg extends FrameLayout {
    private float deftextsize = 16f;
    private int deftextcolor = Color.BLACK;
    private TextView iconFontView1;
    private EditText editTextView;
    private TextView iconFontView2;
    private OnClickListener mListener;

    public ImgInpImg(Context context) {
        this(context,null);
    }

    public ImgInpImg(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ImgInpImg(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ImgInpImg(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImgInpImg);
        //-------- 获取值
        //iconFontView1
        String iconfont1Text = a.getString(R.styleable.ImgInpImg_iconfont1_text);
        int iconfont1TextColor = a.getColor(R.styleable.ImgInpImg_iconfont1_color, deftextcolor);

        //iconFontView2
        String iconfont2Text = a.getString(R.styleable.ImgInpImg_iconfont2_text);
        int iconfont2TextColor = a.getColor(R.styleable.ImgInpImg_iconfont2_color, deftextcolor);
        // EditText
        String editTextHit = a.getString(R.styleable.ImgInpImg_edittext_hit);
        String editTextText = a.getString( R.styleable.ImgInpImg_edittext_text);
        int editTextTextColor = a.getColor(R.styleable.ImgInpImg_edittext_textcolor,deftextcolor);

        boolean backgroundisNull = a.getBoolean(R.styleable.ImgInpImg_edittext_background_isnull, false);
        Drawable background_drawable = a.getDrawable(R.styleable.ImgInpImg_edittext_background);
//        int resourceId = a.getResourceId(R.styleable.ImgInpImg_edittext_background, 0);
//        Bitmap background_bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);

        //---------布局
        View inflate = LayoutInflater.from(context).inflate(R.layout.view_img_inp_img, this);

        iconFontView1 = (TextView)inflate.findViewById(R.id.img_inp_img__imgs);

        editTextView = (EditText)inflate.findViewById(R.id.img_inp_img__input);
        iconFontView2 = (TextView)inflate.findViewById(R.id.img_inp_img__imge);

        //----------  设置值
        //iconFontView1
        iconFontView1.setText(iconfont1Text);
        iconFontView1.setTextColor(iconfont1TextColor);
        iconFontView1.setTextSize(deftextsize);
        //editTextView
        editTextView.setText(editTextText);
        editTextView.setHint(editTextHit);
        editTextView.setTextColor(editTextTextColor);
        editTextView.setTextSize(deftextsize);

        if(background_drawable != null ) {

            editTextView.setBackgroundDrawable(background_drawable);
        }else if(backgroundisNull){
            editTextView.setBackgroundDrawable(null);
        }
        // iconFontView2
        iconFontView2.setText(iconfont2Text);
        iconFontView2.setTextColor(iconfont2TextColor);
        iconFontView2.setTextSize(deftextsize);
    }

    public void setInputType(int type){
        editTextView.setInputType(type);
    }

    public String getText(){

        return editTextView.getText()+"";
    }

    public IconfontTextview getIconFontView2() {
        return (IconfontTextview)iconFontView2;
    }

    public IconfontTextview getIconFontView1() {
        return (IconfontTextview)iconFontView1;
    }

    public EditText getEditTextView() {
        return editTextView;
    }

    /**
     * EditTextView 与 左边的距离
     *
     * @param f
     */
    public void setEditTextViewMaginLeft(int f) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) editTextView.getLayoutParams();
        layoutParams.leftMargin = DpAndPx.dpToPx(getContext(), f);
        editTextView.setLayoutParams(layoutParams);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
//                mListener.onClick(this);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:

//                if (x + getLeft() < getRight() && y + getTop() < getBottom()) {
//                    mListener.onClick(this);
//                }

                break;

        }

        return super.onTouchEvent(event);
    }


    /**
     * iconFontView2 的点击事件
     * @param mListener
     */
    public  void setViewOnClickListener(OnClickListener mListener){
        this.mListener = mListener;
    }
}
