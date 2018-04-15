package uk.ac.uws.msc.shakh.restclient;

import android.util.Log;

import com.github.chen0040.androidmagentoclient.AndroidMagentoClient;

/**
 * Created by chen0 on 4/7/2017.
 */

public class CustomerClient {
    private static final String TAG = "CustomerClient";

    public static final String HOME_URL = "http://192.168.1.91";
    public static final String USERNAME = "shakhmehedi@yahoo.com";
    public static final String PASSWORD = "shakhmscpasS1";


    private static CustomerClient instance;
    private AndroidMagentoClient client;

    public synchronized static CustomerClient getInstance() {
        if (instance == null) {
            instance = new CustomerClient();
        }
        return instance;
    }

    private CustomerClient() {
        client = new AndroidMagentoClient(HOME_URL);
    }

    public AndroidMagentoClient client() {
        return client;
    }

    public String loginDemoClient() {
        String token = client.loginAsClient(USERNAME, PASSWORD);
        Log.i(TAG, "login as client: " + token);
        return token;
    }
}
