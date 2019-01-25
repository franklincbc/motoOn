package com.franklincbc.motoon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.franklincbc.motoon.Utils.Constants;
import com.franklincbc.motoon.model.Usuario;

public class AguardandoDocActivity extends AppCompatActivity {

    TextView txtView;
    Usuario mUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aguardando_doc);

        mUsuario = (Usuario) getIntent().getSerializableExtra(Constants.USUARIO_EXTRA);
        txtView = (TextView)findViewById(R.id.activity_aguardando_doc_txtObs);
        txtView.setText("OBS: Se ainda não enviou seus DOCUMENTOS, envie o mais rápido possível para: cadastro@motoonbr.com.br\n\nCom as seguintes especificações:\nAssunto do email: ID: "+mUsuario.getId_servidor()+" - NOME: " + mUsuario.getNome());

    }
}
