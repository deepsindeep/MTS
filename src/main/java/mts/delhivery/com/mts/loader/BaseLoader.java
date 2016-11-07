package mts.delhivery.com.mts.loader;

import com.loopj.android.http.RequestParams;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

import mts.delhivery.com.mts.MTS;
import mts.delhivery.com.mts.config.Config;
import mts.delhivery.com.mts.domain.ErrorD;
import mts.delhivery.com.mts.domain.ErrorResponse;
import mts.delhivery.com.mts.listener.BaseListener;
import mts.delhivery.com.mts.net.DelhiveryClient;
import mts.delhivery.com.mts.net.ResponseHandler;
import mts.delhivery.com.mts.util.JsonUtil;
import mts.delhivery.com.mts.util.Tracer;
import mts.delhivery.com.mts.util.Utils;

public abstract class BaseLoader {
    private final String TAG = Config.logger + BaseLoader.class.getSimpleName();
    public DelhiveryClient mClient;

    protected abstract void onSuccess(String json, BaseListener baseListner);

    protected abstract void onFailure(ErrorD error, BaseListener baseListner, String json);

    public BaseLoader() {
        Tracer.debug(TAG, "[BaseLoader] _ " + "");
        try {
            mClient = DelhiveryClient.getInstance(MTS.getinstance().getContext());
        } catch (Exception e) {
            Tracer.debug(TAG, " BaseLoader " + " " + e);
            e.printStackTrace();
        }

    }

