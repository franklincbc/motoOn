package com.franklincbc.motoon.http;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.franklincbc.motoon.model.Solicitacao;

/**
 * Created by Priscila on 21/12/2016.
 */

public class CarregaSolicitacaoCorrenteMotociclistaAtendTask extends AsyncTaskLoader<Solicitacao> {

    private Integer mMototaxiID;
    private Solicitacao mSolicitacao = null;

    public CarregaSolicitacaoCorrenteMotociclistaAtendTask(Context context, Integer mototaxi_id) {
        super(context);
                this.mMototaxiID = mototaxi_id;
    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }


    @Override
    public Solicitacao loadInBackground() {
        mSolicitacao = SolicitacaoHttp.carregaSolicitacaoCorrenteMotociclistaAtendimento(mMototaxiID);

        if(mSolicitacao == null) {
            mSolicitacao = new Solicitacao();
            return mSolicitacao;
        }

        return mSolicitacao;
    }
}
