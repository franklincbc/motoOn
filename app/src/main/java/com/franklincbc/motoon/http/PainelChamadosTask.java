package com.franklincbc.motoon.http;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.franklincbc.motoon.Utils.Util;
import com.franklincbc.motoon.model.Solicitacao;
import com.franklincbc.motoon.model.Usuario;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Priscila on 10/11/2016.
 */

public class PainelChamadosTask extends AsyncTaskLoader<List<Solicitacao>> {

private Integer mLoaderID;
private Usuario mUsuario;
private List<Solicitacao> mSolicitacoes = null;

public PainelChamadosTask(Context context, Integer loaderID, Usuario usuario) {
        super(context);
        this.mLoaderID = loaderID;
        this.mSolicitacoes = new ArrayList<>();
        this.mUsuario = usuario;
        }


@Override
protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
        }


@Override
public List<Solicitacao> loadInBackground() {
    try {
        List<Solicitacao> solicitacoes = SolicitacaoHttp.carregaPainelChamados(mUsuario.getCidade(), mUsuario.getBairro(), mUsuario.getMototaxi_id(), mUsuario.getTipo_veiculo());
        if(solicitacoes == null){
            return null;
        }

        List<Solicitacao> lstOrdenadaAprox = orderByDistanceNearToFar(solicitacoes);

        mSolicitacoes.clear();
        //mSolicitacoes.addAll(solicitacoes);
        mSolicitacoes.addAll(lstOrdenadaAprox);
    } catch (JSONException e) {
        e.printStackTrace();
    }

    return mSolicitacoes;
    }


    public List<Solicitacao> orderByDistanceNearToFar(List<Solicitacao> lstWeb){

        LatLng latLongMototaxi = new LatLng(mUsuario.getLatitude_atual(),mUsuario.getLongitude_atual());
        LatLng latLongSolicitante;
        Double distanciaMenor = 99999.999;
        Double distanciaCalculada;
        Integer indiceMenorDistancia = 0;

        List<Solicitacao> lstOrdenada = new ArrayList<>();

        Solicitacao solicitacao;
        while (lstWeb.size() != 0) {
            distanciaMenor = 99999.999;
            for (int i = 0; i < lstWeb.size(); i++) {
                solicitacao = lstWeb.get(i);
                latLongSolicitante = new LatLng(solicitacao.getLatitude(),solicitacao.getLongitude());
                distanciaCalculada = Util.calcularDistancia(latLongMototaxi, latLongSolicitante);
                if(distanciaCalculada < distanciaMenor){
                    indiceMenorDistancia = i;
                    distanciaMenor = distanciaCalculada;
                }
            }

            lstOrdenada.add(lstWeb.get(indiceMenorDistancia));
            lstWeb.remove(lstWeb.get(indiceMenorDistancia));
        }



        return lstOrdenada;
    }


}


