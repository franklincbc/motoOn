package com.franklincbc.motoon;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.franklincbc.motoon.Service.DataService;
import com.franklincbc.motoon.Utils.Constants;
import com.franklincbc.motoon.http.RetornaQtdMototaxiCidadeTask;
import com.franklincbc.motoon.http.SolicitacaoRegistraSolicitacaoTask;
import com.franklincbc.motoon.model.Solicitacao;
import com.franklincbc.motoon.model.Usuario;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.franklincbc.motoon.Utils.Util.verificaConexao;

public class SolicitarMotoActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks {

    public static final Integer LOADER_REGISTRA_SOLICITACAO = 0;
    public static final int LOADER_RETORNA_QUANT_MOTOTAXI_CIDADE = 1;

    Solicitacao solicitacao = null;
    Usuario mUsuario;
    LoaderManager mLoaderManager;
    DataResultReceiver mDataResultReceiver;

    EditText edtSolicitante;
    EditText edtOrigem;
    EditText edtDestino;
    EditText edtReferencia;
    EditText edtInfAdicional;
    TextView textViewPrecoPresumido;

    RadioButton rbMoto;
    ImageView imgTipoVeiculo;

    Button btnConfirmar;
    Integer qtdMototaxiCid = 0;

    String mFaixaPrecoPresumidoMoto;
    String mFaixaPrecoPresumidoTukTuk;
    String mFaixaPrecoPresumidoCarro;

