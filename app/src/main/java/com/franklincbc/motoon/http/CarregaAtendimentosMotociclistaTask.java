package com.franklincbc.motoon.http;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.franklincbc.motoon.model.Solicitacao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Priscila on 29/12/2016.
 */

public class CarregaAtendimentosMotociclistaTask extends AsyncTaskLoader<List<Solicitacao>> {

    private Integer mMototaxiID;
    private List<Solicitacao> mSolicitacoes = null;

    public CarregaAtendimentosMotociclistaTask(Context context, Integer mototaxiID) {
        super(context);
        this.mSolicitacoes = new ArrayList<>();
        this.mMototaxiID = mototaxiID;
    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }


    @Override
    public List<Solicitacao> loadInBackground() {
        List<Solicitacao> solicitacoes = SolicitacaoHttp.carregaAtendimentosMotociclista(mMototaxiID);
        if(solicitacoes == null){
            return null;
        }
        mSolicitacoes.clear();
        mSolicitacoes.addAll(solicitacoes);
        return mSolicitacoes;
    }
}
