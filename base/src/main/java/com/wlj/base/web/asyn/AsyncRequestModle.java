package com.wlj.base.web.asyn;

import android.app.Activity;
import android.util.Base64;

import com.wlj.base.bean.Base;
import com.wlj.base.util.AppContext;
import com.wlj.base.web.HttpPost;
import java.io.Serializable;
import org.json.JSONObject;

public class AsyncRequestModle
  implements Serializable
{
  private static final long serialVersionUID = -7241890269318232468L;
  private Activity activity;
  private boolean autoToLoginPage = true;
  private String cachekey;
  private HttpPost httpPost;
  private boolean jiami;
  private BaseAsyncModle parserBase;
  private Class<?> parserCla;
  private boolean refresh = true;
  private boolean save = true;
  private boolean showLoading;
  private int type;
  
  public AsyncRequestModle() {}

  public Activity getActivity()
  {

    return this.activity;
  }
  
  public String getCachekey()
  {

    String str = this.httpPost.getJSONObjectParemeter().toString();
    int i = 10;
    if (str.length() < 10) {
      i = str.length();
    }
    return Base64.encodeToString((AppContext.getAppContext().getProperty("type") + "_" + AppContext.getAppContext().getProperty("name") + "_" + getParserCla().getSimpleName() + "_" + this.cachekey + "_" + str.substring(0, i)).getBytes(), 0);
  }
  
  public HttpPost getHttpPost()
  {

    return this.httpPost;
  }
  
  public BaseAsyncModle getParserBase()
  {

    return this.parserBase;
  }
  
  public Class<?> getParserCla()
  {

    return this.parserCla;
  }
  
  public int getType()
  {

    return this.type;
  }
  
  public boolean isAutoToLoginPage()
  {

    return this.autoToLoginPage;
  }
  
  public boolean isJiami()
  {

    return this.jiami;
  }
  
  public boolean isRefresh()
  {

    return this.refresh;
  }
  
  public boolean isSave()
  {

    return this.save;
  }
  
  public boolean isShowLoading()
  {

    return this.showLoading;
  }
  
  public void setActivity(Activity paramActivity)
  {

    this.activity = paramActivity;
  }
  
  public void setAutoToLoginPage(boolean paramBoolean)
  {

    this.autoToLoginPage = paramBoolean;
  }
  
  public void setCachekey(String paramString)
  {

    this.cachekey = paramString;
  }
  
  public void setHttpPost(HttpPost paramHttpPost)
  {

    this.httpPost = paramHttpPost;
  }
  
  public void setJiami(boolean paramBoolean)
  {

    this.jiami = paramBoolean;
  }
  
  public void setParserBase(BaseAsyncModle paramBaseAsyncModle)
  {

    this.parserBase = paramBaseAsyncModle;
  }
  
  public void setParserCla(Class<?> paramClass)
  {

    this.parserCla = paramClass;
  }
  
  public void setRefresh(boolean paramBoolean)
  {

    this.refresh = paramBoolean;
  }
  
  public void setSave(boolean paramBoolean)
  {

    this.save = paramBoolean;
  }
  
  public void setShowLoading(boolean paramBoolean)
  {

    this.showLoading = paramBoolean;
  }
  
  public void setType(int paramInt)
  {

    this.type = paramInt;
  }
}
