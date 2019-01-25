package com.franklincbc.motoon.http;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.franklincbc.motoon.model.Chat;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Priscila on 19/01/2017.
 */

public class InsertChatSolicitacaoTask extends AsyncTaskLoader<List<Chat>> {

    private Integer mLoaderID;
    private List<Chat> mChatList = null;
    private Integer mSolicitacao_usuario_id;
    private Integer mSolicitacao_id;
    private Integer mUsuario_id;
    private String mData_hora;
    private String mNome;
    private String mTexto;

    public InsertChatSolicitacaoTask(Context context, Integer loaderID, Integer solicitacao_usuario_id, Integer solicitacao_id, Integer usuario_id, String data_hora, String nome, String texto ) {
        super(context);

        this.mLoaderID = loaderID;
        this.mChatList = new ArrayList<>();
        this.mSolicitacao_usuario_id = solicitacao_usuario_id;
        this.mSolicitacao_id = solicitacao_id;
        this.mUsuario_id = usuario_id;
        this.mData_hora = data_hora;
        this.mNome = nome;
        this.mTexto = texto;
    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }


    @Override
    public List<Chat> loadInBackground() {
        List<Chat> chatList = null;

        try {
            chatList = SolicitacaoHttp.insertChatSolicitacao(mSolicitacao_usuario_id, mSolicitacao_id, mUsuario_id, mData_hora, mNome, mTexto );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(chatList == null){
            return null;
        }
        mChatList.clear();
        mChatList.addAll(chatList);
        return mChatList;
    }


}
