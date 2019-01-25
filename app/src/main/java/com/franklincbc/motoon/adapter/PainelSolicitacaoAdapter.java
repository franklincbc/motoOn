package com.franklincbc.motoon.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.franklincbc.motoon.OnButtonFinalizarSolicitacaoClickListener;
import com.franklincbc.motoon.OnButtonSolicitacaoClickListener;
import com.franklincbc.motoon.OnSolicitacaoClickListener;
import com.franklincbc.motoon.R;
import com.franklincbc.motoon.Utils.Util;
import com.franklincbc.motoon.model.Solicitacao;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Priscila on 10/11/2016.
 */

public class PainelSolicitacaoAdapter extends RecyclerView.Adapter<PainelSolicitacaoAdapter.VH>{

    private List<Solicitacao> mSolicitacoes;
    private Context mContext;
    private OnSolicitacaoClickListener mSolicitacaoClickListener;
    private OnButtonSolicitacaoClickListener mButtonSolicitacaoClickListener;
    private int mMototaxi_ID;
    private OnButtonFinalizarSolicitacaoClickListener mButtonFinalizarSolicitacaoClickListener;
    private LatLng latLngUsuario;

public PainelSolicitacaoAdapter(Context ctx, List<Solicitacao> solicitacoes, Integer mototaxi_id, LatLng latLng){
        this.mContext = ctx;
        this.mSolicitacoes = solicitacoes;
        this.mMototaxi_ID = mototaxi_id;
        this.latLngUsuario = latLng;
}

public void setSolicitacaoClickListener(OnSolicitacaoClickListener scl){
        this.mSolicitacaoClickListener = scl;
        }

public void setButtonSolicitacaoClickListener(OnButtonSolicitacaoClickListener bscl){
    this.mButtonSolicitacaoClickListener = bscl;
}

public void setButtonFinalizarSolicitacaoClickListener(OnButtonFinalizarSolicitacaoClickListener bfscl){
    this.mButtonFinalizarSolicitacaoClickListener = bfscl;
}

@Override
public PainelSolicitacaoAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_lst_painel_solicitacao, parent, false);
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

        LatLng latLngSolicitante = new LatLng(solicitacao.getLatitude(), solicitacao.getLongitude());
        Double distanciaCalculada;
        distanciaCalculada = Util.calcularDistancia(latLngUsuario, latLngSolicitante);

        holder.txtDataSolicitacao.setText(solicitacao.getData_solicitacao());
        holder.txtSolicitante.setText(solicitacao.getSolicitante());
        holder.txtBairro.setText(solicitacao.getBairro() + " ("+ distanciaCalculada + " km)");

        holder.btnAtender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mButtonSolicitacaoClickListener != null){
                    mButtonSolicitacaoClickListener.onButtonSolicitacaoClick(view, solicitacao, position );
                }
            }
        });

        holder.btnFinalizar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mButtonFinalizarSolicitacaoClickListener != null){
                mButtonFinalizarSolicitacaoClickListener.onButtonFinalizarSolicitacaoClick(view, solicitacao, position );
            }
        }
    });

    //Seta os Status
        if(solicitacao.getStatus_sol().equals("E") && solicitacao.getMototaxi_id().equals(mMototaxi_ID) ){
            holder.view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.em_Atendimento));
            holder.txtStatus.setText("Em Atendimento por você");
            holder.btnFinalizar.setVisibility(View.VISIBLE);
            holder.btnAtender.setVisibility(View.VISIBLE);
            holder.btnAtender.setText("CANCELAR");
        }
        else if(solicitacao.getStatus_sol().equals("I") && solicitacao.getMototaxi_id().equals(mMototaxi_ID) ){
            holder.view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.em_Atendimento));
            holder.txtStatus.setText("Corrida Iniciada");
            holder.btnFinalizar.setVisibility(View.VISIBLE);
            holder.btnFinalizar.setText("FINALIZAR");
            holder.btnAtender.setVisibility(View.INVISIBLE);
        }
        else {
            holder.view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.aguardando_atendimento));
            holder.btnAtender.setVisibility(View.VISIBLE);
            holder.btnFinalizar.setVisibility(View.INVISIBLE);
            holder.btnAtender.setText("ATENDER");
            holder.btnFinalizar.setText("INICIAR");
            if(solicitacao.getStatus_sol().equals("E") || solicitacao.getStatus_sol().equals("I")){
                holder.txtStatus.setText("Em Atendimento por outro mototaxi");
            } else if(solicitacao.getStatus_sol().equals("A")){
                holder.txtStatus.setText("Aguardando Atendimento");
            }
        }


}

@Override
public int getItemCount() {
        return mSolicitacoes.size();
        }



class VH extends RecyclerView.ViewHolder {
    TextView txtDataSolicitacao;
    TextView txtSolicitante;
    TextView txtBairro;
    TextView txtStatus;

    Button btnAtender;
    Button btnFinalizar;
    View view;

    public VH(View itemView) {
        super(itemView);
        txtDataSolicitacao = (TextView)itemView.findViewById(R.id.item_lst_painel_data);
        txtSolicitante = (TextView)itemView.findViewById(R.id.item_lst_painel_nome);
        txtBairro = (TextView)itemView.findViewById(R.id.item_lst_painel_bairro);
        txtStatus = (TextView)itemView.findViewById(R.id.item_lst_painel_status);
        btnAtender = (Button) itemView.findViewById(R.id.item_lst_painel_btn_atender);
        btnFinalizar = (Button) itemView.findViewById(R.id.item_lst_painel_btn_finalizar);
        view = itemView;
    }
}

}

