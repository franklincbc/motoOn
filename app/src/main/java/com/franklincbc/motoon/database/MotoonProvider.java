package com.franklincbc.motoon.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.franklincbc.motoon.Contract.SolicitacaoContract;
import com.franklincbc.motoon.Contract.UsuarioContract;

/**
 * Created by Priscila on 17/10/2016.
 */
public class MotoonProvider extends ContentProvider {

    public static final String AUTHORITY = "com.franklincbc.motoon";
    public static final String PATH_USUARIO = "usuario";
    public static final String PATH_SOLICITACAO = "solicitacao";

    public static Uri BASE_URI = Uri.parse("content://"+AUTHORITY);//BASE_URI
    public static Uri USUARIO_URI = BASE_URI.withAppendedPath(BASE_URI, PATH_USUARIO); //USUARIO_URI
    public static Uri SOLICITACAO_URI = BASE_URI.withAppendedPath(BASE_URI, PATH_SOLICITACAO); //SOLICITACAO_URI

    public static final int TYPE_GENERIC = 0; //Usada no insert e query = content://com.franklincbc.motoon/usuario
    public static final int TYPE_ID = 1; //PorID, usada no delete e query = content://com.franklincbc.motoon/usuario/{id}

    private UriMatcher mMatcher;
    private MotoOnDBHelper mHelper;

    public MotoonProvider() {
        mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //Usuario
        mMatcher.addURI(AUTHORITY, PATH_USUARIO, TYPE_GENERIC);
        mMatcher.addURI(AUTHORITY, PATH_USUARIO + "/#", TYPE_ID);

        //Solicitacao
        mMatcher.addURI(AUTHORITY, PATH_SOLICITACAO, TYPE_GENERIC);
        mMatcher.addURI(AUTHORITY, PATH_SOLICITACAO + "/#", TYPE_ID);

    }

    @Override
    public boolean onCreate() {
        mHelper = new MotoOnDBHelper(getContext());
        return true;
    }


