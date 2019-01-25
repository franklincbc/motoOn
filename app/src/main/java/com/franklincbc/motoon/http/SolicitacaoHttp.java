package com.franklincbc.motoon.http;

import android.support.annotation.Nullable;

import com.franklincbc.motoon.model.Chat;
import com.franklincbc.motoon.model.Solicitacao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by franklin.carvalho on 26/10/2016.
 */

public class SolicitacaoHttp {

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

    public static final String URL_REGISTRA_SOLICITACAO = URL_ATIVADA + "/datasnap/rest/tsmusuario/RegistraSolicitacaoMoto/";
    public static final String URL_CARREGA_SOLICITACAOES = URL_ATIVADA + "/datasnap/rest/tsmusuario/CarregaSolicitacoesUsuario/%s/";
    public static final String URL_CARREGA_PAINEL_CHAMADOS = URL_ATIVADA + "/datasnap/rest/tsmusuario/CarregaPainelDeChamados/";
    public static final String URL_ATENDER_CHAMADO = URL_ATIVADA + "/datasnap/rest/tsmusuario/AtenderChamado/";
    public static final String URL_INICIAR_CORRIDA_CHAMADO = URL_ATIVADA + "/datasnap/rest/tsmusuario/IniciarCorridaChamado/";
    public static final String URL_FINALIZAR_CHAMADO = URL_ATIVADA + "/datasnap/rest/tsmusuario/FinalizarChamado/";
    public static final String URL_DESISTIR_CHAMADO = URL_ATIVADA + "/datasnap/rest/tsmusuario/DesistirChamado/";
    public static final String URL_CARREGA_SOLICITACAO_CORRENTE = URL_ATIVADA + "/datasnap/rest/tsmusuario/CarregaSolicitacaoCorrenteUsuario/%s/";
    private static final String URL_CANCELAR_CHAMADO = URL_ATIVADA + "/datasnap/rest/tsmusuario/CancelarChamado/";
    private static final String URL_CARREGA_SOLICITACAO_CORRENTE_MOTOCICLISTA_ATENDIMENTO = URL_ATIVADA + "/datasnap/rest/tsmusuario/CarregaSolicitacaoCorrenteMotociclistaAtendimento/%s/";
    public static final String URL_CARREGA_ATENDIMENTOS_MOTOCICLISTA = URL_ATIVADA + "/datasnap/rest/tsmusuario/CarregaAtendimentosMotociclista/%s/";

    public static final String URL_CARREGA_CHAT = URL_ATIVADA + "/datasnap/rest/tsmusuario/CarregaChatSolicitacao/%s/%s/";
    public static final String URL_INSERT_CHAT_SOLICITACAO = URL_ATIVADA + "/datasnap/rest/tsmusuario/InsertChatSolicitacao/";

