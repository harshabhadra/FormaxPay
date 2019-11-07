package com.rechargeweb.rechargeweb;

import com.easypay.epmoney.epmoneylib.application.PaisaNikalApp;

public class ApplicationTest extends PaisaNikalApp {

    @Override
    public void onCreate() {
        super.onCreate();
        PaisaNikalApp.init(this);
    }
}
