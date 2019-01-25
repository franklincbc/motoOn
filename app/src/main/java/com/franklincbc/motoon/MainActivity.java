package com.franklincbc.motoon;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.os.ResultReceiver;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.franklincbc.motoon.Contract.UsuarioContract;
import com.franklincbc.motoon.Loaders.LoaderAddressGetFromLocation;
import com.franklincbc.motoon.Loaders.LoaderAtualizaLocalizacaoUsuario;
import com.franklincbc.motoon.Service.BackgroundNotificationService;
import com.franklincbc.motoon.Service.DataService;
import com.franklincbc.motoon.Utils.Constants;
import com.franklincbc.motoon.database.MotoonProvider;
import com.franklincbc.motoon.http.AtualizaStatusMtxDisponivelTask;
import com.franklincbc.motoon.http.CarregaMotociclistaProximoUsuarioTask;
import com.franklincbc.motoon.http.CarregaSolicitacaoCorrenteMotociclistaAtendTask;
import com.franklincbc.motoon.http.CarregaSolicitacaoCorrenteUsuarioTask;
import com.franklincbc.motoon.http.RequestDirectionRotaHttpTask;
import com.franklincbc.motoon.http.RetornaQtdNovasMensagensChat;
import com.franklincbc.motoon.http.UsuarioAtualizaLocalizacaoUsuarioTask;
import com.franklincbc.motoon.http.UsuarioCarregaDadosMotociclistaTask;
import com.franklincbc.motoon.http.UsuarioCarregaDadosUsuarioTask;
import com.franklincbc.motoon.model.Solicitacao;
import com.franklincbc.motoon.model.Usuario;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.franklincbc.motoon.R.id.map;
import static com.franklincbc.motoon.Utils.CalcularPrecoPresumido.PrecoPresumidoMoto;
import static com.franklincbc.motoon.Utils.CalcularPrecoPresumido.PrecoPresumidoTukTuk;
import static com.franklincbc.motoon.Utils.Util.calcularDistancia;
import static com.franklincbc.motoon.Utils.Util.truncateDecimal;
import static com.franklincbc.motoon.Utils.Util.verificaConexao;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,/* GoogleMap.OnMapLongClickListener,*/
        LoaderManager.LoaderCallbacks {

    public static final int SECONDS_UPDATE = 10;
    private Integer mSec = 0;

    //*************************************** BackgroundServiceNotification
    public static final String ACTIVITY_NOTIFICATION_SERVICE = "notification_service";
    public static final String ACTIVITY_NOTIFICATION_CHAT_SERVICE = "notification_chat_service";
    //****************************************************************

    private static final String TAG = "FKN";
    public static final String API_KEY_GOOGLE_MAPS = "AIzaSyASQmexhBkwwBZmRFjfV7QSHBSoDqpTRSc";
    public static final String API_KEY_FIREBASE = "AIzaSyDem7SvXo7ExNW-g79rDBpRYdlOIRch9tw";
    private static final int MY_LOCATION_REQUEST_CODE = 0;
    private static final int RC_SOLICITAR_MOTO = 1;
    private static final int RC_PAINEL_CHAMADOS = 2;
    public static final int RC_PESQUISA_ENDERECO = 3;

    public static final int LOADER_CARREGA_DADOS_USU = 0;
    public static final int LOADER_ATUALIZA_LOCALIZACAO_USU = 1;
    public static final int LOADER_ATUALIZA_TEXTVIEW_PAINEL_SOLICITACAO = 2;
    public static final int LOADER_PINTAR_CAMINHO = 3;
    public static final int LOADER_CARREGA_ATENDIMENTO_MOTOCICLISTA = 4;
    public static final int LOADER_CARREGA_DADOS_MOTOCICLISTA = 5;
    public static final int LOADER_ADDRESS_GET_LOCATION = 6;
    public static final int LOADER_GET_FROM_LOCATION_USUARO = 7;
    public static final int LOADER_CALCULA_DISTANCIA_PRESUMIDA = 8;
    public static final int LOADER_CHAT_NOVA_MENSAGEM = 9;
    public static final int LOADER_CARREGA_MTX_PROXIMO_USUARIO = 10;
    public static final int LOADER_ATUALIZA_STATUS_MTX_DISPONIVEL = 11;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    ImageView imgMarkerAddress;
    TextView mTxtViewDefineLocal;
    TextView mtxtViewUsuario;
    TextView mtxtViewUsuEmail;
    TextView mtxtViewID;
    ImageView mimageViewUsuario;
    Button btn_solicitar;
    TextView textViewPainelSolicitacao; //Será atualizado por um Service futuramente

    LinearLayout linearLayoutPartDest;
    LinearLayout linearlayout_preco_estimado;
    TextView textView_preco_estimado_titulo;
    TextView textView_preco_estimado;
    Button btnPartida;
    Button btnDestino;

    Switch mSwitchDisponivel;

    FloatingActionButton fabNovaMensagem;
    Integer iQtdMensagemChat = 0;

    private GoogleApiClient mGoogleApiClient;
    NavigationView navigationView;

    Geocoder geocoder;

    MapFragment mapFragment;
    GoogleMap mGoogleMap;
    LocationManager manager;
    LocationRequest mLocationRequest;

    //Usuario *********************************
    Double mDistanciaDaRota = 0.000;
    String mFaixaPrecoPresumidoMoto = "";
    String mFaixaPrecoPresumidoTukTuk = "";
    String mFaixaPrecoPresumidoCarro = "";

    boolean bDefinePartida = true;
    Usuario mUsuario = null;
    Location mLastLocationUsuario;
    Marker mMarkerUsuario;
    LatLng mLatLngUsuario; //Corrente
    LatLng mLatLngOrigem = null; //Endereco posicionado no mapa que pode ou nao ser o local corrente do usuario
    LatLng mLatLngDestino = null; //Endereco posicionado no mapa definido como destino do usuario

    private boolean mMarkerPosicionado = false;
    private AlertDialog alerta;

    List<Address> addressesMarkerDrag = null;

    //Para cque o motociclista possa ver o solicitante no mapa e ir até ele
    Solicitacao mSolicitacaoAtendimento;
    Marker mMarkerSolicitante;
    LatLng mLatLngSolicitante;

    //PAra que o usuário possa ver a moto no mapa chegar até ele
    Usuario mMotociclista = null;
    boolean bMarkerMotociclistaPosicionado = false;
    Marker mMarkerMotociclista;
    LatLng mLatLngMotociclista;
    LatLng mLatLngSolicitacaoUsu;

    LoaderManager mLoaderManager;
    DataResultReceiver mDataResultReceiver;
    Polyline polylineMap;

    //Quando usuario solicitar uma moto ativa variavel
    boolean bUsuSolicitouMoto;
    boolean bMotociclistaEmAtendimento;

    List<Marker> lstMarkersMtxProximasUsu = null;

    private boolean bSair = false;

   /* GoogleMap.OnMarkerDragListener onMarkerDragListener = new GoogleMap.OnMarkerDragListener() {
        @Override
        public void onMarkerDragStart(Marker marker) {

        }

        @Override
        public void onMarkerDrag(Marker marker) {
            //Toast.makeText(MainActivity.this, retornaEndereco(marker), Toast.LENGTH_SHORT).show();
            //Double lat = marker.getPosition().latitude;
            //Double lon = marker.getPosition().longitude;

        }

        @Override
        public void onMarkerDragEnd(Marker marker) {

        }

    }; */

    private String retornaEndereco(Marker marker) {

        if (marker == null) {
            return "";
        }
        if (!verificaConexao(this)){
            return "";
        }

        try {
            addressesMarkerDrag = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
            if (addressesMarkerDrag.size() > 0) {
                //
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        if(addressesMarkerDrag == null){
            return "";
        }

        String address = "";
        try {
            address = addressesMarkerDrag.get(0).getAddressLine(0);
        } catch(Exception e){
            address = "";
        }
        addressesMarkerDrag.clear();
        return address;
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent it = null;

            switch (v.getId()){

                case R.id.content_main_btn_partida:
                    bDefinePartida = true;
                    //mTxtViewDefineLocal.setBackgroundColor((ContextCompat.getColor(MainActivity.this,R.color.green)));
                    mTxtViewDefineLocal.setBackgroundResource(R.drawable.shape_textview_green);
                    mTxtViewDefineLocal.setText("PONTO DE PARTIDA");
                    ShowViews();

                    btnPartida.setPressed(true);
                    btnDestino.setPressed(false);

                    //Chama tela para pesquisar
                    it = new Intent(MainActivity.this, PesquisaEnderecoActivity.class);
                    it.putExtra("DEFINE_ENDERECO","PARTIDA");
                    startActivityForResult(it,RC_PESQUISA_ENDERECO);

                    /*
                    if(mLatLngOrigem!= null){
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(mLatLngOrigem);
                        mGoogleMap.animateCamera(cameraUpdate);
                    }
                    */


                    break;

                case R.id.content_main_btn_destino:
                    bDefinePartida = false;
                    //mTxtViewDefineLocal.setBackgroundColor((ContextCompat.getColor(MainActivity.this,R.color.red)));
                    mTxtViewDefineLocal.setBackgroundResource(R.drawable.shape_textview_red);
                    mTxtViewDefineLocal.setText("DESTINO");
                    ShowViews();

                    btnPartida.setPressed(false);
                    btnDestino.setPressed(true);

                    //Chama tela para pesquisar
                    it = new Intent(MainActivity.this, PesquisaEnderecoActivity.class);
                    it.putExtra("DEFINE_ENDERECO","DESTINO");
                    startActivityForResult(it,RC_PESQUISA_ENDERECO);

                    /*
                    if(mLatLngDestino!= null){
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(mLatLngDestino);
                        mGoogleMap.animateCamera(cameraUpdate);
                    }
                    */

                    break;
            }
        }
    };

    private void HideViews(){

        mTxtViewDefineLocal.setVisibility(View.INVISIBLE);
        imgMarkerAddress.setVisibility(ImageView.INVISIBLE);
        btn_solicitar.setVisibility(Button.INVISIBLE);
        linearlayout_preco_estimado.setVisibility(View.INVISIBLE);

    }

    private void ShowViews(){

        imgMarkerAddress.setVisibility(View.VISIBLE);
        mTxtViewDefineLocal.setVisibility(View.VISIBLE);
        btn_solicitar.setVisibility(View.VISIBLE);

        if(bDefinePartida) {
            //Se for destino
            linearlayout_preco_estimado.setVisibility(INVISIBLE);
        }
        else
        {
            linearlayout_preco_estimado.setVisibility(VISIBLE);
            textView_preco_estimado.setText("R$ 0.00");
            //textView_preco_estimado_titulo.setText("PRECO ESTIMADO (R$)");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bDefinePartida = true; //inicia como definição de enderenco partida

        //Deixar a tela sempre ativa
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState!= null){
            bUsuSolicitouMoto = savedInstanceState.getBoolean("solicitoumoto");
            bMotociclistaEmAtendimento = savedInstanceState.getBoolean("MotociclistaEmAtendimento");
        }

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // Usuário logado no Firebase
                    //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // Usuário não está logado, chamar a tela de Login
                    //Log.d(TAG, "onAuthStateChanged:signed_out");
                    chamaTelaLogin();
                    return;
                }
            }
        };

        mDataResultReceiver = new DataResultReceiver(null);
        mLoaderManager = getSupportLoaderManager();

        CarregaUsuario();
        if (mUsuario == null) {
            mAuth.signOut();
            chamaTelaLogin();
            return;
        }
        imgMarkerAddress = (ImageView)findViewById(R.id.img_address_map_marker);
        mTxtViewDefineLocal = (TextView) findViewById(R.id.txtView_define_local);
        textViewPainelSolicitacao = (TextView) findViewById(R.id.textView_painelSolicitacao);
        btn_solicitar = (Button)findViewById(R.id.btn_solicitar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        linearLayoutPartDest = (LinearLayout)findViewById(R.id.content_main_linearlayout_part_Dest);
        linearlayout_preco_estimado = (LinearLayout)findViewById(R.id.content_main_linearlayout_preco_estimado);
        textView_preco_estimado = (TextView)findViewById(R.id.content_main_textView_preco_estimado);
        textView_preco_estimado_titulo =  (TextView)findViewById(R.id.content_main_textView_preco_estimado_titulo);

        btnPartida = (Button)findViewById(R.id.content_main_btn_partida);
        btnDestino = (Button)findViewById(R.id.content_main_btn_destino);
        btnPartida.setOnClickListener(onClickListener);
        btnDestino.setOnClickListener(onClickListener);

        fabNovaMensagem = (FloatingActionButton)findViewById(R.id.content_main_fab_nova_mensagem);

        mSwitchDisponivel = (Switch)findViewById(R.id.content_main_switch_disponivel);
        mSwitchDisponivel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Bundle params = new Bundle();
                    params.putInt("mototaxi_id", mUsuario.getMototaxi_id());
                    params.putString("sn_disponivel", "S");
                    mLoaderManager.restartLoader(LOADER_ATUALIZA_STATUS_MTX_DISPONIVEL, params, MainActivity.this);
                    mSwitchDisponivel.setText("Disponível");
                    mSwitchDisponivel.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_textview_green));

                }else{
                    Bundle params = new Bundle();
                    params.putInt("mototaxi_id", mUsuario.getMototaxi_id());
                    params.putString("sn_disponivel", "N");
                    mLoaderManager.restartLoader(LOADER_ATUALIZA_STATUS_MTX_DISPONIVEL, params, MainActivity.this);
                    mSwitchDisponivel.setText("Indisponível");
                    mSwitchDisponivel.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_textview_red));
                }

            }
        });

        final String url = getIntent().getStringExtra("url");

        mtxtViewUsuario = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_main_usuario);
        mtxtViewUsuEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_main_usuario_email);
        mimageViewUsuario = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_main_usuario_img);
        mtxtViewID = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_main_id);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(AppIndex.API)
                    .build();
        }

        //Ativar o gps
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isOn = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isOn) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }


        //Google map
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_REQUEST_CODE);
        }

        geocoder = new Geocoder(this, Locale.getDefault());

        bUsuSolicitouMoto = true;
        bMotociclistaEmAtendimento = false;
        lstMarkersMtxProximasUsu = new ArrayList<>();

        updateUI();

        StartBackgroundNotificationService();

        //Abertura do aplicativo atraves da notificação
        if(getIntent().getStringExtra(ACTIVITY_NOTIFICATION_SERVICE) != null ){
            if(mUsuario.getSn_disponivel().equals("S")) {
                Intent it = new Intent(this, PainelChamadosActivity.class);
                it.putExtra(Constants.USUARIO_EXTRA, mUsuario);
                startActivityForResult(it, RC_PAINEL_CHAMADOS);
            }else{
                Toast.makeText(this, "Altere seu status para DISPONÍVEL\n" +
                        "para ter acesso ao PAINEL DE CHAMADOS", Toast.LENGTH_SHORT).show();
            }
        }

        if(getIntent().getStringExtra(ACTIVITY_NOTIFICATION_CHAT_SERVICE) != null ){
            //Clicou na notificação nova mensagem CHAT, ao abrir o aplicativo iniciar a tela do CHAT
            fabNovaMensagem.setVisibility(VISIBLE);
        }



    }

    private void StartBackgroundNotificationService(){

        //Rodar em background para exibir notificação de chamados
        if(mUsuario.getCidade()!=null && mUsuario.getTipo_veiculo()!=null) {
            Intent it = new Intent(this, BackgroundNotificationService.class);
            it.putExtra("cidade", mUsuario.getCidade());
            it.putExtra("tipoveiculo", mUsuario.getTipo_veiculo());
            it.setFlags(Service.START_FLAG_REDELIVERY);
            startService(it);
        }


    }

    private void chamaTelaLogin() {
        finish();
        Intent it = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(it);
    }

    private void updateUI(){
        try {
            mtxtViewUsuario.setText(mUsuario.getNome());
            mtxtViewUsuEmail.setText(mUsuario.getEmail());
            String url = mUsuario.getUrl_photo();
            Glide.with(MainActivity.this)
                    .load(url)
                    .into(mimageViewUsuario);

            //Habilitar Opcoes
            if (mUsuario.getSn_mototaxi() != null && mUsuario.getSn_mototaxi().equals("S")) {
                mTxtViewDefineLocal.setVisibility(View.INVISIBLE);
                mtxtViewID.setText("ID Mototáxi: " + mUsuario.getMototaxi_id());
                imgMarkerAddress.setVisibility(View.INVISIBLE);
                //Habilitar Visão MotoTaxi
                btn_solicitar.setVisibility(View.INVISIBLE);
                navigationView.getMenu().getItem(1).setVisible(false); //Histórico
                navigationView.getMenu().getItem(2).setVisible(false); //Trabalhe conosco
                navigationView.getMenu().getItem(3).setVisible(false); //Trabalhe conosco
                navigationView.getMenu().getItem(4).setVisible(true); //Painel de Chamado
                navigationView.getMenu().getItem(5).setVisible(true); //Atendimentos Motociclista

                linearLayoutPartDest.setVisibility(View.INVISIBLE);
                linearlayout_preco_estimado.setVisibility(View.INVISIBLE);

                mSwitchDisponivel.setVisibility(VISIBLE);
                if(mUsuario.getSn_disponivel().equals("N")) {
                    mSwitchDisponivel.setText("Indisponível");
                    mSwitchDisponivel.setChecked(false);
                    mSwitchDisponivel.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_textview_red));
                }else{
                    mSwitchDisponivel.setText("Disponível");
                    mSwitchDisponivel.setChecked(true);
                    mSwitchDisponivel.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_textview_green));
                }

            }
            else
            {
                linearLayoutPartDest.setVisibility(View.VISIBLE);

                imgMarkerAddress.setVisibility(VISIBLE);
                //Habilita visao usuário
                mtxtViewID.setText("ID Usuário: " + mUsuario.getId_servidor());
                btn_solicitar.setVisibility(VISIBLE);

                //Franklin apenas - TEstando
                if(mUsuario.getId_servidor() == 97 || mUsuario.getId_servidor() == 4 || mUsuario.getId_servidor() == 12 ) {
                    //enviar documentos
                    navigationView.getMenu().getItem(3).setVisible(true);
                }else{
                    navigationView.getMenu().getItem(3).setVisible(false);
                }

                navigationView.getMenu().getItem(4).setVisible(false);
                navigationView.getMenu().getItem(5).setVisible(false); //Atendimentos Motociclista

                mSwitchDisponivel.setVisibility(INVISIBLE);

            }

            mimageViewUsuario.setVisibility(VISIBLE);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("solicitoumoto",bUsuSolicitouMoto);
        outState.putBoolean("MotociclistaEmAtendimento",bMotociclistaEmAtendimento);
    }

    private void CarregaUsuario() {
        try {
            //Pega usuario no banco local
            Cursor cursor = getApplicationContext().getContentResolver().query(MotoonProvider.USUARIO_URI, null, null, null, null);
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
                    mUsuario.setSn_disponivel(cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_SN_DISPONIVEL)));

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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_main_normal) {
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            return true;
        } else if (id == R.id.menu_main_hibrido) {
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            return true;
        } else if (id == R.id.menu_main_satelite) {
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            return true;
        } else if (id == R.id.menu_main_terreno) {
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            return true;
        } else if (id == R.id.menu_main_nenhum) {
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_meus_dados) {
            Intent it = new Intent(this, MeusDadosActivity.class);
            it.putExtra(Constants.USUARIO_EXTRA, mUsuario);
            startActivity(it);

        } else if (id == R.id.nav_minhas_solicitacoes) {
            Intent it = new Intent(this, Lista_Solicitacao_HistoricoActivity.class);
            //it.putExtra(Lista_Solicitacao_HistoricoActivity.USUARIO_ID_EXTRA, mUsuario.getId_servidor());
            it.putExtra(Constants.USUARIO_EXTRA, mUsuario);
            startActivity(it);

        } else if (id == R.id.nav_trab_conosco) {
            if (mUsuario.getSn_aceitou_termo().equals("S")){
                Intent it = new Intent(this, AguardandoDocActivity.class);
                it.putExtra(Constants.USUARIO_EXTRA, mUsuario);
                startActivity(it);
            }else {
                Intent it = new Intent(this, TrabalheConoscoActivity.class);
                it.putExtra(Constants.USUARIO_EXTRA, mUsuario);
                startActivity(it);
            }

        }
        else if (id == R.id.nav_enviar_documentos) {
            Intent it = new Intent(this, EnviarDocumentosActivity.class);
            it.putExtra(Constants.USUARIO_EXTRA, mUsuario);
            startActivity(it);

        }
        else if (id == R.id.nav_painel_chamado) {

            if (mUsuario.getCelular() == null || mUsuario.getCelular().equals("")) {
                Toast.makeText(MainActivity.this, "Cadastre o número do seu CELULAR e a Data de Nascimento para concluir seu cadastro", Toast.LENGTH_LONG).show();
                Intent it = new Intent(this, MeusDadosActivity.class);
                it.putExtra(Constants.USUARIO_EXTRA, mUsuario);
                startActivity(it);
                return false;
            }

            if(mUsuario.getSn_disponivel().equals("N")){
                Toast.makeText(this, "Altere seu status para DISPONÍVEL\n" +
                        "para ter acesso ao PAINEL DE CHAMADOS", Toast.LENGTH_LONG).show();
                return false;
            }

            Intent it = new Intent(this, PainelChamadosActivity.class);
            it.putExtra(Constants.USUARIO_EXTRA, mUsuario);
            startActivityForResult(it, RC_PAINEL_CHAMADOS);

        } else if (id == R.id.nav_meus_atendimentos) {
            Intent it = new Intent(this, ListaAtendimentosMotociclistaActivity.class);
            it.putExtra(Constants.USUARIO_EXTRA, mUsuario);
            startActivity(it);
        }
        else if (id == R.id.nav_encerrar) {

            if(mUsuario.getSn_mototaxi()!= null && mUsuario.getSn_mototaxi().equals("S") ) {
                bSair = true;
                mSwitchDisponivel.setChecked(false);//chama o onchange e atualiza no servidor deixando o mototaxi indisponivel
            }
            else {
                mAuth.signOut();
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        bSair = false;
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        AppIndex.AppIndexApi.start(mGoogleApiClient, getIndexApiAction());
        mAuth.addAuthStateListener(mAuthListener); //Firebase

        fabNovaMensagem.setVisibility(INVISIBLE);

        if (verificaConexao(this)) {
            CarregaDadosUsuarioWeb(); //Está no oncreate tbm, ao carrega usuario
        }


    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(mGoogleApiClient, getIndexApiAction());

        if (mAuthListener != null) { //Firebase
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        //
    }

    @Override
    protected void onResume() {
        super.onResume();
        //
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
        }

        //mGoogleMap.setOnMapLongClickListener(this);  não uso mais esse metodo
        //mGoogleMap.setOnMarkerDragListener(onMarkerDragListener);

        //Chgamado quando inicia movimentacao do mapa
        mGoogleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {


            }
        });

        //Chamado repetidas vezes
        mGoogleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {

            }
        });

        //chamdo quando para o movimento
        mGoogleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if(mUsuario.getSn_mototaxi()!= null && mUsuario.getSn_mototaxi().equals("N")) {

                    if(mTxtViewDefineLocal.getVisibility() == INVISIBLE){
                        return;
                    }

                    Double lat = mGoogleMap.getCameraPosition().target.latitude;
                    Double lng = mGoogleMap.getCameraPosition().target.longitude;
                    if (lat != null && lng != null) {

                        Bundle params = new Bundle();
                        params.putDouble("lat", lat);
                        params.putDouble("lng", lng);

                        if (bDefinePartida) {
                            mLatLngOrigem = null;
                            mLatLngOrigem = new LatLng(lat, lng);
                            mTxtViewDefineLocal.setText("Buscando endereço...");
                            mLoaderManager.restartLoader(LOADER_ADDRESS_GET_LOCATION, params, MainActivity.this);
                        }
                        else
                        {
                            mLatLngDestino = null;
                            mLatLngDestino = new LatLng(lat, lng);
                            mTxtViewDefineLocal.setText("Buscando endereço...");
                            mLoaderManager.restartLoader(LOADER_ADDRESS_GET_LOCATION, params, MainActivity.this);

                        }



                    }
                }
            }
        });


    }

    public void chamarMotoOnClick(View view) {
        try {
            if (mUsuario.getCelular() == null || mUsuario.getCelular().equals("")) {
                Toast.makeText(MainActivity.this, "Cadastre o número do seu CELULAR e a Data de Nascimento para concluir seu cadastro", Toast.LENGTH_LONG).show();
                Intent it = new Intent(this, MeusDadosActivity.class);
                it.putExtra(Constants.USUARIO_EXTRA, mUsuario);
                startActivity(it);

                return;
            }

            if (!verificaConexao(this)) {
                Snackbar.make(view, "SEM CONEXÃO COM INTERNET", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return;
            }

            //geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addressesOrig = null;
            List<Address> addressesDest = null;
            String endOrigem = "";
            String endDestino = "";
            Double lat = 0.000000;
            Double lon = 0.000000;
            Double latDest = 0.000000;
            Double lonDest = 0.000000;

            if (mLastLocationUsuario == null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mLastLocationUsuario = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
            }

            if (mLatLngOrigem == null) {
                //Posicao do usuario corrente
                Snackbar.make(view, "LOCAL DE PARTIDA NÃO IDENTIFICADO", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return;
            }
            else
            {
                addressesOrig = geocoder.getFromLocation(mLatLngOrigem.latitude, mLatLngOrigem.longitude, 1);
                lat = mLatLngOrigem.latitude;
                lon = mLatLngOrigem.longitude;
            }

            if ((mLatLngDestino != null)) {
                addressesDest = geocoder.getFromLocation(mLatLngDestino.latitude, mLatLngDestino.longitude, 1);
                latDest = mLatLngDestino.latitude;
                lonDest = mLatLngDestino.longitude;
            }


            //forma de validar -????????????
            if (addressesOrig.size() > 0) {
                //preenche info end
            }

            endOrigem = addressesOrig.get(0).getAddressLine(0);
            String bairro = addressesOrig.get(0).getSubLocality();
            String cidade = addressesOrig.get(0).getLocality();

            if (addressesDest != null && addressesDest.size() > 0) {
                endDestino = addressesDest.get(0).getAddressLine(0);
            }

            Intent it = new Intent(MainActivity.this, SolicitarMotoActivity.class);
            it.putExtra("ORIGEM", endOrigem);
            it.putExtra("DESTINO", endDestino);
            it.putExtra("LATITUDE", lat);
            it.putExtra("LONGITUDE", lon);
            it.putExtra("BAIRRO", bairro);
            it.putExtra("CIDADE", cidade);

            it.putExtra("LATITUDE_DESTINO", latDest);
            it.putExtra("LONGITUDE_DESTINO", lonDest);
            it.putExtra("DISTANCIA_PRESUMIDA", mDistanciaDaRota);
            it.putExtra("FAIXA_PRECO_PRESUMIDO_MOTO", mFaixaPrecoPresumidoMoto );
            it.putExtra("FAIXA_PRECO_PRESUMIDO_TUKTUK", mFaixaPrecoPresumidoTukTuk );
            it.putExtra("FAIXA_PRECO_PRESUMIDO_CARRO", mFaixaPrecoPresumidoCarro );

            it.putExtra(Constants.USUARIO_EXTRA, mUsuario);

            startActivityForResult(it, RC_SOLICITAR_MOTO);

        } catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0].equals(android.Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                // Permission was denied. Display an error message.
                Toast.makeText(MainActivity.this, "Autorização negada pelo usuário! Ative-a para ver sua posição no mapa.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SOLICITAR_MOTO){
            if(resultCode == RESULT_OK){
                iQtdMensagemChat = 0;
                bUsuSolicitouMoto = true;
                Toast.makeText(this, "Solicitação registrada com sucesso", Toast.LENGTH_SHORT).show();
                //textViewPainelSolicitacao.setText("MOTO SOLICITADA\nSTATUS: Aguardando atendimento\nMOTO-TAXISTA: ");
                //textViewPainelSolicitacao.setVisibility(View.VISIBLE);
                HideViews();
            } else {
                bUsuSolicitouMoto = false;
            }
            mLatLngDestino = null;

        }
        else if(requestCode == RC_PAINEL_CHAMADOS){

            if(resultCode == RESULT_OK){
                String acao = data.getStringExtra("acao");
                if (acao.equals("ATENDIMENTO")) {
                    try {
                        bMotociclistaEmAtendimento = false; //Variável para consultar no servidor se o moto ta em atendimento

                        //Adicionar O Marker do Solicitante para O motoqueiro ver onde ele está
                        mSolicitacaoAtendimento = null;
                        mSolicitacaoAtendimento = (Solicitacao) data.getSerializableExtra(Constants.SOLICITACAO_EXTRA);
                        adicionaMarkerSolicitanteAtendimentoCalcRota();
                        iQtdMensagemChat = 0;

                    } catch (Exception e) {
                        mSolicitacaoAtendimento = null;
                        if (mMarkerSolicitante != null) {
                            mMarkerSolicitante.remove();
                            mMarkerSolicitante = null;
                        }
                        mLatLngSolicitante = null;
                        e.printStackTrace();
                    }
                }

            } else {

                //Checa novamente
                Bundle params = new Bundle();
                params.putInt("mototaxi_id", mUsuario.getMototaxi_id());
                mLoaderManager.restartLoader(LOADER_CARREGA_ATENDIMENTO_MOTOCICLISTA, params, MainActivity.this);
            }

        }
        else if(requestCode == RC_PESQUISA_ENDERECO){
            LatLng latLngSelecionada = null;
            //Retorno da busca
            if(resultCode == RESULT_OK){

                Double lat = data.getDoubleExtra("lat",0.00);
                Double lng = data.getDoubleExtra("lng",0.00);
                latLngSelecionada = new LatLng(lat, lng);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latLngSelecionada);
                mGoogleMap.animateCamera(cameraUpdate);

                if(bDefinePartida){
                    mLatLngOrigem = latLngSelecionada;
                    Toast.makeText(this, "Ponto de Partida atualizado", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mLatLngDestino = latLngSelecionada;
                    Toast.makeText(this, "Destino atualizado", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                if(bDefinePartida){
                    if(mLatLngOrigem!=null) {
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(mLatLngOrigem);
                        mGoogleMap.animateCamera(cameraUpdate);
                    }
                }
                else
                {
                    if(mLatLngDestino!=null) {
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(mLatLngDestino);
                        mGoogleMap.animateCamera(cameraUpdate);
                    }
                }
            }
        }

    }

    private void adicionaMarkerMotociclistaMapa(){
        try {
            if (mMarkerMotociclista != null) {
                mMarkerMotociclista.remove();
                mMarkerMotociclista = null;
            }

            mLatLngMotociclista = null;
            mLatLngMotociclista = new LatLng(mMotociclista.getLatitude_atual(), mMotociclista.getLongitude_atual());

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(mLatLngMotociclista)
                    .title(mMotociclista.getNome())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.moto_motoqueiro));
            //.icon(BitmapDescriptorFactory.defaultMarker());
            mMarkerMotociclista = mGoogleMap.addMarker(markerOptions);

            if (!bMarkerMotociclistaPosicionado) {

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(mLatLngSolicitacaoUsu);
                builder.include(mLatLngMotociclista);
                LatLngBounds bounds = builder.build();
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
                mGoogleMap.animateCamera(cu);
                bMarkerMotociclistaPosicionado = true;

            }

            mMarkerMotociclista.showInfoWindow();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void adicionaMarkerSolicitanteAtendimentoCalcRota(){
        try {
            if (polylineMap != null) {
                polylineMap.remove();
                polylineMap = null;
            }
            if (mMarkerSolicitante != null) {
                mMarkerSolicitante.remove();
                mMarkerSolicitante = null;
            }
            mLatLngSolicitante = null;

            //Atualiza
            mLatLngSolicitante = new LatLng(mSolicitacaoAtendimento.getLatitude(), mSolicitacaoAtendimento.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(mLatLngSolicitante)
                    .title(mSolicitacaoAtendimento.getSolicitante())
                    .snippet("Estou aqui esperando")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.boneco));
            //.icon(BitmapDescriptorFactory.defaultMarker());
            mMarkerSolicitante = mGoogleMap.addMarker(markerOptions);

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(mLatLngUsuario);
            builder.include(mLatLngSolicitante);
            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
            mGoogleMap.animateCamera(cu);
            String url = montarURLRotaMapa(mLatLngUsuario.latitude, mLatLngUsuario.longitude, mLatLngSolicitante.latitude, mLatLngSolicitante.longitude);

            //Traçar a Rota
            Bundle params = new Bundle();
            params.putString("url_rota", url);
            mLoaderManager.restartLoader(LOADER_PINTAR_CAMINHO, params, MainActivity.this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String montarURLRotaMapa(double latOrigem, double lngOrigem, double latDestino, double lngDestino){
        //Base URL
        String url = "http://maps.googleapis.com/maps/api/directions/json?origin=";

        //Local de origem
        url += latOrigem + "," + lngOrigem;
        url += "&destination=";

        //Local de destino
        url += latDestino + "," + lngDestino;

        //Outros parametros
        url += "&sensor=false&mode=driving&alternatives=true";
        return url;
    }


    public void pintarCaminho(JSONObject json) {
        try {
            //Recupera a lista de possíveis rotas
            JSONArray listaRotas = json.getJSONArray("routes");
            //Para efeito de aprendizado iremos utilizar apenas a primeira opção
            JSONObject rota = listaRotas.getJSONObject(0);
            //Recuperamos os pontos a serem pintados para que surga a 'linha' no mapa
            String pontosPintar = rota.getJSONObject("overview_polyline").getString("points");
            //Recuperamos a lista de latitudes e longitudes para sabermos exatamente onde pintar
            List<LatLng> listaCordenadas = extrairLatLngDaRota(pontosPintar);

            if(polylineMap != null) {
                polylineMap.remove();
                polylineMap = null;
            }

            //Percorremos por cada cordenada obtida
            for(int ponto = 0; ponto < listaCordenadas.size()-1 ; ponto++){
                //Definimos o ponto atual como origem
                LatLng pontoOrigem= listaCordenadas.get(ponto);
                //Definimos o próximo ponto como destino
                LatLng pontoDestino= listaCordenadas.get(ponto + 1);
                //Criamos um objeto do tipo PolylineOption para adicionarmos os pontos de origem e destino
                PolylineOptions opcoesDaLinha = new PolylineOptions();
                //Adicionamos os pontos de origem e destino da linha que vamos traçar
                opcoesDaLinha.add(new LatLng(pontoOrigem.latitude, pontoOrigem.longitude),
                        new LatLng(pontoDestino.latitude,  pontoDestino.longitude));
                //Criamos a linha de acordo com as opções que configuramos acima e adicionamos em nosso mapa
                polylineMap = mGoogleMap.addPolyline(opcoesDaLinha);
                //Mudamos algumas propriedades da linha que acabamos de adicionar em nosso mapa
                polylineMap.setWidth(5);
                polylineMap.setColor(Color.BLUE);
                polylineMap.setGeodesic(true);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            //Log.e("ProjetoMapas", e.getMessage());
        }
    }

    public Double DistanciaPresumidaDaRota(JSONObject json) {
        try {
            //Recupera a lista de possíveis rotas
            JSONArray listaRotas = json.getJSONArray("routes");
            //Para efeito de aprendizado iremos utilizar apenas a primeira opção
            JSONObject rota = listaRotas.getJSONObject(0);
            //Recuperamos os pontos a serem pintados para que surga a 'linha' no mapa
            String pontosPintar = rota.getJSONObject("overview_polyline").getString("points");
            //Recuperamos a lista de latitudes e longitudes para sabermos exatamente onde pintar
            List<LatLng> listaCordenadas = extrairLatLngDaRota(pontosPintar);

            Double DistanciaCalculada = 0.000;

            //Percorremos por cada cordenada obtida
            for(int ponto = 0; ponto < listaCordenadas.size()-1 ; ponto++){
                //Definimos o ponto atual como origem
                LatLng pontoOrigem= listaCordenadas.get(ponto);
                //Definimos o próximo ponto como destino
                LatLng pontoDestino= listaCordenadas.get(ponto + 1);

                DistanciaCalculada = DistanciaCalculada + calcularDistancia(pontoOrigem, pontoDestino);

            }

            return DistanciaCalculada;
        }
        catch (JSONException e) {
            e.printStackTrace();
            //Log.e("ProjetoMapas", e.getMessage());
        }

        return 0.000;
    }


    private List<LatLng> extrairLatLngDaRota(String pontosPintar) {
        List<LatLng> listaResult = new ArrayList<LatLng>();
        int index = 0, len = pontosPintar.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = pontosPintar.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = pontosPintar.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            listaResult.add(p);
        }

        return listaResult;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setSmallestDisplacement(0.1F); //added
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //changed
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        atualizaLatLngUsuario(location, mMarkerPosicionado);

    }

    private void atualizaLatLngUsuario(Location location, boolean MarkerPosicionado) {
        try {
            mLastLocationUsuario = location;
            mLatLngUsuario = new LatLng(mLastLocationUsuario.getLatitude(), mLastLocationUsuario.getLongitude());
            mUsuario.setLatitude_atual(mLatLngUsuario.latitude);
            mUsuario.setLongitude_atual(mLatLngUsuario.longitude);

            /*if(mUsuario.getSn_mototaxi()!= null && mUsuario.getSn_mototaxi().equals("S")) {

                if (mMarkerUsuario != null) {
                    mMarkerUsuario.remove();
                }

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(mLatLngUsuario)
                        .title(mUsuario.getNome())
                        .snippet("Minha posição atual")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador_net_1));
                //.icon(BitmapDescriptorFactory.defaultMarker());
                mMarkerUsuario = mGoogleMap.addMarker(markerOptions);

            }*/

            //}

            if (!MarkerPosicionado) {
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(mLatLngUsuario));
                mMarkerPosicionado = true;
            }


            if (verificaConexao(this)) {
                //Atualiza

                    Bundle params = new Bundle();
                    params.putDouble("lat", mLatLngUsuario.latitude);
                    params.putDouble("lng", mLatLngUsuario.longitude);
                    mLoaderManager.restartLoader(LOADER_GET_FROM_LOCATION_USUARO, params, MainActivity.this);


                if(mUsuario.getSn_mototaxi() != null){
                    if(mUsuario.getSn_mototaxi().equals("N")){
                        //Usuário é cliente
                        if(bUsuSolicitouMoto){
                            mLoaderManager.restartLoader(LOADER_ATUALIZA_TEXTVIEW_PAINEL_SOLICITACAO, null, MainActivity.this);
                            LimpaListaMarkerMtxProximosUsuario();
                        }
                        else
                        {
                            CarregaMtxProximosUsuario();
                        }
                    }
                    else
                    {
                        //Uusário é mototáxi
                        if(!bMotociclistaEmAtendimento){
                            //Checa se o motociclista está atendendo algum chamado
                            params = new Bundle();
                            params.putInt("mototaxi_id", mUsuario.getMototaxi_id());
                            mLoaderManager.restartLoader(LOADER_CARREGA_ATENDIMENTO_MOTOCICLISTA, params, MainActivity.this);
                        }
                    }
                }


                    //Nova mensagem
                    if(mSolicitacaoAtendimento != null && mUsuario != null) {

                        mUsuario.setIsAtendimento("S");
                        mUsuario.setAtendSolicitacao_Id(mSolicitacaoAtendimento.getSolicitacao_id());
                        mUsuario.setAtendSolicitacao_UsuarioId(mSolicitacaoAtendimento.getUsuario_id());
                        mUsuario.setAtendSolicitacao_StatusSol(mSolicitacaoAtendimento.getStatus_sol());
                        AtualizaDadosUsuarioDataService();

                        //DEixa o botão ativo enquanto esta em atendimeto
                        if(mSolicitacaoAtendimento.getStatus_sol().equals("E")) {
                            fabNovaMensagem.setVisibility(VISIBLE);
                        }
                        else{
                            fabNovaMensagem.setVisibility(INVISIBLE);
                        }

                        Bundle param = new Bundle();
                        param.putInt("solicitacao_usuario_id", mSolicitacaoAtendimento.getUsuario_id());
                        param.putInt("solicitacao_id", mSolicitacaoAtendimento.getSolicitacao_id());
                        param.putInt("usuario_id", mUsuario.getId_servidor());

                        if(mUsuario.getSn_mototaxi()!=null && mUsuario.getSn_mototaxi().equals("S")) {
                            param.putInt("mototaxi_id", mUsuario.getMototaxi_id());
                        }else {
                            param.putInt("mototaxi_id", 0);
                        }
                        param.putString("backgroundservice", "N");

                        mLoaderManager.restartLoader(LOADER_CHAT_NOVA_MENSAGEM, param, MainActivity.this);
                    } else {

                        mUsuario.setIsAtendimento("N");
                        mUsuario.setAtendSolicitacao_Id(0);
                        mUsuario.setAtendSolicitacao_UsuarioId(0);
                        mUsuario.setAtendSolicitacao_StatusSol("");
                        AtualizaDadosUsuarioDataService();

                        fabNovaMensagem.setVisibility(INVISIBLE);
                    }


                    /*
                    if(mUsuario.getSn_mototaxi()!= null && mUsuario.getSn_mototaxi().equals("S")) {
                        mMarkerUsuario.setSnippet(retornaEndereco(mMarkerUsuario));
                    } */




            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void CarregaMtxProximosUsuario(){
        Bundle params = new Bundle();
        params.putSerializable(Constants.USUARIO_EXTRA, mUsuario);
        mLoaderManager.restartLoader(LOADER_CARREGA_MTX_PROXIMO_USUARIO, params, MainActivity.this);

    }

    private void LimpaListaMarkerMtxProximosUsuario(){
        if (lstMarkersMtxProximasUsu != null && lstMarkersMtxProximasUsu.size() > 0) {
            //remove os markers do mapa
            for (int i = 0; i < lstMarkersMtxProximasUsu.size(); i++) {
                lstMarkersMtxProximasUsu.get(i).remove();
            }

        }
    }

    /*@Override
    public void onMapLongClick(LatLng latLng) {
        //alerta_addNewMarker();
    }
    */

    private void alerta_addNewMarker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);//Cria o gerador do AlertDialog
        builder.setTitle("Atenção");//define o titulo
        builder.setMessage("Adicionar DESTINO no mapa?");//define a mensagem

        //define um botão como positivo para adicionar a origem
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        //Usa  o botao de negative para adicionar o destino. Gambi
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        alerta = builder.create();//cria o AlertDialog
        alerta.show();//Exibe
    }

    public void textViewPainelSolicitacaoOnClick(View view) {
        if(mUsuario.getSn_mototaxi()!= null) {
            if (mUsuario.getSn_mototaxi().equals("S")) {

                if(mUsuario.getSn_disponivel().equals("N")){
                    Toast.makeText(this, "Altere seu status para DISPONÍVEL\n" +
                            "para ter acesso ao PAINEL DE CHAMADOS", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent it = new Intent(this, PainelChamadosActivity.class);
                it.putExtra(Constants.USUARIO_EXTRA, mUsuario);
                startActivityForResult(it, RC_PAINEL_CHAMADOS);
            } else {
                Intent it = new Intent(this, Lista_Solicitacao_HistoricoActivity.class);
                //it.putExtra(Lista_Solicitacao_HistoricoActivity.USUARIO_ID_EXTRA, mUsuario.getId_servidor());
                it.putExtra(Constants.USUARIO_EXTRA, mUsuario);
                startActivity(it);
            }
        }

    }


    //Adicionado abaixo, pois dava erro ao instalar no tablet com android 4.2,
    //solucao encontrada em sites
    //Add nas activity
    @Override
    protected void attachBaseContext(Context newBase) {
        MultiDex.install(newBase);
        super.attachBaseContext(newBase);
    }

    private void CarregaAtualizaDadosUsuarioBanco(){
        try {
            //Pega usuario no banco local
            Cursor cursor = getApplicationContext().getContentResolver().query(MotoonProvider.USUARIO_URI, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    if (mUsuario == null) {
                        mUsuario = new Usuario();
                    }

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
                    mUsuario.setSn_disponivel(cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_SN_DISPONIVEL)));
                    mUsuario.setUrl_photo(cursor.getString(cursor.getColumnIndex(UsuarioContract.COL_URL_PHOTO)));

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

    private void CarregaDadosUsuarioWeb(){
        //Carregar dados da WEB
        Bundle params = new Bundle();
        params.putSerializable(Constants.USUARIO_EXTRA, mUsuario);
        mLoaderManager.initLoader(LOADER_CARREGA_DADOS_USU, params, MainActivity.this);

    }

    private void AtualizaLocalizacaoUsuarioWeb(){
        //Carregar dados da WEB
        Bundle params = new Bundle();
        params.putSerializable(Constants.USUARIO_EXTRA, mUsuario);
        mLoaderManager.restartLoader(LOADER_ATUALIZA_LOCALIZACAO_USU, params, MainActivity.this);

    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        if (!verificaConexao(this)){

            if (id == LOADER_CARREGA_DADOS_USU) {
                mLoaderManager.destroyLoader(LOADER_CARREGA_DADOS_USU);
            }
            else if (id == LOADER_ATUALIZA_TEXTVIEW_PAINEL_SOLICITACAO) {
                mLoaderManager.destroyLoader(LOADER_ATUALIZA_TEXTVIEW_PAINEL_SOLICITACAO);
            }
            else if (id == LOADER_ATUALIZA_LOCALIZACAO_USU)
            {
                mLoaderManager.destroyLoader(LOADER_ATUALIZA_LOCALIZACAO_USU);
            }
            else if (id == LOADER_PINTAR_CAMINHO){
                mLoaderManager.destroyLoader(LOADER_PINTAR_CAMINHO);
            }
            else if (id == LOADER_CARREGA_ATENDIMENTO_MOTOCICLISTA){
                mLoaderManager.destroyLoader(LOADER_CARREGA_ATENDIMENTO_MOTOCICLISTA);
            }
            else if (id == LOADER_CARREGA_DADOS_MOTOCICLISTA){
                mLoaderManager.destroyLoader(LOADER_CARREGA_DADOS_MOTOCICLISTA);
            }
            else if (id == LOADER_ADDRESS_GET_LOCATION){
                mLoaderManager.destroyLoader(LOADER_ADDRESS_GET_LOCATION);
            }
            else if (id == LOADER_GET_FROM_LOCATION_USUARO){
                mLoaderManager.destroyLoader(LOADER_GET_FROM_LOCATION_USUARO);
            }
            else if (id == LOADER_CALCULA_DISTANCIA_PRESUMIDA){
                mLoaderManager.destroyLoader(LOADER_CALCULA_DISTANCIA_PRESUMIDA);
            }
            else if (id == LOADER_CHAT_NOVA_MENSAGEM){
                mLoaderManager.destroyLoader(LOADER_CHAT_NOVA_MENSAGEM);
            }
            else if (id == LOADER_CARREGA_MTX_PROXIMO_USUARIO){
                mLoaderManager.destroyLoader(LOADER_CARREGA_MTX_PROXIMO_USUARIO);
            }
            else if (id == LOADER_ATUALIZA_STATUS_MTX_DISPONIVEL){
                mLoaderManager.destroyLoader(LOADER_ATUALIZA_STATUS_MTX_DISPONIVEL);
            }

            Snackbar.make(mtxtViewUsuario, "SEM CONEXÃO COM INTERNET", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return null; //se nao tem conexao nao envia requisicao http
        }


        if (id == LOADER_CARREGA_DADOS_USU){
            return new UsuarioCarregaDadosUsuarioTask(MainActivity.this, id, args );
        }
        else if (id == LOADER_ATUALIZA_LOCALIZACAO_USU){
            return new UsuarioAtualizaLocalizacaoUsuarioTask(MainActivity.this, id, args );
        }
        else if (id == LOADER_ATUALIZA_TEXTVIEW_PAINEL_SOLICITACAO){
            return new CarregaSolicitacaoCorrenteUsuarioTask(MainActivity.this, id, mUsuario.getId_servidor() );
        }
        else if (id == LOADER_PINTAR_CAMINHO){
            String url = args.getString("url_rota");
            return new RequestDirectionRotaHttpTask(MainActivity.this, url );
        }
        else if (id == LOADER_CARREGA_ATENDIMENTO_MOTOCICLISTA){
            Integer mototaxi_id = args.getInt("mototaxi_id");
            return new CarregaSolicitacaoCorrenteMotociclistaAtendTask(MainActivity.this, mototaxi_id );
        }
        else if (id == LOADER_CARREGA_DADOS_MOTOCICLISTA){
            return new UsuarioCarregaDadosMotociclistaTask(MainActivity.this, args );
        }
        else if (id == LOADER_ADDRESS_GET_LOCATION){
            return new LoaderAddressGetFromLocation(MainActivity.this, 1 ,args );
        }
        else if (id == LOADER_GET_FROM_LOCATION_USUARO) {
            return new LoaderAtualizaLocalizacaoUsuario(MainActivity.this, args );
        }
        else if (id == LOADER_CALCULA_DISTANCIA_PRESUMIDA){
            //Igual a chamada do LOADER_PINTAR_CAMINHO
            String url = args.getString("url_rota");
            return new RequestDirectionRotaHttpTask(MainActivity.this, url );
        }
        else if (id == LOADER_CHAT_NOVA_MENSAGEM){
            return new RetornaQtdNovasMensagensChat(MainActivity.this, id, args);
        }
        else if (id == LOADER_CARREGA_MTX_PROXIMO_USUARIO){
            return new CarregaMotociclistaProximoUsuarioTask(MainActivity.this, args);
        }
        else if (id == LOADER_ATUALIZA_STATUS_MTX_DISPONIVEL){
            return new AtualizaStatusMtxDisponivelTask(MainActivity.this, args);
        }
        else {
            return null;
        }

    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

        if (data != null ){

            if (loader.getId() == LOADER_CARREGA_DADOS_USU) {

                Usuario usuario = (Usuario) data;
                mUsuario.setMototaxi_id(usuario.getMototaxi_id());
                mUsuario.setNome(usuario.getNome());
                mUsuario.setLatitude_atual(usuario.getLatitude_atual());
                mUsuario.setLongitude_atual(usuario.getLongitude_atual());
                mUsuario.setData_cadastro(usuario.getData_cadastro());
                mUsuario.setCelular(usuario.getCelular());
                mUsuario.setSexo(usuario.getSexo());
                mUsuario.setEstado(usuario.getEstado());
                mUsuario.setCidade(usuario.getCidade());
                mUsuario.setBairro(usuario.getBairro());

                mUsuario.setCpf(usuario.getCpf());
                mUsuario.setData_nasc(usuario.getData_nasc());
                mUsuario.setCnh(usuario.getCnh());
                mUsuario.setCnh_validade(usuario.getCnh_validade());
                mUsuario.setCrlv(usuario.getCrlv());
                mUsuario.setPlaca_moto(usuario.getPlaca_moto());

                mUsuario.setSn_aceitou_termo(usuario.getSn_aceitou_termo());
                mUsuario.setSn_mototaxi(usuario.getSn_mototaxi());
                mUsuario.setSn_disponivel(usuario.getSn_disponivel());

                mUsuario.setTipo_veiculo(usuario.getTipo_veiculo());

                AtualizaDadosUsuarioDataService(); //Salva no banco

            }

            else if (loader.getId() == LOADER_ATUALIZA_LOCALIZACAO_USU) {
                //Não precisa fazer, a base ja foi atualizada

            }

            else if (loader.getId() == LOADER_ATUALIZA_TEXTVIEW_PAINEL_SOLICITACAO) {
                List<Solicitacao> lstSolicitacao = (List) data;

                if(lstSolicitacao.size() == 0){
                    textViewPainelSolicitacao.setVisibility(View.INVISIBLE);
                    bUsuSolicitouMoto = false;

                    if(mMarkerMotociclista!=null) {
                        mMarkerMotociclista.remove();
                        mGoogleMap.clear();
                    }

                    mLatLngSolicitacaoUsu = null;
                    mLatLngMotociclista = null;
                    mMotociclista = null;
                    bMarkerMotociclistaPosicionado = false;
                    ShowViews();
                    return;
                }
                HideViews();
                bUsuSolicitouMoto = true;
                mSolicitacaoAtendimento = null; //??? Antes alimentava apenas para usuarios mtotaxista, aproveitando a mesma variavel

                for (int i = 0; i < lstSolicitacao.size(); i++){
                    Solicitacao solicitacao = lstSolicitacao.get(i);
                    String status = "";
                    String NomeMototaxista = solicitacao.getMototaxi_nome();
                    if(solicitacao.getStatus_sol().equals("A")){
                        status = "Aguardando Atendimento";
                        textViewPainelSolicitacao.setText("MOTO SOLICITADA\nSTATUS: " + status);
                    } else if(solicitacao.getStatus_sol().equals("E")){
                        textViewPainelSolicitacao.setText("Olá, " +mUsuario.getNome() +"!" +"\n"+NomeMototaxista+" está a caminho.");

                        //Carrega dados do motociclista
                        mLatLngSolicitacaoUsu = null;
                        mLatLngSolicitacaoUsu = new LatLng(solicitacao.getLatitude(), solicitacao.getLongitude());

                        mSolicitacaoAtendimento = solicitacao;

                        mUsuario.setIsAtendimento("S");
                        mUsuario.setAtendSolicitacao_Id(mSolicitacaoAtendimento.getSolicitacao_id());
                        mUsuario.setAtendSolicitacao_UsuarioId(mSolicitacaoAtendimento.getUsuario_id());
                        mUsuario.setAtendSolicitacao_StatusSol(mSolicitacaoAtendimento.getStatus_sol());
                        AtualizaDadosUsuarioDataService();

                        if(mMotociclista == null){
                            mMotociclista = new Usuario();
                        }

                        mMotociclista.setMototaxi_id(solicitacao.getMototaxi_id());

                        Bundle params = new Bundle();
                        params.putSerializable(Constants.USUARIO_EXTRA, mMotociclista);
                        mLoaderManager.restartLoader(LOADER_CARREGA_DADOS_MOTOCICLISTA, params, MainActivity.this);

                    }
                    else if(solicitacao.getStatus_sol().equals("I")){
                        textViewPainelSolicitacao.setText("Olá, " +mUsuario.getNome() +"!" +"\nSua corrida acaba de ser iniciada.\nBoa viagem!");

                        //Limpa mapa, o motociclista já esta com passageiro
                        if(mMarkerMotociclista!=null) {
                            mMarkerMotociclista.remove();
                            mGoogleMap.clear();
                        }

                        mLatLngSolicitacaoUsu = null;
                        mLatLngMotociclista = null;
                        mMotociclista = null;
                        bMarkerMotociclistaPosicionado = false;

                    }
                    textViewPainelSolicitacao.setVisibility(VISIBLE);

                }


            }
            else if (loader.getId() == LOADER_PINTAR_CAMINHO) {
                JSONObject json;
                json = (JSONObject) data;
                pintarCaminho(json);
                mLoaderManager.destroyLoader(LOADER_PINTAR_CAMINHO);
            }
            else if (loader.getId() == LOADER_CARREGA_ATENDIMENTO_MOTOCICLISTA) {
                mSolicitacaoAtendimento = null;
                mSolicitacaoAtendimento = (Solicitacao) data;
                if(mSolicitacaoAtendimento.getSolicitacao_id() != null && mSolicitacaoAtendimento.getSolicitacao_id()>0) {
                    bMotociclistaEmAtendimento = true;
                    textViewPainelSolicitacao.setVisibility(VISIBLE);
                    if(mSolicitacaoAtendimento.getStatus_sol().equals("E")) {
                        textViewPainelSolicitacao.setText("SOLICITANTE: " + mSolicitacaoAtendimento.getSolicitante() + "\nTELEFONE: " + mSolicitacaoAtendimento.getCelular_solicitante() + "\nEND.: " + mSolicitacaoAtendimento.getLocal_origem());
                        adicionaMarkerSolicitanteAtendimentoCalcRota();
                    }
                    else
                    {
                        textViewPainelSolicitacao.setText("SOLICITANTE: " + mSolicitacaoAtendimento.getSolicitante() + "\nDESTINO: " + mSolicitacaoAtendimento.getLocal_destino());
                    }
                }
                else
                {
                    textViewPainelSolicitacao.setVisibility(View.INVISIBLE);

                    bMotociclistaEmAtendimento = false;
                    mSolicitacaoAtendimento = null;
                    if(mMarkerSolicitante != null) {
                        mMarkerSolicitante.remove();
                        mMarkerSolicitante = null;
                        mGoogleMap.clear();
                    }
                    mLatLngSolicitante = null;
                    if (polylineMap != null) {
                        polylineMap.remove();
                        polylineMap = null;
                    }

                }
                mLoaderManager.destroyLoader(LOADER_CARREGA_ATENDIMENTO_MOTOCICLISTA);
            }
            else if (loader.getId() == LOADER_CARREGA_DADOS_MOTOCICLISTA) {
                mMotociclista = null;
                mMotociclista = (Usuario) data;
                adicionaMarkerMotociclistaMapa();
                mLoaderManager.destroyLoader(LOADER_CARREGA_DADOS_MOTOCICLISTA);
            }
            else if (loader.getId() == LOADER_ADDRESS_GET_LOCATION){
                 List<Address> retornoAddress = (List<Address>) data;
                 if (retornoAddress.size() > 0) {

                     if(mTxtViewDefineLocal.getVisibility() == View.INVISIBLE) {
                         mTxtViewDefineLocal.setVisibility(View.VISIBLE);
                     }
                    String Local = retornoAddress.get(0).getAddressLine(0);
                     if(bDefinePartida) {
                         mTxtViewDefineLocal.setText("PONTO DE PARTIDA\n" + Local);
                     }
                     else
                     {
                         mTxtViewDefineLocal.setText("DESTINO\n" + Local);

                         //Traçar a Rota para calcular a distancia baseada na origem e destino
                         String url = montarURLRotaMapa(mLatLngOrigem.latitude, mLatLngOrigem.longitude,
                                 mLatLngDestino.latitude, mLatLngDestino.longitude );

                         mDistanciaDaRota           = 0.000;
                         mFaixaPrecoPresumidoTukTuk = "";
                         mFaixaPrecoPresumidoMoto   = "";
                         mFaixaPrecoPresumidoCarro  = "";

                         Bundle params = new Bundle();
                         params.putString("url_rota", url);
                         mLoaderManager.restartLoader(LOADER_CALCULA_DISTANCIA_PRESUMIDA, params, MainActivity.this);
                     }


                 }

                mLoaderManager.destroyLoader(LOADER_ADDRESS_GET_LOCATION);
            }
            else if (loader.getId() == LOADER_GET_FROM_LOCATION_USUARO){

                List<Address> retornoAddress = (List<Address>) data;
                if (retornoAddress.size() > 0) {

                    String estado = retornoAddress.get(0).getAdminArea();
                    String cidade = retornoAddress.get(0).getLocality();
                    String bairro = retornoAddress.get(0).getSubLocality();

                    mUsuario.setCidade(cidade);
                    mUsuario.setEstado(estado);
                    mUsuario.setBairro(bairro);

                    AtualizaLocalizacaoUsuarioWeb();

                }

                //mLoaderManager.destroyLoader(LOADER_GET_FROM_LOCATION_USUARO);
            }
            else if (loader.getId() == LOADER_CALCULA_DISTANCIA_PRESUMIDA) {
                //Igual ao LOADER_PINTAR_CAMINHO só que ao invés de pintar o mapa calculará a distancia em cima do json extraido
                JSONObject json;
                json = (JSONObject) data;
                //pintarCaminho(json);

                //Calcular Distância
                mDistanciaDaRota = DistanciaPresumidaDaRota(json);
                mDistanciaDaRota = truncateDecimal(mDistanciaDaRota,3).doubleValue();

                mFaixaPrecoPresumidoMoto = PrecoPresumidoMoto(mDistanciaDaRota);
                mFaixaPrecoPresumidoTukTuk = PrecoPresumidoTukTuk(mDistanciaDaRota);

                textView_preco_estimado.setText("Moto: "+mFaixaPrecoPresumidoMoto+"\nTukTuk: "+mFaixaPrecoPresumidoTukTuk);
                linearlayout_preco_estimado.setVisibility(VISIBLE);

                mLoaderManager.destroyLoader(LOADER_CALCULA_DISTANCIA_PRESUMIDA);
            }
            else if (loader.getId() == LOADER_CHAT_NOVA_MENSAGEM) {
                Integer quant = (Integer) data;
                if (quant > iQtdMensagemChat){
                    //fabNovaMensagem.setVisibility(VISIBLE);
                    iQtdMensagemChat = quant;
                    Toast.makeText(MainActivity.this, "Você recebeu uma nova mensagem", Toast.LENGTH_LONG).show();
                }
                mLoaderManager.destroyLoader(LOADER_CHAT_NOVA_MENSAGEM);
            }
            else if (loader.getId() == LOADER_CARREGA_MTX_PROXIMO_USUARIO) {
                List<Usuario> lstMotociclistas = (List) data;

                if(lstMotociclistas.size() > 0) {
                    LimpaListaMarkerMtxProximosUsuario();
                    for (int i = 0; i < lstMotociclistas.size(); i++) {
                        Usuario motociclista = lstMotociclistas.get(i);

                        LatLng latLngMtx = new LatLng(motociclista.getLatitude_atual(), motociclista.getLongitude_atual());
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLngMtx)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.moto_motoqueiro));
                        //.icon(BitmapDescriptorFactory.defaultMarker());
                        lstMarkersMtxProximasUsu.add(mGoogleMap.addMarker(markerOptions));
                    }
                }

                mLoaderManager.destroyLoader(LOADER_CARREGA_MTX_PROXIMO_USUARIO);
            }

            else if (loader.getId() == LOADER_ATUALIZA_STATUS_MTX_DISPONIVEL) {
                String valueRetorno = (String) data;
                if(valueRetorno.equals("OK")){
                    if(mSwitchDisponivel.isChecked()) {
                        mUsuario.setSn_disponivel("S");
                        Toast.makeText(MainActivity.this, "Status alterado para Disponível", Toast.LENGTH_SHORT).show();
                    }else {
                        mUsuario.setSn_disponivel("N");
                        Toast.makeText(MainActivity.this, "Status alterado para Indisponível", Toast.LENGTH_SHORT).show();
                    }
                    AtualizaDadosUsuarioDataService(); //Salva no banco
                }
                mLoaderManager.destroyLoader(LOADER_ATUALIZA_STATUS_MTX_DISPONIVEL);

                if(bSair) {
                    mAuth.signOut();
                }
            }

        }

        else

        {
            if (loader.getId() == LOADER_CARREGA_DADOS_USU) {
                Toast.makeText(MainActivity.this, "SEM COMUNICAÇÃO COM O SERVIDOR", Toast.LENGTH_SHORT).show();
                mLoaderManager.destroyLoader(LOADER_CARREGA_DADOS_USU);
            }
            else if (loader.getId() == LOADER_ATUALIZA_TEXTVIEW_PAINEL_SOLICITACAO) {
                Toast.makeText(MainActivity.this, "SEM COMUNICAÇÃO COM O SERVIDOR", Toast.LENGTH_SHORT).show();
                mLoaderManager.destroyLoader(LOADER_ATUALIZA_TEXTVIEW_PAINEL_SOLICITACAO);
            }
            else if (loader.getId() == LOADER_ATUALIZA_LOCALIZACAO_USU)
            {
                Toast.makeText(MainActivity.this, "SEM COMUNICAÇÃO COM O SERVIDOR", Toast.LENGTH_SHORT).show();
                mLoaderManager.destroyLoader(LOADER_ATUALIZA_LOCALIZACAO_USU);
            }
            else if (loader.getId() == LOADER_PINTAR_CAMINHO) {
                //Não acessa o servidor
                mLoaderManager.destroyLoader(LOADER_PINTAR_CAMINHO);
            }
            else if (loader.getId() == LOADER_CARREGA_ATENDIMENTO_MOTOCICLISTA) {
                textViewPainelSolicitacao.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "SEM COMUNICAÇÃO COM O SERVIDOR", Toast.LENGTH_SHORT).show();
                bMotociclistaEmAtendimento = false;
                mLoaderManager.destroyLoader(LOADER_CARREGA_ATENDIMENTO_MOTOCICLISTA);
            }
            else if (loader.getId() == LOADER_CARREGA_DADOS_MOTOCICLISTA) {
                Toast.makeText(MainActivity.this, "SEM COMUNICAÇÃO COM O SERVIDOR", Toast.LENGTH_SHORT).show();
                mLoaderManager.destroyLoader(LOADER_CARREGA_DADOS_MOTOCICLISTA);

            }
            else if (loader.getId() == LOADER_ADDRESS_GET_LOCATION){
                mLoaderManager.destroyLoader(LOADER_ADDRESS_GET_LOCATION);
            }
            else if (loader.getId() == LOADER_GET_FROM_LOCATION_USUARO){
                mLoaderManager.destroyLoader(LOADER_GET_FROM_LOCATION_USUARO);
            }
            else if (loader.getId() == LOADER_CALCULA_DISTANCIA_PRESUMIDA) {
                //Não acessa o servidor
                mLoaderManager.destroyLoader(LOADER_CALCULA_DISTANCIA_PRESUMIDA);
            }
            else if (loader.getId() == LOADER_CHAT_NOVA_MENSAGEM) {
                mLoaderManager.destroyLoader(LOADER_CHAT_NOVA_MENSAGEM);
            }
            else if (loader.getId() == LOADER_CARREGA_MTX_PROXIMO_USUARIO) {
                mLoaderManager.destroyLoader(LOADER_CARREGA_MTX_PROXIMO_USUARIO);
            }
            else if (loader.getId() == LOADER_ATUALIZA_STATUS_MTX_DISPONIVEL) {
                mLoaderManager.destroyLoader(LOADER_ATUALIZA_STATUS_MTX_DISPONIVEL);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }


    private void AtualizaDadosUsuarioDataService() {
        //Após carregados os dados do servidor WEB salvar no banco local pra manter atualizado
        Intent intent = new Intent(this, DataService.class);
        intent.putExtra(Constants.RECEIVER, mDataResultReceiver);
        intent.putExtra(Constants.CODE_TABLE_EXTRA, Constants.CODE_TABLE_USUARIO);
        intent.putExtra(Constants.CODE_OPERATION_EXTRA, Constants.OPERATION_UPDATE);
        intent.putExtra(Constants.USUARIO_EXTRA, mUsuario);
        startService(intent);
    }

    public void defineLocalOnClick(View view) {
        //Evento Declarado no XML do 'mTxtViewDefineLocal'
        //
        /*
        if(bDefinePartida){
            Intent it = new Intent(this, PesquisaEnderecoActivity.class);
            startActivityForResult(it,RC_PESQUISA_ENDERECO);
        }
        else
        {
            Intent it = new Intent(this, PesquisaEnderecoActivity.class);
            startActivityForResult(it,RC_PESQUISA_ENDERECO);
        }
        */
    }

    public void fabNovaMensagemOnClick(View view) {
        if(mSolicitacaoAtendimento==null){
            return;
        }
        Intent it = new Intent(this, ChatActivity.class);
        it.putExtra(Constants.SOLICITACAO_EXTRA, mSolicitacaoAtendimento);
        it.putExtra(Constants.USUARIO_EXTRA, mUsuario);
        startActivity(it);
    }


    //Result para o service execução - Service - DataService
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
                        if (resultData.getInt(Constants.CODE_OPERATION_EXTRA) == Constants.OPERATION_INSERT) {

                        }
                        else if (resultData.getInt(Constants.CODE_OPERATION_EXTRA) == Constants.OPERATION_UPDATE) {
                            //Foi atualizado a tabela de usuario, recarrega o objeto usuario
                            CarregaAtualizaDadosUsuarioBanco(); //carrega do banco

                            if(!bUsuSolicitouMoto) {
                                updateUI();
                            } else {
                                HideViews();
                            }
                        }
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resultData.getInt(Constants.CODE_OPERATION_EXTRA) == Constants.OPERATION_INSERT) {
                            //
                            return;
                        }
                        else if (resultData.getInt(Constants.CODE_OPERATION_EXTRA) == Constants.OPERATION_UPDATE) {
                            //
                            return;
                        }

                    }
                });
            }

            //Parar o servico
            Intent intent = new Intent(MainActivity.this, DataService.class);
            stopService(intent);

        }


    }



}
