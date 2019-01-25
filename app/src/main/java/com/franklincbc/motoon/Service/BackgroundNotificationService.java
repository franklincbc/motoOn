package com.franklincbc.motoon.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import com.franklincbc.motoon.ChatActivity;
import com.franklincbc.motoon.Contract.UsuarioContract;
import com.franklincbc.motoon.MainActivity;
import com.franklincbc.motoon.PainelChamadosActivity;
import com.franklincbc.motoon.R;
import com.franklincbc.motoon.database.MotoonProvider;
import com.franklincbc.motoon.http.SolicitacaoHttp;
import com.franklincbc.motoon.http.UsuarioHttp;
import com.franklincbc.motoon.model.Solicitacao;
import com.franklincbc.motoon.model.Usuario;

import java.util.List;

import static com.franklincbc.motoon.MainActivity.ACTIVITY_NOTIFICATION_CHAT_SERVICE;
import static com.franklincbc.motoon.MainActivity.ACTIVITY_NOTIFICATION_SERVICE;
import static com.franklincbc.motoon.Utils.Util.verificaConexao;

/**
 * Created by Priscila on 26/12/2016.
 */

public class BackgroundNotificationService extends Service {

    public static final String BROADCAST_NETWORK_CHANGE_RECEIVER = "com.franklincbc.motoon.SEND_BROADCAST_TESTE";

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    public String mCidade = "";
    public String mTipoVeiculo = "M";
    public static final int mIdNotificacao = 0;

    Intent mIt;
    Integer mFlags;
    Integer mStartId;

    Usuario mUsuario;
    String mUlt_Status_Sol = "A";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //Toast.makeText(this, "Service created!", Toast.LENGTH_LONG).show();

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                //Toast.makeText(context, "Service is still running", Toast.LENGTH_LONG).show();

                if (verificaConexao(context)){
                    try {
                        //Carrega dados do usuario
                        CarregaUsuario(context);

                        if(mUsuario.getSn_mototaxi()!=null&&mUsuario.getSn_mototaxi().equals("S")) {
                            if (mUsuario.getSn_disponivel().equals("S")) {
                                Thread.sleep(3000);
                                ChamaAsyncTaskQtdSolicitacao();
                            }
                        }

                        if(mUsuario.getIsAtendimento().equals("S")) {

                            //Ambos
                            Thread.sleep(3000);
                            ChamaAsyncTaskQtdNovasMensagensChat();

                            if(mUsuario.getSn_mototaxi()!=null && mUsuario.getSn_mototaxi().equals("N")) {
                                Thread.sleep(3000);
                                CarregaSolicitacaoCorrenteUsuarioServiceTask task  = new CarregaSolicitacaoCorrenteUsuarioServiceTask();
                                task.execute();
                            }

                        }else{
                            mUlt_Status_Sol = "A";
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                handler.postDelayed(runnable, 50000);
            }
        };
        handler.postDelayed(runnable, 50000);

    }

