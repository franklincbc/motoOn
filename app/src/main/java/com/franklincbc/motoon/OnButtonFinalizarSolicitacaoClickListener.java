package com.franklincbc.motoon;

import android.view.View;

import com.franklincbc.motoon.model.Solicitacao;

/**
 * Created by Priscila on 12/11/2016.
 */

public interface OnButtonFinalizarSolicitacaoClickListener {
    void onButtonFinalizarSolicitacaoClick(View view, Solicitacao solicitacao, int position);
}
