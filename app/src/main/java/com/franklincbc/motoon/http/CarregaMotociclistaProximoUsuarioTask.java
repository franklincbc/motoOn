package com.franklincbc.motoon.http;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.franklincbc.motoon.Utils.Constants;
import com.franklincbc.motoon.Utils.Util;
import com.franklincbc.motoon.model.Usuario;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Priscila on 24/01/2017.
 */

public class CarregaMotociclistaProximoUsuarioTask extends AsyncTaskLoader<List<Usuario>> {

    private List<Usuario> mLstMotociclista = null;
    private Usuario mUsuario = null;
    Context ctx;

    public CarregaMotociclistaProximoUsuarioTask(Context context, Bundle params) {
        super(context);
        this.ctx = context;
        this.mUsuario = (Usuario) params.getSerializable(Constants.USUARIO_EXTRA);
        this.mLstMotociclista = new ArrayList<>();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<Usuario> loadInBackground() {
        Usuario motociclista = null;
        try {

            List<Usuario> LstMotociclista = UsuarioHttp.carregaMotociclistaProximoUsuario(mUsuario.getCidade());
            if (LstMotociclista == null) {
                //Toast.makeText(ctx, "SEM COMUNICAÇÃO COM O SERVIDOR", Toast.LENGTH_SHORT).show();
                return null;
            }


            if(LstMotociclista.size() > 1) {
                List<Usuario> lstOrdenadaAprox = orderByDistanceNearToFar(LstMotociclista);
                mLstMotociclista.clear();
                mLstMotociclista.addAll(lstOrdenadaAprox);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return mLstMotociclista;

    }


    public List<Usuario> orderByDistanceNearToFar(List<Usuario> lstWeb){

        LatLng latLongUsuario = new LatLng(mUsuario.getLatitude_atual(),mUsuario.getLongitude_atual());
        LatLng latLongMototaxi;
        Double distanciaMenor = 99999.999;
        Double distanciaCalculada;
        Integer indiceMenorDistancia = 0;

        List<Usuario> lstOrdenada = new ArrayList<>();

        Usuario usuario;
        while (lstWeb.size() != 0) {
            distanciaMenor = 99999.999;
            for (int i = 0; i < lstWeb.size(); i++) {
                usuario = lstWeb.get(i);
                latLongMototaxi = new LatLng(usuario.getLatitude_atual(),usuario.getLongitude_atual());
                distanciaCalculada = Util.calcularDistancia(latLongUsuario, latLongMototaxi);
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


