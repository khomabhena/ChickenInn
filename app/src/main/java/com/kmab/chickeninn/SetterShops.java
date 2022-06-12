package com.kmab.chickeninn;

public class SetterShops {

    public SetterShops() {
    }

    private String uniqueKey, city, address, prefix;
    private long merchant, whatsApp;

    public SetterShops(String uniqueKey, String city, String address, long merchant, String prefix, long whatsApp) {
        this.uniqueKey = uniqueKey;
        this.city = city;
        this.address = address;
        this.merchant = merchant;
        this.prefix = prefix;
        this.whatsApp = whatsApp;
    }

    public long getWhatsApp() {
        return whatsApp;
    }

    public void setWhatsApp(long whatsApp) {
        this.whatsApp = whatsApp;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getMerchant() {
        return merchant;
    }

    public void setMerchant(long merchant) {
        this.merchant = merchant;
    }
}
