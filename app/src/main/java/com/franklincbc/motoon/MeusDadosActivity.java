package com.franklincbc.motoon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.franklincbc.motoon.Utils.Constants;
import com.franklincbc.motoon.http.UsuarioSalvarDadosUsuarioTask;
import com.franklincbc.motoon.model.Usuario;

public class MeusDadosActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    public static final int LOADER_SALVAR_DADOS_USUARIO = 0;
    public static final int RC_VALIDAR_CELULAR = 0;

    private Usuario mUsuario;
    private EditText mEditTextID;
    private EditText mEditTextEmail;
    private EditText mEditTextNomeUsuario;
    private EditText mEditTextCelular;
    private Spinner mSpinnerSexo;

    LinearLayout viewMototaxista;

    private EditText mEditCpf;
    private DatePicker mEditDataNasc;
    private EditText mEditCnh;
    private DatePicker mEditCnhValidade;
    private EditText mEditCrlv;
    private EditText mEditPlacaMoto;
    Button btnSalvar;

    boolean bNumCelularValidado = true;
    String mCelularValidado = "";



    private Spinner mSpinnerTipoVeiculo;

    LoaderManager mLoaderManager;

    DatePicker.OnDateChangedListener onDateChangedListener = new DatePicker.OnDateChangedListener() {
        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            String Ano = String.valueOf(year);
            String Mes = String.valueOf(monthOfYear + 1);
            String Dia = String.valueOf(dayOfMonth);
            if(Dia.length() == 1){
                Dia = "0"+Dia;
            }
            if(Mes.length() == 1){
                Mes = "0"+Mes;
            }

            if(view.getId() == R.id.activity_meus_dados_data_nasc){
                mUsuario.setData_nasc(Dia + "/" + Mes + "/" + Ano );
            }
            else if(view.getId() == R.id.activity_meus_dados_cnh_validade)
            {
                mUsuario.setCnh_validade(Dia + "/" + Mes + "/" + Ano );
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_dados);
        setTitle("MEUS DADOS");

        if(savedInstanceState==null) {
            mUsuario = (Usuario) getIntent().getSerializableExtra(Constants.USUARIO_EXTRA);
        }
        else
        {
            mUsuario = (Usuario) savedInstanceState.getSerializable(Constants.USUARIO_EXTRA);
        }

        mEditTextID             = (EditText)findViewById(R.id.activity_meus_dados_edt_meuID);
        mEditTextEmail          = (EditText)findViewById(R.id.activity_meus_dados_edt_email);
        mEditTextNomeUsuario    = (EditText)findViewById(R.id.activity_meus_dados_edt_nome_usuario);
        mEditTextCelular        = (EditText)findViewById(R.id.activity_meus_dados_edt_celular);
        mSpinnerSexo            = (Spinner) findViewById(R.id.activity_meus_dados_spinnerSexo);
        mEditDataNasc           = (DatePicker) findViewById(R.id.activity_meus_dados_data_nasc);

        viewMototaxista         = (LinearLayout)findViewById(R.id.activity_meus_Dados_viewMototaxista);
        mEditCpf                = (EditText) findViewById(R.id.activity_meus_dados_cpf);
        mEditCnh                = (EditText) findViewById(R.id.activity_meus_dados_cnh);
        mEditCnhValidade        = (DatePicker) findViewById(R.id.activity_meus_dados_cnh_validade);
        mEditCrlv               = (EditText) findViewById(R.id.activity_meus_dados_crlv);
        mEditPlacaMoto          = (EditText) findViewById(R.id.activity_meus_dados_placa_moto);
        mSpinnerTipoVeiculo     = (Spinner) findViewById(R.id.activity_meus_dados_spinnerTipoVeiculo);

        btnSalvar  = (Button)findViewById(R.id.activity_meus_dados_btnSalvar);


        mEditTextID.setText(mUsuario.getId_servidor().toString());
        mEditTextNomeUsuario.setText(mUsuario.getNome());
        mEditTextEmail.setText(mUsuario.getEmail());
        mEditTextCelular.setText(mUsuario.getCelular());

        if(mUsuario.getCelular()== null || mUsuario.getCelular().equals("")){
            bNumCelularValidado = false;
            mCelularValidado = "";
        }
        else
        {
            mCelularValidado = mUsuario.getCelular().toString();
            bNumCelularValidado = true;
        }

        if(mUsuario.getSexo().equals("M")){
            mSpinnerSexo.setSelection(1);
        } else if(mUsuario.getSexo().equals("F")){
            mSpinnerSexo.setSelection(2);
        } else {
            mSpinnerSexo.setSelection(0); //Não Informado
        }

        mEditCpf.setText(mUsuario.getCpf());

        if(mUsuario.getData_nasc() != null && !mUsuario.getData_nasc().equals("") ) {
            String data = mUsuario.getData_nasc();
            String ano = data.substring(6,10);
            String mes = data.substring(3,5);
            String dia = data.substring(0,2);

            mEditDataNasc.init(Integer.valueOf(ano),
                               Integer.valueOf(mes) -1,
                               Integer.valueOf(dia),
                               onDateChangedListener);
        }
        else
        {
            mEditDataNasc.init(1998,01,01, onDateChangedListener);
        }
        mEditCnh.setText(mUsuario.getCnh());

        if(mUsuario.getCnh_validade() != null && !mUsuario.getCnh_validade().equals("")) {
            String dataValidade = mUsuario.getCnh_validade();
            String ano = dataValidade.substring(6,10);
            String mes = dataValidade.substring(3,5);
            String dia = dataValidade.substring(0,2);

            mEditCnhValidade.init(Integer.valueOf(ano),
                    Integer.valueOf(mes) - 1,
                    Integer.valueOf(dia),
                    onDateChangedListener);
        }
        else
        {
            mEditCnhValidade.init(2020,01,01, onDateChangedListener);
        }
        mEditCnhValidade.setEnabled(false);

        mEditCrlv.setText(mUsuario.getCrlv());
        mEditPlacaMoto.setText(mUsuario.getPlaca_moto());

        if(mUsuario.getTipo_veiculo().equals("M")){
            mSpinnerTipoVeiculo.setSelection(0);
        } else if(mUsuario.getTipo_veiculo().equals("T")){
            mSpinnerTipoVeiculo.setSelection(1);
        } else {
            mSpinnerTipoVeiculo.setSelection(0); //Padrão ser Moto
            mUsuario.setTipo_veiculo("M");
        }

        mLoaderManager = getSupportLoaderManager();



        //Se for mototaxista visualiza demais campo
        if(mUsuario.getSn_mototaxi().equals("S")){
            viewMototaxista.setVisibility(View.VISIBLE);
        }
        else
        {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1);
            viewMototaxista.setLayoutParams(params);
            viewMototaxista.setVisibility(View.INVISIBLE);
        }


    }

    public void btnSalvarOnClick(View view) {

        if(!bNumCelularValidado){
            Toast.makeText(this, "Número do Celular precisa estar validado", Toast.LENGTH_LONG).show();
            return;
        }
        if(!mEditTextCelular.getText().toString().equals(mCelularValidado)){
            Toast.makeText(this, "Celular não corresponde ao número que foi validado("+mCelularValidado+")", Toast.LENGTH_LONG).show();
            return;
        }

        mUsuario.setNome(mEditTextNomeUsuario.getText().toString());
        mUsuario.setCelular(mEditTextCelular.getText().toString());
        String sSexo = mSpinnerSexo.getSelectedItem().toString();

        if (sSexo.equals("Masculino")){
            mUsuario.setSexo("M");
        } else if (sSexo.equals("Feminino")){
            mUsuario.setSexo("F");
        } else {
            mUsuario.setSexo("");
        }

        //Valida campos obrigatórios
        if(mUsuario.getNome().equals("")){
            mEditTextNomeUsuario.setError("Campo Obrigatório");
            return;
        }
        else if (mUsuario.getCelular().equals("")){
            mEditTextCelular.setError("Campo Obrigatório");
            return;
        }
        else if (sSexo.equals("")){
            mSpinnerSexo.setFocusable(true);
            return;
        }

        mUsuario.setCpf(mEditCpf.getText().toString());
        //mUsuario.setData_nasc(Util.StringToDate(mEditDataNasc.getText().toString(), "dd/MM/yyyy"));
        String sDataNasc = mUsuario.getData_nasc();
        mUsuario.setCnh(mEditCnh.getText().toString());
        //mUsuario.setCnh_validade(Util.StringToDate(mEditCnhValidade.getText().toString(), "dd/MM/yyyy"));
        String sCnh_Validade = mUsuario.getCnh_validade();
        mUsuario.setCrlv(mEditCrlv.getText().toString());
        mUsuario.setPlaca_moto(mEditPlacaMoto.getText().toString());

        String sTipoVeiculo = mSpinnerTipoVeiculo.getSelectedItem().toString();
        if (sTipoVeiculo.equals("MOTO")){
            mUsuario.setTipo_veiculo("M");
        } else if (sTipoVeiculo.equals("TUK TUK")){
            mUsuario.setTipo_veiculo("T");
        } else {
            mUsuario.setTipo_veiculo("M");
        }

        Bundle params = new Bundle();
        params.putSerializable(Constants.USUARIO_EXTRA, mUsuario);

        mLoaderManager.initLoader(LOADER_SALVAR_DADOS_USUARIO, params, MeusDadosActivity.this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (id == LOADER_SALVAR_DADOS_USUARIO){
            return new UsuarioSalvarDadosUsuarioTask(MeusDadosActivity.this, id, args );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

        if (data != null ) {
            if (loader.getId() == LOADER_SALVAR_DADOS_USUARIO) {
                Integer rowsAffected = (Integer) data;
                if(rowsAffected == 1){
                    Toast.makeText(MeusDadosActivity.this, "Dados Atualizados", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Toast.makeText(MeusDadosActivity.this, "Problema para atualizar as informações", Toast.LENGTH_SHORT).show();
                }

            }
            mLoaderManager.destroyLoader(LOADER_SALVAR_DADOS_USUARIO);
        }
        else
        {
            Toast.makeText(MeusDadosActivity.this, "SEM COMUNICAÇÃO COM O SERVIDOR", Toast.LENGTH_SHORT).show();
            mLoaderManager.destroyLoader(LOADER_SALVAR_DADOS_USUARIO);
        }

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Constants.USUARIO_EXTRA, mUsuario);
    }

    public void btnValidarCelularOnClick(View view) {
        if (bNumCelularValidado && mEditTextCelular.getText().toString().equals(mCelularValidado)){
            Toast.makeText(this, "O número " + mCelularValidado + " já foi validado!", Toast.LENGTH_SHORT).show();
            return;
        }

        //Chama activity para validar celular
        String numero = mEditTextCelular.getText().toString();
        if(numero.length() != 11){
            Toast.makeText(this, "Número de Celular inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent it = new Intent(this, ValidarCelularSMSActivity.class);
        it.putExtra("numero", numero);
        startActivityForResult(it,RC_VALIDAR_CELULAR);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == RC_VALIDAR_CELULAR){
                bNumCelularValidado = true;
                mCelularValidado = mEditTextCelular.getText().toString();
                Toast.makeText(this, "Celular Validado", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            bNumCelularValidado = false;
            mCelularValidado = "";
            mEditTextCelular.setText("");
            Toast.makeText(this, "Celular não Validado", Toast.LENGTH_SHORT).show();
        }

    }
}
