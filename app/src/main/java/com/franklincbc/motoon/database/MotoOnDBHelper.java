package com.franklincbc.motoon.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.franklincbc.motoon.Contract.SolicitacaoContract;
import com.franklincbc.motoon.Contract.UsuarioContract;

/**
 * Created by Priscila on 17/10/2016.
 */

public class MotoOnDBHelper extends SQLiteOpenHelper {

        public static final String NOME_BANCO = "dbMotoOn.db";
        public static final int VERSAO_BANCO = 4;
        //public static final int VERSAO_BANCO = 3;
        //public static final int VERSAO_BANCO = 2;

        public MotoOnDBHelper(Context context) {
                super(context, NOME_BANCO, null, VERSAO_BANCO); }

        @Override
        public void onCreate(SQLiteDatabase db) {

            //Cria tabela Usuario
            db.execSQL(
                    "CREATE TABLE " + UsuarioContract.TABLE_NAME + " (" +
                            UsuarioContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            UsuarioContract.COL_ID_SERVIDOR + " INTEGER, " +
                            UsuarioContract.COL_MOTOTAXI_ID + " INTEGER, " +
                            UsuarioContract.COL_NOME + " TEXT, " +
                            UsuarioContract.COL_EMAIL + " TEXT, " +
                            UsuarioContract.COL_DATA_CADASTRO + " TEXT, " +
                            UsuarioContract.COL_LATITUDE_ATUAL + " DOUBLE, " +
                            UsuarioContract.COL_LONGITUDE_ATUAL + " DOUBLE, " +
                            UsuarioContract.COL_ESTADO + " TEXT, " +
                            UsuarioContract.COL_CIDADE + " TEXT, " +
                            UsuarioContract.COL_BAIRRO + " TEXT, " +
                            UsuarioContract.COL_CELULAR + " TEXT, " +
                            UsuarioContract.COL_SEXO + " TEXT, " +
                            UsuarioContract.COL_CPF + " TEXT, " +
                            UsuarioContract.COL_DATA_NASC + " TEXT, " +
                            UsuarioContract.COL_CNH + " TEXT, " +
                            UsuarioContract.COL_CNH_VALIDADE + " TEXT, " +
                            UsuarioContract.COL_CRLV + " TEXT, " +
                            UsuarioContract.COL_PLACA_MOTO + " TEXT, " +
                            UsuarioContract.COL_SN_ACEITOU_TERMO + " TEXT, " +
                            UsuarioContract.COL_SN_MOTOTAXI + " TEXT, " +
                            UsuarioContract.COL_SN_DISPONIVEL + " TEXT, " +
                            UsuarioContract.COL_DATA_ULT_ATUALIZACAO + " TEXT, " +
                            UsuarioContract.COL_TIPO_VEICULO + " TEXT, " +
                            UsuarioContract.COL_URL_PHOTO + " TEXT, " +

                            UsuarioContract.COL_IS_ATENDIMENTO + " TEXT DEFAULT 'N', " +
                            UsuarioContract.COL_ATEND_SOLICITACAO_ID + " INTEGER, " +
                            UsuarioContract.COL_ATEND_SOLICITACAO_USUARIO_ID + " INTEGER, " +
                            UsuarioContract.COL_ATEND_SOLICITACAO_sTATUS_SOL + " TEXT " +

                            ")"

            );

            //Cria tabela Solicitacao
            db.execSQL(
                    "CREATE TABLE " + SolicitacaoContract.TABLE_NAME + " (" +
                            SolicitacaoContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            SolicitacaoContract.COL_SOLICITACAO_ID + " INTEGER, " +
                            SolicitacaoContract.COL_USUARIO_ID + " INTEGER, " +
                            SolicitacaoContract.COL_MOTOTAXI_ID + " INTEGER, " +
                            SolicitacaoContract.COL_SOLICITANTE + " TEXT, " +
                            SolicitacaoContract.COL_DATA_SOLICITACAO + " DATE, " +
                            SolicitacaoContract.COL_LATITUDE + " DOUBLE, " +
                            SolicitacaoContract.COL_LONGITUDE + " DOUBLE, " +
                            SolicitacaoContract.COL_LOCAL_ORIGEM + " TEXT, " +
                            SolicitacaoContract.COL_PONTO_REFERENCIA + " TEXT, " +
                            SolicitacaoContract.COL_LOCAL_DESTINO + " TEXT, " +
                            SolicitacaoContract.COL_INFORMACAO_ADICIONAL + " TEXT, " +
                            SolicitacaoContract.COL_CIDADE + " TEXT, " +
                            SolicitacaoContract.COL_BAIRRO + " TEXT, " +
                            SolicitacaoContract.COL_TIPO_VEICULO + " TEXT, " +
                            SolicitacaoContract.COL_DATA_ATENDIMENTO + " TEXT, " +
                            SolicitacaoContract.COL_DATA_CANCELAMENTO + " TEXT, " +
                            SolicitacaoContract.COL_DATA_INI_CORRIDA + " TEXT, " +
                            SolicitacaoContract.COL_DATA_FIM_CORRIDA + " TEXT, " +
                            SolicitacaoContract.COL_STATUS_SOL + " TEXT, " +

                            SolicitacaoContract.COL_LATITUDE_DESTINO + " DOUBLE, " +
                            SolicitacaoContract.COL_LONGITUDE_DESTINO + " DOUBLE, " +
                            SolicitacaoContract.COL_DISTANCIA_PRESUMIDA + " DOUBLE, " +
                            SolicitacaoContract.COL_FAIXA_PRECO_PRESUMIDO + " TEXT, " +
                            SolicitacaoContract.COL_DISTANCIA_CALCULADA + " DOUBLE, " +
                            SolicitacaoContract.COL_FAIXA_PRECO_CALCULADO + " TEXT, " +
                            SolicitacaoContract.COL_VALOR_CORRIDA + " DOUBLE, " +
                            SolicitacaoContract.COL_TIPO_DESCONTO_MOTO + " TEXT " +

                            ")"

            );


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            if(newVersion > oldVersion){

                if(newVersion == 4){
                    //14/02/2017
                    db.execSQL("ALTER TABLE " + UsuarioContract.TABLE_NAME + " ADD COLUMN " + UsuarioContract.COL_IS_ATENDIMENTO + " TEXT DEFAULT 'N' "  );
                    db.execSQL("ALTER TABLE " + UsuarioContract.TABLE_NAME + " ADD COLUMN " + UsuarioContract.COL_ATEND_SOLICITACAO_ID + " INTEGER "  );
                    db.execSQL("ALTER TABLE " + UsuarioContract.TABLE_NAME + " ADD COLUMN " + UsuarioContract.COL_ATEND_SOLICITACAO_USUARIO_ID + " INTEGER "  );
                    db.execSQL("ALTER TABLE " + UsuarioContract.TABLE_NAME + " ADD COLUMN " + UsuarioContract.COL_ATEND_SOLICITACAO_sTATUS_SOL + " TEXT "  );

                }
                else if(newVersion == 3){

                    db.execSQL("ALTER TABLE " + SolicitacaoContract.TABLE_NAME + " ADD COLUMN " + SolicitacaoContract.COL_LATITUDE_DESTINO + " DOUBLE "  );
                    db.execSQL("ALTER TABLE " + SolicitacaoContract.TABLE_NAME + " ADD COLUMN " + SolicitacaoContract.COL_LONGITUDE_DESTINO + " DOUBLE "  );
                    db.execSQL("ALTER TABLE " + SolicitacaoContract.TABLE_NAME + " ADD COLUMN " + SolicitacaoContract.COL_DISTANCIA_PRESUMIDA + " DOUBLE "  );
                    db.execSQL("ALTER TABLE " + SolicitacaoContract.TABLE_NAME + " ADD COLUMN " + SolicitacaoContract.COL_FAIXA_PRECO_PRESUMIDO + " TEXT "  );
                    db.execSQL("ALTER TABLE " + SolicitacaoContract.TABLE_NAME + " ADD COLUMN " + SolicitacaoContract.COL_DISTANCIA_CALCULADA + " DOUBLE "  );
                    db.execSQL("ALTER TABLE " + SolicitacaoContract.TABLE_NAME + " ADD COLUMN " + SolicitacaoContract.COL_FAIXA_PRECO_CALCULADO + " TEXT "  );
                    db.execSQL("ALTER TABLE " + SolicitacaoContract.TABLE_NAME + " ADD COLUMN " + SolicitacaoContract.COL_VALOR_CORRIDA + " DOUBLE "  );
                    db.execSQL("ALTER TABLE " + SolicitacaoContract.TABLE_NAME + " ADD COLUMN " + SolicitacaoContract.COL_TIPO_DESCONTO_MOTO + " TEXT "  );


                }
                else if(newVersion == 2){
                    db.execSQL("ALTER TABLE " + UsuarioContract.TABLE_NAME + " ADD COLUMN " + UsuarioContract.COL_SN_DISPONIVEL + " TEXT "  );
                }

            }

            //db.execSQL("drop table " + UsuarioContract.TABLE_NAME);
            //db.execSQL("drop table " + SolicitacaoContract.TABLE_NAME);
            //onCreate(db);
        }

}
