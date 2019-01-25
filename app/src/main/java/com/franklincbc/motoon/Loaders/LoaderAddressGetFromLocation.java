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
 * Created by Priscila on 23/12/2016.
 */

public class LoaderAddressGetFromLocation  extends AsyncTaskLoader<List<Address>> {
    Context ctx;

    Integer mType;
    Double mLat;
    Double mLng;
    String mAddressName;

    public LoaderAddressGetFromLocation(Context context, int type, Bundle params) {
        super(context);
        this.ctx = context;
        this.mType = type;

        if (mType == 1) {
            //Busca pela latitude e longitude
            this.mLat = params.getDouble("lat");
            this.mLng = params.getDouble("lng");
        }
        else if(mType == 2) {
            this.mAddressName = params.getString("addressName");
        }

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
        String errorMessage = null;

        //while (addresses.size()==0) {
        //    addresses = geocoder.getFromLocationName(mAddressName, 1);
        //}

        // Tenta ate 3 vezes. Geralmente vai na primeira. Quando muito na
        // segunda.
        for (int i = 0; 2 > i; i++) {
            try {
                if (mType == 1){
                    addresses = geocoder.getFromLocation(mLat, mLng,1);
                }
                else if(mType == 2){
                    addresses = geocoder.getFromLocationName(mAddressName, 5);
                }
        /*
        else if (fetchType == Constants.USE_ADDRESS_LOCATION){
            Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
            try{
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
            }catch (IOException ioException){
                errorMessage = "Service Not Available";
                Log.e(TAG, errorMessage, ioException);
            }catch (IllegalArgumentException illegalArgumentException){
                errorMessage = "Invalid Latitude or Longitude USed";
                Log.e(TAG, errorMessage + ". ", illegalArgumentException);
            }
        }*/

                else {
                    return null;
                }

            } catch (IOException e) {
                continue;
            }
            if (i >= 1) {
                break;
            }
        }


        if(addresses == null || addresses.size() == 0){
            return null;
        }

        return addresses;
    }
}
