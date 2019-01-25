package com.franklincbc.motoon.http;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.franklincbc.motoon.MeusDadosActivity;
import com.franklincbc.motoon.Utils.Constants;
import com.franklincbc.motoon.model.Usuario;

import org.json.JSONException;

/**
 * Created by Priscila on 22/11/2016.
 */

public class UsuarioSalvarDadosUsuarioTask extends AsyncTaskLoader<Integer> {
    private Integer mLoaderID;
    private Usuario mUsuario = null;

    public UsuarioSalvarDadosUsuarioTask(Context context, Integer loaderID, Bundle params) {
        super(context);
        mLoaderID = loaderID;

        if(MeusDadosActivity.LOADER_SALVAR_DADOS_USUARIO == loaderID){
            mUsuario = (Usuario) params.getSerializable(Constants.USUARIO_EXTRA);
        }


    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Integer loadInBackground() {
        Integer rowsAffected = 0;
        if (mUsuario == null){
            return rowsAffected;
        }

        if(mLoaderID == MeusDadosActivity.LOADER_SALVAR_DADOS_USUARIO){
            try {
                rowsAffected = UsuarioHttp.SalvarDadosUsuario(mUsuario);
                if(rowsAffected == null){
                    return null;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return rowsAffected;
    }
}
