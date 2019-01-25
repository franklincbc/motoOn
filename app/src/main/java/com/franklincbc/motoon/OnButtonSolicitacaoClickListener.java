package com.franklincbc.motoon;

import android.view.View;

import com.franklincbc.motoon.model.Solicitacao;

/**
 * Created by Priscila on 10/11/2016.
 */

public interface OnButtonSolicitacaoClickListener {
    void onButtonSolicitacaoClick(View view, Solicitacao solicitacao, int position);
}
