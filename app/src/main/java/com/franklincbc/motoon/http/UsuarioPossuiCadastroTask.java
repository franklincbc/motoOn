package com.franklincbc.motoon.http;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by Priscila on 24/11/2016.
 */

public class UsuarioPossuiCadastroTask extends AsyncTaskLoader<Long> {

    private Integer mLoaderID;
    private String mEmail;

    public UsuarioPossuiCadastroTask(Context context, Integer loaderID, Bundle params) {
        super(context);
        this.mLoaderID = loaderID;
        String email = params.getString("email");
        this.mEmail = email;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Long loadInBackground() {
        boolean bPossuiCadastro = UsuarioHttp.UsuarioPossuiCadastro(mEmail);
        if(bPossuiCadastro){
            return Long.valueOf(1); //possui cadastro
        }
        else
        {
            return Long.valueOf(0);  //nao possui cadastro
        }
    }

}

