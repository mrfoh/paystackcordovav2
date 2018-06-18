/**
 * This class provides a Javascript API for the Paystack Android SDK.
 *
 * @constructor
 */
function Paystack() {}

Paystack.prototype.getToken = function (options, successCallback, errorCallback) {
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

Paystack.prototype.chargeCard = function (options, successCallback, errorCallback) {
    cordova.
    exec(
        successCallback,
        errorCallback,
        "PaystackCordova",
        "chargeCard",
        [{
            "card_number": options.card_number,
            "expiry_month": options.expiry_month,
            "expiry_year": options.expiry_year,
            "cvc": options.cvc,
            "email": options.email,
            "amount": options.amount,
            "currency": options.currency,
            "reference": options.reference,
            "plan": options.plan,
            "subaccount": options.subaccount,
            "transaction_charge": options.transaction_charge,
            "bearer": options.bearer
        }]
    );
}

module.exports = new Paystack();
