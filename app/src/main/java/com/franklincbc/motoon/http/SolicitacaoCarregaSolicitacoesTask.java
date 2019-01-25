package com.franklincbc.motoon.http;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.franklincbc.motoon.model.Solicitacao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Priscila on 29/10/2016.
 */

public class SolicitacaoCarregaSolicitacoesTask extends AsyncTaskLoader<List<Solicitacao>> {

    private Integer mLoaderID;
    private Integer mUsuarioID;
    private List<Solicitacao> mSolicitacoes = null;

    public SolicitacaoCarregaSolicitacoesTask(Context context, Integer loaderID, Integer mUsuarioID) {
        super(context);
        this.mLoaderID = loaderID;
        this.mSolicitacoes = new ArrayList<>();
        this.mUsuarioID = mUsuarioID;
    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }


    @Override
    public List<Solicitacao> loadInBackground() {
        List<Solicitacao> solicitacoes = SolicitacaoHttp.carregaSolicitacoes(mUsuarioID);
        if(solicitacoes == null){
            return null;
        }
        mSolicitacoes.clear();
        mSolicitacoes.addAll(solicitacoes);
        return mSolicitacoes;
    }
}