    @Nullable
    public static Long registraSolicitacao(Integer usuario_id,
                                           String solicitante,
                                           String local_origem,
                                           String ponto_referencia,
                                           String local_destino,
                                           String informacao_adicional,
                                           String data_solicitacao,
                                           Double latitude,
                                           Double longitude,
                                           String bairro,
                                           String cidade,
                                           String tipo_veiculo,
                                           Double latitude_destino,
                                           Double longitude_destino,
                                           Double distancia_presumida,
                                           String faixa_preco_presumido) throws JSONException {


        // Abre a conexão com o servidor
        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = newClientOkHttp();

        /*String url = String.format(URL_REGISTRA_SOLICITACAO,
                                                usuario_id,
                                                solicitante,
                                                local_origem,
                                                ponto_referencia,
                                                local_destino,
                                                informacao_adicional,
                                                "",
                                                latitude,
                                                longitude ); */

        String url = URL_REGISTRA_SOLICITACAO;

        JSONObject jsonObjt = new JSONObject();
        jsonObjt.put("usuario_id", usuario_id);
        jsonObjt.put("solicitante",solicitante);
        jsonObjt.put("local_origem",local_origem);
        jsonObjt.put("ponto_referencia",ponto_referencia);
        jsonObjt.put("local_destino",local_destino);
        jsonObjt.put("informacao_adicional",informacao_adicional);
        jsonObjt.put("data_solicitacao",data_solicitacao);
        jsonObjt.put("latitude",latitude);
        jsonObjt.put("longitude",longitude);
        jsonObjt.put("bairro",bairro);
        jsonObjt.put("cidade",cidade);
        jsonObjt.put("tipo_veiculo",tipo_veiculo);

        jsonObjt.put("latitude_destino",latitude_destino);
        jsonObjt.put("longitude_destino",longitude_destino);
        jsonObjt.put("distancia_presumida",distancia_presumida);
        jsonObjt.put("faixa_preco_presumido",faixa_preco_presumido);

        RequestBody formBody = new FormBody.Builder()
                .add("solicitacao", String.valueOf(jsonObjt))
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


    public static List<Solicitacao> carregaSolicitacoes(Integer usuario_id){
        List<Solicitacao> solicitacoes = new ArrayList<>();

        // Abre a conexão com o servidor
        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = newClientOkHttp();

        String url = String.format(URL_CARREGA_SOLICITACAOES, usuario_id);

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
            jsonArray = jsonObject.getJSONArray("solicitacao");

            for (int i = 0; i < jsonArray.length(); i++){

                //Ler o objecto JSON
                jsonObject = jsonArray.getJSONObject(i);

                Solicitacao solicitacao = new Solicitacao();
                solicitacao.setUsuario_id(usuario_id);
                solicitacao.setSolicitacao_id( jsonObject.getInt("solicitacao_id") );
                solicitacao.setSolicitante( jsonObject.getString("solicitante") );
                solicitacao.setMototaxi_id(jsonObject.getInt("mototaxi_id"));
                solicitacao.setData_solicitacao(jsonObject.getString("data_solicitacao"));
                solicitacao.setLatitude(jsonObject.getDouble("latitude"));
                solicitacao.setLongitude(jsonObject.getDouble("longitude"));
                solicitacao.setLocal_origem(jsonObject.getString("local_origem"));
                solicitacao.setPonto_referencia(jsonObject.getString("ponto_referencia"));
                solicitacao.setInformacao_adicional(jsonObject.getString("informacao_adicional"));
                solicitacao.setLocal_destino(jsonObject.getString("local_destino"));
                solicitacao.setStatus_sol(jsonObject.getString("status_sol"));
                solicitacao.setBairro(jsonObject.getString("bairro"));
                solicitacao.setCidade(jsonObject.getString("cidade"));
                solicitacao.setUrl_photo_solicitante(jsonObject.getString("url_photo_solic"));
                solicitacao.setUrl_photo_mototaxista(jsonObject.getString("url_photo_moto"));
                solicitacao.setMototaxi_nome(jsonObject.getString("nome_mototaxi"));
                solicitacao.setCelular_mototaxista(jsonObject.getString("celular_mototaxi"));
                solicitacao.setCelular_solicitante(jsonObject.getString("celular_solicitante"));
                solicitacao.setPlaca_moto(jsonObject.getString("placa_moto"));
                solicitacoes.add(solicitacao);
            }

            return solicitacoes;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static List<Solicitacao> carregaPainelChamados(String cidade, String bairro, Integer mototaxi_id, String tipo_veiculo) throws JSONException {
        List<Solicitacao> solicitacoes = new ArrayList<>();

        // Abre a conexão com o servidor
        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = newClientOkHttp();

        /*String url = String.format(URL_CARREGA_SOLICITACAOES, usuario_id);*/
        String url = URL_CARREGA_PAINEL_CHAMADOS;

        JSONObject jsonObjt = new JSONObject();

        jsonObjt.put("cidade", cidade);
        jsonObjt.put("bairro", bairro);
        jsonObjt.put("mototaxi_id", mototaxi_id);
        jsonObjt.put("tipo_veiculo", tipo_veiculo);

        RequestBody formBody = new FormBody.Builder()
                .add("solicitacao", String.valueOf(jsonObjt))
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
            //json = jsonArray.toString();

            jsonObject = jsonArray.getJSONObject(0);
            jsonArray = jsonObject.getJSONArray("solicitacao");

            for (int i = 0; i < jsonArray.length(); i++){

                //Ler o objecto JSON
                jsonObject = jsonArray.getJSONObject(i);

                Solicitacao solicitacao = new Solicitacao();
                solicitacao.setUsuario_id(jsonObject.getInt("usuario_id"));
                solicitacao.setSolicitacao_id( jsonObject.getInt("solicitacao_id") );
                solicitacao.setSolicitante( jsonObject.getString("solicitante") );
                solicitacao.setMototaxi_id(jsonObject.getInt("mototaxi_id"));
                solicitacao.setData_solicitacao(jsonObject.getString("data_solicitacao"));
                solicitacao.setLatitude(jsonObject.getDouble("latitude"));
                solicitacao.setLongitude(jsonObject.getDouble("longitude"));
                solicitacao.setLocal_origem(jsonObject.getString("local_origem"));
                solicitacao.setPonto_referencia(jsonObject.getString("ponto_referencia"));
                solicitacao.setInformacao_adicional(jsonObject.getString("informacao_adicional"));
                solicitacao.setLocal_destino(jsonObject.getString("local_destino"));
                solicitacao.setStatus_sol(jsonObject.getString("status_sol"));
                solicitacao.setBairro(jsonObject.getString("bairro"));
                solicitacao.setCidade(jsonObject.getString("cidade"));
                solicitacao.setUrl_photo_solicitante(jsonObject.getString("url_photo_solic"));
                solicitacao.setUrl_photo_mototaxista(jsonObject.getString("url_photo_moto"));
                solicitacao.setMototaxi_nome(jsonObject.getString("nome_mototaxi"));
                solicitacao.setCelular_mototaxista(jsonObject.getString("celular_mototaxi"));
                solicitacao.setCelular_solicitante(jsonObject.getString("celular_solicitante"));
                solicitacao.setPlaca_moto(jsonObject.getString("placa_moto"));

                solicitacao.setLatitude_destino(jsonObject.getDouble("latitude_destino"));
                solicitacao.setLongitude_destino(jsonObject.getDouble("longitude_destino"));
                solicitacao.setDistancia_presumida(jsonObject.getDouble("distancia_presumida"));
                solicitacao.setFaixa_preco_presumido(jsonObject.getString("faixa_preco_presumido"));

                solicitacoes.add(solicitacao);
            }

            return solicitacoes;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static String atenderChamado(Integer solicitacao_id, Integer mototaxi_id, Integer usuario_id, String dataAtendimento) throws JSONException {

        // Abre a conexão com o servidor
        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = newClientOkHttp();

        /*String url = String.format(URL_CARREGA_SOLICITACAOES, usuario_id);*/
        String url = URL_ATENDER_CHAMADO;

        JSONObject jsonObjt = new JSONObject();

        jsonObjt.put("solicitacao_id", solicitacao_id);
        jsonObjt.put("mototaxi_id", mototaxi_id);
        jsonObjt.put("usuario_id", usuario_id);
        jsonObjt.put("data_atendimento", dataAtendimento);

        RequestBody formBody = new FormBody.Builder()
                .add("atenderChamado", String.valueOf(jsonObjt))
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
            String resposta = jsonArray.getString(0);

            return resposta;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static String iniciarCorridaChamado(Integer solicitacao_id, Integer mototaxi_id, Integer usuario_id, String dataIniCorrida) throws JSONException {

        // Abre a conexão com o servidor
        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = newClientOkHttp();

        /*String url = String.format(URL_CARREGA_SOLICITACAOES, usuario_id);*/
        String url = URL_INICIAR_CORRIDA_CHAMADO;

        JSONObject jsonObjt = new JSONObject();

        jsonObjt.put("solicitacao_id", solicitacao_id);
        jsonObjt.put("mototaxi_id", mototaxi_id);
        jsonObjt.put("usuario_id", usuario_id);
        jsonObjt.put("data_ini_corrida", dataIniCorrida);

        RequestBody formBody = new FormBody.Builder()
                .add("iniciarChamado", String.valueOf(jsonObjt))
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
            //json = jsonArray.toString();

            //jsonObject = jsonArray.getJSONObject(0);
            String resposta = jsonArray.getString(0);
            //String resposta = jsonObject.getString("");

            return resposta;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }



    public static String finalizarChamado(Integer solicitacao_id, Integer mototaxi_id, Integer usuario_id, String dataFimCorrida) throws JSONException {

        // Abre a conexão com o servidor
        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = newClientOkHttp();

        /*String url = String.format(URL_CARREGA_SOLICITACAOES, usuario_id);*/
        String url = URL_FINALIZAR_CHAMADO;

        JSONObject jsonObjt = new JSONObject();

        jsonObjt.put("solicitacao_id", solicitacao_id);
        jsonObjt.put("mototaxi_id", mototaxi_id);
        jsonObjt.put("usuario_id", usuario_id);
        jsonObjt.put("data_fim_corrida", dataFimCorrida);

        RequestBody formBody = new FormBody.Builder()
                .add("finalizarChamado", String.valueOf(jsonObjt))
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
            //json = jsonArray.toString();

            //jsonObject = jsonArray.getJSONObject(0);
            String resposta = jsonArray.getString(0);
            //String resposta = jsonObject.getString("");

            return resposta;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String DesistirChamado(Integer solicitacao_id, Integer mototaxi_id, Integer usuario_id) throws JSONException {

        //Para o mototaxista pois o mesmo nao cancela, desiste do chamado. Quem cancelará será o usuário

        // Abre a conexão com o servidor
        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = newClientOkHttp();

        /*String url = String.format(URL_CARREGA_SOLICITACAOES, usuario_id);*/
        String url = URL_DESISTIR_CHAMADO;

        JSONObject jsonObjt = new JSONObject();

        jsonObjt.put("solicitacao_id", solicitacao_id);
        jsonObjt.put("mototaxi_id", mototaxi_id);
        jsonObjt.put("usuario_id", usuario_id);

        RequestBody formBody = new FormBody.Builder()
                .add("finalizarChamado", String.valueOf(jsonObjt))
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
            //json = jsonArray.toString();

            //jsonObject = jsonArray.getJSONObject(0);
            String resposta = jsonArray.getString(0);
            //String resposta = jsonObject.getString("");

            return resposta;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String CancelarChamado(Integer solicitacao_id, Integer usuario_id, String dataCancelamento) throws JSONException {

        // Abre a conexão com o servidor
        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = newClientOkHttp();

        /*String url = String.format(URL_CARREGA_SOLICITACAOES, usuario_id);*/
        String url = URL_CANCELAR_CHAMADO;

        JSONObject jsonObjt = new JSONObject();

        jsonObjt.put("solicitacao_id", solicitacao_id);
        jsonObjt.put("usuario_id", usuario_id);
        jsonObjt.put("data_cancelamento", dataCancelamento);

        RequestBody formBody = new FormBody.Builder()
                .add("cancelarChamado", String.valueOf(jsonObjt))
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
            //json = jsonArray.toString();

            //jsonObject = jsonArray.getJSONObject(0);
            String resposta = jsonArray.getString(0);
            //String resposta = jsonObject.getString("");

            return resposta;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static List<Solicitacao> carregaSolicitacaoCorrenteUsuario(Integer usuario_id){
        List<Solicitacao> solicitacoes = new ArrayList<>();

        // Abre a conexão com o servidor
        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = newClientOkHttp();

        String url = String.format(URL_CARREGA_SOLICITACAO_CORRENTE, usuario_id);

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
            jsonArray = jsonObject.getJSONArray("solicitacao");

            for (int i = 0; i < jsonArray.length(); i++){

                //Ler o objecto JSON
                jsonObject = jsonArray.getJSONObject(i);

                Solicitacao solicitacao = new Solicitacao();
                solicitacao.setUsuario_id(usuario_id);
                solicitacao.setSolicitacao_id( jsonObject.getInt("solicitacao_id") );
                solicitacao.setSolicitante( jsonObject.getString("solicitante") );
                solicitacao.setMototaxi_id(jsonObject.getInt("mototaxi_id"));
                solicitacao.setData_solicitacao(jsonObject.getString("data_solicitacao"));
                solicitacao.setLatitude(jsonObject.getDouble("latitude"));
                solicitacao.setLongitude(jsonObject.getDouble("longitude"));
                solicitacao.setLocal_origem(jsonObject.getString("local_origem"));
                solicitacao.setPonto_referencia(jsonObject.getString("ponto_referencia"));
                solicitacao.setInformacao_adicional(jsonObject.getString("informacao_adicional"));
                solicitacao.setLocal_destino(jsonObject.getString("local_destino"));
                solicitacao.setStatus_sol(jsonObject.getString("status_sol"));
                solicitacao.setMototaxi_nome(jsonObject.getString("apelido"));
                solicitacoes.add(solicitacao);
            }

            return solicitacoes;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }



    public static Solicitacao carregaSolicitacaoCorrenteMotociclistaAtendimento(Integer mototaxi_id){

        // Abre a conexão com o servidor
        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = newClientOkHttp();

        String url = String.format(URL_CARREGA_SOLICITACAO_CORRENTE_MOTOCICLISTA_ATENDIMENTO, mototaxi_id);

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


            Solicitacao solicitacao = new Solicitacao();
            solicitacao.setSolicitacao_id( jsonObject.getInt("solicitacao_id") );

            if (solicitacao.getSolicitacao_id() == 0){
                //Não existe solicitacao encontrada no servidor
                return solicitacao;
            }

            solicitacao.setUsuario_id(jsonObject.getInt("usuario_id"));
            solicitacao.setSolicitante( jsonObject.getString("solicitante") );
            solicitacao.setMototaxi_id(jsonObject.getInt("mototaxi_id"));
            solicitacao.setData_solicitacao(jsonObject.getString("data_solicitacao"));
            solicitacao.setLatitude(jsonObject.getDouble("latitude"));
            solicitacao.setLongitude(jsonObject.getDouble("longitude"));
            solicitacao.setLocal_origem(jsonObject.getString("local_origem"));
            solicitacao.setPonto_referencia(jsonObject.getString("ponto_referencia"));
            solicitacao.setInformacao_adicional(jsonObject.getString("informacao_adicional"));
            solicitacao.setLocal_destino(jsonObject.getString("local_destino"));
            solicitacao.setStatus_sol(jsonObject.getString("status_sol"));
            solicitacao.setCidade(jsonObject.getString("cidade"));
            solicitacao.setBairro(jsonObject.getString("bairro"));
            solicitacao.setUrl_photo_solicitante(jsonObject.getString("url_photo_solic"));
            solicitacao.setCelular_solicitante(jsonObject.getString("celular_solicitante"));


            return solicitacao;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static List<Solicitacao> carregaAtendimentosMotociclista(Integer mototaxi_id){
        List<Solicitacao> solicitacoes = new ArrayList<>();

        // Abre a conexão com o servidor
        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = newClientOkHttp();

        String url = String.format(URL_CARREGA_ATENDIMENTOS_MOTOCICLISTA, mototaxi_id);

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
            jsonArray = jsonObject.getJSONArray("solicitacao");

            for (int i = 0; i < jsonArray.length(); i++){

                //Ler o objecto JSON
                jsonObject = jsonArray.getJSONObject(i);

                Solicitacao solicitacao = new Solicitacao();
                solicitacao.setUsuario_id(jsonObject.getInt("usuario_id"));
                solicitacao.setSolicitacao_id( jsonObject.getInt("solicitacao_id") );
                solicitacao.setSolicitante( jsonObject.getString("solicitante") );
                solicitacao.setMototaxi_id(mototaxi_id);
                solicitacao.setData_solicitacao(jsonObject.getString("data_solicitacao"));
                solicitacao.setLatitude(jsonObject.getDouble("latitude"));
                solicitacao.setLongitude(jsonObject.getDouble("longitude"));
                solicitacao.setLocal_origem(jsonObject.getString("local_origem"));
                solicitacao.setPonto_referencia(jsonObject.getString("ponto_referencia"));
                solicitacao.setInformacao_adicional(jsonObject.getString("informacao_adicional"));
                solicitacao.setLocal_destino(jsonObject.getString("local_destino"));
                solicitacao.setStatus_sol(jsonObject.getString("status_sol"));
                solicitacao.setBairro(jsonObject.getString("bairro"));
                solicitacao.setCidade(jsonObject.getString("cidade"));
                solicitacao.setUrl_photo_solicitante(jsonObject.getString("url_photo_solic"));
                solicitacao.setUrl_photo_mototaxista(jsonObject.getString("url_photo_moto"));
                solicitacao.setMototaxi_nome(jsonObject.getString("nome_mototaxi"));
                solicitacao.setCelular_mototaxista(jsonObject.getString("celular_mototaxi"));
                solicitacao.setCelular_solicitante(jsonObject.getString("celular_solicitante"));
                solicitacao.setPlaca_moto(jsonObject.getString("placa_moto"));
                solicitacoes.add(solicitacao);
            }

            return solicitacoes;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Chat> carregaChatSolicitacao(Integer solicitacao_usuario_id, Integer solicitacao_id){
        List<Chat> chatList = new ArrayList<>();

        // Abre a conexão com o servidor
        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = newClientOkHttp();

        String url = String.format(URL_CARREGA_CHAT, solicitacao_usuario_id, solicitacao_id);

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
            jsonArray = jsonObject.getJSONArray("chat");

            for (int i = 0; i < jsonArray.length(); i++){

                //Ler o objecto JSON
                jsonObject = jsonArray.getJSONObject(i);

                Chat chat = new Chat();
                chat.setSolicitacao_usuario_id( jsonObject.getInt("solicitacao_usuario_id") );
                chat.setSolicitacao_id( jsonObject.getInt("solicitacao_id") );
                chat.setUsuario_id(jsonObject.getInt("usuario_id"));
                chat.setData_hora(jsonObject.getString("data_hora"));
                chat.setNome( jsonObject.getString("nome") );
                chat.setTexto(jsonObject.getString("texto"));
                chatList.add(chat);
            }

            return chatList;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Chat> insertChatSolicitacao(Integer solicitacao_usuario_id, Integer solicitacao_id, Integer usuario_id, String data_hora, String nome, String texto) throws JSONException {
        List<Chat> chatList = new ArrayList<>();

        // Abre a conexão com o servidor
        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = newClientOkHttp();

        //String url = String.format(URL_INSERT_CHAT_SOLICITACAO, solicitacao_usuario_id, solicitacao_id, usuario_id, data_hora, nome, texto);
        String url = URL_INSERT_CHAT_SOLICITACAO;

        JSONObject jsonObjt = new JSONObject();

        jsonObjt.put("solicitacao_usuario_id", solicitacao_usuario_id);
        jsonObjt.put("solicitacao_id", solicitacao_id);
        jsonObjt.put("usuario_id", usuario_id);
        jsonObjt.put("data_hora", data_hora);
        jsonObjt.put("nome", nome);
        jsonObjt.put("texto", texto);

        RequestBody formBody = new FormBody.Builder()
                .add("finalizarChamado", String.valueOf(jsonObjt))
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
            //json = jsonArray.toString();

            jsonObject = jsonArray.getJSONObject(0);
            jsonArray = jsonObject.getJSONArray("chat");

            for (int i = 0; i < jsonArray.length(); i++){

                //Ler o objecto JSON
                jsonObject = jsonArray.getJSONObject(i);

                Chat chat = new Chat();
                chat.setSolicitacao_usuario_id( jsonObject.getInt("solicitacao_usuario_id") );
                chat.setSolicitacao_id( jsonObject.getInt("solicitacao_id") );
                chat.setUsuario_id(jsonObject.getInt("usuario_id"));
                chat.setData_hora(jsonObject.getString("data_hora"));
                chat.setNome( jsonObject.getString("nome") );
                chat.setTexto(jsonObject.getString("texto"));
                chatList.add(chat);
            }

            return chatList;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    private static OkHttpClient newClientOkHttp() {
        try {
            Builder b = new Builder();
            b.readTimeout(15000, TimeUnit.MILLISECONDS);
            b.writeTimeout(15000, TimeUnit.MILLISECONDS);
            return b.build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
