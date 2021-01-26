package com.sanchit.Upsilon;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PaymentDetailsViewModel extends ViewModel {
    private MutableLiveData<String> account_number;
    private MutableLiveData<String> ifsc_code;
    private MutableLiveData<String> mobile_number;
    private MutableLiveData<String> upi_id;

    public PaymentDetailsViewModel() {
        account_number = new MutableLiveData<>();
        ifsc_code = new MutableLiveData<>();
        mobile_number = new MutableLiveData<>();
        upi_id = new MutableLiveData<>();
    }

    public MutableLiveData<String> getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number.setValue(account_number);
    }

    public MutableLiveData<String> getIfsc_code() {
        return ifsc_code;
    }

    public void setIfsc_code(String ifsc_code) {
        this.ifsc_code.setValue(ifsc_code);
    }

    public MutableLiveData<String> getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number.setValue(mobile_number);
    }

    public MutableLiveData<String> getUpi_id() {
        return upi_id;
    }

    public void setUpi_id(String upi_id) {
        this.upi_id.setValue(upi_id);
    }
}