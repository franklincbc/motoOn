package com.franklincbc.motoon;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.franklincbc.motoon.Utils.Constants;
import com.franklincbc.motoon.adapter.ChatAdapter;
import com.franklincbc.motoon.http.CarregaChatSolicitacaoTask;
import com.franklincbc.motoon.http.InsertChatSolicitacaoTask;
import com.franklincbc.motoon.model.Chat;
import com.franklincbc.motoon.model.Solicitacao;
import com.franklincbc.motoon.model.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.franklincbc.motoon.Utils.Util.DataHoraSecNow;
import static com.franklincbc.motoon.Utils.Util.verificaConexao;

public class ChatActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    private static final int LOADER_CARREGA_CHAT_SOLICITACAO = 0;
    private static final int LOADER_INSERT_CHAT_SOLICITACAO = 1;

    RecyclerView mRecyclerView;
    ChatAdapter mAdapter;
    List<Chat> mChatList;
    LoaderManager mLoaderMAnager;

    Solicitacao mSolicitacao;
    Usuario mUsuario;

    EditText mEdtTexto;

    Chat mChat = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setTitle("CHAT MOTOON");

        mSolicitacao = (Solicitacao) getIntent().getSerializableExtra(Constants.SOLICITACAO_EXTRA);
        mUsuario = (Usuario) getIntent().getSerializableExtra(Constants.USUARIO_EXTRA);

        mEdtTexto = (EditText)findViewById(R.id.chat_edittext_msg);

        mChatList = new ArrayList<>();
        mAdapter = new ChatAdapter(this, mChatList);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView_lst_chat);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        if (!verificaConexao(this)){
            Snackbar.make(mRecyclerView, "SEM CONEXAO COM INTERNET", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        mLoaderMAnager = getSupportLoaderManager();
        mLoaderMAnager.initLoader(LOADER_CARREGA_CHAT_SOLICITACAO, null, this);

        ChatTimerTask chatTimerTask = new ChatTimerTask();
        Timer chatTimer = new Timer();
        chatTimer.schedule(chatTimerTask,10000, 5000);

    }


    public void btnEnviarOnClick(View view) {
        String datahora = DataHoraSecNow();
        String texto = mEdtTexto.getText().toString();

        mChat = null;
        mChat = new Chat();

        mChat.setSolicitacao_usuario_id(mSolicitacao.getUsuario_id());
        mChat.setSolicitacao_id(mSolicitacao.getSolicitacao_id());
        mChat.setUsuario_id(mUsuario.getId_servidor());
        mChat.setData_hora(datahora);
        mChat.setNome(mUsuario.getNome());
        mChat.setTexto(texto);

        mLoaderMAnager.restartLoader(LOADER_INSERT_CHAT_SOLICITACAO, null, this);

    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (!verificaConexao(this)) {
            Snackbar.make(mEdtTexto, "SEM CONEXÃO COM INTERNET", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return null;
        }

        if (id == LOADER_CARREGA_CHAT_SOLICITACAO){
            return new CarregaChatSolicitacaoTask(this, id, mSolicitacao.getUsuario_id(), mSolicitacao.getSolicitacao_id() );
        }
        else if (id == LOADER_INSERT_CHAT_SOLICITACAO){
            return new InsertChatSolicitacaoTask(this, id, mChat.getSolicitacao_usuario_id(), mChat.getSolicitacao_id(), mChat.getUsuario_id(), mChat.getData_hora(), mChat.getNome(), mChat.getTexto() );
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        if (loader.getId() == LOADER_CARREGA_CHAT_SOLICITACAO ) {
            if(data != null){
                Integer iSize = mChatList.size();
                mChatList.clear();
                List<Chat> lst = (List) data;
                if (lst.size() > 0) {
                    mChatList.addAll(lst);

                    if(mChatList.size() > iSize){
                        mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount());
                    }

                }
            }
            else
            {
                Toast.makeText(ChatActivity.this, "SEM COMUNICAÇÃO COM O SERVIDOR", Toast.LENGTH_SHORT).show();
                mLoaderMAnager.destroyLoader(LOADER_CARREGA_CHAT_SOLICITACAO);
            }

            mAdapter.notifyDataSetChanged();

        }

        else if (loader.getId() == LOADER_INSERT_CHAT_SOLICITACAO ) {
            if(data != null){
                mChatList.clear();
                List<Chat> lst = (List) data;
                if (lst.size() > 0) {
                    mChatList.addAll(lst);
                }
                mEdtTexto.setText("");
                mEdtTexto.setFocusable(true);
                mLoaderMAnager.destroyLoader(LOADER_INSERT_CHAT_SOLICITACAO);
            }
            else
            {
                Toast.makeText(ChatActivity.this, "SEM COMUNICAÇÃO COM O SERVIDOR", Toast.LENGTH_SHORT).show();
                mLoaderMAnager.destroyLoader(LOADER_INSERT_CHAT_SOLICITACAO);
            }

            mAdapter.notifyDataSetChanged();
            mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount());

        }


    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    class ChatTimerTask extends TimerTask{

        @Override
        public void run() {
            mLoaderMAnager.restartLoader(LOADER_CARREGA_CHAT_SOLICITACAO, null, ChatActivity.this);
        }
    }


}
