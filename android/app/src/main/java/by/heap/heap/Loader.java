package by.heap.heap;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class Loader extends AsyncTask<Void, Void, Void> {
    private ProgressDialog pDialog;
    protected Response response;
    protected String url;
    protected int method;
    protected String params;
    protected boolean showDialod = true;

    public Loader(String url,int method)
    {
        this.url=url;
        this.method=method;
        this.params=null;
    }

    public Loader(String url,int method,String params)
    {
        this.url=url;
        this.method=method;
        this.params=params;
    }

    public Loader(String url,int method,boolean showDialod)
    {
        this.url=url;
        this.method=method;
        this.params=null;
        this.showDialod=showDialod;
    }

    public Loader(String url,int method,String params,boolean showDialod)
    {
        this.url=url;
        this.method=method;
        this.params=params;
        this.showDialod=showDialod;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(showDialod) {
            pDialog = new ProgressDialog(Tools.getMainActivity());
            pDialog.setMessage("Загрузка");
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        ServiceHandler sh = new ServiceHandler();
        response = sh.makeServiceCall(url, method, params);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        if(showDialod) {
            if (pDialog.isShowing())
                pDialog.dismiss();
        }

        if (response.code>0) {
            result();
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
    }

    protected void result()
    {

    }
}