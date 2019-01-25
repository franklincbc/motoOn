package com.franklincbc.motoon;

import android.view.View;

import com.franklincbc.motoon.model.Solicitacao;

/**
 * Created by Priscila on 29/10/2016.
 */
public interface OnSolicitacaoClickListener {
    void onSolicitacaoClick(View view, Solicitacao solicitacao, int position);
}
