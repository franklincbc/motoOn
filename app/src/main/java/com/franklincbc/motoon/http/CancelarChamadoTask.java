package com.franklincbc.motoon.http;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.franklincbc.motoon.model.Solicitacao;
import com.franklincbc.motoon.model.Usuario;

import org.json.JSONException;

/**
 * Created by Priscila on 24/11/2016.
 */

public class CancelarChamadoTask extends AsyncTaskLoader<String> {

    private Integer mLoaderID;
    private Usuario mUsuario;
    private String result;
    private Solicitacao mSolicitacao;

    public CancelarChamadoTask(Context context, Integer loaderID, Usuario usuario, Solicitacao solicitacao) {
        super(context);
        this.mLoaderID = loaderID;
        this.mUsuario = usuario;
        this.mSolicitacao = solicitacao;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        try {
            result = SolicitacaoHttp.CancelarChamado(mSolicitacao.getSolicitacao_id(),
                                                     mSolicitacao.getUsuario_id(),
                                                    mSolicitacao.getData_cancelamento());
            if(result == null){
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

}
