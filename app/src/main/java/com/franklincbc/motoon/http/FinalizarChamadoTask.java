package com.franklincbc.motoon.http;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.franklincbc.motoon.model.Solicitacao;
import com.franklincbc.motoon.model.Usuario;

import org.json.JSONException;

/**
 * Created by Priscila on 12/11/2016.
 */

public class FinalizarChamadoTask extends AsyncTaskLoader<String> {

    private Integer mLoaderID;
    private Usuario mUsuario;
    private String result;
    private Solicitacao mSolicitacao;

    public FinalizarChamadoTask(Context context, Integer loaderID, Usuario usuario, Solicitacao solicitacao) {
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
            result = SolicitacaoHttp.finalizarChamado(  mSolicitacao.getSolicitacao_id(),
                                                        mUsuario.getMototaxi_id(),
                                                        mSolicitacao.getUsuario_id(),
                                                        mSolicitacao.getData_fim_corrida());
            if (result == null){
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

}