    protected void requestPost(String url, String json, String token, final BaseListener mListner) {
        StringEntity entity;
//        Tracer.reportOnLogen(TAG, " requestPost " + "Url " + url + "  Json" + json);
        if (initialValidationPost(mListner, json)) {

            try {
                entity = new StringEntity(json);
                mClient.postJson(url, (HttpEntity) entity, token, new ResponseHandler() {
                    @Override
                    public void onSuccess(String str, int statuscode) {
//                        Tracer.debug(TAG, "[onSuccess] _ ");
                        BaseLoader.this.onSuccess(str, mListner);
                    }

                    @Override
                    public void onFailure(String json, int statuscode) {
                        Tracer.debug(TAG, "[onFailure] _ " + json);
                        BaseLoader.this.onFailure(getErrorD(json), mListner,
                                json);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                Tracer.debug(TAG, " requestPost " + e);
                BaseLoader.this.onFailure(new ErrorD("Connection error!", ErrorD.SERVER_ERROR),
                        mListner, null);
            }
        }
    }

    protected void requestPost(String url, String token, final BaseListener mListner) {
        if (initialValidationPost(mListner)) {

            mClient.postJson(url, null, token, new ResponseHandler() {
                @Override
                public void onSuccess(String str, int statuscode) {
                    Tracer.debug(TAG, "[onSuccess] _ ");
                    BaseLoader.this.onSuccess(str, mListner);
                }

                @Override
                public void onFailure(String json, int statuscode) {
                    Tracer.debug(TAG, "[onFailure] _ " + json);
                    BaseLoader.this.onFailure(getErrorD(json), mListner,
                            json);
                }

            });
        }
    }

    protected void requestPut(String url, String json, String token, final BaseListener mListner) {
        StringEntity entity;
        Tracer.debug(TAG, "[requestPut] _ " + " token " + token + " url " + url + "  json " + json);
        if (initialValidationPost(mListner, json)) {

            try {
                entity = new StringEntity(json);

                mClient.put(url, (HttpEntity) entity, token, new ResponseHandler() {
                    @Override
                    public void onSuccess(String str, int statuscode) {
                        Tracer.debug(TAG, "[onSuccess] _ ");
                        BaseLoader.this.onSuccess(str, mListner);
                    }

                    @Override
                    public void onFailure(String json, int statuscode) {
                        Tracer.debug(TAG, "[onFailure] _ " + json);
                        BaseLoader.this.onFailure(getErrorD(json), mListner, json);
                    }

                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                BaseLoader.this.onFailure(new ErrorD("Connection error!", ErrorD.SERVER_ERROR),
                        mListner, null);
            }
        }
    }


    protected void requestPatch(String url, String json, String token, final BaseListener mListner) {
        StringEntity entity;
        Tracer.reportOnLogenteries(TAG, "[requestPatch] _ " + "  url  " + url + "  json " + json);
        if (initialValidationPost(mListner, json)) {

            try {
                entity = new StringEntity(json);

                mClient.patch(url, (HttpEntity) entity, token, new ResponseHandler() {
                    @Override
                    public void onSuccess(String str, int statuscode) {
                        Tracer.debug(TAG, "[onSuccess] _ ");
                        BaseLoader.this.onSuccess(str, mListner);
                    }

                    @Override
                    public void onFailure(String json, int statuscode) {
                        Tracer.debug(TAG, "[onFailure] _ ");
                        BaseLoader.this.onFailure(getErrorD(json), mListner, json);
                    }

                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                BaseLoader.this.onFailure(new ErrorD("Connection error!", ErrorD.SERVER_ERROR),
                        mListner, null);
            }
        }
    }



    /*
    * Get Request
    * @params url, url params , token, Base listner for callback
    * */

    protected void requestGet(String url, RequestParams reqParams, String token,
                              final BaseListener mListner) {
        Tracer.reportOnLogenteries(TAG, "[requestGet] _ " + "url " + url + " Pram " + reqParams + " token "
                + token);
        if (initialValidationGet(mListner, " ns")) {
            try {

                mClient.get(url, reqParams, token, new ResponseHandler() {

                    @Override
                    public void onSuccess(String str, int statuscode) {
                        Tracer.debug(TAG, "[onSuccess] _ ");
                        BaseLoader.this.onSuccess(str, mListner);
                    }

                    @Override
                    public void onFailure(String json, int statuscode) {
                        Tracer.debug(TAG, " onFailure " + json);
                        BaseLoader.this.onFailure(getErrorD(json), mListner,
                                json);
                    }
                });
            } catch (Exception e) {
                Tracer.debug(TAG, " requestGet Exceprtion " + e);
                BaseLoader.this.onFailure(new ErrorD("Connection error!", ErrorD.SERVER_ERROR),
                        mListner, null);
            }
        }

    }

    protected void ping(String url,
                        final BaseListener mListner) {
        Tracer.debug(TAG, "[requestGet] _ " + "url " + url);
        try {

            mClient.ping(url, new ResponseHandler() {

                @Override
                public void onSuccess(String str, int statuscode) {
                    Tracer.debug(TAG, "[onSuccess] _ ");
                    BaseLoader.this.onSuccess(str, mListner);
                }

                @Override
                public void onFailure(String json, int statuscode) {
                    Tracer.debug(TAG, " onFailure " + json);
                    BaseLoader.this.onFailure(getErrorD(json), mListner,
                            json);
                }
            });
        } catch (Exception e) {
            Tracer.debug(TAG, " requestGet Exceprtion " + e);
            BaseLoader.this.onFailure(new ErrorD("Connection error!", ErrorD.SERVER_ERROR),
                    mListner, null);
        }

    }


    private boolean initialValidationPost(BaseListener baseListner, String json) {
        if (!Utils.isConnectionAvailable(MTS.getinstance().getContext())) {
            onFailure(new ErrorD("No Internet Connection!", ErrorD.NO_INTERNET), baseListner, null);
            return false;
        }
        return true;
    }

    private boolean initialValidationPost(BaseListener baseListner) {
        if (!Utils.isConnectionAvailable(MTS.getinstance().getContext())) {
            onFailure(new ErrorD("No Internet Connection!", ErrorD.NO_INTERNET), baseListner, null);
            return false;
        }
        return true;

    }

    private boolean initialValidationGet(BaseListener baseListner, String token) {
        Tracer.debug(TAG, " initialValidationGet " + " ");
        if (!Utils.isConnectionAvailable(MTS.getinstance().getContext())) {
            onFailure(new ErrorD("No Internet Connection!", ErrorD.NO_INTERNET), baseListner, null);
            return false;
        } else if (Utils.isEmpty(token)) {
            onFailure(new ErrorD("Invalid Sesion!", ErrorD.SESSION_INVALID), baseListner, null);
            return false;
        }
        return true;

    }


    private ErrorD getErrorD(String jsonMessage) {
        Tracer.debug(TAG, " getErrorD " + " ");
        try {
            ErrorResponse baseResponse = (ErrorResponse) JsonUtil
                    .objectify(jsonMessage, ErrorResponse.class);
            return new ErrorD(baseResponse.data.message, baseResponse.data.error_code);
        } catch (Exception e) {
            Tracer.debug(TAG, "[onFailure] _ " + "Exception e");
            return ErrorD.getServerError();
        }
    }
}
