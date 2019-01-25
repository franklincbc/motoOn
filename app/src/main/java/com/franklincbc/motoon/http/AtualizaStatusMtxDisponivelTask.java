package com.franklincbc.motoon.http;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import org.json.JSONException;

/**
 * Created by Priscila on 25/01/2017.
 */

public class AtualizaStatusMtxDisponivelTask extends AsyncTaskLoader<String> {

    private Integer mMototaxi_id = null;
    private String mSn_disponivel = "S";

    public AtualizaStatusMtxDisponivelTask(Context context, Bundle params) {
        super(context);
        this.mMototaxi_id = params.getInt("mototaxi_id");
        this.mSn_disponivel = params.getString("sn_disponivel");
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        String value = "";
        try {
            value = UsuarioHttp.AtualizaStatusMtxDisponivel(mMototaxi_id, mSn_disponivel);
            if(value == null){
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;

    }
}
