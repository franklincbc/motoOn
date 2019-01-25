package com.franklincbc.motoon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.franklincbc.motoon.Contract.UsuarioContract;
import com.franklincbc.motoon.Service.DataService;
import com.franklincbc.motoon.Utils.Constants;
import com.franklincbc.motoon.database.MotoonProvider;
import com.franklincbc.motoon.http.UsuarioPossuiCadastroTask;
import com.franklincbc.motoon.http.UsuarioRegistraUsuarioTask;
import com.franklincbc.motoon.model.Usuario;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import static com.franklincbc.motoon.Utils.Util.verificaConexao;

public class LoginActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener,
                    LoaderManager.LoaderCallbacks<Long> {


    private static final String TAG = "FKN";
    private static final int RC_SIGN_IN = 9001;
    public static final String ID_CLIENTE_OAUTH = "338603102551-iolto19h2n10eul64m6aapnju55ekk5k.apps.googleusercontent.com";
    public static final String EXTRA_USUARIO = "usuario";

    public static final int LOADER_REGISTRA_USU = 0;
    public static final int LOADER_USUARIO_POSSUI_CADASTRO = 1;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    GoogleApiClient mGoogleApiClient;
    GoogleSignInAccount acct;
    MotoonProvider provider;

    DataResultReceiver mDataResultReceiver;

    LoaderManager mLoaderManager;
    Usuario mUsuario;
    private ProgressDialog mDialog;

    TextView txtTermoAdesao;
    Button btnGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    //Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        btnGoogle = (Button)findViewById(R.id.btnGoogle);

        initGoogle();
        provider = new MotoonProvider();
        mDataResultReceiver = new DataResultReceiver(null);

        mLoaderManager = getSupportLoaderManager();
        mUsuario = new Usuario();

        //o nome AQUI ficar sublinhado.  esse texto abre o tero de adesao
        txtTermoAdesao = (TextView)findViewById(R.id.activity_login_termo_Adesao);
        SpannableString content = new SpannableString("AQUI");
        content.setSpan(new UnderlineSpan(),0,content.length(),0);
        txtTermoAdesao.setText(content);


    }

    private void initGoogle() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(ID_CLIENTE_OAUTH)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        if (mGoogleApiClient == null) {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(LoginActivity.this /* FragmentActivity */, LoginActivity.this /* OnConnectionFailedListener */)
                .addOnConnectionFailedListener(this)
                .addApi(AppIndex.API)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        }

    }

    @Override
    public void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        try {
            mGoogleApiClient.connect();
            mAuth.addAuthStateListener(mAuthListener);
            // ATTENTION: This was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            AppIndex.AppIndexApi.start(mGoogleApiClient, getIndexApiAction());
        }catch (Exception e){
            //Log.d("FKN",e.getMessage());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(mGoogleApiClient, getIndexApiAction());
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.disconnect();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    public void btnGoogleOnClick(View view) {
        signIn();
    }

    private void signIn() {
        if (!verificaConexao(this)){
            Snackbar.make(btnGoogle, "SEM CONEXAO COM INTERNET", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        mDialog = ProgressDialog.show(this,"Espere por favor...", "Registrando usuário no servidor...", true);

        //Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Log.d(TAG, "LoginGoogleResult:" + result.isSuccess());
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                acct = result.getSignInAccount();
                String sEmail = acct.getEmail();
                Bundle params = new Bundle();
                params.putString("email", sEmail);
                mLoaderManager.initLoader(LOADER_USUARIO_POSSUI_CADASTRO, params, LoginActivity.this);
                //firebaseAuthWithGoogle(acct);
            } else {
                // Signed out, show unauthenticated UI.
                Toast.makeText(this, "Login Google não efetuado", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

        }
    }


    private void updateUI(boolean b) {
        if (b) {
            Toast.makeText(this, getString(R.string.notaBoaVindas) + ", " + acct.getDisplayName(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.naoAutorizado), Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                //Log.w(TAG, "signInWithCredential", task.getException());
                                Toast.makeText(LoginActivity.this, "Falha na autenticação com Google - credentiais.",
                                        Toast.LENGTH_SHORT).show();
                                mDialog.dismiss();
                            } else {
                                if (checaUsuarioNoBanco() == 0) {
                                    RegistraUsuarioWeb();
                                } else {
                                    mDialog.dismiss();
                                    updateUI(true);
                                    carregarMainActivity();
                                }
                            }
                        }
                    });

    }

    private void carregarMainActivity() {
        finish();
        Intent it = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(it);
    }

    private Integer checaUsuarioNoBanco() {
        Integer count = null;
        Cursor cursor = getApplicationContext().getContentResolver().query(MotoonProvider.USUARIO_URI, null, null, null, null);
        //deleteUsuario(cursor);
        //cursor.close();
        //cursor = getApplicationContext().getContentResolver().query(MotoonProvider.USUARIO_URI, null, null, null, null);

        count = cursor.getCount();
        cursor.close();
        return count;
    }

    //O metodo abaixo é para efeitos de teste, pois apagando o app nao remove o usuario do banco
    private void deleteUsuario(Cursor cursor) {
        if (cursor.moveToFirst()) {
            Long id = cursor.getLong(cursor.getColumnIndex(UsuarioContract._ID));
            String WhereClausula = UsuarioContract._ID + " = ?";
            String[] Args = {String.valueOf(id)};
            //Uri uri = BASE_URI.withAppendedPath( MotoonProvider.USUARIO_URI, "/"+String.valueOf(id));
            Integer count = getApplicationContext().getContentResolver().delete(MotoonProvider.USUARIO_URI, WhereClausula, Args);
            //Toast.makeText(this, "Registro deletado = " + String.valueOf(count), Toast.LENGTH_SHORT).show();

        }
    }

    private void RegistraUsuarioWeb(){
        mUsuario.setEmail(acct.getEmail());
        mUsuario.setNome(acct.getDisplayName());
        mUsuario.setUrl_photo(acct.getPhotoUrl().toString());

        //Inserir na WEB
        Bundle params = new Bundle();
        params.putSerializable(Constants.USUARIO_EXTRA, mUsuario);
        mLoaderManager.initLoader(LOADER_REGISTRA_USU, params, LoginActivity.this);

    }

    private void RegistraUsuarioDataService() {
        Intent intent = new Intent(this, DataService.class);
        intent.putExtra(Constants.RECEIVER, mDataResultReceiver);
        intent.putExtra(Constants.CODE_TABLE_EXTRA, Constants.CODE_TABLE_USUARIO);
        intent.putExtra(Constants.CODE_OPERATION_EXTRA, Constants.OPERATION_INSERT);
        intent.putExtra(Constants.USUARIO_EXTRA, mUsuario);
        startService(intent);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Login Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    //*********** LOADER
    @Override
    public Loader<Long> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_REGISTRA_USU) {
            return new UsuarioRegistraUsuarioTask(this, id, args);
        }
        else if (id == LOADER_USUARIO_POSSUI_CADASTRO){
            return new UsuarioPossuiCadastroTask(this, id, args);
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Long> loader, Long data) {
        if (data != null ){
            if (loader.getId() == LOADER_REGISTRA_USU) {
                mUsuario.setId_servidor(Integer.valueOf(String.valueOf(data)));
                RegistraUsuarioDataService();
            }
            else if (loader.getId() == LOADER_USUARIO_POSSUI_CADASTRO){
                Long value = (Long) data;
                if (value == 1){
                    firebaseAuthWithGoogle(acct);
                } else {
                    Cursor cursor = getApplicationContext().getContentResolver().query(MotoonProvider.USUARIO_URI, null, null, null, null);
                    deleteUsuario(cursor);
                    cursor.close();
                    firebaseAuthWithGoogle(acct);
                }

            }
        } else {
            if (loader.getId() == LOADER_REGISTRA_USU) {
                mLoaderManager.destroyLoader(LOADER_REGISTRA_USU);
                Toast.makeText(LoginActivity.this, "SEM COMUNICAÇÃO COM O SERVIDOR", Toast.LENGTH_SHORT).show();
            }
            mDialog.dismiss();
        }

    }

    @Override
    public void onLoaderReset(Loader<Long> loader) {

    }

    public void termoAdesaoClick(View view) {
        Intent it = new Intent(this, TermoAdesaoActivity.class);
        startActivity(it);
    }

    //*********************** LOADER FIM


    //Result para o service execução

    class DataResultReceiver extends ResultReceiver {
        public DataResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            if (resultCode == Constants.SUCCESS_RESULT) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                        if (resultData.getInt(Constants.CODE_OPERATION_EXTRA) == Constants.OPERATION_INSERT) {
                            updateUI(true);
                            carregarMainActivity();
                        }
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                        if (resultData.getInt(Constants.CODE_OPERATION_EXTRA) == Constants.OPERATION_INSERT) {
                            Toast.makeText(LoginActivity.this, resultData.getString(Constants.RESULT_MESSAGE), Toast.LENGTH_SHORT).show();
                            mAuth.signOut();
                            return;
                        }

                    }
                });
            }
        }


    }


}
