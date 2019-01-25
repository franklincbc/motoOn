package com.franklincbc.motoon;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.franklincbc.motoon.Utils.Constants;
import com.franklincbc.motoon.http.AceitouTermoAdesaoTask;
import com.franklincbc.motoon.model.Usuario;

public class AceitouTermoActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    public static final int LOADER_ACEITOU_TERMO_ADESAO = 0;
    Usuario mUsuario;
    LoaderManager mLoaderManager;
    boolean bConfirmado;

    TextView txtViewSubTitulo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aceitou_termo);

        mUsuario = (Usuario) getIntent().getSerializableExtra(Constants.USUARIO_EXTRA);

        mLoaderManager = getSupportLoaderManager();

        if(savedInstanceState==null) {
            bConfirmado = false;
        }else
        {
            bConfirmado = savedInstanceState.getBoolean("confirmado");
        }

        txtViewSubTitulo = (TextView)findViewById(R.id.activity_aceitou_termo_txtSubTexTitulo1);
        txtViewSubTitulo.setText("Envie seus documentos para o email:\ncadastro@motoonbr.com.br\n\nCom as seguintes especificações:\n\nAssunto do email: ID: "+mUsuario.getId_servidor() +" - NOME: "+mUsuario.getNome());

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("confirmado",bConfirmado);
    }

    public void btn_confirmarOnClick(View view) {
        if(bConfirmado){
            Toast.makeText(AceitouTermoActivity.this, "Sua solicitação já foi enviada, aguardamos o envio dos seus documentos", Toast.LENGTH_LONG).show();
            return;
        }
        Bundle params = new Bundle();
        params.putSerializable(Constants.USUARIO_EXTRA, mUsuario);
        mLoaderManager.initLoader(LOADER_ACEITOU_TERMO_ADESAO, params, AceitouTermoActivity.this);
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ACEITOU_TERMO_ADESAO){
            return new AceitouTermoAdesaoTask(AceitouTermoActivity.this, id, args );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        if (data != null ) {
            if (loader.getId() == LOADER_ACEITOU_TERMO_ADESAO) {
                String retorno = (String) data;
                if(retorno.equals("OK")){
                    Toast.makeText(AceitouTermoActivity.this, "Registramos sua solicitação.\nAguardamos o envio do seus documentos para validação", Toast.LENGTH_LONG).show();
                    bConfirmado = true;
                }
                if(retorno.equals("SOLICITADO")){
                    Toast.makeText(AceitouTermoActivity.this, "Sua solicitação já foi enviada", Toast.LENGTH_LONG).show();
                    bConfirmado = true;
                }
                else
                {
                    Toast.makeText(AceitouTermoActivity.this, "Não foi possível enviar sua solicitação", Toast.LENGTH_LONG).show();
                    bConfirmado = false;
                }

            }
        }
        else
        {
            bConfirmado = false;
            Toast.makeText(AceitouTermoActivity.this, "SEM COMUNICAÇÃO COM O SERVIDOR", Toast.LENGTH_LONG).show();
            mLoaderManager.destroyLoader(LOADER_ACEITOU_TERMO_ADESAO);
        }

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

}
