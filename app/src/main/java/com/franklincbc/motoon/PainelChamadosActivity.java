package com.franklincbc.motoon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.franklincbc.motoon.Utils.Constants;
import com.franklincbc.motoon.adapter.PainelSolicitacaoAdapter;
import com.franklincbc.motoon.http.AtenderChamadoTask;
import com.franklincbc.motoon.http.CarregaSaldoCreditoMototaxistaTask;
import com.franklincbc.motoon.http.DesistirChamadoTask;
import com.franklincbc.motoon.http.FinalizarChamadoTask;
import com.franklincbc.motoon.http.IniciarCorridaChamadoTask;
import com.franklincbc.motoon.http.PainelChamadosTask;
import com.franklincbc.motoon.http.UsuarioAtualizaLocalizacaoUsuarioTask;
import com.franklincbc.motoon.model.Solicitacao;
import com.franklincbc.motoon.model.Usuario;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.franklincbc.motoon.Utils.Util.DataHoraNow;
import static com.franklincbc.motoon.Utils.Util.verificaConexao;

public class PainelChamadosActivity
        extends AppCompatActivity
        implements  OnSolicitacaoClickListener,
                    OnButtonSolicitacaoClickListener,
                    GoogleApiClient.ConnectionCallbacks,
                    GoogleApiClient.OnConnectionFailedListener,
                    LocationListener,
                    LoaderManager.LoaderCallbacks{

    private static final int LOADER_SOLICITACAO = 0;
    private static final int LOADER_ATENDER_SOLICITACAO = 1;
    private static final int LOADER_FINALIZAR_SOLICITACAO = 2;
    private static final int LOADER_DESISTIR_SOLICITACAO = 3;
    private static final int LOADER_CARREGA_SALDO_CREDITO = 4;
    public static final int LOADER_ATUALIZA_LOCALIZACAO_USU_PNL = 5;
    private static final int LOADER_INICIAR_CORRIDA_SOLICITACAO = 6;

    public static final String USUARIO_ID_EXTRA = "usuario_id";


    RecyclerView mRecyclerView;
    PainelSolicitacaoAdapter mAdapter;
    List<Solicitacao> mSolicitacoesList;
    LoaderManager mLoaderMAnager;
    Usuario mUsuario;
    Solicitacao mSolicitacao;

    TextView textViewCidade;
    FloatingActionButton fabRefresh;

    private ProgressDialog mDialog;

    Geocoder geocoder;
    private GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painel_chamados);

        mUsuario = (Usuario) getIntent().getSerializableExtra(Constants.USUARIO_EXTRA);

        setTitle("PAINEL DE CHAMADOS");

        textViewCidade = (TextView)findViewById(R.id.activity_painel_chamados_textView_cidade);
        if(mUsuario.getCidade().length() <= 15) {
            textViewCidade.setText(mUsuario.getCidade() + " - SALDO: ");
        }
        else
        {
            textViewCidade.setText(mUsuario.getCidade() + "\nSALDO: ");
        }

        fabRefresh = (FloatingActionButton)findViewById(R.id.activity_painel_chamados_fabRefresh);

        mRecyclerView = (RecyclerView)findViewById(R.id.activity_painel_chamados_recyclerView);

        mSolicitacoesList = new ArrayList<>();
        LatLng latLng = new LatLng(mUsuario.getLatitude_atual(), mUsuario.getLongitude_atual());
        mAdapter = new PainelSolicitacaoAdapter(this, mSolicitacoesList, mUsuario.getMototaxi_id(), latLng);
        mAdapter.setSolicitacaoClickListener(new OnSolicitacaoClickListener() {

            @Override
            public void onSolicitacaoClick(View view, Solicitacao solicitacao, int position) {
                Activity activity = PainelChamadosActivity.this;
                if(activity instanceof OnSolicitacaoClickListener){
                    ((OnSolicitacaoClickListener)activity).onSolicitacaoClick(view, solicitacao, position);
                }
            }
        });

        //Implementa evento do butao Atender
        mAdapter.setButtonSolicitacaoClickListener(new OnButtonSolicitacaoClickListener() {

            @Override
            public void onButtonSolicitacaoClick(View view, Solicitacao solicitacao, int position) {
                //Evento do Botão Atender (Botão Cancelar usa a mesma view (desistir do chamado)
                Bundle args = new Bundle();
                args.putSerializable(Constants.SOLICITACAO_EXTRA, solicitacao);
                //args.putInt("solicitacao_id", solicitacao.getSolicitacao_id());
                Button btnAtenderCancelar = (Button) view.findViewById(R.id.item_lst_painel_btn_atender);
                if(btnAtenderCancelar.getText().equals("ATENDER")) {
                    mLoaderMAnager.restartLoader(LOADER_ATENDER_SOLICITACAO, args, PainelChamadosActivity.this);
                }
                else
                {
                    mLoaderMAnager.restartLoader(LOADER_DESISTIR_SOLICITACAO, args, PainelChamadosActivity.this);
                }

            }
        });

        //Implementa evento do butao Finalizar
        mAdapter.setButtonFinalizarSolicitacaoClickListener(new OnButtonFinalizarSolicitacaoClickListener() {

            @Override
            public void onButtonFinalizarSolicitacaoClick(View view, Solicitacao solicitacao, int position) {

                Bundle args = new Bundle();
                args.putSerializable(Constants.SOLICITACAO_EXTRA, solicitacao);

                Button btnIniciarFinalizar = (Button) view.findViewById(R.id.item_lst_painel_btn_finalizar);
                if(btnIniciarFinalizar.getText().equals("INICIAR")) {
                    mLoaderMAnager.restartLoader(LOADER_INICIAR_CORRIDA_SOLICITACAO, args, PainelChamadosActivity.this);
                }
                else
                {
                    mLoaderMAnager.restartLoader(LOADER_FINALIZAR_SOLICITACAO, args, PainelChamadosActivity.this);
                }

            }
        });



        /*if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                && getResources().getBoolean(R.bool.phone)){
            mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT
                && getResources().getBoolean(R.bool.tablet)){
            mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        }
        else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }*/

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        //No lugar da mensagem, ler da base de dados local e exibir os dados
        if (!verificaConexao(this)){
            Snackbar.make(mRecyclerView, "SEM CONEXAO COM INTERNET", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(AppIndex.API)
                    .build();
        }
        geocoder = new Geocoder(this, Locale.getDefault());

        mDialog = ProgressDialog.show(this,"Espere por favor...", "Carregando dados...", true);

        mLoaderMAnager = getSupportLoaderManager();
        mLoaderMAnager.initLoader(LOADER_SOLICITACAO, null, this);

    }


    @Override
    public void onButtonSolicitacaoClick(View view, Solicitacao solicitacao, int position) {

    }

    @Override
    public void onSolicitacaoClick(View view, Solicitacao solicitacao, int position) {
        /*
        if(getResources().getBoolean(R.bool.phone)){
            Intent it = new Intent(PainelChamadosActivity.this, SolicitacaoDetalheActivity.class );
            it.putExtra(SolicitacaoDetalheActivity.EXTRA_SOLICITACAO, solicitacao);
            ActivityCompat.startActivity(PainelChamadosActivity.this, it, null);
        }
        else
        {
            //Tablet
            SolicitacaoDetalheFragment solicitacaoDetalheFragment = SolicitacaoDetalheFragment.newInstance(solicitacao);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.placeholderDetalheSolicitacao, solicitacaoDetalheFragment)
                    .commit();

        }*/

        Intent it = new Intent(PainelChamadosActivity.this, SolicitacaoDetalheActivity.class );
        it.putExtra(SolicitacaoDetalheActivity.EXTRA_SOLICITACAO, solicitacao);
        it.putExtra(Constants.USUARIO_EXTRA, mUsuario);
        ActivityCompat.startActivity(PainelChamadosActivity.this, it, null);

    }


    public void fabRefresh_onClick(View view) {
        if (verificaConexao(this)) {
            mDialog = ProgressDialog.show(this, "Espere por favor...", "Carregando dados...", true);
            mLoaderMAnager.restartLoader(LOADER_SOLICITACAO, null, this);
        } else {
            Snackbar.make(view, "SEM CONEXAO COM INTERNET", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    //Loader Callbacks
    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (!verificaConexao(this)) {
            Snackbar.make(textViewCidade, "SEM CONEXÃO COM INTERNET", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return null;
        }
        mSolicitacao = null;
        if (id == LOADER_SOLICITACAO){
            return new PainelChamadosTask(this, id, mUsuario );
        }
        else if (id == LOADER_ATENDER_SOLICITACAO){
            String datahora = DataHoraNow();
            Solicitacao solicitacao = (Solicitacao) args.getSerializable(Constants.SOLICITACAO_EXTRA);
            mSolicitacao = solicitacao;
            mSolicitacao.setData_atendimento(datahora);
            solicitacao.setData_atendimento(datahora);
            return new AtenderChamadoTask(this, id, mUsuario, solicitacao );
        }
        else if (id == LOADER_DESISTIR_SOLICITACAO){
            Solicitacao solicitacao = (Solicitacao) args.getSerializable(Constants.SOLICITACAO_EXTRA);
            mSolicitacao = solicitacao;
            return new DesistirChamadoTask(this, id, mUsuario, solicitacao );
        }
        else if (id == LOADER_FINALIZAR_SOLICITACAO){
            Solicitacao solicitacao = (Solicitacao) args.getSerializable(Constants.SOLICITACAO_EXTRA);
            mSolicitacao = solicitacao;

            String datahora = DataHoraNow();
            mSolicitacao.setData_fim_corrida(datahora);
            solicitacao.setData_fim_corrida(datahora);

            return new FinalizarChamadoTask(this, id, mUsuario, solicitacao);
        }
        if (id == LOADER_INICIAR_CORRIDA_SOLICITACAO){
            Solicitacao solicitacao = (Solicitacao) args.getSerializable(Constants.SOLICITACAO_EXTRA);
            mSolicitacao = solicitacao;

            String datahora = DataHoraNow();
            mSolicitacao.setData_ini_corrida(datahora);
            solicitacao.setData_ini_corrida(datahora);

            return new IniciarCorridaChamadoTask(this, id, mUsuario, solicitacao);
        }
        else if (id == LOADER_CARREGA_SALDO_CREDITO){
            return new CarregaSaldoCreditoMototaxistaTask(this, id, mUsuario.getMototaxi_id());
        }
        else if (id == LOADER_ATUALIZA_LOCALIZACAO_USU_PNL){
            return new UsuarioAtualizaLocalizacaoUsuarioTask(this, id, args);
        }
        else return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        if (loader.getId() == LOADER_SOLICITACAO ) {
            mDialog.dismiss();

            mSolicitacoesList.clear();
            if(data != null) {
                List<Solicitacao> lst = (List) data;
                if (lst.size() > 0) {
                    mSolicitacoesList.addAll(lst);
                }
            }
            else
            {
                Toast.makeText(PainelChamadosActivity.this, "SEM COMUNICAÇÃO COM O SERVIDOR", Toast.LENGTH_SHORT).show();
                mLoaderMAnager.destroyLoader(LOADER_SOLICITACAO);
            }

            mAdapter.notifyDataSetChanged();
            mLoaderMAnager.restartLoader(LOADER_CARREGA_SALDO_CREDITO, null, this);

        }

        if (loader.getId() == LOADER_CARREGA_SALDO_CREDITO ) {

            if(data != null) {
                Integer saldo = (Integer) data;
                if(mUsuario.getCidade().length()<=15) {
                    textViewCidade.setText(mUsuario.getCidade() + " - SALDO: " + saldo);
                }
                else
                {
                    textViewCidade.setText(mUsuario.getCidade() + "\nSALDO: " + saldo);
                }
                mLoaderMAnager.destroyLoader(LOADER_CARREGA_SALDO_CREDITO);
            }
            else
            {
                Toast.makeText(PainelChamadosActivity.this, "SEM COMUNICAÇÃO COM O SERVIDOR", Toast.LENGTH_SHORT).show();
                mLoaderMAnager.destroyLoader(LOADER_CARREGA_SALDO_CREDITO);
            }
            mAdapter.notifyDataSetChanged();

        }

        else if (loader.getId() == LOADER_ATENDER_SOLICITACAO ) {
            if(data!= null) {

                String result = (String) data;
                if (result.equals("OK")) {
                    //Toast.makeText(PainelChamadosActivity.this, "Usuário está a sua espera!", Toast.LENGTH_SHORT).show();
                    //Snackbar.make(textViewCidade, "ESSA CORRIDA É SUA, NÃO DEMORE!", Snackbar.LENGTH_LONG)
                    //        .setAction("Action", null).show();
                    //fabRefresh_onClick(fabRefresh);

                    Toast.makeText(PainelChamadosActivity.this, "ESSA CORRIDA É SUA, NÃO DEMORE!", Toast.LENGTH_LONG).show();
                    Intent it = new Intent();
                    it.putExtra(Constants.SOLICITACAO_EXTRA, mSolicitacao);
                    it.putExtra("acao","ATENDIMENTO");

                    setResult(RESULT_OK, it);
                    finish();

                }
                else
                if (result.equals("EU")) {
                    //Toast.makeText(PainelChamadosActivity.this, "Chamado já está em atendimento por outro mototáxi", Toast.LENGTH_SHORT).show();
                    Snackbar.make(textViewCidade, "VOCÊ JÁ ESTA COM ESSE CHAMADO, NÃO DEMORE!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else
                if (result.equals("E")) {
                    //Toast.makeText(PainelChamadosActivity.this, "Chamado já está em atendimento por outro mototáxi", Toast.LENGTH_SHORT).show();
                    Snackbar.make(textViewCidade, "CHAMADO EM ATENDIMENTO POR OUTRO!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else
                if (result.equals("C")) {
                    //Toast.makeText(PainelChamadosActivity.this, "Chamado cancelado", Toast.LENGTH_SHORT).show();
                    Snackbar.make(textViewCidade, "CHAMADO CANCELADO", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else
                if (result.equals("F")) {
                    //Toast.makeText(PainelChamadosActivity.this, "Chamado finalizado", Toast.LENGTH_SHORT).show();
                    Snackbar.make(textViewCidade, "CHAMADO FINALIZADO", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else if (result.equals("1")) {
                    //Toast.makeText(PainelChamadosActivity.this, "Chamado finalizado", Toast.LENGTH_SHORT).show();
                    Snackbar.make(textViewCidade, "VOCÊ JÁ ESTA COM OUTRO CHAMADO!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else if (result.equals("solicitacao nao encontrada")) {
                    //Toast.makeText(PainelChamadosActivity.this, "Chamado finalizado", Toast.LENGTH_SHORT).show();
                    Snackbar.make(textViewCidade, "NÃO FOI POSSÍVEL ATENDER ESSE CHAMADO, SERVIDOR INSTÁVEL!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else if (result.equals("-999")) {
                    //Toast.makeText(PainelChamadosActivity.this, "Chamado finalizado", Toast.LENGTH_SHORT).show();
                    Snackbar.make(textViewCidade, "VOCÊ ESTÁ SEM CREDITO!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

                mAdapter.notifyDataSetChanged();
                mLoaderMAnager.destroyLoader(LOADER_ATENDER_SOLICITACAO);
            }
            else
            {
                Toast.makeText(PainelChamadosActivity.this, "SEM COMUNICAÇÃO COM O SERVIDOR", Toast.LENGTH_SHORT).show();
                mLoaderMAnager.destroyLoader(LOADER_ATENDER_SOLICITACAO);
            }

        }
        else if (loader.getId() == LOADER_INICIAR_CORRIDA_SOLICITACAO ) {
            if(data!= null) {

                String result = (String) data;
                if (result.equals("OK")) {
                    //Toast.makeText(PainelChamadosActivity.this, "Usuário está a sua espera!", Toast.LENGTH_SHORT).show();
                    Snackbar.make(textViewCidade, "CORRIDA INICIADA!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    fabRefresh_onClick(fabRefresh);
                }
                else
                {
                    //Toast.makeText(PainelChamadosActivity.this, "Chamado já está em atendimento por outro mototáxi", Toast.LENGTH_SHORT).show();
                    Snackbar.make(textViewCidade, "PROBLEMA AO INICIAR A CORRIDA, TENTE NOVAMENTE!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

                mAdapter.notifyDataSetChanged();
                mLoaderMAnager.destroyLoader(LOADER_INICIAR_CORRIDA_SOLICITACAO);
            }
            else
            {
                Toast.makeText(PainelChamadosActivity.this, "SEM COMUNICAÇÃO COM O SERVIDOR", Toast.LENGTH_SHORT).show();
                mLoaderMAnager.destroyLoader(LOADER_INICIAR_CORRIDA_SOLICITACAO);
            }

        }
        else if (loader.getId() == LOADER_FINALIZAR_SOLICITACAO ) {
            if(data!= null) {

                String result = (String) data;
                if (result.equals("OK")) {
                    //Toast.makeText(PainelChamadosActivity.this, "Usuário está a sua espera!", Toast.LENGTH_SHORT).show();
                    Snackbar.make(textViewCidade, "CHAMADO FINALIZADO!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    fabRefresh_onClick(fabRefresh);
                }
                else
                {
                    //Toast.makeText(PainelChamadosActivity.this, "Chamado já está em atendimento por outro mototáxi", Toast.LENGTH_SHORT).show();
                    Snackbar.make(textViewCidade, "PROBLEMA AO FINALIZAR SUA CORRIDA, TENTE NOVAMENTE!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

                mAdapter.notifyDataSetChanged();
                mLoaderMAnager.destroyLoader(LOADER_FINALIZAR_SOLICITACAO);
            }
            else
            {
                Toast.makeText(PainelChamadosActivity.this, "SEM COMUNICAÇÃO COM O SERVIDOR", Toast.LENGTH_SHORT).show();
                mLoaderMAnager.destroyLoader(LOADER_FINALIZAR_SOLICITACAO);
            }

        }
        else if (loader.getId() == LOADER_DESISTIR_SOLICITACAO ) {
            if(data!= null) {

                String result = (String) data;
                if (result.equals("OK")) {
                    //Toast.makeText(PainelChamadosActivity.this, "Usuário está a sua espera!", Toast.LENGTH_SHORT).show();
                    Snackbar.make(textViewCidade, "ATENDIMENTO CANCELADO COM SUCESSO!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    fabRefresh_onClick(fabRefresh);
                }
                else
                {
                    //Toast.makeText(PainelChamadosActivity.this, "Chamado já está em atendimento por outro mototáxi", Toast.LENGTH_SHORT).show();
                    Snackbar.make(textViewCidade, "PROBLEMA AO CANCELAR ESSE CHAMADO!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

                mAdapter.notifyDataSetChanged();
                mLoaderMAnager.destroyLoader(LOADER_DESISTIR_SOLICITACAO);
            }
            else
            {
                Toast.makeText(PainelChamadosActivity.this, "SEM COMUNICAÇÃO COM O SERVIDOR", Toast.LENGTH_SHORT).show();
                mLoaderMAnager.destroyLoader(LOADER_DESISTIR_SOLICITACAO);
            }

        }
        else if (loader.getId() == LOADER_ATUALIZA_LOCALIZACAO_USU_PNL ) {
            if(data!= null) {
                //Atualizado com sucesso???
                mLoaderMAnager.destroyLoader(LOADER_ATUALIZA_LOCALIZACAO_USU_PNL);
            }
            else
            {
                Toast.makeText(PainelChamadosActivity.this, "SEM COMUNICAÇÃO COM O SERVIDOR", Toast.LENGTH_SHORT).show();
                mLoaderMAnager.destroyLoader(LOADER_ATUALIZA_LOCALIZACAO_USU_PNL);
            }



        }

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }



    @Override
    public void onLocationChanged(Location location) {
        if (verificaConexao(this)) {
            try {
                mUsuario.setLatitude_atual(location.getLatitude());
                mUsuario.setLongitude_atual(location.getLongitude());

                List<Address> addresses = geocoder.getFromLocation(mUsuario.getLatitude_atual(), mUsuario.getLongitude_atual(), 1);

                if (addresses.size() > 0) {

                    String estado = addresses.get(0).getAdminArea();
                    String cidade = addresses.get(0).getLocality();
                    String bairro = addresses.get(0).getSubLocality();

                    mUsuario.setCidade(cidade);
                    mUsuario.setEstado(estado);
                    mUsuario.setBairro(bairro);

                    AtualizaLocalizacaoUsuarioWeb();
                }

            } catch (IOException e ) {
                e.printStackTrace();
            }

        }
    }


    private void AtualizaLocalizacaoUsuarioWeb(){
        //Carregar dados da WEB
        Bundle params = new Bundle();
        params.putSerializable(Constants.USUARIO_EXTRA, mUsuario);
        mLoaderMAnager.restartLoader(LOADER_ATUALIZA_LOCALIZACAO_USU_PNL, params, PainelChamadosActivity.this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(15000);
        mLocationRequest.setFastestInterval(15000);
        mLocationRequest.setSmallestDisplacement(0.1F); //added
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //changed
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
