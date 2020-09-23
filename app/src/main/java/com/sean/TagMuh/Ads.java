package com.sean.TagMuh;

class Ads {

    private String adDescription,adImage1,adImage2,adImage3,adTitle,category,servicerId;


    public Ads() {
    }

    public Ads(String adDescription, String adImage1, String adImage2, String adImage3, String adTitle, String category, String servicerId) {
        this.adDescription = adDescription;
        this.adImage1 = adImage1;
        this.adImage2 = adImage2;
        this.adImage3 = adImage3;
        this.adTitle = adTitle;
        this.category = category;
        this.servicerId = servicerId;
    }

    public String getAdDescription() {
        return adDescription;
    }

    public void setAdDescription(String adDescription) {
        this.adDescription = adDescription;
    }

    public String getAdImage1() {
        return adImage1;
    }

    public void setAdImage1(String adImage1) {
        this.adImage1 = adImage1;
    }

    public String getAdImage2() {
        return adImage2;
    }

    public void setAdImage2(String adImage2) {
        this.adImage2 = adImage2;
    }

    public String getAdImage3() {
        return adImage3;
    }

    public void setAdImage3(String adImage3) {
        this.adImage3 = adImage3;
    }

    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getServicerId() {
        return servicerId;
    }

    public void setServicerId(String servicerId) {
        this.servicerId = servicerId;
    }
}