    private AlertDialog alerta;


    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar_moto);

        solicitacao = new Solicitacao();
        Intent it = getIntent();

        mUsuario = (Usuario)it.getSerializableExtra(Constants.USUARIO_EXTRA);

        solicitacao.setSolicitante(mUsuario.getNome());
        solicitacao.setUsuario_id( mUsuario.getId_servidor() );
        solicitacao.setLatitude( it.getDoubleExtra("LATITUDE", 0.000000) );
        solicitacao.setLongitude( it.getDoubleExtra("LONGITUDE", 0.000000) );
        solicitacao.setLocal_origem(it.getStringExtra("ORIGEM"));
        solicitacao.setLocal_destino(it.getStringExtra("DESTINO"));
        solicitacao.setStatus_sol("A"); //Aguardando atendimento
        solicitacao.setCidade(it.getStringExtra("CIDADE"));
        solicitacao.setBairro(it.getStringExtra("BAIRRO"));

        solicitacao.setLatitude_destino(it.getDoubleExtra("LATITUDE_DESTINO", 0.000000));
        solicitacao.setLongitude_destino(it.getDoubleExtra("LONGITUDE_DESTINO", 0.000000));
        solicitacao.setDistancia_presumida(it.getDoubleExtra("DISTANCIA_PRESUMIDA", 0.000));

        mFaixaPrecoPresumidoMoto = it.getStringExtra("FAIXA_PRECO_PRESUMIDO_MOTO");
        mFaixaPrecoPresumidoTukTuk = it.getStringExtra("FAIXA_PRECO_PRESUMIDO_TUKTUK");
        mFaixaPrecoPresumidoCarro = it.getStringExtra("FAIXA_PRECO_PRESUMIDO_CARRO");

        solicitacao.setFaixa_preco_presumido(mFaixaPrecoPresumidoMoto);

        edtSolicitante = (EditText)findViewById(R.id.activity_solicitar_moto_editText_solicitante);
        edtOrigem = (EditText)findViewById(R.id.activity_solicitar_moto_editText_endOrigem);
        edtDestino = (EditText)findViewById(R.id.activity_solicitar_moto_editText_Destino);
        edtDestino.setEnabled(false);
        edtReferencia = (EditText)findViewById(R.id.activity_solicitar_moto_editText_referencia);
        edtInfAdicional = (EditText)findViewById(R.id.activity_solicitar_moto_editText_inf_adicional);
        btnConfirmar = (Button)findViewById(R.id.activity_solicitar_moto_btn_confirmar);

        rbMoto = (RadioButton)findViewById(R.id.activity_solicitar_moto_rbMoto);
        rbMoto.setChecked(true);
        solicitacao.setTipo_veiculo("M");
        imgTipoVeiculo = (ImageView)findViewById(R.id.activity_solicitar_moto_imgTipoVeiculo);
        imgTipoVeiculo.setImageResource(R.mipmap.ic_motoon_1);

        textViewPrecoPresumido = (TextView)findViewById(R.id.activity_solicitar_moto_textview_preco_presumido);
        textViewPrecoPresumido.setText(solicitacao.getFaixa_preco_presumido());

        edtSolicitante.setText(solicitacao.getSolicitante());

        if(solicitacao.getLocal_origem() != null && solicitacao.getLocal_origem() != ""){
            edtOrigem.setText(solicitacao.getLocal_origem());
        }
        if(solicitacao.getLocal_destino() != null && solicitacao.getLocal_destino() != ""){
            edtDestino.setText(solicitacao.getLocal_destino());
        }

        mLoaderManager = getSupportLoaderManager();
        mDataResultReceiver = new DataResultReceiver(null);

        //Carregar Quantidade de Mototaxi na Cidade
        if (mUsuario.getSn_mototaxi()!= null && mUsuario.getSn_mototaxi().equals("N")) {
            Bundle params = new Bundle();
            params.putString("cidade", mUsuario.getCidade());
            mLoaderManager.initLoader(LOADER_RETORNA_QUANT_MOTOTAXI_CIDADE, params, SolicitarMotoActivity.this);
        }

    }

    public void btn_confirmarOnClick(View view) {
        if (!verificaConexao(this)){
            Snackbar.make(view, "SEM CONEXAO COM INTERNET", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        if (qtdMototaxiCid == 0){
            Toast.makeText(SolicitarMotoActivity.this, "Em "+ mUsuario.getCidade()+ " não existe Mototaxista Credenciado", Toast.LENGTH_SHORT).show();
            return;
        }

        alerta_bloqueio();


    }

    private void RegistraSolicitacaoWeb(){

        //Inserir na WEB
        Bundle params = new Bundle();
        params.putSerializable(Constants.SOLICITACAO_EXTRA, solicitacao);
        mLoaderManager.initLoader(LOADER_REGISTRA_SOLICITACAO, params, SolicitarMotoActivity.this);

    }

    private void RegistraSolicitacaoDataService() {
        Intent intent = new Intent(this, DataService.class);
        intent.putExtra(Constants.RECEIVER, mDataResultReceiver);
        intent.putExtra(Constants.CODE_TABLE_EXTRA, Constants.CODE_TABLE_SOLICITACAO);
        intent.putExtra(Constants.CODE_OPERATION_EXTRA, Constants.OPERATION_INSERT);
        intent.putExtra(Constants.SOLICITACAO_EXTRA, solicitacao);
        startService(intent);
    }


    private void alerta_bloqueio() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);//Cria o gerador do AlertDialog
        builder.setTitle("Atenção");//define o titulo
        builder.setMessage("Só Solicite um motociclista se precisar! Evite que sua conta seja bloqueada.");//define a mensagem

        //define um botão como positivo para adicionar a origem
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                mDialog = ProgressDialog.show(SolicitarMotoActivity.this,"Espere por favor...", "Registrando sua solicitação...", true);
                solicitacao.setSolicitante(edtSolicitante.getText().toString().trim());
                solicitacao.setLocal_origem(edtOrigem.getText().toString().trim());
                solicitacao.setPonto_referencia(edtReferencia.getText().toString().trim());
                solicitacao.setLocal_destino(edtDestino.getText().toString().trim());
                solicitacao.setInformacao_adicional(edtInfAdicional.getText().toString().trim());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm"); //hh tras hora format 12 - HH hora formato 24
                String format = sdf.format(Calendar.getInstance().getTime());
                solicitacao.setData_solicitacao(format);
                RegistraSolicitacaoWeb();

            }
        });

        //Usa  o botao de negative para adicionar o destino. Gambi
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                return;
            }
        });

        alerta = builder.create();//cria o AlertDialog
        alerta.show();//Exibe
    }



    //********************** LOADER - REgistrar na WEB a solicitação
    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (id == LOADER_REGISTRA_SOLICITACAO) {
            return new SolicitacaoRegistraSolicitacaoTask(SolicitarMotoActivity.this, id, args);
        }else if (id == LOADER_RETORNA_QUANT_MOTOTAXI_CIDADE){
            return new RetornaQtdMototaxiCidadeTask(SolicitarMotoActivity.this, id, args );
        }else{
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        if (data != null ){

            if(loader.getId() == LOADER_REGISTRA_SOLICITACAO) {
                Long id = (Long) data;
                solicitacao.setSolicitacao_id(Integer.valueOf(String.valueOf(id)));
                RegistraSolicitacaoDataService();
            }
            else
            if (loader.getId() == LOADER_RETORNA_QUANT_MOTOTAXI_CIDADE){
                Integer quant = (Integer) data;
                qtdMototaxiCid = quant;
                if (qtdMototaxiCid == 0){
                    Toast.makeText(SolicitarMotoActivity.this, "Em "+ mUsuario.getCidade()+ " não existe Mototaxista credenciado", Toast.LENGTH_LONG).show();
                }

            }

        }
        else
        {
            if(loader.getId() == LOADER_REGISTRA_SOLICITACAO) {
                Toast.makeText(SolicitarMotoActivity.this, "SEM COMUNICAÇÃO COM O SERVIDOR", Toast.LENGTH_SHORT).show();
                mLoaderManager.destroyLoader(LOADER_REGISTRA_SOLICITACAO);
            }
            else if (loader.getId() == LOADER_RETORNA_QUANT_MOTOTAXI_CIDADE){
                Toast.makeText(SolicitarMotoActivity.this, "SEM COMUNICAÇÃO COM O SERVIDOR", Toast.LENGTH_SHORT).show();
                mLoaderManager.destroyLoader(LOADER_RETORNA_QUANT_MOTOTAXI_CIDADE);
            }

        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    public void onRadioButtonClick(View view) {
        boolean checked = ((RadioButton)view).isChecked();
        switch(view.getId()){
            case R.id.activity_solicitar_moto_rbMoto:
                if(checked){
                    solicitacao.setTipo_veiculo("M");
                    imgTipoVeiculo.setImageResource(R.mipmap.ic_motoon_1);
                    solicitacao.setFaixa_preco_presumido(mFaixaPrecoPresumidoMoto);
                    textViewPrecoPresumido.setText(solicitacao.getFaixa_preco_presumido());
                }
                break;

            case R.id.activity_solicitar_moto_rbTucTuc:
                if(checked){
                    solicitacao.setTipo_veiculo("T");
                    imgTipoVeiculo.setImageResource(R.mipmap.ic_tuctuc);
                    solicitacao.setFaixa_preco_presumido(mFaixaPrecoPresumidoTukTuk);
                    textViewPrecoPresumido.setText(solicitacao.getFaixa_preco_presumido());
                }
                break;
        }

    }

    //Loader FIM ******************


    class DataResultReceiver extends ResultReceiver {
        public DataResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(final int resultCode, final Bundle resultData) {
            if (resultCode == Constants.SUCCESS_RESULT) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                        if (resultData.getInt(Constants.CODE_OPERATION_EXTRA) == Constants.OPERATION_INSERT) {
                            Toast.makeText(SolicitarMotoActivity.this, getString(R.string.solicitacao_realizada_sucesso), Toast.LENGTH_SHORT).show();
                            Intent it = new Intent();
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                        if (resultData.getInt(Constants.CODE_OPERATION_EXTRA) == Constants.OPERATION_INSERT) {
                            Toast.makeText(SolicitarMotoActivity.this, resultData.getString(Constants.RESULT_MESSAGE), Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }
                });
            }
        }


    }



}
