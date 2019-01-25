package com.franklincbc.motoon.http;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.franklincbc.motoon.SolicitarMotoActivity;
import com.franklincbc.motoon.Utils.Constants;
import com.franklincbc.motoon.model.Solicitacao;

import org.json.JSONException;

/**
 * Created by franklin.carvalho on 26/10/2016.
 */

public class SolicitacaoRegistraSolicitacaoTask extends AsyncTaskLoader<Long>{

    private Integer mLoaderID;
    private Solicitacao mSolicitacao = null;

    public SolicitacaoRegistraSolicitacaoTask(Context context, Integer loaderID, Bundle params) {
        super(context);
        mLoaderID = loaderID;

        if(SolicitarMotoActivity.LOADER_REGISTRA_SOLICITACAO == loaderID){
            mSolicitacao = (Solicitacao) params.getSerializable(Constants.SOLICITACAO_EXTRA);
        }


    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Long loadInBackground() {
        Long value = Long.valueOf(-1);
        if (mSolicitacao == null){
            return Long.valueOf(-1);
        }

        if(mLoaderID == SolicitarMotoActivity.LOADER_REGISTRA_SOLICITACAO){
            try {
                value = SolicitacaoHttp.registraSolicitacao(Integer.valueOf(String.valueOf(mSolicitacao.getUsuario_id())),
                                                            mSolicitacao.getSolicitante(),
                                                            mSolicitacao.getLocal_origem(),
                                                            mSolicitacao.getPonto_referencia(),
                                                            mSolicitacao.getLocal_destino(),
                                                            mSolicitacao.getInformacao_adicional(),
                                                            mSolicitacao.getData_solicitacao(),
                                                            mSolicitacao.getLatitude(),
                                                            mSolicitacao.getLongitude(),
                                                            mSolicitacao.getBairro(),
                                                            mSolicitacao.getCidade(),
                                                            mSolicitacao.getTipo_veiculo(),
                                                            mSolicitacao.getLatitude_destino(),
                                                            mSolicitacao.getLongitude_destino(),
                                                            mSolicitacao.getDistancia_presumida(),
                                                            mSolicitacao.getFaixa_preco_presumido());

                if (value == null){
                    return null;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return value;
    }

}
