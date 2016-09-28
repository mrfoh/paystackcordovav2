/**
 * This class provides a Javascript API for the Paystack Android SDK.
 *
 * @constructor
 */
function Paystack() {}

Paystack.prototype.getToken = function(options, successCallback, errorCallback) {
    cordova.
    exec(
        successCallback,
        errorCallback,
        "PaystackCordovaV2",
        "getToken",
        [{
            "card_number": options.cardNumber,
            "expiry_month": options.expiryMonth,
            "expiry_year": options.expiryYear,
            "cvc:": options.cvc
        }]
    );
}

Paystack.prototype.chargeCard = function(options, successCallback, errorCallback) {
    cordova.
    exec(
        successCallback,
        errorCallback,
        "PaystackCordovaV2",
        "chargeCard",
        [{
            "card_umber": options.cardNumber,
            "expiry_month": options.expiryMonth,
            "expiry_year": options.expiryYear,
            "cvc": options.cvc,
            "email": options.email,
            "amount": options.amount
        }]
    );
}

module.exports = new Paystack();