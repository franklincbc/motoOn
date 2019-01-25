package com.franklincbc.motoon.Service;

import android.app.IntentService;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.os.ResultReceiver;

import com.franklincbc.motoon.Contract.SolicitacaoContract;
import com.franklincbc.motoon.Contract.UsuarioContract;
import com.franklincbc.motoon.Utils.Constants;
import com.franklincbc.motoon.database.MotoonProvider;
import com.franklincbc.motoon.model.Solicitacao;
import com.franklincbc.motoon.model.Usuario;

/**
 * Created by Priscila on 21/10/2016.
 */

public class DataService extends IntentService {

    protected MotoonProvider provider;
    protected ResultReceiver resultReceiver;
    public static final String TAG = "data service";

    public DataService() {
        super("DataService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        provider = new MotoonProvider();
        String errorMessage = null;

        resultReceiver = intent.getParcelableExtra(Constants.RECEIVER);
        int codeTable = intent.getIntExtra(Constants.CODE_TABLE_EXTRA, 0);
        int codeOperation = intent.getIntExtra(Constants.CODE_OPERATION_EXTRA, 0);

        if(codeTable == Constants.CODE_TABLE_USUARIO){

            Usuario usuario = (Usuario) intent.getSerializableExtra(Constants.USUARIO_EXTRA);

            //INSERT
            if(codeOperation == Constants.OPERATION_INSERT){
                //Inserir no banco Local - Provider
                ContentValues contentValues = new ContentValues();
                contentValues.put(UsuarioContract.COL_ID_SERVIDOR, usuario.getId_servidor());
                contentValues.put(UsuarioContract.COL_NOME, usuario.getNome());
                contentValues.put(UsuarioContract.COL_EMAIL, usuario.getEmail());
                contentValues.put(UsuarioContract.COL_URL_PHOTO, usuario.getUrl_photo());

                Uri uri = getApplicationContext().getContentResolver().insert(MotoonProvider.USUARIO_URI, contentValues);
                Long id = ContentUris.parseId(uri);
                deliverResultToReceiver(Constants.SUCCESS_RESULT, codeTable, codeOperation, null, id, null);

            }
            else if (codeOperation == Constants.OPERATION_UPDATE) {

                //Atualizar no banco Local - Provider
                ContentValues contentValues = new ContentValues();
                contentValues.put(UsuarioContract.COL_MOTOTAXI_ID, usuario.getMototaxi_id());
                contentValues.put(UsuarioContract.COL_CELULAR, usuario.getCelular());
                contentValues.put(UsuarioContract.COL_NOME, usuario.getNome());
                contentValues.put(UsuarioContract.COL_SEXO, usuario.getSexo());
                contentValues.put(UsuarioContract.COL_LATITUDE_ATUAL, usuario.getLatitude_atual());
                contentValues.put(UsuarioContract.COL_LONGITUDE_ATUAL, usuario.getLongitude_atual());
                contentValues.put(UsuarioContract.COL_ESTADO, usuario.getEstado());
                contentValues.put(UsuarioContract.COL_CIDADE, usuario.getCidade());
                contentValues.put(UsuarioContract.COL_BAIRRO, usuario.getBairro());

                contentValues.put(UsuarioContract.COL_CPF, usuario.getCpf());
                if(usuario.getData_nasc() != null ) {
                    contentValues.put(UsuarioContract.COL_DATA_NASC, usuario.getData_nasc().toString());
                }
                contentValues.put(UsuarioContract.COL_CNH, usuario.getCnh());
                if(usuario.getCnh_validade() != null) {
                    contentValues.put(UsuarioContract.COL_CNH_VALIDADE, usuario.getCnh_validade().toString());
                }
                contentValues.put(UsuarioContract.COL_CRLV, usuario.getCrlv());
                contentValues.put(UsuarioContract.COL_PLACA_MOTO, usuario.getPlaca_moto());

                contentValues.put(UsuarioContract.COL_SN_ACEITOU_TERMO, usuario.getSn_aceitou_termo());
                contentValues.put(UsuarioContract.COL_SN_MOTOTAXI, usuario.getSn_mototaxi());
                contentValues.put(UsuarioContract.COL_TIPO_VEICULO, usuario.getTipo_veiculo());
                contentValues.put(UsuarioContract.COL_SN_DISPONIVEL, usuario.getSn_disponivel());

                contentValues.put(UsuarioContract.COL_IS_ATENDIMENTO, usuario.getIsAtendimento());
                contentValues.put(UsuarioContract.COL_ATEND_SOLICITACAO_ID, usuario.getAtendSolicitacao_Id());
                contentValues.put(UsuarioContract.COL_ATEND_SOLICITACAO_USUARIO_ID, usuario.getAtendSolicitacao_UsuarioId());
                contentValues.put(UsuarioContract.COL_ATEND_SOLICITACAO_sTATUS_SOL, usuario.getAtendSolicitacao_StatusSol());

                Long id = usuario.getId();
                String WhereClausula = UsuarioContract._ID + " = ?";
                String[] Args = {String.valueOf(id)};

                //Não funciona quando é type_id
                //Uri uri = BASE_URI.withAppendedPath( MotoonProvider.USUARIO_URI,"/" + String.valueOf(id));

                Integer rowsAffected = getApplicationContext().getContentResolver().update(MotoonProvider.USUARIO_URI, contentValues, WhereClausula, Args);
                deliverResultToReceiver(Constants.SUCCESS_RESULT, codeTable, codeOperation, null, Long.valueOf(rowsAffected), null);

            }
            else if(codeOperation == Constants.OPERATION_DELETE){

            }
            else if (codeOperation == Constants.OPERATION_QUERY){
                Cursor cursor =  getApplicationContext().getContentResolver().query(MotoonProvider.USUARIO_URI, null, null, null, null);
                deliverResultToReceiver(Constants.SUCCESS_RESULT, codeTable, codeOperation, null, null, cursor);
            }


        }


        else if(codeTable == Constants.CODE_TABLE_SOLICITACAO){

            Solicitacao solicitacao = (Solicitacao) intent.getSerializableExtra(Constants.SOLICITACAO_EXTRA);

            if(codeOperation == Constants.OPERATION_INSERT){

                //Inserir no banco Local - Provider
                ContentValues contentValues = new ContentValues();
                contentValues.put(SolicitacaoContract.COL_SOLICITACAO_ID, solicitacao.getSolicitacao_id() );
                contentValues.put(SolicitacaoContract.COL_USUARIO_ID, solicitacao.getUsuario_id());
                contentValues.put(SolicitacaoContract.COL_SOLICITANTE, solicitacao.getSolicitante());
                contentValues.put(SolicitacaoContract.COL_DATA_SOLICITACAO, solicitacao.getData_solicitacao());
                contentValues.put(SolicitacaoContract.COL_LATITUDE, solicitacao.getLatitude());
                contentValues.put(SolicitacaoContract.COL_LONGITUDE, solicitacao.getLongitude());
                contentValues.put(SolicitacaoContract.COL_LOCAL_ORIGEM, solicitacao.getLocal_origem());
                contentValues.put(SolicitacaoContract.COL_LOCAL_DESTINO, solicitacao.getLocal_destino());
                contentValues.put(SolicitacaoContract.COL_PONTO_REFERENCIA, solicitacao.getPonto_referencia());
                contentValues.put(SolicitacaoContract.COL_INFORMACAO_ADICIONAL, solicitacao.getInformacao_adicional());
                contentValues.put(SolicitacaoContract.COL_STATUS_SOL, solicitacao.getStatus_sol());
                contentValues.put(SolicitacaoContract.COL_CIDADE, solicitacao.getCidade());
                contentValues.put(SolicitacaoContract.COL_BAIRRO, solicitacao.getBairro());
                contentValues.put(SolicitacaoContract.COL_TIPO_VEICULO, solicitacao.getTipo_veiculo());

                contentValues.put(SolicitacaoContract.COL_DATA_ATENDIMENTO, solicitacao.getData_atendimento());
                contentValues.put(SolicitacaoContract.COL_DATA_CANCELAMENTO, solicitacao.getData_cancelamento());
                contentValues.put(SolicitacaoContract.COL_DATA_INI_CORRIDA, solicitacao.getData_ini_corrida());
                contentValues.put(SolicitacaoContract.COL_DATA_FIM_CORRIDA, solicitacao.getData_fim_corrida());

                contentValues.put(SolicitacaoContract.COL_LATITUDE_DESTINO, solicitacao.getLatitude_destino());
                contentValues.put(SolicitacaoContract.COL_LONGITUDE_DESTINO, solicitacao.getLongitude_destino());
                contentValues.put(SolicitacaoContract.COL_DISTANCIA_PRESUMIDA, solicitacao.getDistancia_presumida());
                contentValues.put(SolicitacaoContract.COL_FAIXA_PRECO_PRESUMIDO, solicitacao.getFaixa_preco_presumido());
                contentValues.put(SolicitacaoContract.COL_DISTANCIA_CALCULADA, solicitacao.getDistancia_calculada());
                contentValues.put(SolicitacaoContract.COL_FAIXA_PRECO_CALCULADO, solicitacao.getFaixa_preco_calculado());
                contentValues.put(SolicitacaoContract.COL_VALOR_CORRIDA, solicitacao.getValor_corrida());
                contentValues.put(SolicitacaoContract.COL_TIPO_DESCONTO_MOTO, solicitacao.getTipo_desconto_moto());


                Uri uri = getApplicationContext().getContentResolver().insert(MotoonProvider.SOLICITACAO_URI, contentValues);
                Long id = ContentUris.parseId(uri);
                deliverResultToReceiver(Constants.SUCCESS_RESULT, codeTable, codeOperation, null, id, null);

            }
            else if (codeOperation == Constants.OPERATION_UPDATE) {

            }
            else if(codeOperation == Constants.OPERATION_DELETE){

            }
            else if (codeOperation == Constants.OPERATION_QUERY){
                Cursor cursor =  getApplicationContext().getContentResolver().query(MotoonProvider.SOLICITACAO_URI, null, null, null, null);
                deliverResultToReceiver(Constants.SUCCESS_RESULT, codeTable, codeOperation, null, null, cursor);
            }

        }
        else {
            errorMessage = "UnKnown Code Table";
            deliverResultToReceiver(Constants.FAILURE_RESULT, codeTable, codeOperation, errorMessage, null, null);
        }


    }


    private void deliverResultToReceiver(int resultCode, int codeTable, int codeOperation, String message, Long i, Cursor cursor) {
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.CODE_TABLE_EXTRA, codeTable);
        bundle.putInt(Constants.CODE_OPERATION_EXTRA, codeOperation);
        bundle.putLong(Constants.RESULT_INSERT, i);
        bundle.putLong(Constants.RESULT_UPDATE, i);
        bundle.putLong(Constants.RESULT_DELETE, i);
        bundle.putString(Constants.RESULT_MESSAGE, message);
        bundle.putParcelable(Constants.RESULT_QUERY, (Parcelable) cursor);
        resultReceiver.send(resultCode, bundle);
    }

}
