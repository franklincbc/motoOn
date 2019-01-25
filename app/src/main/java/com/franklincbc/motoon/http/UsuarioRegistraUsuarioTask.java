package com.franklincbc.motoon.http;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.franklincbc.motoon.LoginActivity;
import com.franklincbc.motoon.model.Usuario;

import org.json.JSONException;

/**
 * Created by Priscila on 23/09/2016.
 */
public class UsuarioRegistraUsuarioTask extends AsyncTaskLoader<Long> {
    private Integer mLoaderID;
    private Usuario mUsuario = null;

    public UsuarioRegistraUsuarioTask(Context context, Integer loaderID, Bundle params) {
        super(context);
        mLoaderID = loaderID;

        if(LoginActivity.LOADER_REGISTRA_USU == loaderID){
            mUsuario = (Usuario) params.getSerializable(LoginActivity.EXTRA_USUARIO);
        }


    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Long loadInBackground() {
        Long value = Long.valueOf(-1);
        if (mUsuario == null){
            return Long.valueOf(-1);
        }

        if(mLoaderID == LoginActivity.LOADER_REGISTRA_USU){
            try {
                value = UsuarioHttp.registraUsuario(mUsuario);
                if(value == null){
                    return null;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return value;
    }
}
