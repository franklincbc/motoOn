package com.franklincbc.motoon.http;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.franklincbc.motoon.Utils.Constants;
import com.franklincbc.motoon.model.Usuario;

import org.json.JSONException;

import static com.franklincbc.motoon.AceitouTermoActivity.LOADER_ACEITOU_TERMO_ADESAO;

/**
 * Created by Priscila on 29/11/2016.
 */

public class AceitouTermoAdesaoTask extends AsyncTaskLoader<String> {
    private Integer mLoaderID;
    private Usuario mUsuario = null;

    public AceitouTermoAdesaoTask(Context context, Integer loaderID, Bundle params) {
        super(context);
        mLoaderID = loaderID;

        if(LOADER_ACEITOU_TERMO_ADESAO == loaderID){
            mUsuario = (Usuario) params.getSerializable(Constants.USUARIO_EXTRA);
        }


    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        String retorno = null;
        if (mUsuario == null){
            return null;
        }

        if(mLoaderID == LOADER_ACEITOU_TERMO_ADESAO){
            try {
                retorno = UsuarioHttp.AceitouTermoAdesao(mUsuario);
                if(retorno == null){
                    return null;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return retorno;
    }
}

