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
import co.paystack.android.model.Charge;
import co.paystack.android.Transaction;

public class PaystackCordova extends CordovaPlugin {

    protected Token token;
    protected Card card;

    private  Charge charge;
    private Transaction transaction;

    public static final String TAG = "com.mrfoh.paystackcordovav2";

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
            JSONObject arg = args.getJSONObject(0);

            if("chargeCard".equals(action)) {
                context = callbackContext;
                chargeCard(arg);
                return true;
            }
            else if("chargeCardWithAccessCode".equals(action)) {
                context = callbackContext;
                chargeWithAccessCode(arg);
                return true;
            }
            else {
                return false;
            }

        }
        catch(Exception e) {
            handleError(e.getMessage(), 0);
            return false;
        }
    }

    /**
     * @param args JSON object of arguments
     * @throws JSONExecption
     */
    private void chargeCard(JSONObject args) throws JSONException {
        validateCustomerTransaction(args);

        if (card != null && card.isValid()) {
            try {
                initTransaction();
            } catch(Exception error) {
                handleError(error.getMessage(), 0);
            }
        }
    }

    /**
     * @param args JSON object of arguments
     * @throws JSONExecption
     */
    private void chargeWithAccessCode(JSONObject args) throws JSONException {
        String accessCode = args.getString("access_code");
        // validate card
        validateCustomerCard(args);
        // create charge
        charge = new Charge();
        charge.setCard(card);
        charge.setAccessCode(accessCode);

        if (card != null && card.isValid()) {
            try {
                initTransaction();
            } catch(Exception error) {
                handleError(error.getMessage(), 0);
            }
        }
    }


    /**
     * @param ref Paystack transaction reference
     */
    private void onChargeSuccess(String ref) {
        try {
            //Log to console using plugin tag
            Log.i(TAG, ref);
            //create success object
            JSONObject success = new JSONObject();
            success.put("reference", ref);
            context.success(success);

        } catch (JSONException e) {
            handleError(e.getMessage(), 0);
        }
    }

    /**
     * Initiate a transaction
     */
    private void initTransaction() {
        //reset current transaction
        transaction = null;
        final PaystackCordova plugin = this;

        PaystackSdk.chargeCard(this.cordova.getActivity(), charge, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {
                plugin.transaction = transaction;
                onChargeSuccess(transaction.getReference());
            }

            @Override
            public void beforeValidate(Transaction transaction) {
                plugin.transaction = transaction;
            }

            @Override
            public void onError(Throwable e, Transaction transaction) {

                if (plugin.transaction == null) {
                    handleError(e.getMessage(), 2);
                } else {
                    handleError(transaction.getReference() + " failed with error: " + e.getMessage(), 2);
                }
            }
        });
    }

    /**
     *
     * @param arg_object JSON object of arguments
     * @throws JSONExecption
     */
    private void validateCustomerTransaction(JSONObject arg_object) throws JSONException {
        //validate card
        validateCustomerCard(arg_object);

        String email = arg_object.getString("email");
        Integer amount = arg_object.getInt("amount");
        String currency = arg_object.getString("currency");
        String reference = arg_object.getString("reference");
        String plan = arg_object.getString("plan");
        String subaccount = arg_object.getString("subaccount");
        Integer transaction_charge = arg_object.getInt("transaction_charge");
        String  bearer = arg_object.getString("bearer");

        //create charge object
        charge = new Charge();
        //associate card with charge
        charge.setCard(card);

        if (reference != null && !reference.isEmpty()) {
            charge.setReference(reference);
        }

        if (isEmpty(email)) {
            handleError("Email cannot be empty.", 1);
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            handleError("Invalid email.", 1);
        }

        charge.setEmail(email);

        if (amount < 1) {
            handleError("Invalid amount.", 1);
        }

        charge.setAmount(amount);

        if (currency != null && !isEmpty(currency)) {
            charge.setCurrency(currency);
        }

        if(plan != null & !isEmpty(plan)) {
            charge.setPlan(plan);
        }

        if(subaccount != null && !isEmpty(subaccount)) {
            charge.setSubaccount(subaccount);

            if(bearer != null && isEmpty(bearer) && bearer.equals("subaccount") || bearer.equals("account")) {
                switch (bearer) {
                    case "subaccount":
                        charge.setBearer(Charge.Bearer.subaccount);
                        break;
                    case "account":
                        charge.setBearer(Charge.Bearer.account);
                        break;
                }

                if (transaction_charge >= 0) {
                    charge.setTransactionCharge(transaction_charge;
                }
                else {
                    handleError("Invalid transaction_charge.", 1);
                }
            }
            else {
                handleError("Invalid bearer.", 1);
            }
        }
    }

    /**
     *
     * @param args JSON object of arguments
     * @throws JSONExecption
     */
    private void validateCustomerCard(JSONObject args) throws JSONException {
        String cardNum = args.getString("card_number");
        Integer expiryMonth = args.getInt("expiry_month");
        Integer expiryYear = args.getInt("expiry_year");
        String cvc = args.getString("cvc");

        if(isEmpty(cardNum)) {
            handleError("Invalid card number provided.", 1);
        }

        card = new Card.Builder(cardNum, 0, 0, "").build();

        //validate card number
        if (!card.validNumber()) {
            handleError("Invalid card number provided.", 1);
        }

        //validate expiry month
        if(expiryMonth < 1 || expiryMonth > 12) {
            handleError("Invalid expiry month provided.", 1);
        }
        //update the expiryMonth field of the card
        card.setExpiryMonth(expiryMonth);

        //validate expiry year
        if(expiryYear < 1) {
            handleError("Invalid expiry year provided.", 1);
        }
        //update the expiryYear field of the card
        card.setExpiryYear(expiryYear);

        //validate card expiration date
        if (!card.validExpiryDate()) {
            handleError("Invalid expiration date provided.", 1);
        }

        if(isEmpty(cvc)) {
            handleError("Empty cvc code.", 1);
        }

        //update the cvc field of the card
        card.setCvc(cvc);

        //check that it's valid
        if (!card.validCVC()) {
            handleError("Invalid cvc code provided.", 1);
        }
    }

    /**
     * @param s String
     * String to check
     * @return Boolean
     */
    private boolean isEmpty(String s) {
        return s == null || s.length() < 1;
    }
}
