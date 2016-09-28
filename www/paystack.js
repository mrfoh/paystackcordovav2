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
        "PaystackCordova",
        "getToken",
        [{
            "card_number": options.card_number,
            "expiry_month": options.expiry_month,
            "expiry_year": options.expiry_year,
            "cvc": options.cvc
        }]
    );
}

Paystack.prototype.chargeCard = function(options, successCallback, errorCallback) {
    cordova.
    exec(
        successCallback,
        errorCallback,
        "PaystackCordova",
        "chargeCard",
        [{
            "card_umber": options.card_number,
            "expiry_month": options.expiry_month,
            "expiry_year": options.expiry_year,
            "cvc": options.cvc,
            "email": options.email,
            "amount": options.amount
        }]
    );
}

module.exports = new Paystack();