    @Nullable
    @Override
    public String getType(Uri uri) {
        int uriType = mMatcher.match(uri);
        switch (uriType){
            case TYPE_GENERIC:
                //retorna vários registros
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY;
            case TYPE_ID:
                //retornará um unico registro
                return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY;
            default:
                throw new IllegalArgumentException("URI Inválida");
        }

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int uriType = mMatcher.match(uri);

        if(uriType == TYPE_GENERIC){

            if(uri.equals(USUARIO_URI)) {
                SQLiteDatabase db = mHelper.getWritableDatabase();
                long id = db.insertWithOnConflict(UsuarioContract.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                db.close();
                //se der erro o id retornado é -1
                if (id == -1) {
                    throw new RuntimeException("Erro ao inserir o usuário.");
                }
                notifyChanges(uri);
                return ContentUris.withAppendedId(USUARIO_URI, id);
            }
            else
            if (uri.equals(SOLICITACAO_URI)) {
                SQLiteDatabase db = mHelper.getWritableDatabase();
                long id = db.insertWithOnConflict(SolicitacaoContract.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                db.close();
                //se der erro o id retornado é -1
                if (id == -1) {
                    throw new RuntimeException("Erro ao inserir o usuário.");
                }
                notifyChanges(uri);
                return ContentUris.withAppendedId(SOLICITACAO_URI, id);
            }
            else
            {
                throw new IllegalArgumentException("Uri inválida");
            }

        }
        else
        {
            throw new IllegalArgumentException("Uri inválida");
        }


    }


    @Override
    public int delete(Uri uri, String s, String[] strings) {
        int uriType = mMatcher.match(uri);
        //implementado para so aceitar exclusao baseada no id
        if(uriType == TYPE_ID) {
            if(uri.equals(USUARIO_URI)) {
                SQLiteDatabase db = mHelper.getWritableDatabase();
                long id = ContentUris.parseId(uri);
                int rowsAffected = db.delete(
                        UsuarioContract.TABLE_NAME,
                        UsuarioContract._ID + " =?",
                        new String[]{String.valueOf(id)});
                db.close();
                // Se nenhuma linha foi afetada pela exclusao, levantamos uma excecao
                if (rowsAffected == 0) {
                    throw new RuntimeException("Falha ao apagar usuário");
                }
                notifyChanges(uri);
                return rowsAffected;
            }
            else if (uri.equals(SOLICITACAO_URI)){

                SQLiteDatabase db = mHelper.getWritableDatabase();
                long id = ContentUris.parseId(uri);
                int rowsAffected = db.delete(
                        SolicitacaoContract.TABLE_NAME,
                        SolicitacaoContract._ID + " =?",
                        new String[]{String.valueOf(id)});
                db.close();
                // Se nenhuma linha foi afetada pela exclusao, levantamos uma excecao
                if (rowsAffected == 0) {
                    throw new RuntimeException("Falha ao apagar usuário");
                }
                notifyChanges(uri);
                return rowsAffected;

            }
            else
            {
                throw new IllegalArgumentException("Uri inválida");
            }

        }
        if(uriType == TYPE_GENERIC){
            //Não funciona por ID, inclui como generic
            if(uri.equals(USUARIO_URI)) {
                SQLiteDatabase db = mHelper.getWritableDatabase();
                //long id = ContentUris.parseId(uri);
                int rowsAffected = db.delete(
                        UsuarioContract.TABLE_NAME,
                        s,
                        strings);
                db.close();
                // Se nenhuma linha foi afetada pela exclusao, levantamos uma excecao
                if (rowsAffected == 0) {
                    throw new RuntimeException("Falha ao apagar usuário");
                }
                notifyChanges(uri);
                return rowsAffected;
            }
            else if (uri.equals(SOLICITACAO_URI)){

                SQLiteDatabase db = mHelper.getWritableDatabase();
                long id = ContentUris.parseId(uri);
                int rowsAffected = db.delete(
                        SolicitacaoContract.TABLE_NAME,
                        SolicitacaoContract._ID + " =?",
                        new String[]{String.valueOf(id)});
                db.close();
                // Se nenhuma linha foi afetada pela exclusao, levantamos uma excecao
                if (rowsAffected == 0) {
                    throw new RuntimeException("Falha ao apagar a solicitação");
                }
                notifyChanges(uri);
                return rowsAffected;

            }
            else
            {
                throw new IllegalArgumentException("Uri inválida");
            }
        }
        else {
            throw new IllegalArgumentException("Uri inválida");
        }



    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String where, String[] args) {
        //Implementar futuramente
        int uriType = mMatcher.match(uri);
        //implementado para so aceitar exclusao baseada no id
        if(uriType == TYPE_ID) {
            //Não esta funcionando quando é por type_id ?????????????????????????
            if( uri.equals(USUARIO_URI) ) {
                SQLiteDatabase db = mHelper.getWritableDatabase();
                long id = ContentUris.parseId(uri);
                int rowsAffected = db.updateWithOnConflict(
                        UsuarioContract.TABLE_NAME,
                        contentValues,
                        UsuarioContract._ID + " =?",
                        new String[]{String.valueOf(id)},
                        SQLiteDatabase.CONFLICT_REPLACE);
                db.close();
                // Se nenhuma linha foi afetada pela exclusao, levantamos uma excecao
                if (rowsAffected == 0) {
                    throw new RuntimeException("Falha ao atualizar usuário");
                }
                notifyChanges(uri);
                return rowsAffected;
            }
            else if (uri.equals(SOLICITACAO_URI)){

                SQLiteDatabase db = mHelper.getWritableDatabase();
                long id = ContentUris.parseId(uri);
                int rowsAffected = db.updateWithOnConflict(
                        SolicitacaoContract.TABLE_NAME,
                        contentValues,
                        SolicitacaoContract._ID + " =?",
                        new String[]{String.valueOf(id)},
                        SQLiteDatabase.CONFLICT_REPLACE);
                db.close();
                // Se nenhuma linha foi afetada pela exclusao, levantamos uma excecao
                if (rowsAffected == 0) {
                    throw new RuntimeException("Falha ao atualizar solicitacao");
                }
                notifyChanges(uri);
                return rowsAffected;

            }
            else
            {
                throw new IllegalArgumentException("Uri inválida");
            }

        }

        //so funciona desse modo genery
        else if(uriType == TYPE_GENERIC) {
            //Não estava funcionando, entao retirei o ID anexado a uri e estou utilizando a dos parametros
            if( uri.equals(USUARIO_URI) ) {
                SQLiteDatabase db = mHelper.getWritableDatabase();
               //long id = ContentUris.parseId(uri);
                int rowsAffected = db.updateWithOnConflict(
                        UsuarioContract.TABLE_NAME,
                        contentValues,
                        where,
                        args,
                        SQLiteDatabase.CONFLICT_REPLACE);
                db.close();
                // Se nenhuma linha foi afetada pela exclusao, levantamos uma excecao
                if (rowsAffected == 0) {
                    throw new RuntimeException("Falha ao atualizar usuário");
                }
                notifyChanges(uri);
                return rowsAffected;
            }
            else if (uri.equals(SOLICITACAO_URI)){

                SQLiteDatabase db = mHelper.getWritableDatabase();
                long id = ContentUris.parseId(uri);
                int rowsAffected = db.updateWithOnConflict(
                        SolicitacaoContract.TABLE_NAME,
                        contentValues,
                        SolicitacaoContract._ID + " =?",
                        new String[]{String.valueOf(id)},
                        SQLiteDatabase.CONFLICT_REPLACE);
                db.close();
                // Se nenhuma linha foi afetada pela exclusao, levantamos uma excecao
                if (rowsAffected == 0) {
                    throw new RuntimeException("Falha ao atualizar solicitacao");
                }
                notifyChanges(uri);
                return rowsAffected;

            }
            else
            {
                throw new IllegalArgumentException("Uri inválida");
            }

        }
        else {
            throw new IllegalArgumentException("Uri inválida");
        }

    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int uriType = mMatcher.match(uri);
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = null;

        switch (uriType){
            case TYPE_GENERIC:
                //Busca genérica, listagem geral
                if(uri.equals(USUARIO_URI)) {
                    cursor = db.query(UsuarioContract.TABLE_NAME,
                            projection, selection, selectionArgs, null, null, sortOrder);
                }

                else if(uri.equals(SOLICITACAO_URI)){
                    cursor = db.query(SolicitacaoContract.TABLE_NAME,
                            projection, selection, selectionArgs, null, null, sortOrder);
                }

                break;

            case TYPE_ID:
                //Traz informacoes de  um registro especifico
                if(uri.equals(USUARIO_URI)) {
                    long id = ContentUris.parseId(uri);
                    cursor = db.query(UsuarioContract.TABLE_NAME,
                            projection, UsuarioContract._ID + " = ?",
                            new String[]{String.valueOf(id)}, null, null, sortOrder);
                }
                else if (uri.equals(SOLICITACAO_URI)) {
                    long id = ContentUris.parseId(uri);
                    cursor = db.query(SolicitacaoContract.TABLE_NAME,
                            projection, SolicitacaoContract._ID + " = ?",
                            new String[]{String.valueOf(id)}, null, null, sortOrder);
                }

                break;

            default:
                throw new IllegalArgumentException("Uri Invalida");

        }

        if(cursor == null) {
            return null;
        }

        if (getContext() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }



    private void notifyChanges(Uri uri) {
        //Caso a operação no banco ocorra sem problemas, notiicamos a uri
        if(getContext()!= null){
            getContext().getContentResolver().notifyChange(uri,null);
        }
    }


}
