package com.franklincbc.motoon.http;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.franklincbc.motoon.MainActivity;
import com.franklincbc.motoon.PainelChamadosActivity;
import com.franklincbc.motoon.Utils.Constants;
import com.franklincbc.motoon.model.Usuario;

import org.json.JSONException;

/**
 * Created by Priscila on 08/11/2016.
 */

public class UsuarioAtualizaLocalizacaoUsuarioTask extends AsyncTaskLoader<Usuario> {

    private Integer mLoaderID;
    private Usuario mUsuario = null;

    public UsuarioAtualizaLocalizacaoUsuarioTask(Context context, Integer loaderID, Bundle params) {
        super(context);
        this.mLoaderID = loaderID;
        if(MainActivity.LOADER_ATUALIZA_LOCALIZACAO_USU == loaderID){
            this.mUsuario = (Usuario) params.getSerializable(Constants.USUARIO_EXTRA);
        }
        else if(PainelChamadosActivity.LOADER_ATUALIZA_LOCALIZACAO_USU_PNL == loaderID){
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
        if(mLoaderID == MainActivity.LOADER_ATUALIZA_LOCALIZACAO_USU){
            try {
                Integer retorno = UsuarioHttp.AtualizaLocalizacaoUsuario(mUsuario);
                if(retorno == null){
                    return null;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }


        }

        return mUsuario;
    }
}
