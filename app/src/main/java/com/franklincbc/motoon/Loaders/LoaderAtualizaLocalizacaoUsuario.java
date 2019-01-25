package com.franklincbc.motoon.Loaders;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Priscila on 25/12/2016.
 */

public class LoaderAtualizaLocalizacaoUsuario extends AsyncTaskLoader<List<Address>> {
    Context ctx;
    Double mLat;
    Double mLng;
    String mAddressName;

    public LoaderAtualizaLocalizacaoUsuario(Context context, Bundle params) {
        super(context);
        this.ctx = context;

        //Busca pela latitude e longitude
        this.mLat = params.getDouble("lat");
        this.mLng = params.getDouble("lng");
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<Address> loadInBackground() {

        Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
        List<Address> addresses = null;

            try{
                addresses = geocoder.getFromLocation(mLat, mLng,1);
            }catch (IOException | IllegalArgumentException e){
                e.printStackTrace();
            }

        if(addresses == null){
            return null;
        }

        return addresses;
    }
}