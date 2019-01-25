package com.franklincbc.motoon.BroadCastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.franklincbc.motoon.Contract.UsuarioContract;
import com.franklincbc.motoon.Service.BackgroundNotificationService;
import com.franklincbc.motoon.database.MotoonProvider;
import com.franklincbc.motoon.model.Usuario;

/**
 * Created by Priscila on 20/01/2017.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    Usuario mUsuario;
    boolean bStartServiceNotification = false;

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            boolean isConnected = false;

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if( (mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()) ) {
                isConnected = true;
            }

            if (isConnected) {

                CarregaUsuario(context);

                //Start Service Notificacior only Motociclista ****************
                if (mUsuario.getSn_mototaxi() != null && mUsuario.getSn_mototaxi().equals("S")) {
                    //Rodar em background para exibir notificação de chamados
                    if (mUsuario.getCidade() != null && mUsuario.getTipo_veiculo() != null) {
                        if (!bStartServiceNotification) {
                            Intent it = new Intent(context.getApplicationContext(), BackgroundNotificationService.class);
                            it.putExtra("cidade", mUsuario.getCidade());
                            it.putExtra("tipoveiculo", mUsuario.getTipo_veiculo());
                            context.startService(it);
                            bStartServiceNotification = true;
                        }
                    }
                }
                //********************************************


            } else {
                bStartServiceNotification = false;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void CarregaUsuario(Context context) {
        try {
            //Pega usuario no banco local
            mUsuario = null;
            Cursor cursor = context.getContentResolver().query(MotoonProvider.USUARIO_URI, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    mUsuario = new Usuario();
                    mUsuario.setId(cursor.getLong(cursor.getColumnIndex(UsuarioContract._ID)));
                    mUsuario.setNome(cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_NOME)));
                    mUsuario.setEmail(cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_EMAIL)));
                    mUsuario.setId_servidor(cursor.getInt(cursor.getColumnIndex(UsuarioContract.COL_ID_SERVIDOR)));

                    if (cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_LATITUDE_ATUAL)) != null) {
                        mUsuario.setLatitude_atual(cursor.getDouble(cursor.getColumnIndex(UsuarioContract.COL_LATITUDE_ATUAL)));
                    }
                    if (cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_LONGITUDE_ATUAL)) != null) {
                        mUsuario.setLongitude_atual(cursor.getDouble(cursor.getColumnIndex(UsuarioContract.COL_LONGITUDE_ATUAL)));
                    }

                    mUsuario.setMototaxi_id(cursor.getInt(cursor.getColumnIndex(UsuarioContract.COL_MOTOTAXI_ID)));
                    mUsuario.setCelular(cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_CELULAR)));
                    mUsuario.setSexo(cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_SEXO)));
                    mUsuario.setEstado(cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_ESTADO)));
                    mUsuario.setCidade(cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_CIDADE)));
                    mUsuario.setBairro(cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_BAIRRO)));
                    mUsuario.setSn_aceitou_termo(cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_SN_ACEITOU_TERMO)));
                    mUsuario.setSn_mototaxi(cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_SN_MOTOTAXI)));

                    if (cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_URL_PHOTO)) != null) {
                        mUsuario.setUrl_photo(cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_URL_PHOTO)));
                    }
                    if (cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_CPF)) != null) {
                        mUsuario.setCpf(cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_CPF)));
                    }
                    if (cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_DATA_NASC)) != null) {
                        String data = cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_DATA_NASC));
                        if (data != null) {
                            mUsuario.setData_nasc(cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_DATA_NASC)));
                        }
                    }
                    if (cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_CNH)) != null) {
                        mUsuario.setCnh(cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_CNH)));
                    }
                    if (cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_CNH_VALIDADE)) != null) {
                        String data = cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_CNH_VALIDADE));
                        if (data != null) {
                            mUsuario.setCnh_validade(cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_CNH_VALIDADE)));
                        }
                    }
                    if (cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_CRLV)) != null) {
                        mUsuario.setCrlv(cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_CRLV)));
                    }
                    if (cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_PLACA_MOTO)) != null) {
                        mUsuario.setPlaca_moto(cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_PLACA_MOTO)));
                    }

                    mUsuario.setTipo_veiculo(cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_TIPO_VEICULO)));


                }

                cursor.close();

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
