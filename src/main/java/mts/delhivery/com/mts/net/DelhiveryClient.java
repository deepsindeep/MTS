package mts.delhivery.com.mts.net;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;

import org.apache.http.HttpEntity;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import mts.delhivery.com.mts.config.Config;
import mts.delhivery.com.mts.util.Tracer;
import mts.delhivery.com.mts.util.Utils;


public class DelhiveryClient {
    /*
     * Tag for logs
     */
    public static final String TAG = Config.logger + DelhiveryClient.class.getSimpleName();

    /**
     * time out for the get or post request.
     */
    private final int timeout = 60000;

    /**
     * AsyncHttpClientobject
     */
    private final AsyncHttpClient clientPost;

    /**
     * AsyncHttpClientobject
     */
    private final AsyncHttpClient clientGet;
    private final AsyncHttpClient clientPatch;

    private final AsyncHttpClient clientPut;
    private final AsyncHttpClient clientPing;
    private MySSLSocketFactory socketFactory;
    private static Context mContext;

    /**
     * GBAsycnHttpClient instance
     */
    private static DelhiveryClient _instance;

    /**
     * private GBAsyncHttpClient constructer
     *
     * @throws IOException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws UnrecoverableKeyException
     * @throws KeyManagementException
     */
    private DelhiveryClient() throws NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException, UnrecoverableKeyException {
        Tracer.debug(TAG, "[RhAsyncHttpClient] _ " + "" + "");

        clientPost = new AsyncHttpClient();
        clientPost.setTimeout(timeout);
        clientPost.setEnableRedirects(true);
        clientGet = new AsyncHttpClient();
        clientGet.setTimeout(timeout);
        clientPing= new AsyncHttpClient();
        clientPing.setTimeout(timeout);

        clientPatch = new AsyncHttpClient();
        clientPatch.setTimeout(timeout);

        clientPut = new AsyncHttpClient();
        clientPut.setTimeout(timeout);


        clientPatch.addHeader("App-Version", Utils.getVersionName(mContext));
        clientGet.addHeader("app_version", Utils.getVersionName(mContext));
        clientPut.addHeader("app_version", Utils.getVersionName(mContext));
        clientPost.addHeader("app_version", Utils.getVersionName(mContext));

        try {
            SchemeRegistry schemeRegistry = clientPost.getHttpClient().getConnectionManager().getSchemeRegistry();
            schemeRegistry.register(new Scheme("https", new TlsSniSocketFactory(), 443));
            clientPost.setLoggingEnabled(true);

        } catch (Exception e) {
            Tracer.debug(TAG, " DelhiveryClient " + " " + e);
            e.printStackTrace();
        }
    }

    /**
     * static method to make {@link DelhiveryClient} singleton
     *
     * @return if _instance is null then create a {@link DelhiveryClient}
     * instance and return
     * @throws IOException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws UnrecoverableKeyException
     * @throws KeyManagementException
     */
    public static DelhiveryClient getInstance(Context context) throws UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException {
        Tracer.debug(TAG, "[getInstance] _ " + "" + "");
        mContext = context;
        if (_instance == null) {
            _instance = new DelhiveryClient();
        }


        return _instance;
    }


    /**
     * @param url
     * @param entity
     * @param responseHandler
     */
    public void postJson(String url, HttpEntity entity, String token,
                         AsyncHttpResponseHandler responseHandler) {
        String contentType = "application/json";
        if (token != null) {
            clientPost.addHeader("Authorization", "Bearer " + token);
            clientGet.addHeader("Content-Type", "application/json");
            clientPost.addHeader("Accept", contentType);
            clientPost.addHeader("App-Version", "1.17.hardcoded");
        }
        clientPost.post(mContext, url, entity,
                contentType, responseHandler);
    }


    /**
     * @param url
     * @param responseHandler
     */
    public void get(String url, RequestParams params, String token,
                    AsyncHttpResponseHandler responseHandler) {
        clientGet.addHeader("Accept", "application/json");
        clientGet.addHeader("Content-Type", "application/json");

        if (token != null) {
            clientGet.addHeader("Authorization", "Bearer " + token);
        }
        clientGet.get(url, params, responseHandler);

    }
    public void ping(String url,
                    AsyncHttpResponseHandler responseHandler) {
        Tracer.debug(TAG," ping "+" ");
        clientPing.get(url, null, responseHandler);

    }

    public void put(String url, HttpEntity entity, String token,
                    AsyncHttpResponseHandler responseHandler) {
        String contentType = "application/json";
        if (token != null) {
            clientPut.addHeader("Authorization", "Bearer " + token);
            clientPut.addHeader("Accept", contentType);
        }
        clientPut.put(mContext, url, entity, contentType, responseHandler);
    }


    public void patch(String url, HttpEntity entity, String token,
                      AsyncHttpResponseHandler responseHandler) {
        String contentType = "application/json";
        if (token != null) {
            clientPatch.addHeader("Authorization", "Bearer " + token);
            clientPatch.addHeader("Accept", contentType);
        }
        clientPatch.patch(mContext, url, entity, contentType, responseHandler);

    }
}
