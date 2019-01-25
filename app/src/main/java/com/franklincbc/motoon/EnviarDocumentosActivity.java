package com.franklincbc.motoon;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;

public class EnviarDocumentosActivity extends AppCompatActivity {


    public static final int RC_CNH = 0;
    public static final int RC_CNH_GALERIA = 1;
    public static final int RC_CRLV = 2;
    public static final int RC_CRLV_GALERIA = 3;
    public static final int RC_COMP_RES = 4;
    public static final int RC_COMP_RES_GALERIA = 5;
    public static final int RC_FOTO = 6;
    public static final int RC_FOTO_GALERIA = 7;

    public static final int IMAGE_GALLERY_REQUEST = 8;

    LinearLayout layoutCNH;
    LinearLayout layoutCRLV;
    LinearLayout layoutCompRes;
    LinearLayout layoutFoto;

    String foto = "";

    Button btnCnh;
    Button btnCrlv;
    Button btnCompRes;
    Button btnFoto;

    Bitmap bitmapAux;

    Bitmap imgCnh;
    Bitmap imgCrlv;
    Bitmap imgCompRes;
    Bitmap imgFoto;

    ImageView imageViewCnh;
    ImageView imageViewCrlv;
    ImageView imageViewCompRes;
    ImageView imageViewFoto;

    private AlertDialog alerta;

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            foto = "";
            switch (v.getId()){
                case R.id.enviar_documentos_button_cnh:
                    foto = "CNH";
                    break;
                case R.id.enviar_documentos_button_crlv:
                    foto = "CRLV";
                    break;
                case R.id.enviar_documentos_button_comp_res:
                    foto = "COMP_RES";
                    break;
                case R.id.enviar_documentos_button_foto:
                    foto = "FOTO";
                    break;
                case R.id.enviar_documentos_button_enviar:
                    foto = "";
                    //
                    break;
            }

            if(!foto.equals("")) {
                if (ActivityCompat.checkSelfPermission(EnviarDocumentosActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    alerta_galeriaOuCamera();
                }
                else
                {
                    ActivityCompat.requestPermissions(EnviarDocumentosActivity.this,
                            new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE },
                            IMAGE_GALLERY_REQUEST);
                }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_documentos);

        layoutCNH = (LinearLayout)findViewById(R.id.enviar_documentos_container_cnh);
        layoutCRLV = (LinearLayout)findViewById(R.id.enviar_documentos_container_crlv);
        layoutCompRes = (LinearLayout)findViewById(R.id.enviar_documentos_container_comp_res);
        layoutFoto = (LinearLayout)findViewById(R.id.enviar_documentos_container_foto);

        imageViewCnh = (ImageView)findViewById(R.id.enviar_documentos_imageView_Cnh);
        imageViewCrlv = (ImageView)findViewById(R.id.enviar_documentos_imageView_Crlv);
        imageViewCompRes = (ImageView)findViewById(R.id.enviar_documentos_imageView_Comp_res);
        imageViewFoto = (ImageView)findViewById(R.id.enviar_documentos_imageView_Foto);

        btnCnh = (Button)findViewById(R.id.enviar_documentos_button_cnh);
        btnCrlv = (Button)findViewById(R.id.enviar_documentos_button_crlv);
        btnCompRes = (Button)findViewById(R.id.enviar_documentos_button_comp_res);
        btnFoto = (Button)findViewById(R.id.enviar_documentos_button_foto);

