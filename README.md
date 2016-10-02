**Paystack Cordova Plugin**
=======================
Cordova Android Plugin for Paystack. Paystack is a Nigerian payment platform allowing users to collect payments via MasterCard, Visa, and Verve cards.
This plugin uses the [Paystack Android SDK](https://github.com/PaystackHQ/paystack-android)

Installation
----------------
From github

    cordova plugin add https://github.com/mrfoh/paystackcordovav2 --variable PUBLIC_KEY="your paystack public key"

From CLI/Plugman

    cordova plugin add paystack-cordova --variable PUBLIC_KEY="your paystack public ket"

After installing, you have to build your project so the Paystack SDK can be downloaded. While in your project directory run this;

    cordova build android

API
-----
The plugin exposes a simple API for generating paystack transaction tokens which can used to charge a customer without them re-entering their card, as well as making a one-time charge.

**window.plugin.paystackCordova.getToken(options, successCallback, errorCallback)**

This method creates a unique Paystack token using a card's details; card number, expiry month, year and cvc number.

Method Arguments

**options**

    {
	    card_number: "",
	    expiry_month: "",
	    expiry_year: "",
	    cvc: ""
    }

**successCallback**

    function(response) {
    }

   The response object


    {
	   "token": "Paystack token",
	   "last4": "Last 4 digits of the card"
    }

  **errorCallback**

    function(error) {
    }

The error object

    {
	   "code": "Error code; 0 for fatal errors, 1 for validation errors, 2 for paystack errors",
	   "message": "Error message"
    }


**window.plugins.paystackCordova.chargeCard(options, successCallback, errorCallback)**

This method creates a one-time charge on a card

Method Arguments

**options**

| Property    | Type | Description |
|-------------|------|-------------|
|card_number  |      | the card number as a String without any seperator e.g 5555555555554444
            |
|expiry_month |      |             |
|expiry_year  |      |             |
|cvc          |      |             |

 **successCallback**


    function(response) {
    }

The response object


    {
	   "reference": "Unique paystack transaction reference"
    }

**errorCallback**

    function(error) {
    }

The error object

    {
	   "code": "Error code; 0 for fatal errors, 1 for validation errors, 2 for paystack errors",
	   "message": "Error message"
    }

Usage
---------
**Charging a card**

The paystack android sdk allows you to make a one-time charge on a card. Making a one-time charge with this plugin is quite simple;

    var options = {
	    card_number: "4123450131001381",
	    expiry_month: 7,
	    exipry_year: 2019,
	    cvc: 883,
	    email: "john.doe@acme.com",
	    amount: 50000
    }
    window.plugins.paystackCordova.chargeCard(options, function(response) {
    //Perform verification with response.reference
    }, function(err) {
    //Perform error handling
    });
