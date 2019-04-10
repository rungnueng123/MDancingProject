package com.mocom.com.mdancingproject.Dao;

public class StudentPaymentGatewayDao {

    private String paymentUrl, version, merchantId, currency, resultUrl1, hashValue, paymentDesc, orderId;
    private Double amount;

    public StudentPaymentGatewayDao(String paymentUrl, String version, String merchantId, String currency, String resultUrl1, String hashValue, String paymentDesc, String orderId, Double amount) {
        this.paymentUrl = paymentUrl;
        this.version = version;
        this.merchantId = merchantId;
        this.currency = currency;
        this.resultUrl1 = resultUrl1;
        this.hashValue = hashValue;
        this.paymentDesc = paymentDesc;
        this.orderId = orderId;
        this.amount = amount;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getResultUrl1() {
        return resultUrl1;
    }

    public void setResultUrl1(String resultUrl1) {
        this.resultUrl1 = resultUrl1;
    }

    public String getHashValue() {
        return hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }

    public String getPaymentDesc() {
        return paymentDesc;
    }

    public void setPaymentDesc(String paymentDesc) {
        this.paymentDesc = paymentDesc;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
