package by.heap.heap;

import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class ServiceHandler {

    public final static int GET = 1;
    public final static int POST = 2;
    public final static int PUT = 3;

    public ServiceHandler() {

    }

    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * */
    public Response makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }

    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     * */
    public Response makeServiceCall(String url, int method,
                                  String params) {
        Response response = new Response();
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            Log.e("query", url + "?" + params);
            // Checking http request method type
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                // adding post params
                if (params != null) {
                    StringEntity entity = new StringEntity(params, HTTP.UTF_8);
                    entity.setContentType("application/json");
                    httpPost.setEntity(entity);
                }
                httpPost.setHeader("Content-Type","application/json");
                if(Tools.auth_token!=null)
                    httpPost.setHeader("Authorization",Tools.auth_token);

                httpResponse = httpClient.execute(httpPost);

            } else if (method == PUT) {
                HttpPut httpPut = new HttpPut(url);
                // adding post params
                if (params != null) {
                    StringEntity entity = new StringEntity(params, HTTP.UTF_8);
                    entity.setContentType("application/json");
                    httpPut.setEntity(entity);
                }

                httpPut.setHeader("Content-Type","application/json");
                if(Tools.auth_token!=null)
                    httpPut.setHeader("Authorization",Tools.auth_token);

                httpResponse = httpClient.execute(httpPut);

            } else if (method == GET) {
                // appending params to url
                /*if (params != null) {
                    String paramString = URLEncodedUtils
                            .format(params, "utf-8");
                    url += "?" + paramString;
                }*/
                HttpGet httpGet = new HttpGet(url);

                httpGet.setHeader("Content-Type","application/json");
                if(Tools.auth_token!=null)
                    httpGet.setHeader("Authorization",Tools.auth_token);

                httpResponse = httpClient.execute(httpGet);

            }
            httpEntity = httpResponse.getEntity();
            response.body = EntityUtils.toString(httpEntity);
            response.code = httpResponse.getStatusLine().getStatusCode();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }
}