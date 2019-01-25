package com.franklincbc.motoon.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.franklincbc.motoon.OnButtonSolicitacaoClickListener;
import com.franklincbc.motoon.OnSolicitacaoClickListener;
import com.franklincbc.motoon.R;
import com.franklincbc.motoon.model.Solicitacao;

import java.util.List;

/**
 * Created by Priscila on 29/10/2016.
 */

public class SolicitacaoAdapter extends RecyclerView.Adapter<SolicitacaoAdapter.VH>{

    private List<Solicitacao> mSolicitacoes;
    private Context mContext;
    private OnSolicitacaoClickListener mSolicitacaoClickListener;
    private OnButtonSolicitacaoClickListener mButtonSolicitacaoClickListener;

    public SolicitacaoAdapter(Context ctx, List<Solicitacao> solicitacoes){
        this.mContext = ctx;
        this.mSolicitacoes = solicitacoes;
    }

    public void setSolicitacaoClickListener(OnSolicitacaoClickListener scl){
        this.mSolicitacaoClickListener = scl;
    }

    public void setButtonSolicitacaoClickListener(OnButtonSolicitacaoClickListener bscl){
        this.mButtonSolicitacaoClickListener = bscl;
    }

    @Override
    public SolicitacaoAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_lst_solicitacao, parent, false);
        final VH viewHolder = new VH(view);
        view.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                int pos = viewHolder.getAdapterPosition();
                if(mSolicitacaoClickListener != null){
                    mSolicitacaoClickListener.onSolicitacaoClick(view, mSolicitacoes.get(pos), pos);
                }
            }
        } );

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        final Solicitacao solicitacao = mSolicitacoes.get(position);
        holder.txtDataSolicitacao.setText(solicitacao.getData_solicitacao());
        holder.txtSolicitante.setText(solicitacao.getSolicitante());

        holder.btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mButtonSolicitacaoClickListener != null){
                    mButtonSolicitacaoClickListener.onButtonSolicitacaoClick(view, solicitacao, position );
                }
            }
        });

        String status_sol;

        if (solicitacao.getStatus_sol().equals("A") ){
            status_sol = "Aguardando Atendimento";
            holder.view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.aguardando_atendimento));
            holder.btnCancelar.setVisibility(View.VISIBLE);
        } else
        if (solicitacao.getStatus_sol().equals("E")){
            status_sol = "Em Atendimento";
            holder.view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.em_Atendimento));
            holder.btnCancelar.setVisibility(View.VISIBLE);
        } else
        if (solicitacao.getStatus_sol().equals("F")){
            status_sol = "Finalizado";
            holder.view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.atendimento_finalizado));
            holder.btnCancelar.setVisibility(View.INVISIBLE);
        } else
        if (solicitacao.getStatus_sol().equals("C")){
            status_sol = "Cancelado";
            holder.view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.atendimento_cancelado));
            holder.btnCancelar.setVisibility(View.INVISIBLE);
        }
        else
        if (solicitacao.getStatus_sol().equals("I")){
            //Motociclista está com o passageiro, nao permitir cancelar
            status_sol = "Corrida Iniciada";
            holder.view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.em_Atendimento));
            holder.btnCancelar.setVisibility(View.INVISIBLE);
        }
        else
        {   status_sol = "Desconhecido";
            holder.btnCancelar.setVisibility(View.INVISIBLE);}

        holder.txtStatus.setText(status_sol);

    }

    @Override
    public int getItemCount() {
        return mSolicitacoes.size();
    }



    class VH extends RecyclerView.ViewHolder {
        TextView txtDataSolicitacao;
        TextView txtSolicitante;
        TextView txtStatus;
        Button btnCancelar;
        View view;

        public VH(View itemView) {
            super(itemView);
            txtDataSolicitacao = (TextView)itemView.findViewById(R.id.item_lst_solicitacao_dataSolicitacao);
            txtSolicitante = (TextView)itemView.findViewById(R.id.item_lst_solicitacao_solicitante);
            txtStatus = (TextView)itemView.findViewById(R.id.item_lst_solicitacao_status);
            btnCancelar = (Button)itemView.findViewById(R.id.item_lst_solicitacao_btnCancelar);
            view = itemView;
        }
    }

}
