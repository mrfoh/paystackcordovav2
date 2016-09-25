package com.mrfoh.paystackcordovav2;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Patterns;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.net.Uri;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.model.Card;
import co.paystack.android.model.Token;
import co.paystack.android.model.Charge;
import co.paystack.android.model.Transaction;

public class Paystack extends CordovaPlugin {

    protected Token token;
    protected Card card;

    private  Charge charge;
    private Transaction transaction;

    public static final TAG = "com.mrfoh.paystackcordovav2";

    protected CallbackContext context;

    /**
     * Provides the Paystack SDK with the cordova webview's activity context
     * @param cordova The context of the main Activity.
     * @param webView The CordovaWebView Cordova is running in.
     */
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        //initialize sdk
        PaystackSdk.initialize(this.cordova.getActivity().getApplicationContext());
    }

    /**
     * Handles errors
     * @param errorMsg The exception message
     * @param errorCode The associated exception error code
     */
    protected void handleError(String errorMsg, int errorCode){
        try {
            //Log error using plugin tag
            Log.e(TAG, errorMsg);

            //Create error object
            JSONObject error = new JSONObject();
            error.put("error", errorMsg);
            error.put("code", errorCode);
            context.error(error);

        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        try {
            if(action.equals('chargeCard')) {
                JSONObject arg_object = args.getJSONObject(0);
                context = callbackContext;

                chargeCard(arg_object);
                return true;
            }
            else if(action.equals('getToken')) {
                JSONObject arg_object = args.getJSONObject(0);
                context = callbackContext;

                getToken(arg_object);
                return true;
            }
            else {
                return false;
            }

        }
        catch(Exception e) {
            handleError(e.getMessage(), 401);
            return false;
        }
    }

    private void chargeCard(JSONObject arg_object) throw JSONExecption {

    }

    private void getToken(JSONObject arg_object) throw JSONExecption {

    }
}