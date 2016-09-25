/**
 * This class provides a Javascript API for the Paystack Android SDK.
 *
 * @constructor
 */
function Paystack() {}

Paystack.prototype.getToken = function(options, successCallback, errorCallback) {
    cordova.
    execute(
        successCallback,
        errorCallback,
        "PaystackCordovaV2",
        "getToken",
        [{
            "cardNumber": options.cardNumber,
            "expiryMonth": options.expiryMonth,
            "expiryYear": options.expiryYear,
            "cvc:": options.cvc
        }]
    );
}

Paystack.prototype.chargeCard = function(options, successCallback, errorCallback) {
    cordova.
    execute(
        successCallback,
        errorCallback,
        "PaystackCordovaV2",
        "chargeCard",
        [{
            "cardNumber": options.cardNumber,
            "expiryMonth": options.expiryMonth,
            "expiryYear": options.expiryYear,
            "cvc": options.cvc,
            "email": options.email,
            "amount": options.amount
        }]
    );
}

module.exports = new Paystack();