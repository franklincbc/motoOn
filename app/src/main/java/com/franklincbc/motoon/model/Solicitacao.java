package com.franklincbc.motoon.model;

import java.io.Serializable;

/**
 * Created by franklin.carvalho on 26/10/2016.
 */

public class Solicitacao implements Serializable {

    private Long id;
    private Integer solicitacao_id;
    private Integer usuario_id; //código do Servidor Web
    private Integer mototaxi_id; //código do Servidor web
    private String solicitante;
    private String data_solicitacao;
    private Double latitude;
    private Double longitude;
    private String local_origem;
    private String ponto_referencia;
    private String local_destino;
    private String informacao_adicional;
    private String status_sol;
    private String bairro;
    private String cidade;

    private String tipo_veiculo;

    private String url_photo_solicitante;
    private String url_photo_mototaxista;

    private String celular_solicitante;
    private String celular_mototaxista;
    private String placa_moto;

    private String data_atendimento;
    private String data_cancelamento;
    private String data_ini_corrida;
    private String data_fim_corrida;

    private Double latitude_destino;
    private Double longitude_destino;
    private Double distancia_presumida;
    private String faixa_preco_presumido;
    private Double distancia_calculada;
    private String faixa_preco_calculado;
    private Double valor_corrida;
    private String tipo_desconto_moto;


    //Não será persistido na base local por enquanto
    private String mototaxi_nome;

    public Solicitacao() {
        this.solicitante = "";
        this.local_origem = "";
        this.ponto_referencia = "";
        this.local_destino = "";
        this.informacao_adicional = "";
        this.status_sol = "";
        this.bairro = "";
        this.cidade = "";
        this.url_photo_mototaxista = "";
        this.url_photo_solicitante = "";
        this.tipo_veiculo = "M"; //M moto ou T Tuc tuc
        this.celular_mototaxista = "";
        this.celular_solicitante = "";
        this.placa_moto = "";

        this.data_atendimento = "";
        this.data_cancelamento = "";
        this.data_ini_corrida = "";
        this.data_fim_corrida = "";

        this.faixa_preco_presumido = "";
        this.faixa_preco_calculado = "";
        this.tipo_desconto_moto = "0";
        this.valor_corrida = 0.00;
        this.distancia_presumida = 0.000;
        this.distancia_calculada = 0.000;
        this.valor_corrida = 0.00;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSolicitacao_id() {
        return solicitacao_id;
    }

    public void setSolicitacao_id(Integer solicitacao_id) {
        this.solicitacao_id = solicitacao_id;
    }

    public Integer getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Integer usuario_id) {
        this.usuario_id = usuario_id;
    }

    public Integer getMototaxi_id() {
        return mototaxi_id;
    }

    public void setMototaxi_id(Integer mototaxi_id) {
        this.mototaxi_id = mototaxi_id;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public String getData_solicitacao() {
        return data_solicitacao;
    }

    public void setData_solicitacao(String data_solicitacao) {
        this.data_solicitacao = data_solicitacao;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getLocal_origem() {
        return local_origem;
    }

    public void setLocal_origem(String local_origem) {
        this.local_origem = local_origem;
    }

    public String getPonto_referencia() {
        return ponto_referencia;
    }

    public void setPonto_referencia(String ponto_referencia) {
        this.ponto_referencia = ponto_referencia;
    }

    public String getLocal_destino() {
        return local_destino;
    }

    public void setLocal_destino(String local_destino) {
        this.local_destino = local_destino;
    }

    public String getInformacao_adicional() {
        return informacao_adicional;
    }

    public void setInformacao_adicional(String informacao_adicional) {
        this.informacao_adicional = informacao_adicional;
    }

    public String getStatus_sol() {
        return status_sol;
    }

    public void setStatus_sol(String status_sol) {
        this.status_sol = status_sol;
    }


    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }


    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }





    public String getMototaxi_nome() {
        return mototaxi_nome;
    }

    public void setMototaxi_nome(String mototaxi_nome) {
        this.mototaxi_nome = mototaxi_nome;
    }


    public String getUrl_photo_mototaxista() {
        return url_photo_mototaxista;
    }

    public void setUrl_photo_mototaxista(String url_photo_mototaxista) {
        this.url_photo_mototaxista = url_photo_mototaxista;
    }

    public String getUrl_photo_solicitante() {
        return url_photo_solicitante;
    }

    public void setUrl_photo_solicitante(String url_photo_solicitante) {
        this.url_photo_solicitante = url_photo_solicitante;
    }



    public String getTipo_veiculo() {
        return tipo_veiculo;
    }

    public void setTipo_veiculo(String tipo_veiculo) {
        this.tipo_veiculo = tipo_veiculo;
    }


    public String getCelular_solicitante() {
        return celular_solicitante;
    }

    public void setCelular_solicitante(String celular_solicitante) {
        this.celular_solicitante = celular_solicitante;
    }

    public String getCelular_mototaxista() {
        return celular_mototaxista;
    }

    public void setCelular_mototaxista(String celular_mototaxista) {
        this.celular_mototaxista = celular_mototaxista;
    }

    public String getPlaca_moto() {
        return placa_moto;
    }

    public void setPlaca_moto(String placa_moto) {
        this.placa_moto = placa_moto;
    }

    public String getData_atendimento() {
        return data_atendimento;
    }

    public void setData_atendimento(String data_atendimento) {
        this.data_atendimento = data_atendimento;
    }

    public String getData_cancelamento() {
        return data_cancelamento;
    }

    public void setData_cancelamento(String data_cancelamento) {
        this.data_cancelamento = data_cancelamento;
    }

    public String getData_ini_corrida() {
        return data_ini_corrida;
    }

    public void setData_ini_corrida(String data_ini_corrida) {
        this.data_ini_corrida = data_ini_corrida;
    }

    public String getData_fim_corrida() {
        return data_fim_corrida;
    }

    public void setData_fim_corrida(String data_fim_corrida) {
        this.data_fim_corrida = data_fim_corrida;
    }


    public Double getLatitude_destino() {
        return latitude_destino;
    }

    public void setLatitude_destino(Double latitude_destino) {
        this.latitude_destino = latitude_destino;
    }

    public Double getLongitude_destino() {
        return longitude_destino;
    }

    public void setLongitude_destino(Double longitude_destino) {
        this.longitude_destino = longitude_destino;
    }

    public Double getDistancia_presumida() {
        return distancia_presumida;
    }

    public void setDistancia_presumida(Double distancia_presumida) {
        this.distancia_presumida = distancia_presumida;
    }

    public String getFaixa_preco_presumido() {
        return faixa_preco_presumido;
    }

    public void setFaixa_preco_presumido(String faixa_preco_presumido) {
        this.faixa_preco_presumido = faixa_preco_presumido;
    }

    public Double getDistancia_calculada() {
        return distancia_calculada;
    }

    public void setDistancia_calculada(Double distancia_calculada) {
        this.distancia_calculada = distancia_calculada;
    }

    public String getFaixa_preco_calculado() {
        return faixa_preco_calculado;
    }

    public void setFaixa_preco_calculado(String faixa_preco_calculado) {
        this.faixa_preco_calculado = faixa_preco_calculado;
    }

    public Double getValor_corrida() {
        return valor_corrida;
    }

    public void setValor_corrida(Double valor_corrida) {
        this.valor_corrida = valor_corrida;
    }

    public String getTipo_desconto_moto() {
        return tipo_desconto_moto;
    }

    public void setTipo_desconto_moto(String tipo_desconto_moto) {
        this.tipo_desconto_moto = tipo_desconto_moto;
    }

}
