package aia.test.android.com.aiatest.senders;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import aia.test.android.com.aiatest.constants.ImgurConstants;
import aia.test.android.com.aiatest.constants.StringConstants;
import aia.test.android.com.aiatest.interfaces.IImgurSenderObserver;
import aia.test.android.com.aiatest.model.ErrorResponse;
import aia.test.android.com.aiatest.util.CommonUtils;

public class ImgurServices {
    public static final int NETWORK_TIMEOUT = 10000;
    public static RequestQueue mRequestQueue;
    private static Context fromContext;
    private static String jsonResponse;

    public static void sendJsonRequest(Context context, final String url, final IImgurSenderObserver observer) {
        fromContext = context;
        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        CommonUtils.getInstance().setLoading(false);

                        jsonResponse = response.toString();
                        // display response
                      /*  Log.d(StringConstants.TAG + " " + response, jsonResponse);*/
                        Log.d(StringConstants.TAG + " " + "URL", url);
                        notifyOnSuccessfulImagurSending(jsonResponse, observer);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            CommonUtils.getInstance().setLoading(false);

                            String body = "";
                            String statusCode = "defaultErr";

                            try {
                                if (error != null) {
                                    String localizedMessage = error.toString();
                                    if (error.toString().contains("403")) {
                                        body = getAuthError(error, "");
                                    } else if (error.toString().contains("403")) {
                                        body = getAuthError(error, "");
                                    } else if (error instanceof TimeoutError || error instanceof NetworkError) {
                                        final ObjectMapper mapper = new ObjectMapper();
                                        final ErrorResponse errorResponse = new ErrorResponse();
                                        errorResponse.setResponseCode(ImgurConstants.NO_INTERNET);
                                        errorResponse.setResponseMsg("No Internet");
                                        try {
                                            body = mapper.writeValueAsString(errorResponse);
                                        } catch (IOException ex) {
                                            body = "";
                                        }
                                    } else if (error.networkResponse.data != null) {

                                        try {
                                            statusCode = String.valueOf(error.networkResponse.statusCode);
                                            body = new String(error.networkResponse.data, "UTF-8");
                                            body = getAuthError(error, "");

                                        } catch (UnsupportedEncodingException e) {
                                            notifyOnFailedImgurSending(jsonResponse, observer);
                                        }
                                    } else {
                                        body = getAuthError(error, body);
                                    }

                                    notifyOnFailedImgurSending(body, observer);

                                }
                            } catch (Exception e) {
                                body = getAuthError(error, body);
                                notifyOnFailedImgurSending(body, observer);


                            }

                        } catch (Exception ex) {

                        }
                    }
                }) {
            /** Passing some request headers* */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put(StringConstants.AUTHORIZATION, ImgurConstants.IMGUR_CLIENT_ID);
                return params;
            }
        };

// add it to the RequestQueue
        RequestQueue mRequestQueue = Volley.newRequestQueue(fromContext);
        mRequestQueue.add(getRequest);
    }

    private static String getAuthError(VolleyError error, String body) {
        if (error.toString().contains("AuthFailureError") || error.toString().contains("403")) {
            final ObjectMapper mapper = new ObjectMapper();
            final ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setResponseCode(ImgurConstants.AUTH_ERROR);
            errorResponse.setResponseMsg("AuthFailure");
            try {
                body = mapper.writeValueAsString(errorResponse);
            } catch (IOException ex) {
                body = "";
            }
        }
        return body;
    }


    public static void notifyOnSuccessfulImagurSending(String jsonResponse, IImgurSenderObserver observer) {
        observer.onNotifySuccessfulImgurSending(jsonResponse);

    }

    public static void notifyOnFailedImgurSending(String jsonResponse, IImgurSenderObserver observer) {
        observer.onNotifyFailedImgurSending(jsonResponse);

    }
}