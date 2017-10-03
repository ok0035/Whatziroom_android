package graduation.whatziroom.network;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import graduation.whatziroom.Data.FriendData;

/**
 * Created by mapl0 on 2017-09-08.
 */

public class Params {

    ArrayList<NameValuePair> params;

    public Params() {
        params = new ArrayList<NameValuePair>();
    }

    public void add(String key, String value) {
        params.add(new BasicNameValuePair(key, value));
    }

    public ArrayList<NameValuePair> getParams() {
        return params;
    }


}
