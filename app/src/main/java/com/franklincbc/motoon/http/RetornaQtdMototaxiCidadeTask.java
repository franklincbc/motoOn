package com.franklincbc.motoon.http;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by Priscila on 15/12/2016.
 */

public class RetornaQtdMototaxiCidadeTask extends AsyncTaskLoader<Integer> {

    private Integer mLoaderID;
    private String mCidade;

    public RetornaQtdMototaxiCidadeTask(Context context, Integer loaderID, Bundle params) {
        super(context);
        this.mLoaderID = loaderID;
        String cidade = params.getString("cidade");
        this.mCidade = cidade;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Integer loadInBackground() {
        Integer quant = UsuarioHttp.RetornaQtdMototaxiCidade(mCidade);
        if(quant == null){
            return null;
        }
        return quant;
    }

}
