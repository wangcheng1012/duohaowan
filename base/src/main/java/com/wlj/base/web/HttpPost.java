package com.wlj.base.web;


import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ly
 */
public class HttpPost {

    private JSONObject jSONObjectParemeter;
    private URL url;
    private URLConnection openConnection;
    private StringBuilder paremeter = new StringBuilder();
    private String charset = null;

    private static Map<String, String> cookies = new HashMap<String, String>();
    private boolean jiami;

    public HttpPost(String paramString) throws IOException {
        this(new URL(paramString));
    }

    /**
     * @param url url地址
     * @throws IOException 如果这个报错就是连接根本没打开
     */
    public HttpPost(URL url, String charset) throws IOException {
        if (charset == null) {
            charset = "utf-8";
        } else {
            this.charset = charset;
        }

        this.url = url;
        this.openConnection = url.openConnection();
        this.openConnection.setDoOutput(true);
        this.openConnection.setDoInput(true);
        this.openConnection.setConnectTimeout(10000);
        this.openConnection.setReadTimeout(10000);
        this.jSONObjectParemeter = new JSONObject();
        this.charset = charset;
    }

    /**
     * @param url url地址
     * @throws IOException 如果这个报错就是连接根本没打开
     */
    public HttpPost(URL url) throws IOException {
        this.charset = "utf-8";
        this.url = url;
        this.openConnection = url.openConnection();
        this.openConnection.setDoOutput(true);
        this.openConnection.setDoInput(true);
        this.openConnection.setConnectTimeout(10000);
        this.openConnection.setReadTimeout(10000);
        this.jSONObjectParemeter = new JSONObject();
    }

    /**
     * 需要 jiami 为false才添加的进去，是为了防止与加密地方的参数重复
     * @param paramString
     * @param paramObject
     */
    public void addParemeter(String paramString, Object paramObject) {

        if (paramString == null) {
            this.paremeter.append(paramObject).append("&");
            return;
        }
        if (!jiami) {
            this.paremeter.append(paramString).append("=").append(URLEncoder.encode(paramObject + "")).append("&");
        }
        try {
            jSONObjectParemeter.put(paramString, paramObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addRequestProperty(String key, String value) {
        openConnection.addRequestProperty(key, value);
    }

    public InputStream getInputStream() throws IOException {
        InputStream inputStream = null;

        if (this.paremeter.length() > 1) {
            this.paremeter = this.paremeter.deleteCharAt(-1 + this.paremeter.length());
        }
        StringBuilder sb = new StringBuilder();
        if (cookies != null) {
            Iterator localIterator2 = cookies.keySet().iterator();
            while (localIterator2.hasNext()) {
                String str2 = (String) localIterator2.next();
                String str3 = (String) cookies.get(str2);
                sb.append(str2 + "=" + str3 + "; ");
            }
        }

        try {
            if (sb.toString().trim().length() > 0) {
                this.openConnection.setRequestProperty("Cookie", sb.toString());
            }
            OutputStreamWriter localOutputStreamWriter = new OutputStreamWriter(this.openConnection.getOutputStream(), this.charset);

            localOutputStreamWriter.write(this.paremeter.toString());
            localOutputStreamWriter.flush();
            localOutputStreamWriter.close();

            if (cookies != null) {
                Map<String, List<String>> headerFields = openConnection.getHeaderFields();
                if (headerFields != null) {
                    List<String> list = headerFields.get("Set-Cookie");
                    if (list != null) {
                        for (String string2 : list) {
                            String[] substring = string2.substring(0, string2.indexOf(";")).split("=");
                            cookies.put(substring[0], substring[1]);
                        }
                    }
                }
            }

            inputStream = openConnection.getInputStream();

        } catch (IOException e) {
            inputStream = ((HttpURLConnection) openConnection).getErrorStream();

            throw e;
        }
        return inputStream;

    }

    /**
     * @return post的返回值
     * @throws IOException 获取结果报错
     */
    public String getResult() throws IOException {
        OutputStreamWriter outputStream = null;
        InputStream inputStream = null;
        ByteArrayOutputStream outByteArray = new ByteArrayOutputStream();
        if (paremeter.length() > 1) {
            paremeter = paremeter.deleteCharAt(paremeter.length() - 1);
        }

        StringBuilder sb = new StringBuilder();

        if (cookies != null) {
            Set<String> keySet = cookies.keySet();
            for (String string : keySet) {
                String string2 = cookies.get(string);
                sb.append(string + "=" + string2 + "; ");
            }
        }

        byte[] returnByte = null;
        try {
            if (sb != null && sb.toString().trim().length() > 0) {
                openConnection.setRequestProperty("Cookie", sb.toString());
            }
            outputStream = new OutputStreamWriter(openConnection.getOutputStream(), charset);
            outputStream.write(paremeter.toString());
            outputStream.flush();
            outputStream.close();
            //System.out.print(openConnection.getHeaderField("Set-Cookie"));

//	    	System.out.println(openConnection.getHeaderFields());

            if (cookies != null) {
                Map<String, List<String>> headerFields = openConnection.getHeaderFields();
                if (headerFields != null) {
                    List<String> list = headerFields.get("Set-Cookie");
                    if (list != null) {
                        for (String string2 : list) {
                            String[] substring = string2.substring(0, string2.indexOf(";")).split("=");
                            cookies.put(substring[0], substring[1]);

//                            Logger.e(cookies.toString());
                        }
                    }
                }
            }

            inputStream = openConnection.getInputStream();
            int b;
            while (inputStream != null && (b = inputStream.read()) != -1) {
                outByteArray.write(b);
            }
            returnByte = outByteArray.toByteArray();
        } catch (IOException e) {
            inputStream = ((HttpURLConnection) openConnection).getErrorStream();
            int b;
            while (inputStream != null && (b = inputStream.read()) != -1) {
                outByteArray.write(b);
            }
            returnByte = outByteArray.toByteArray();
            throw e;
        } finally {
            outByteArray.close();
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }

        }
        return new String(returnByte, charset);
    }


    public JSONObject getJSONObjectParemeter() {

        return this.jSONObjectParemeter;
    }

    public StringBuilder getParemeter() {

        return this.paremeter;
    }

    public URL getUrl() {
        return this.url;
    }

    public boolean isJiami() {
        return jiami;
    }

    public void setJiami(boolean jiami) {
        this.jiami = jiami;
    }
}
