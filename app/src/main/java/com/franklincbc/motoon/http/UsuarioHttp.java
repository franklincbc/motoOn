package com.franklincbc.motoon.http;

import com.franklincbc.motoon.model.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Priscila on 23/09/2016.
 */
public class UsuarioHttp {

    //Produção
    //public static final String URL = "http://192.168.25.200:8080";  //Quando estiver na rede interna
    public static final String URL = "http://franklincbc.ddns.net:8080"; //Fora da rede
    //public static final String URL = "http://177.207.242.57:8080"; //Fora da rede

    //Testes de Desenvolvimento
    //public static final String URL = "http://192.168.25.94:8090";  //Quando estiver na rede local WiFi
    //public static final String URL = "http://franklincbc.ddns.net:8090"; //Fora da rede
    //public static final String URL = "http://177.207.242.57:8090"; //Fora da rede

    //repassa a url para complementar as demais
    public static final String URL_ATIVADA = URL;

    public static final String URL_REGISTRA_USUARIO = URL_ATIVADA + "/datasnap/rest/tsmusuario/registrausuario/";
    public static final String URL_CARREGA_DADOS_USUARIO = URL_ATIVADA + "/datasnap/rest/tsmusuario/CarregaDadosUSuario/%s/";
    public static final String URL_ATUALIZA_LOCALIZACAO_USUARIO = URL_ATIVADA + "/datasnap/rest/tsmusuario/AtualizaLocalizacaoUsuario/";
    public static final String URL_SALVAR_DADOS_USUARIO = URL_ATIVADA + "/datasnap/rest/tsmusuario/SalvarDadosUsuario/";
    public static final String URL_USUARIO_POSSUI_CADASTRO = URL_ATIVADA + "/datasnap/rest/tsmusuario/UsuarioPossuiCadastro/%s/";
    public static final String URL_CARREGA_SALDO_CREDITO_MOTOTAXI = URL_ATIVADA + "/datasnap/rest/tsmusuario/CarregaSaldoCreditoMototaxista/%s/";
    public static final String URL_ACEITOU_TERMO_ADESAO = URL_ATIVADA + "/datasnap/rest/tsmusuario/AceitouTermoAdesao/";
    public static final String URL_RETORNA_QUANT_MOTOTAXI_CIDADE = URL_ATIVADA + "/datasnap/rest/tsmusuario/RetornaQtdMototaxiCidade/%s/";
    public static final String URL_CARREGA_DADOS_MOTOCICLISTA = URL_ATIVADA + "/datasnap/rest/tsmusuario/CarregaDadosMotociclista/%s/";
    public static final String URL_RETORNA_QUANT_SOLICITACAO_CIDADE = URL_ATIVADA + "/datasnap/rest/tsmusuario/RetornaQtdSolicitacaoCidade/%s/%s/";

    public static final String URL_RETORNA_QTD_NOVAS_MENSAGENS_CHAT = URL_ATIVADA + "/datasnap/rest/tsmusuario/RetornaQtdNovasMensagensChat/%s/%s/%s/%s/%s/%s/%s/";
    public static final String URL_CARREGA_MOTOCICLISTA_PROXIMO_USUARIO = URL_ATIVADA + "/datasnap/rest/tsmusuario/CarregaMotociclistaProximoUsuario/%s/";
    public static final String URL_ATUALIZA_STATUS_MTX_DISPONIVEL = URL_ATIVADA + "/datasnap/rest/tsmusuario/AtualizaStatusMtxDisponivel/";

    public static boolean UsuarioPossuiCadastro(String sEmail){

        // Abre a conexão com o servidor
        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = newClientOkHttp();

        String url = String.format(URL_USUARIO_POSSUI_CADASTRO, sEmail);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            // Realiza a chamada ao servidor
            response = client.newCall(request).execute();

            // response.body retorna o corpo da resposta, que no nosso caso é JSON
            String json = response.body().string();

            // Esse JSON retorna um objeto JSON onde a propriedade "result" traz
            // a lista dos resultados que é um tipo Retorno e um tipo usuario. Por isso, obtemos o JSONArray com esse resultado
            // e só então passamos para o GSON ler.
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            boolean bPossuiCadastro = jsonArray.getBoolean(0);

            return bPossuiCadastro;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return false;
    }