        btnCnh.setOnClickListener(onClickListener);
        btnCrlv.setOnClickListener(onClickListener);
        btnCompRes.setOnClickListener(onClickListener);
        btnFoto.setOnClickListener(onClickListener);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_GALLERY_REQUEST){
                alerta_galeriaOuCamera();
            }
            else if(requestCode == RC_CNH){
                //imagem veio da camera
                Bundle extras = data.getExtras();
                imgCnh = (Bitmap) extras.get("data");
                imageViewCnh.setImageBitmap(imgCnh);
                layoutCNH.setBackgroundColor(ContextCompat.getColor(EnviarDocumentosActivity.this, R.color.greenLight));
                foto = "";
            }
            else if(requestCode == RC_CRLV){
                //imagem veio da camera
                Bundle extras = data.getExtras();
                imgCrlv = (Bitmap) extras.get("data");
                imageViewCrlv.setImageBitmap(imgCrlv);
                layoutCRLV.setBackgroundColor(ContextCompat.getColor(EnviarDocumentosActivity.this, R.color.greenLight));
                foto = "";
            }
            else if(requestCode == RC_COMP_RES){
                //imagem veio da camera
                Bundle extras = data.getExtras();
                imgCompRes = (Bitmap) extras.get("data");
                imageViewCompRes.setImageBitmap(imgCompRes);
                layoutCompRes.setBackgroundColor(ContextCompat.getColor(EnviarDocumentosActivity.this, R.color.greenLight));
                foto = "";
            }
            else if(requestCode == RC_FOTO){
                //imagem veio da camera
                Bundle extras = data.getExtras();
                imgFoto = (Bitmap) extras.get("data");
                imageViewFoto.setImageBitmap(imgFoto);
                layoutFoto.setBackgroundColor(ContextCompat.getColor(EnviarDocumentosActivity.this, R.color.greenLight));
                foto = "";
            }
            else if(requestCode == RC_CNH_GALERIA ||
                    requestCode == RC_CRLV_GALERIA ||
                    requestCode == RC_COMP_RES_GALERIA ||
                    requestCode == RC_FOTO_GALERIA){
                //imagem veio da galeria
                bitmapAux = null;
                new LoadImageTask(this).execute(data.getData());
            }
        }
        else
        {
            foto = "";
        }

    }

    private void alerta_galeriaOuCamera() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);//Cria o gerador do AlertDialog
        builder.setTitle("Atenção");//define o titulo
        builder.setMessage("Capturar imagem da Câmera ou Galeria?");//define a mensagem

        //define um botão como positivo para adicionar a origem
        builder.setPositiveButton("GALERIA", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                    if (foto.equals("CNH")) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), RC_CNH_GALERIA);
                    } else if (foto.equals("CRLV")) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), RC_CRLV_GALERIA);
                    } else if (foto.equals("COMP_RES")) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), RC_COMP_RES_GALERIA);
                    } else if (foto.equals("FOTO")) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), RC_FOTO_GALERIA);
                    }
            }
        });

        //Usa  o botao de negative para adicionar o destino. Gambi
        builder.setNegativeButton("CÂMERA", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                if (foto.equals("CNH")){
                    Intent itCnh = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(itCnh, RC_CNH);
                }
                else if (foto.equals("CRLV")){
                    Intent itCrlv = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(itCrlv, RC_CRLV);
                }
                else if (foto.equals("COMP_RES")){
                    Intent itCrlv = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(itCrlv, RC_COMP_RES);
                }
                else if (foto.equals("FOTO")){
                    Intent itCrlv = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(itCrlv, RC_FOTO);
                }

            }
        });

        alerta = builder.create();//cria o AlertDialog
        alerta.show();//Exibe
    }


    class LoadImageTask extends AsyncTask<Uri, Void, Bitmap> {

        WeakReference<EnviarDocumentosActivity> mActivity;

        public LoadImageTask(EnviarDocumentosActivity activity) {
            this.mActivity = new WeakReference<>(activity);
        }

        public EnviarDocumentosActivity getActivity() {
            return mActivity.get();
        }

        @Override
        protected Bitmap doInBackground(Uri... params) {
            if (getActivity() != null) {
                try {
                    return BitmapFactory.decodeStream(
                            getActivity().getContentResolver().openInputStream(params[0]));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (getActivity() != null){

                if (foto.equals("CNH")){
                    getActivity().imgCnh = bitmap;
                    imageViewCnh.setImageBitmap(getActivity().imgCnh);
                    layoutCNH.setBackgroundColor(ContextCompat.getColor(EnviarDocumentosActivity.this, R.color.greenLight));
                }
                else if (foto.equals("CRLV")){
                    getActivity().imgCrlv = bitmap;
                    imageViewCrlv.setImageBitmap(getActivity().imgCrlv);
                    layoutCRLV.setBackgroundColor(ContextCompat.getColor(EnviarDocumentosActivity.this, R.color.greenLight));
                }
                else if (foto.equals("COMP_RES")){
                    getActivity().imgCompRes = bitmap;
                    imageViewCompRes.setImageBitmap(getActivity().imgCompRes);
                    layoutCompRes.setBackgroundColor(ContextCompat.getColor(EnviarDocumentosActivity.this, R.color.greenLight));
                }
                else if (foto.equals("FOTO")){
                    getActivity().imgFoto = bitmap;
                    imageViewFoto.setImageBitmap(getActivity().imgFoto);
                    layoutFoto.setBackgroundColor(ContextCompat.getColor(EnviarDocumentosActivity.this, R.color.greenLight));
                }

                foto = "";
            }

        }

    }

}
