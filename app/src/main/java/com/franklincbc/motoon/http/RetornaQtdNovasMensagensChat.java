package com.franklincbc.motoon.http;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by Priscila on 21/01/2017.
 */

public class RetornaQtdNovasMensagensChat extends AsyncTaskLoader<Integer> {

    private Integer mLoaderID;
    private Integer mSolicitacaoUsuarioID;
    private Integer mSolicitacaoID;
    private Integer mUsuarioID;
    private Integer mMototaxiID = 0;
    private String  mBackgroundService = "N";

    public RetornaQtdNovasMensagensChat(Context context, Integer loaderID, Bundle params) {
        super(context);
        this.mLoaderID = loaderID;
        Integer solicitacao_usuario_id = params.getInt("solicitacao_usuario_id",0);
        Integer solicitacao_id = params.getInt("solicitacao_id",0);

        this.mSolicitacaoUsuarioID = solicitacao_usuario_id;
        this.mSolicitacaoID = solicitacao_id;
        this.mUsuarioID = params.getInt("usuario_id",0);
        this.mMototaxiID = params.getInt("mototaxi_id",0);
        this.mBackgroundService = params.getString("backgroundservice");
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Integer loadInBackground() {
        String sn_mototaxi = "N";
        if (mMototaxiID > 0){
            sn_mototaxi = "S";
        }
        Integer quant = UsuarioHttp.RetornaQtdNovasMensagensChat(mSolicitacaoUsuarioID, mSolicitacaoID, mUsuarioID, mMototaxiID, mBackgroundService, sn_mototaxi);
        if(quant == null){
            return null;
        }
        return quant;
    }

}