    public static Long registraUsuario(Usuario usuario) throws JSONException {

        // Abre a conexão com o servidor
        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = newClientOkHttp();

        /*String url = String.format(URL_REGISTRA_USUARIO,
                                    usuario.getEmail(),
                                    usuario.getNome());*/

        String url = URL_REGISTRA_USUARIO;

        JSONObject jsonObjt = new JSONObject();
        jsonObjt.put("email", usuario.getEmail());
        jsonObjt.put("apelido",usuario.getNome());
        jsonObjt.put("url", usuario.getUrl_photo());


        RequestBody formBody = new FormBody.Builder()
                .add("usuario", String.valueOf(jsonObjt))
                .build();

        Request request = new Request.Builder()
                .post(formBody)
                .url(url)
                .build();

        Response response = null;
        try {
            // Realiza a chamada ao servidor
            response = client.newCall(request).execute();

            // response.body retorna o corpo da resposta, que no nosso caso é JSON
            String json = response.body().string();

            // Esse JSON retorna um objeto JSON onde a propriedade "result" traz
            // a lista dos resultados que é um tipo Retorno e um tipo usuario. Por isso, obtemos o JSONArray com esse resultado
            // e só então passamos para o GSON ler.
            JSONObject jsonObject = new JSONObject(json);

            JSONArray jsonArray = jsonObject.getJSONArray("result");
            Long value = jsonArray.getLong(0);
            //String jsonList = jsonArray.toString();

            return value;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Integer SalvarDadosUsuario(Usuario usuario) throws JSONException {

        // Abre a conexão com o servidor
        OkHttpClient client = newClientOkHttp();

        String url = URL_SALVAR_DADOS_USUARIO;

        JSONObject jsonObjt = new JSONObject();
        jsonObjt.put("usuario_id", usuario.getId_servidor());
        jsonObjt.put("apelido",usuario.getNome());
        jsonObjt.put("celular",usuario.getCelular());
        jsonObjt.put("sexo",usuario.getSexo());
        jsonObjt.put("cpf",usuario.getCpf());
        if (usuario.getData_nasc() != null) {
            jsonObjt.put("data_nasc", usuario.getData_nasc().toString());
        }
        else
        {
            jsonObjt.put("data_nasc",null);
        }
        jsonObjt.put("cnh",usuario.getCnh());
        if(usuario.getCnh_validade() != null) {
            jsonObjt.put("cnh_validade", usuario.getCnh_validade().toString());
        }
        else
        {
            jsonObjt.put("cnh_validade",null);
        }
        jsonObjt.put("crlv",usuario.getCrlv());
        jsonObjt.put("placa_moto",usuario.getPlaca_moto());
        jsonObjt.put("tipo_veiculo",usuario.getTipo_veiculo());

        RequestBody formBody = new FormBody.Builder()
                .add("usuario", String.valueOf(jsonObjt))
                .build();

        Request request = new Request.Builder()
                .post(formBody)
                .url(url)
                .build();

        Response response = null;
        try {
            // Realiza a chamada ao servidor
            response = client.newCall(request).execute();

            // response.body retorna o corpo da resposta, que no nosso caso é JSON
            String json = response.body().string();

            // Esse JSON retorna um objeto JSON onde a propriedade "result" traz
            // a lista dos resultados que é um tipo Retorno e um tipo usuario. Por isso, obtemos o JSONArray com esse resultado
            // e só então passamos para o GSON ler.
            JSONObject jsonObject = new JSONObject(json);

            JSONArray jsonArray = jsonObject.getJSONArray("result");
            //Se retornar -1 é por que deu algum erro no Servidor DS
            Integer rowsAffected = jsonArray.getInt(0);

            return rowsAffected;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static Integer AtualizaLocalizacaoUsuario(Usuario usuario) throws JSONException {

        // Abre a conexão com o servidor
        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = newClientOkHttp();

        String url = URL_ATUALIZA_LOCALIZACAO_USUARIO;

        JSONObject jsonObjt = new JSONObject();
        jsonObjt.put("usuario_id", usuario.getId_servidor());
        jsonObjt.put("latitude_atual", usuario.getLatitude_atual());
        jsonObjt.put("longitude_atual",usuario.getLongitude_atual());
        jsonObjt.put("estado", usuario.getEstado());
        jsonObjt.put("cidade", usuario.getCidade());
        jsonObjt.put("bairro", usuario.getBairro());

        jsonObjt.put("sn_mototaxi", usuario.getSn_mototaxi());
        jsonObjt.put("mototaxi_id", usuario.getMototaxi_id());


        RequestBody formBody = new FormBody.Builder()
                .add("localizacaousuario", String.valueOf(jsonObjt))
                .build();

        Request request = new Request.Builder()
                .post(formBody)
                .url(url)
                .build();

        Response response = null;
        try {
            // Realiza a chamada ao servidor
            response = client.newCall(request).execute();

            // response.body retorna o corpo da resposta, que no nosso caso é JSON
            String json = response.body().string();

            // Esse JSON retorna um objeto JSON onde a propriedade "result" traz
            // a lista dos resultados que é um tipo Retorno e um tipo usuario. Por isso, obtemos o JSONArray com esse resultado
            // e só então passamos para o GSON ler.
            JSONObject jsonObject = new JSONObject(json);

            JSONArray jsonArray = jsonObject.getJSONArray("result");
            Boolean value = jsonArray.getBoolean(0);
            //String jsonList = jsonArray.toString();

            if(value){
                return 1;  //true
            } else {
                return 0;  //false
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Usuario carregaDadosUsuario(Usuario usuario){

        // Abre a conexão com o servidor
        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = newClientOkHttp();

        if (usuario.getId_servidor() == null){
            return null;
        }

        String url = String.format(URL_CARREGA_DADOS_USUARIO, usuario.getId_servidor());

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            // Realiza a chamada ao servidor
            response = client.newCall(request).execute();

            // response.body retorna o corpo da resposta, que no nosso caso é JSON
            String json = response.body().string();

            // Esse JSON retorna um objeto JSON onde a propriedade "result" traz
            // a lista dos resultados que é um tipo Retorno e um tipo usuario. Por isso, obtemos o JSONArray com esse resultado
            // e só então passamos para o GSON ler.

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            //json = jsonArray.toString();

            jsonObject = jsonArray.getJSONObject(0);
            jsonObject = jsonObject.getJSONObject("usuario");

            //jsonArray = jsonObject.getJSONArray("usuario");


            usuario.setMototaxi_id( jsonObject.getInt("mototaxi_id") );
            usuario.setNome(jsonObject.getString("apelido"));
            usuario.setLatitude_atual(jsonObject.getDouble("latitude_atual"));
            usuario.setLongitude_atual(jsonObject.getDouble("longitude_atual"));
            usuario.setCelular(jsonObject.getString("celular"));
            usuario.setSexo(jsonObject.getString("sexo"));
            usuario.setEstado(jsonObject.getString("estado"));
            usuario.setCidade(jsonObject.getString("cidade"));
            usuario.setBairro(jsonObject.getString("bairro"));
            usuario.setSn_aceitou_termo(jsonObject.getString("sn_aceitou_termo"));
            usuario.setSn_mototaxi(jsonObject.getString("sn_mototaxi"));
            usuario.setSn_disponivel(jsonObject.getString("sn_disponivel"));
            usuario.setCpf(jsonObject.getString("cpf"));
            usuario.setData_nasc(jsonObject.getString("data_nasc"));
            usuario.setCnh(jsonObject.getString("cnh"));
            usuario.setCnh_validade(jsonObject.getString("cnh_validade"));
            usuario.setCrlv(jsonObject.getString("crlv"));
            usuario.setPlaca_moto(jsonObject.getString("placa_moto"));
            usuario.setTipo_veiculo(jsonObject.getString("tipo_veiculo"));

            return usuario;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static Integer CarregaSaldoCreditoMototaxista(Integer mototaxi_id){

        // Abre a conexão com o servidor
        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = newClientOkHttp();

        String url = String.format(URL_CARREGA_SALDO_CREDITO_MOTOTAXI, mototaxi_id);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            // Realiza a chamada ao servidor
            response = client.newCall(request).execute();

            // response.body retorna o corpo da resposta, que no nosso caso é JSON
            String json = response.body().string();

            // Esse JSON retorna um objeto JSON onde a propriedade "result" traz
            // a lista dos resultados que é um tipo Retorno e um tipo usuario. Por isso, obtemos o JSONArray com esse resultado
            // e só então passamos para o GSON ler.
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            Integer saldo = jsonArray.getInt(0);

            return saldo;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String AceitouTermoAdesao(Usuario usuario) throws JSONException {

        // Abre a conexão com o servidor
        OkHttpClient client = newClientOkHttp();

        String url = URL_ACEITOU_TERMO_ADESAO;

        JSONObject jsonObjt = new JSONObject();
        jsonObjt.put("usuario_id", usuario.getId_servidor());
        jsonObjt.put("cidade",usuario.getCidade());
        jsonObjt.put("bairro",usuario.getBairro());

        RequestBody formBody = new FormBody.Builder()
                .add("usuario", String.valueOf(jsonObjt))
                .build();

        Request request = new Request.Builder()
                .post(formBody)
                .url(url)
                .build();

        Response response = null;
        try {
            // Realiza a chamada ao servidor
            response = client.newCall(request).execute();

            // response.body retorna o corpo da resposta, que no nosso caso é JSON
            String json = response.body().string();

            // Esse JSON retorna um objeto JSON onde a propriedade "result" traz
            // a lista dos resultados que é um tipo Retorno e um tipo usuario. Por isso, obtemos o JSONArray com esse resultado
            // e só então passamos para o GSON ler.
            JSONObject jsonObject = new JSONObject(json);

            JSONArray jsonArray = jsonObject.getJSONArray("result");
            //Se retornar -1 é por que deu algum erro no Servidor DS
            String result = jsonArray.getString(0);

            return result;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Integer RetornaQtdMototaxiCidade(String cidade){

        // Abre a conexão com o servidor
        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = newClientOkHttp();

        String url = String.format(URL_RETORNA_QUANT_MOTOTAXI_CIDADE, cidade);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            // Realiza a chamada ao servidor
            response = client.newCall(request).execute();

            // response.body retorna o corpo da resposta, que no nosso caso é JSON
            String json = response.body().string();

            // Esse JSON retorna um objeto JSON onde a propriedade "result" traz
            // a lista dos resultados que é um tipo Retorno e um tipo usuario. Por isso, obtemos o JSONArray com esse resultado
            // e só então passamos para o GSON ler.
            JSONObject jsonObject = new JSONObject(json);

            JSONArray jsonArray = jsonObject.getJSONArray("result");
            //Se retornar -1 é por que deu algum erro no Servidor DS
            Integer result = jsonArray.getInt(0);

            return result;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Integer RetornaQtdSolicitacaoCidade(String cidade, String tipo_veiculo){

        // Abre a conexão com o servidor
        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = newClientOkHttp();

        String url = String.format(URL_RETORNA_QUANT_SOLICITACAO_CIDADE, cidade, tipo_veiculo);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            // Realiza a chamada ao servidor
            response = client.newCall(request).execute();

            // response.body retorna o corpo da resposta, que no nosso caso é JSON
            String json = response.body().string();

            // Esse JSON retorna um objeto JSON onde a propriedade "result" traz
            // a lista dos resultados que é um tipo Retorno e um tipo usuario. Por isso, obtemos o JSONArray com esse resultado
            // e só então passamos para o GSON ler.
            JSONObject jsonObject = new JSONObject(json);

            JSONArray jsonArray = jsonObject.getJSONArray("result");
            //Se retornar -1 é por que deu algum erro no Servidor DS
            Integer result = jsonArray.getInt(0);

            return result;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static Usuario carregaDadosMotociclista(Usuario usuario){

        // Abre a conexão com o servidor
        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = newClientOkHttp();

        if (usuario.getMototaxi_id() == null){
            return null;
        }

        String url = String.format(URL_CARREGA_DADOS_MOTOCICLISTA, usuario.getMototaxi_id());

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            // Realiza a chamada ao servidor
            response = client.newCall(request).execute();

            // response.body retorna o corpo da resposta, que no nosso caso é JSON
            String json = response.body().string();

            // Esse JSON retorna um objeto JSON onde a propriedade "result" traz
            // a lista dos resultados que é um tipo Retorno e um tipo usuario. Por isso, obtemos o JSONArray com esse resultado
            // e só então passamos para o GSON ler.

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("result");

            jsonObject = jsonArray.getJSONObject(0);
            jsonObject = jsonObject.getJSONObject("usuario");

            usuario.setMototaxi_id( jsonObject.getInt("mototaxi_id") );
            usuario.setNome(jsonObject.getString("apelido"));
            usuario.setLatitude_atual(jsonObject.getDouble("latitude_atual"));
            usuario.setLongitude_atual(jsonObject.getDouble("longitude_atual"));
            usuario.setCelular(jsonObject.getString("celular"));
            usuario.setSexo(jsonObject.getString("sexo"));
            usuario.setEstado(jsonObject.getString("estado"));
            usuario.setCidade(jsonObject.getString("cidade"));
            usuario.setBairro(jsonObject.getString("bairro"));
            usuario.setSn_aceitou_termo(jsonObject.getString("sn_aceitou_termo"));
            usuario.setSn_mototaxi(jsonObject.getString("sn_mototaxi"));
            usuario.setSn_disponivel(jsonObject.getString("sn_disponivel"));
            usuario.setCpf(jsonObject.getString("cpf"));
            usuario.setData_nasc(jsonObject.getString("data_nasc"));
            usuario.setCnh(jsonObject.getString("cnh"));
            usuario.setCnh_validade(jsonObject.getString("cnh_validade"));
            usuario.setCrlv(jsonObject.getString("crlv"));
            usuario.setPlaca_moto(jsonObject.getString("placa_moto"));
            String tipo = jsonObject.getString("tipo_veiculo");
            usuario.setTipo_veiculo(tipo);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return usuario;
    }

    public static Integer RetornaQtdNovasMensagensChat(Integer solicitacao_usuario_id, Integer solicitacao_id, Integer usuario_id, Integer mototaxi_id, String sBackgroundService, String sn_mototaxi){

        // Abre a conexão com o servidor
        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = newClientOkHttp();

        String url = String.format(URL_RETORNA_QTD_NOVAS_MENSAGENS_CHAT, solicitacao_usuario_id, solicitacao_id,"", usuario_id, mototaxi_id, sBackgroundService, sn_mototaxi);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            // Realiza a chamada ao servidor
            response = client.newCall(request).execute();

            // response.body retorna o corpo da resposta, que no nosso caso é JSON
            String json = response.body().string();

            // Esse JSON retorna um objeto JSON onde a propriedade "result" traz
            // a lista dos resultados que é um tipo Retorno e um tipo usuario. Por isso, obtemos o JSONArray com esse resultado
            // e só então passamos para o GSON ler.
            JSONObject jsonObject = new JSONObject(json);

            JSONArray jsonArray = jsonObject.getJSONArray("result");
            //Se retornar -1 é por que deu algum erro no Servidor DS
            Integer result = jsonArray.getInt(0);

            return result;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static List<Usuario> carregaMotociclistaProximoUsuario(String cidade){

        List<Usuario> LstMotociclistas = new ArrayList<>();

        // Abre a conexão com o servidor
        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = newClientOkHttp();

        String url = String.format(URL_CARREGA_MOTOCICLISTA_PROXIMO_USUARIO, cidade);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            // Realiza a chamada ao servidor
            response = client.newCall(request).execute();

            // response.body retorna o corpo da resposta, que no nosso caso é JSON
            String json = response.body().string();

            // Esse JSON retorna um objeto JSON onde a propriedade "result" traz
            // a lista dos resultados que é um tipo Retorno e um tipo usuario. Por isso, obtemos o JSONArray com esse resultado
            // e só então passamos para o GSON ler.

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            //json = jsonArray.toString();

            jsonObject = jsonArray.getJSONObject(0);
            jsonArray = jsonObject.getJSONArray("motociclistas");

            for (int i = 0; i < jsonArray.length(); i++) {

                //Ler o objecto JSON
                jsonObject = jsonArray.getJSONObject(i);

                Usuario usuario = new Usuario();
                usuario.setId_servidor(jsonObject.getInt("usuario_id"));
                usuario.setMototaxi_id(jsonObject.getInt("mototaxi_id"));
                usuario.setNome(jsonObject.getString("apelido"));
                usuario.setLatitude_atual(jsonObject.getDouble("latitude_atual"));
                usuario.setLongitude_atual(jsonObject.getDouble("longitude_atual"));
                usuario.setSexo(jsonObject.getString("sexo"));
                usuario.setCidade(jsonObject.getString("cidade"));
                usuario.setSn_mototaxi(jsonObject.getString("sn_mototaxi"));
                String tipo = jsonObject.getString("tipo_veiculo");
                usuario.setTipo_veiculo(tipo);
                LstMotociclistas.add(usuario);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return LstMotociclistas;
    }


    public static String AtualizaStatusMtxDisponivel(Integer mototaxi_id, String sn_disponivel) throws JSONException {

        // Abre a conexão com o servidor
        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = newClientOkHttp();

        String url = URL_ATUALIZA_STATUS_MTX_DISPONIVEL;

        JSONObject jsonObjt = new JSONObject();
        jsonObjt.put("mototaxi_id", mototaxi_id);
        jsonObjt.put("sn_disponivel",sn_disponivel);

        RequestBody formBody = new FormBody.Builder()
                .add("atualizaStatus", String.valueOf(jsonObjt))
                .build();

        Request request = new Request.Builder()
                .post(formBody)
                .url(url)
                .build();

        Response response = null;
        try {
            // Realiza a chamada ao servidor
            response = client.newCall(request).execute();

            // response.body retorna o corpo da resposta, que no nosso caso é JSON
            String json = response.body().string();

            // Esse JSON retorna um objeto JSON onde a propriedade "result" traz
            // a lista dos resultados que é um tipo Retorno e um tipo usuario. Por isso, obtemos o JSONArray com esse resultado
            // e só então passamos para o GSON ler.
            JSONObject jsonObject = new JSONObject(json);

            JSONArray jsonArray = jsonObject.getJSONArray("result");
            String value = jsonArray.getString(0);
            //String jsonList = jsonArray.toString();

            return value;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static OkHttpClient newClientOkHttp() {
        try {
            OkHttpClient.Builder b = new OkHttpClient.Builder();
            b.readTimeout(30000, TimeUnit.MILLISECONDS);
            b.writeTimeout(30000, TimeUnit.MILLISECONDS);
            return b.build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