    private void ChamaAsyncTaskQtdSolicitacao() {
        try {
            RetornaQtdSolicitacaoTask retornaQtdSolicitacaoTask = new RetornaQtdSolicitacaoTask();
            retornaQtdSolicitacaoTask.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void ChamaAsyncTaskQtdNovasMensagensChat() {
        try {
            RetornaQtdNovasMensagensChatTask retornaQtdNovasMensagensChat = new RetornaQtdNovasMensagensChatTask();
            retornaQtdNovasMensagensChat.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void ChamaAsyncTaskCarregaSolicitacaoCorrenteUsuario() {
        try {
            RetornaQtdNovasMensagensChatTask retornaQtdNovasMensagensChat = new RetornaQtdNovasMensagensChatTask();
            retornaQtdNovasMensagensChat.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            if(intent != null) {
                mIt = intent;
                mFlags = flags;
                mStartId = startId;

                mCidade = intent.getStringExtra("cidade");
                mTipoVeiculo = intent.getStringExtra("tipoveiculo");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //return super.onStartCommand(intent, flags, startId);
        return START_REDELIVER_INTENT;

    }

    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        //handler.removeCallbacks(runnable);
        //Toast.makeText(this, "Service Notification stopped", Toast.LENGTH_LONG).show();

    }

    final class RetornaQtdSolicitacaoTask extends AsyncTask<String, Void, Integer>{

        @Override
        protected Integer doInBackground(String... params) {
            return new UsuarioHttp().RetornaQtdSolicitacaoCidade(mCidade, mTipoVeiculo);
        }

        @Override
        protected void onPostExecute(Integer qtd) {
            super.onPostExecute(qtd);
            Integer quant = (Integer) qtd;
            if(quant != null) {
                if (quant > 0) {
                    try {
                        CriaNotificacaoMoticiclista(quant);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    final class RetornaQtdNovasMensagensChatTask extends AsyncTask<String, Void, Integer>{

        @Override
        protected Integer doInBackground(String... params) {
            Integer mototaxi_id = 0;
            if(mUsuario.getSn_mototaxi()!=null&&mUsuario.getSn_mototaxi().equals("S")){
                mototaxi_id = mUsuario.getMototaxi_id();
            }
            return new UsuarioHttp().RetornaQtdNovasMensagensChat(0, 0, mUsuario.getId_servidor(), mototaxi_id, "S",mUsuario.getSn_mototaxi() );
        }

        @Override
        protected void onPostExecute(Integer qtd) {
            super.onPostExecute(qtd);
            Integer quant = (Integer) qtd;
            if(quant != null) {
                if (quant > 0) {
                    try {
                        CriaNotificacaoNovaMensagemChat(quant);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

        }
    }


    final class CarregaSolicitacaoCorrenteUsuarioServiceTask extends AsyncTask<String, Void, List<Solicitacao>>{

        @Override
        protected List<Solicitacao> doInBackground(String... params) {
            return new SolicitacaoHttp().carregaSolicitacaoCorrenteUsuario(mUsuario.getId_servidor());
        }

        @Override
        protected void onPostExecute(List<Solicitacao> lst) {
            super.onPostExecute(lst);
            List<Solicitacao> resultLstSolicitacao = (List)lst;
            for (int i = 0; i < resultLstSolicitacao.size(); i++){
                Solicitacao solicitacao = resultLstSolicitacao.get(i);
                if(solicitacao.getStatus_sol()!=null && (!solicitacao.getStatus_sol().equals( mUlt_Status_Sol )) ){
                    try {
                        CriaNotificacaoStatusSolicitacaoUsuario(solicitacao.getStatus_sol());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                mUlt_Status_Sol = solicitacao.getStatus_sol();
            }

        }
    }


    public void CriaNotificacaoMoticiclista(Integer qtd){

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setContentTitle("MotoOn")
                .setContentText("Existe(m) " + qtd + " solicitação(ões) de moto!")
                .setSmallIcon(R.mipmap.ic_motoon_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_motoon_icon))
                .setSound(som);



        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        resultIntent.putExtra(ACTIVITY_NOTIFICATION_SERVICE, PainelChamadosActivity.class.getName());

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);


        Notification notification = mBuilder.build();
        notification.vibrate = new long[]{150, 300, 150, 600};

        // mId allows you to update the notification later on.
        mNotificationManager.notify(mIdNotificacao, notification);

        /*
        try{
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(context, som);
            toque.play();
        }
        catch (Exception e){
            e.printStackTrace();
        } */

    }


    public void CriaNotificacaoNovaMensagemChat(Integer qtd){

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setContentTitle("CHAT MotoOn")
                .setContentText("Você recebeu " + qtd + " nova(s) mensagem(ens)")
                .setSmallIcon(R.mipmap.ic_motoon_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_motoon_icon))
                .setSound(som);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        resultIntent.putExtra(ACTIVITY_NOTIFICATION_CHAT_SERVICE, ChatActivity.class.getName());

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);


        Notification notification = mBuilder.build();
        notification.vibrate = new long[]{150, 300, 150, 600};

        // mId allows you to update the notification later on.
        mNotificationManager.notify(mIdNotificacao, notification);

        /*
        try{
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(context, som);
            toque.play();
        }
        catch (Exception e){
            e.printStackTrace();
        } */

    }

    public void CriaNotificacaoStatusSolicitacaoUsuario(String status){

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String msg = "";
        if(status.equals("E")) {
            msg = "Um Parceiro acaba de atender sua solicitação";
        }
        else if(status.equals("I")) {
            msg = "Sua corrida foi iniciada, Boa Viagem!";
        }
        else if(status.equals("F")) {
            msg = "Sua solicitação foi finalizada";
        }

        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setContentTitle("MotoOn")
                .setContentText(msg)
                .setSmallIcon(R.mipmap.ic_motoon_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_motoon_icon))
                .setSound(som);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        resultIntent.putExtra(ACTIVITY_NOTIFICATION_CHAT_SERVICE, ChatActivity.class.getName());

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);


        Notification notification = mBuilder.build();
        notification.vibrate = new long[]{150, 300, 150, 600};

        // mId allows you to update the notification later on.
        mNotificationManager.notify(mIdNotificacao, notification);

    }



    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopSelf(mStartId);

        Intent intent = new Intent();
        intent.setAction(BROADCAST_NETWORK_CHANGE_RECEIVER);
        sendBroadcast(intent);

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

                    if (cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_IS_ATENDIMENTO)) != null) {
                        mUsuario.setIsAtendimento(cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_IS_ATENDIMENTO)));
                    }

                    if (cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_ATEND_SOLICITACAO_ID)) != null) {
                        mUsuario.setAtendSolicitacao_Id(cursor.getInt(cursor.getColumnIndex(UsuarioContract.COL_ATEND_SOLICITACAO_ID)));
                    }

                    if (cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_ATEND_SOLICITACAO_USUARIO_ID)) != null) {
                        mUsuario.setAtendSolicitacao_UsuarioId(cursor.getInt(cursor.getColumnIndex(UsuarioContract.COL_ATEND_SOLICITACAO_USUARIO_ID)));
                    }

                    if (cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_ATEND_SOLICITACAO_sTATUS_SOL)) != null) {
                        mUsuario.setAtendSolicitacao_StatusSol(cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_ATEND_SOLICITACAO_sTATUS_SOL)));
                    }

                }

                cursor.close();

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
