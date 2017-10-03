package graduation.whatziroom.network;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Heronation on 2017-07-26.
 */

public class HttpNetwork extends AsyncTask<String, Void, String> {

    private ProgressDialog mProgressDialog;
    private List<NameValuePair> params;
    private String response = "";

    public interface AsyncResponse {
        void onSuccess(String response);
        void onFailure(String response);
        void onPreExcute();
    }

    public AsyncResponse delegate = null;

    public HttpNetwork(String URL, List<NameValuePair> params, AsyncResponse delegate) {

        this.delegate = delegate;
        this.params = params;
        this.execute("http://210.122.38.41/whatziroom/PHP/" + URL);

    }



    @Override
    protected void onPreExecute() {
        delegate.onPreExcute();
//        mProgressDialog = new ProgressDialog(BaseActivity.mContext);
//        mProgressDialog.setMessage("Please wait...");
//        mProgressDialog.setCancelable(false);
//        mProgressDialog.show();

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        try {

            Log.d("param", params[0]);
            return downloadData(params[0]);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onPostExecute(String result) {
        delegate.onSuccess(result + "");
        Log.d("onPostExecuete", result + "");

//        if (mProgressDialog != null)
//            mProgressDialog.hide();

    }

    @Override
    protected void onCancelled(String s) {
        delegate.onFailure(s);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    public String downloadData(String urlString) throws IOException {
        InputStream is = null;

        try {

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));

            writer.write(getQuery(params));
            writer.flush();
            writer.close();
            os.close();

            conn.connect();

            is = conn.getInputStream();
            return convertToString(is);

        } finally {
            if (is != null) {
                is.close();
            }
        }
    }



    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            System.out.println("What?!,    " + pair.getValue());
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));

        }

        return result.toString();
    }

    private String convertToString(InputStream is) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        return new String(total);
    }
}
