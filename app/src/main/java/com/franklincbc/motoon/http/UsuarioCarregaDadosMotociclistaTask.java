package com.franklincbc.motoon.http;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.franklincbc.motoon.Utils.Constants;
import com.franklincbc.motoon.model.Usuario;

/**
 * Created by Priscila on 21/12/2016.
 */

public class UsuarioCarregaDadosMotociclistaTask extends AsyncTaskLoader<Usuario> {

    private Usuario mMotociclista = null;
    Context ctx;

    public UsuarioCarregaDadosMotociclistaTask(Context context, Bundle params) {
        super(context);
        this.ctx = context;
        this.mMotociclista = (Usuario) params.getSerializable(Constants.USUARIO_EXTRA);
     }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Usuario loadInBackground() {
        Usuario motociclista = null;
            try {

                motociclista = UsuarioHttp.carregaDadosMotociclista(mMotociclista);
                if (motociclista == null) {
                    //Toast.makeText(ctx, "SEM COMUNICAÇÃO COM O SERVIDOR", Toast.LENGTH_SHORT).show();
                    return null;
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            return motociclista;

    }


}

