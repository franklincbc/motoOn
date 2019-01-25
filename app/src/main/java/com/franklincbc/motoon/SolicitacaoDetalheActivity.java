package com.franklincbc.motoon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.franklincbc.motoon.Utils.Constants;
import com.franklincbc.motoon.model.Solicitacao;
import com.franklincbc.motoon.model.Usuario;

import static com.franklincbc.motoon.R.id.solicitacaoDetalhe_button_Chat;

public class SolicitacaoDetalheActivity extends AppCompatActivity {

    public static final String NAO_DISPONIVEL = " indisponível";
    public static final String EXTRA_SOLICITACAO = "solicitacao";
    TextView txtDataSolicitacao;
    TextView txtSolicitante;
    Button btnChat;

    TextView txtLocalOrigem;
    TextView txtPontoReferencia;
    TextView txtLocalDestino;
    TextView txtInformacaoAdicional;
    TextView txtMotoTaxista;
    TextView txtStatus;
    ImageView imgSolicitante;
    ImageView imgMototaxista;

    TextView txtCelularSolic;
    TextView txtCelularMototaxi;
    TextView txtPlacaMoto;

    Solicitacao mSolicitacao;
    Usuario mUsuario;

    boolean bEmAtendimento = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitacao_detalhe);

        setTitle("DETALHE");
        txtDataSolicitacao = (TextView)findViewById(R.id.SolicitacaoDetalhe_dataSolicitacao);
        txtSolicitante = (TextView)findViewById(R.id.SolicitacaoDetalhe_solicitante);

        btnChat = (Button)findViewById(solicitacaoDetalhe_button_Chat);

        txtLocalOrigem = (TextView)findViewById(R.id.SolicitacaoDetalhe_origem);
        txtPontoReferencia = (TextView)findViewById(R.id.SolicitacaoDetalhe_pontoReferencia);
        txtLocalDestino = (TextView)findViewById(R.id.SolicitacaoDetalhe_destino);
        txtInformacaoAdicional = (TextView)findViewById(R.id.SolicitacaoDetalhe_informacaoAdicional);
        txtMotoTaxista = (TextView)findViewById(R.id.SolicitacaoDetalhe_mototaxiID);
        txtStatus = (TextView)findViewById(R.id.SolicitacaoDetalhe_status);

        txtCelularSolic = (TextView)findViewById(R.id.SolicitacaoDetalhe_celular_solicitante);
        txtCelularMototaxi = (TextView)findViewById(R.id.SolicitacaoDetalhe_celular_mototaxista);
        txtPlacaMoto = (TextView)findViewById(R.id.SolicitacaoDetalhe_placa_moto);

        imgSolicitante = (ImageView)findViewById(R.id.solicitacaoDetalhe_imgSolicitante);
        imgMototaxista = (ImageView)findViewById(R.id.solicitacaoDetalhe_imgMototaxista);


        if(savedInstanceState == null){
            mSolicitacao = (Solicitacao)getIntent().getSerializableExtra(EXTRA_SOLICITACAO);
            mUsuario = (Usuario) getIntent().getSerializableExtra(Constants.USUARIO_EXTRA);
        }
        else
        {
            mSolicitacao = (Solicitacao)savedInstanceState.getSerializable(EXTRA_SOLICITACAO);
            mUsuario = (Usuario) savedInstanceState.getSerializable(Constants.USUARIO_EXTRA);
        }

        txtDataSolicitacao.setText(mSolicitacao.getData_solicitacao());
        txtSolicitante.setText(mSolicitacao.getSolicitante());
        txtLocalOrigem.setText(mSolicitacao.getLocal_origem());

        txtPontoReferencia.setText(mSolicitacao.getPonto_referencia());
        if (txtPontoReferencia.getText().toString().equals("")){
            txtPontoReferencia.setText("Não informado");
        }

        txtLocalDestino.setText(mSolicitacao.getLocal_destino());
        if (txtLocalDestino.getText().toString().equals("")){
            txtLocalDestino.setText("Não informado");
        }

        txtInformacaoAdicional.setText(mSolicitacao.getInformacao_adicional());
        if (txtInformacaoAdicional.getText().toString().equals("")){
            txtInformacaoAdicional.setText("Não informado");
        }

        txtMotoTaxista.setText(String.valueOf(mSolicitacao.getMototaxi_nome()));
        txtCelularSolic.setText(mSolicitacao.getCelular_solicitante());
        txtCelularMototaxi.setText(mSolicitacao.getCelular_mototaxista());
        txtPlacaMoto.setText(mSolicitacao.getPlaca_moto());

        String status_sol;
        if (mSolicitacao.getStatus_sol().equals("A") ){
            status_sol = getString(R.string.status_aguardando_atendimento);
            bEmAtendimento = false; }
        else
        if (mSolicitacao.getStatus_sol().equals("E")){ status_sol = getString(R.string.status_em_atendimento); bEmAtendimento = true;} else
        if (mSolicitacao.getStatus_sol().equals("I")){ status_sol = getString(R.string.status_corrida_iniciada); bEmAtendimento = true;} else
        if (mSolicitacao.getStatus_sol().equals("F")){ status_sol = getString(R.string.status_finalizado); bEmAtendimento = false;} else
        if (mSolicitacao.getStatus_sol().equals("C")){ status_sol = getString(R.string.status_cancelado); bEmAtendimento = false;} else
        {status_sol = getString(R.string.status_desconhecido);}
        txtStatus.setText(status_sol);

        //Seta foto Solicitante
        if(mSolicitacao.getUrl_photo_solicitante().equals("")){
            Glide.with(SolicitacaoDetalheActivity.this)
                    .load(R.drawable.semfoto)
                    .into(imgSolicitante);
        }
        else {
            Glide.with(SolicitacaoDetalheActivity.this)
                    .load(mSolicitacao.getUrl_photo_solicitante())
                    .into(imgSolicitante);
        }

        //Seta foto Mototaxista
        if(mSolicitacao.getUrl_photo_mototaxista().equals("")){
            Glide.with(SolicitacaoDetalheActivity.this)
                    .load(R.drawable.semfoto)
                    .into(imgMototaxista);
        }
        else {
            Glide.with(SolicitacaoDetalheActivity.this)
                    .load(mSolicitacao.getUrl_photo_mototaxista())
                    .into(imgMototaxista);
        }

        OcultaInformacoes();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXTRA_SOLICITACAO, mSolicitacao);
        outState.putSerializable(Constants.USUARIO_EXTRA, mUsuario);
    }

    public void OcultaInformacoes(){
        if (bEmAtendimento){
            imgMototaxista.setVisibility(View.VISIBLE);
            txtMotoTaxista.setVisibility(View.VISIBLE);
            txtCelularMototaxi.setVisibility(View.VISIBLE);
            txtPlacaMoto.setVisibility(View.VISIBLE);
            btnChat.setEnabled(true);

        }
        else
        {
            txtLocalOrigem.setText("Endereço" + NAO_DISPONIVEL);
            txtCelularSolic.setText("Celular" + NAO_DISPONIVEL);

            imgMototaxista.setVisibility(View.INVISIBLE);
            txtMotoTaxista.setVisibility(View.INVISIBLE);
            txtCelularMototaxi.setVisibility(View.INVISIBLE);
            txtPlacaMoto.setVisibility(View.INVISIBLE);

            btnChat.setEnabled(false);

        }
    }


    public void btnChatOnClick(View view) {
        Intent it = new Intent(this, ChatActivity.class);
        it.putExtra(Constants.SOLICITACAO_EXTRA, mSolicitacao);
        it.putExtra(Constants.USUARIO_EXTRA, mUsuario);
        startActivity(it);
    }

}
