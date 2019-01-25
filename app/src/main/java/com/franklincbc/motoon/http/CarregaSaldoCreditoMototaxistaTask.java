package com.franklincbc.motoon.http;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by Priscila on 25/11/2016.
 */

public class CarregaSaldoCreditoMototaxistaTask extends AsyncTaskLoader<Integer> {

    private Integer mLoaderID;
    private Integer mMototaxi_ID;

    public CarregaSaldoCreditoMototaxistaTask(Context context, Integer loaderID, Integer mototaxi_id) {
        super(context);
        this.mLoaderID = loaderID;
        this.mMototaxi_ID = mototaxi_id;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Integer loadInBackground() {
        Integer saldo = UsuarioHttp.CarregaSaldoCreditoMototaxista(mMototaxi_ID);
        if (saldo == null){
            return null;
        }
        return saldo;
    }

}

