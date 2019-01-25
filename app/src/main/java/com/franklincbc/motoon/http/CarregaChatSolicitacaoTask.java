package com.franklincbc.motoon.http;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.franklincbc.motoon.model.Chat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Priscila on 19/01/2017.
 */

public class CarregaChatSolicitacaoTask extends AsyncTaskLoader<List<Chat>> {

    private Integer mLoaderID;
    private List<Chat> mChatList = null;
    private Integer mSolicitacao_usuario_id;
    private Integer mSolicitacao_id;

    public CarregaChatSolicitacaoTask(Context context, Integer loaderID, Integer solicitacao_usuario_id, Integer solicitacao_id ) {
        super(context);
        this.mLoaderID = loaderID;
        this.mChatList = new ArrayList<>();
        this.mSolicitacao_usuario_id = solicitacao_usuario_id;
        this.mSolicitacao_id = solicitacao_id;
    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }


    @Override
    public List<Chat> loadInBackground() {
        List<Chat> chatList = SolicitacaoHttp.carregaChatSolicitacao(mSolicitacao_usuario_id, mSolicitacao_id);
        if(chatList == null){
            return null;
        }
        mChatList.clear();
        mChatList.addAll(chatList);
        return mChatList;
    }
}

