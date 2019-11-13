package com.rechargeweb.rechargeweb;


import com.easypay.epmoney.epmoneyaeps.application.PaisaNikalApp;

public class ApplicationTest extends PaisaNikalApp {

    @Override
    public void onCreate() {
        super.onCreate();
        PaisaNikalApp.init(this);
    }
}
