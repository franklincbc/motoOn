package com.franklincbc.motoon;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.franklincbc.motoon.model.Solicitacao;

public class SolicitacaoDetalheFragment extends Fragment {

    private static final String EXTRA_SOLICITACAO = "solicitacao";
    TextView txtDataSolicitacao;
    TextView txtSolicitante;
    TextView txtLocalOrigem;
    TextView txtPontoReferencia;
    TextView txtLocalDestino;
    TextView txtInformacaoAdicional;
    TextView txtMotoTaxista;
    TextView txtStatus;

    Solicitacao mSolicitacao;

    public SolicitacaoDetalheFragment() {
        // Required empty public constructor
    }


    public static SolicitacaoDetalheFragment newInstance(Solicitacao solicitacao) {

        Bundle args = new Bundle();
        args.putSerializable(EXTRA_SOLICITACAO,solicitacao);
        SolicitacaoDetalheFragment fragment = new SolicitacaoDetalheFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_solicitacao_detalhe, container, false);
        txtDataSolicitacao = (TextView)view.findViewById(R.id.SolicitacaoDetalheFragment_dataSolicitacao);
        txtSolicitante = (TextView)view.findViewById(R.id.SolicitacaoDetalheFragment_solicitante);
        txtLocalOrigem = (TextView)view.findViewById(R.id.SolicitacaoDetalheFragment_origem);
        txtPontoReferencia = (TextView)view.findViewById(R.id.SolicitacaoDetalheFragment_pontoReferencia);
        txtLocalDestino = (TextView)view.findViewById(R.id.SolicitacaoDetalheFragment_destino);
        txtInformacaoAdicional = (TextView)view.findViewById(R.id.SolicitacaoDetalheFragment_informacaoAdicional);
        txtMotoTaxista = (TextView)view.findViewById(R.id.SolicitacaoDetalheFragment_mototaxiID);
        txtStatus = (TextView)view.findViewById(R.id.SolicitacaoDetalheFragment_status);

        if(savedInstanceState == null){
            mSolicitacao = (Solicitacao)getArguments().getSerializable(EXTRA_SOLICITACAO);
        }
        else
        {
            mSolicitacao = (Solicitacao)savedInstanceState.getSerializable(EXTRA_SOLICITACAO);
        }

        updateUI(mSolicitacao);

        return view;

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXTRA_SOLICITACAO, mSolicitacao);
    }

    private void updateUI(Solicitacao solicitacao){
        mSolicitacao = solicitacao;
        txtDataSolicitacao.setText(mSolicitacao.getData_solicitacao());
        txtSolicitante.setText(mSolicitacao.getSolicitante());
        txtLocalOrigem.setText(mSolicitacao.getLocal_origem());
        txtPontoReferencia.setText(mSolicitacao.getPonto_referencia());
        txtLocalDestino.setText(mSolicitacao.getLocal_destino());
        txtInformacaoAdicional.setText(mSolicitacao.getInformacao_adicional());
        txtMotoTaxista.setText(String.valueOf(mSolicitacao.getMototaxi_id()));
        String status_sol;
        if (mSolicitacao.getStatus_sol().equals("A") ){ status_sol = getString(R.string.status_aguardando_atendimento); } else
        if (mSolicitacao.getStatus_sol().equals("E")){ status_sol = getString(R.string.status_em_atendimento); } else
        if (mSolicitacao.getStatus_sol().equals("F")){ status_sol = getString(R.string.status_finalizado); } else
        if (mSolicitacao.getStatus_sol().equals("C")){ status_sol = getString(R.string.status_cancelado); } else
        {status_sol = getString(R.string.status_desconhecido);}

        txtStatus.setText(status_sol);

    }


    LoaderManager.LoaderCallbacks mSolicitacaoCallback = new LoaderManager.LoaderCallbacks<Solicitacao>(){


        @Override
        public Loader<Solicitacao> onCreateLoader(int id, Bundle args) {
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Solicitacao> loader, Solicitacao data) {

        }

        @Override
        public void onLoaderReset(Loader<Solicitacao> loader) {

        }
    };

}
