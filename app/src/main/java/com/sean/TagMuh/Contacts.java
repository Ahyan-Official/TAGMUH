package com.sean.TagMuh;

class Contacts {

    private String adsId,customerId,requestState,servicerId;

    private int dateTime;


    public Contacts() {
    }

    public Contacts(String adsId, String customerId, String requestState, String servicerId, int dateTime) {
        this.adsId = adsId;
        this.customerId = customerId;
        this.requestState = requestState;
        this.servicerId = servicerId;
        this.dateTime = dateTime;
    }

    public String getAdsId() {
        return adsId;
    }

    public void setAdsId(String adsId) {
        this.adsId = adsId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getRequestState() {
        return requestState;
    }

    public void setRequestState(String requestState) {
        this.requestState = requestState;
    }

    public String getServicerId() {
        return servicerId;
    }

    public void setServicerId(String servicerId) {
        this.servicerId = servicerId;
    }

    public int getDateTime() {
        return dateTime;
    }

    public void setDateTime(int dateTime) {
        this.dateTime = dateTime;
    }
}
