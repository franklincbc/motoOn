package com.franklincbc.motoon;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.franklincbc.motoon.SMS.SMSHelper;

import java.util.Random;

public class ValidarCelularSMSActivity extends AppCompatActivity {

    public static final String LETRAS_NUMEROS="ABCDEFGHIJKLMNOPQRSTUVYWXZ1234567890";
    public static final int RC_PERMISSION_SEND_SMS = 0;

    String mCodigoGerado;
    Random mRandom;

    TextView mTextViewCelular;
    EditText mEdtCodigo;
    Button mBtnNovoCodigo;
    Button mBtnConfirmar;

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId()==R.id.validar_celular_btn_gerarCodigo){
                geraNovoCodigo();
            }
            else if(v.getId()==R.id.validar_celular_btn_Confirmar){
                if(mEdtCodigo.getText().toString().equals(mCodigoGerado)){
                    setResult(RESULT_OK);
                    finish();
                }
                else
                {
                    Toast.makeText(ValidarCelularSMSActivity.this, "Código inválido", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validar_celular_sms);
        setTitle("VALIDAR NÚMERO");

        mTextViewCelular = (TextView) findViewById(R.id.validar_celular_numero);
        mEdtCodigo = (EditText) findViewById(R.id.validar_celular_edt_codigo);
        mBtnNovoCodigo = (Button)findViewById(R.id.validar_celular_btn_gerarCodigo);
        mBtnConfirmar = (Button)findViewById(R.id.validar_celular_btn_Confirmar);

        mBtnNovoCodigo.setOnClickListener(onClickListener);
        mBtnConfirmar.setOnClickListener(onClickListener);

        mTextViewCelular.setText(getIntent().getStringExtra("numero"));
        mRandom = new Random();
        geraNovoCodigo();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case RC_PERMISSION_SEND_SMS:
                for (int i = 0; i < permissions.length ; i++) {
                    if(permissions[i].equalsIgnoreCase(android.Manifest.permission.SEND_SMS) && grantResults[i] == PackageManager.PERMISSION_GRANTED){
                        geraNovoCodigo();
                    }
                    else
                    {
                        Toast.makeText(this, "Necessário permissão para enviar o código via SMS", Toast.LENGTH_SHORT).show();
                    }
                }
        }

    }

    public void geraNovoCodigo(){
        mCodigoGerado = "";
        Integer index = -1;
        for (int i = 0; i <6 ; i++) {
            index = mRandom.nextInt( LETRAS_NUMEROS.length() );
            mCodigoGerado = mCodigoGerado + LETRAS_NUMEROS.substring(index, index+1);
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS}, RC_PERMISSION_SEND_SMS);
        }
        else {

            try {
                String mNumero = mTextViewCelular.getText().toString();
                SMSHelper.enviarSMS(mNumero, "CODIGO: " + mCodigoGerado + ". Digite esse codigo no local indicado para validar seu numero de celular");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

}
