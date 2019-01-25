package com.franklincbc.motoon.http;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.franklincbc.motoon.MainActivity;
import com.franklincbc.motoon.Utils.Constants;
import com.franklincbc.motoon.model.Usuario;


/**
 * Created by Priscila on 08/11/2016.
 */

public class UsuarioCarregaDadosUsuarioTask extends AsyncTaskLoader<Usuario> {

    private Integer mLoaderID;
    private Usuario mUsuario = null;
    Context ctx;

    public UsuarioCarregaDadosUsuarioTask(Context context, Integer loaderID, Bundle params) {
        super(context);

        this.mLoaderID = loaderID;
        this.ctx = context;
        if(MainActivity.LOADER_CARREGA_DADOS_USU == loaderID){
            this.mUsuario = (Usuario) params.getSerializable(Constants.USUARIO_EXTRA);
        }



    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Usuario loadInBackground() {

        if(mLoaderID == MainActivity.LOADER_CARREGA_DADOS_USU){

            try {

                mUsuario = UsuarioHttp.carregaDadosUsuario(mUsuario);
                if (mUsuario == null) {
                    //Toast.makeText(ctx, "SEM COMUNICAÇÃO COM O SERVIDOR", Toast.LENGTH_SHORT).show();
                    return null;
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            return mUsuario;
        }

        return null;
    }


}
