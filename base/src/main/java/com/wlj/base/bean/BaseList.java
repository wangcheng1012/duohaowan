package com.wlj.base.bean;

import java.io.Serializable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BaseList implements Serializable {
    private static final long serialVersionUID = 1525386978044495919L;
    private int allCount;
    private Base baseData;
    private String id;
    private int lastpage;
    private List<Base> list;
    private int pageIndex;
    private String pic;

    public BaseList() {
    }


    public int getAllCount() {

        return this.allCount;
    }

    public Base getBaseData() {

        return this.baseData;
    }

    public String getId() {

        return this.id;
    }

    public int getLastpage() {

        return this.lastpage;
    }

    public List<Base> getList() {

        return this.list;
    }

    public int getPageIndex() {

        return this.pageIndex;
    }

    public String getPic() {

        return this.pic;
    }

    public BaseList parse(JSONObject paramJSONObject, Base paramBase)
            throws JSONException {

        setAllCount(paramJSONObject.optInt("count"));
        int i = paramJSONObject.optInt("pageSize");
        if (i != 0) {
            int b = allCount % i;
            if (b == 0) {
                setLastpage(allCount / i);
            } else {
                setLastpage(getAllCount() / i + 1);
            }
        }
        setPageIndex(paramJSONObject.optInt("page"));
        JSONObject localJSONObject = paramJSONObject.optJSONObject("data");
        JSONArray localJSONArray = paramJSONObject.optJSONArray("data");
        if (localJSONObject != null) {
            setBaseData(paramBase.parse(localJSONObject));
        }
        if (localJSONArray != null) {
            setList(paramBase.parseList(localJSONArray));
        }
        if ((localJSONObject == null) && (localJSONArray == null)) {
            setBaseData(paramBase.parseBean(paramJSONObject));
        }
        return this;

    }

    public BaseList parse(JSONObject paramJSONObject, Class<?> paramClass)
            throws Exception {
        if ((paramClass.newInstance() instanceof Base)) {
            return parse(paramJSONObject, (Base) paramClass.newInstance());
        }
        throw new Exception("传入模型不匹配");
    }

    public void setAllCount(int paramInt) {

        this.allCount = paramInt;
    }

    public void setBaseData(Base paramBase) {

        this.baseData = paramBase;
    }

    public void setId(String paramString) {

        this.id = paramString;
    }

    public void setLastpage(int paramInt) {

        this.lastpage = paramInt;
    }

    public void setList(List<Base> paramList) {

        this.list = paramList;
    }

    public void setPageIndex(int paramInt) {

        this.pageIndex = paramInt;
    }

    public void setPic(String paramString) {

        this.pic = paramString;
    }
}
