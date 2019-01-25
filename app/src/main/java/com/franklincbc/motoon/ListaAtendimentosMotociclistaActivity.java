package com.franklincbc.motoon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.Toast;

import com.franklincbc.motoon.Utils.Constants;
import com.franklincbc.motoon.Utils.Util;
import com.franklincbc.motoon.adapter.SolicitacaoAdapter;
import com.franklincbc.motoon.http.CarregaAtendimentosMotociclistaTask;
import com.franklincbc.motoon.model.Solicitacao;
import com.franklincbc.motoon.model.Usuario;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.franklincbc.motoon.Utils.Util.verificaConexao;

public class ListaAtendimentosMotociclistaActivity extends AppCompatActivity
        implements OnSolicitacaoClickListener,
        OnButtonSolicitacaoClickListener,
        LoaderManager.LoaderCallbacks{

    private static final int LOADER_ATENDIMENTOS_MOTOCICLISTA = 0;

    RecyclerView mRecyclerView;
    SolicitacaoAdapter mAdapter;
    List<Solicitacao> mSolicitacoesList;
    LoaderManager mLoaderMAnager;
    Usuario mUsuario;

    List<Solicitacao> mSolicitacoesListDataSelec;
    DatePicker mCalendar;
    String mDataSelecionada = "";
    SimpleDateFormat sdf;
    Switch switchCalendario;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_atendimentos_motociclista);

        setTitle("MEUS ATENDIMENTOS");

        mUsuario = (Usuario) getIntent().getSerializableExtra(Constants.USUARIO_EXTRA);

        mSolicitacoesList = new ArrayList<>();
        mSolicitacoesListDataSelec = new ArrayList<>();

        mAdapter = new SolicitacaoAdapter(this, mSolicitacoesListDataSelec);
        mAdapter.setSolicitacaoClickListener(new OnSolicitacaoClickListener() {
            @Override
            public void onSolicitacaoClick(View view, Solicitacao solicitacao, int position) {
                Activity activity = ListaAtendimentosMotociclistaActivity.this;
                if(activity instanceof OnSolicitacaoClickListener){
                    ((OnSolicitacaoClickListener)activity).onSolicitacaoClick(view, solicitacao, position);
                }
            }
        });

        //Implementa evento do butao Atender
        mAdapter.setButtonSolicitacaoClickListener(new OnButtonSolicitacaoClickListener() {

            @Override
            public void onButtonSolicitacaoClick(View view, Solicitacao solicitacao, int position) {
                //Bundle args = new Bundle();
                //args.putSerializable(Constants.SOLICITACAO_EXTRA, solicitacao);
                //mLoaderMAnager.restartLoader(LOADER_CANCELAR_SOLICITACAO, args, ListaAtendimentosMotociclistaActivity.this);
            }
        });

        mCalendar = (DatePicker) findViewById(R.id.lista_atendimentos_motociclista_calendarView);
        sdf = new SimpleDateFormat("dd/MM/yyyy");

        mDataSelecionada = Util.DataNow();
        SetDateCalendario(mDataSelecionada);


        switchCalendario = (Switch)findViewById(R.id.lista_atendimentos_motociclista_switchCalendario);
        switchCalendario.setOnCheckedChangeListener(onCheckedChangeListener);
        ChangeHeightCalendar();

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView_lst_atendimentos);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                && getResources().getBoolean(R.bool.phone)){
            mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT
                && getResources().getBoolean(R.bool.tablet)){
            mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        }
        else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        mRecyclerView.setAdapter(mAdapter);

        //No lugar da mensagem, ler da base de dados local e exibir os dados
        if (!verificaConexao(this)){
            Snackbar.make(mRecyclerView, "SEM CONEXAO COM INTERNET", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        mDialog = ProgressDialog.show(this,"Espere por favor...", "Carregando dados...", true);

        mLoaderMAnager = getSupportLoaderManager();
        mLoaderMAnager.initLoader(LOADER_ATENDIMENTOS_MOTOCICLISTA, null, this);

    }

    public void SetDateCalendario(String data){

        String ano = data.substring(6,10);
        String mes = data.substring(3,5);
        String dia = data.substring(0,2);

        mCalendar.init(Integer.valueOf(ano),
                Integer.valueOf(mes) -1,
                Integer.valueOf(dia),
                onDateChangedListener);
    }

    public void ChangeHeightCalendar(){
        if(switchCalendario.isChecked()){
            ViewGroup.LayoutParams params =  mCalendar.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            mCalendar.setLayoutParams(params);

        }
        else
        {
            ViewGroup.LayoutParams params =  mCalendar.getLayoutParams();
            params.height = 0;
            mCalendar.setLayoutParams(params);
        }
    }

    public void SelecionaSolicitacoesDataSelecionada(){
        Solicitacao solicitacao;
        mSolicitacoesListDataSelec.clear();
        if(switchCalendario.isChecked()) {
            for (int i = 0; i < mSolicitacoesList.size(); i++) {
                solicitacao = mSolicitacoesList.get(i);
                Date dataSolicitacao = Util.StringToDate(solicitacao.getData_solicitacao(), "dd/MM/yyyy");
                if (sdf.format(dataSolicitacao).equals(mDataSelecionada)) {
                    mSolicitacoesListDataSelec.add(solicitacao);
                }
            }
        }
        else
        {
            mSolicitacoesListDataSelec.addAll(mSolicitacoesList);
        }
        mAdapter.notifyDataSetChanged();
    }

    CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                ChangeHeightCalendar();
                SelecionaSolicitacoesDataSelecionada();
            }else{
                ChangeHeightCalendar();
                SelecionaSolicitacoesDataSelecionada();
            }
        }
    };

    DatePicker.OnDateChangedListener onDateChangedListener = new DatePicker.OnDateChangedListener(){
        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth)  {
            String Ano = String.valueOf(year);
            String Mes = String.valueOf(monthOfYear + 1);
            String Dia = String.valueOf(dayOfMonth);
            if(Dia.length() == 1){
                Dia = "0"+Dia;
            }
            if(Mes.length() == 1){
                Mes = "0"+Mes;
            }
            mDataSelecionada = Dia+"/"+Mes+"/"+Ano;
            SelecionaSolicitacoesDataSelecionada();
        }
    };


    @Override
    public void onSolicitacaoClick(View view, Solicitacao solicitacao, int position) {
        if(getResources().getBoolean(R.bool.phone)){
            Intent it = new Intent(ListaAtendimentosMotociclistaActivity.this, SolicitacaoDetalheActivity.class );
            it.putExtra(SolicitacaoDetalheActivity.EXTRA_SOLICITACAO, solicitacao);
            ActivityCompat.startActivity(ListaAtendimentosMotociclistaActivity.this, it, null);
        }
        else
        {
            //Tablet
            SolicitacaoDetalheFragment solicitacaoDetalheFragment = SolicitacaoDetalheFragment.newInstance(solicitacao);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.placeholderDetalheSolicitacao, solicitacaoDetalheFragment)
                    .commit();

        }
    }

    @Override
    public void onButtonSolicitacaoClick(View view, Solicitacao solicitacao, int position) {

    }


    //Loader CallBacks *****************
    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ATENDIMENTOS_MOTOCICLISTA) {
            return new CarregaAtendimentosMotociclistaTask(this, mUsuario.getMototaxi_id());
        }
        else
        {
            return null;
        }
    }


    @Override
    public void onLoadFinished(Loader loader, Object data) {
        boolean bCancelado = false;
        if(data != null) {
            if(loader.getId() == LOADER_ATENDIMENTOS_MOTOCICLISTA) {
                mSolicitacoesList.clear();
                List<Solicitacao> lstSolicitacoes = (List<Solicitacao>) data;
                if (lstSolicitacoes.size() > 0) {
                    mSolicitacoesList.addAll(lstSolicitacoes);
                    //mSolicitacoesListDataSelec.clear();
                    //mSolicitacoesListDataSelec.addAll(mSolicitacoesList);
                    SelecionaSolicitacoesDataSelecionada();

                }
            }

        }
        else
        {
            if(loader.getId() == LOADER_ATENDIMENTOS_MOTOCICLISTA) {
                mSolicitacoesList.clear();
                mLoaderMAnager.restartLoader(LOADER_ATENDIMENTOS_MOTOCICLISTA, null, this); //Carrega novamente para atualizar a lista
            }
            Toast.makeText(ListaAtendimentosMotociclistaActivity.this, "SEM COMUNICAÇÃO COM O SERVIDOR", Toast.LENGTH_SHORT).show();
        }

        mDialog.dismiss();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }


}
