package com.franklincbc.motoon.model;

import java.io.Serializable;

/**
 * Created by Priscila on 17/10/2016.
 */

public class Usuario implements Serializable {

    private long id;
    private Integer id_servidor;

    private Integer mototaxi_id;
    private String Nome;
    private String email;
    private String celular;
    private String sexo;
    private String url_photo;
    private Double latitude_atual;
    private Double longitude_atual;
    private String estado;
    private String cidade;
    private String bairro;

    private String cpf;
    private String data_nasc;
    private String cnh;
    private String cnh_validade;
    private String crlv;
    private String placa_moto;

    private String sn_aceitou_termo;
    private String sn_mototaxi;
    private String sn_disponivel;
    private String tipo_veiculo;

    private String isAtendimento;  //Usar quando estiver em atendimento   S - Sim   N- Não
    private Integer atendSolicitacao_Id; //Usar quando estiver em atendimento
    private Integer atendSolicitacao_UsuarioId; //Usar quando estiver em atendimento
    private String atendSolicitacao_StatusSol; //Usar quando estiver em atendimento

    private String data_cadastro;
    private String data_ult_atualizacao;

    public Usuario() {
        //Inicializando umas variáveis - Teste
        this.celular = "";
        this.sexo = "";
        this.estado = "";
        this.cidade = "";
        this.bairro = "";
        this.data_nasc = "";
        this.cpf = "";
        this.cnh = "";
        this.cnh_validade = "";
        this.crlv = "";
        this.placa_moto = "";
        this.data_cadastro = "";
        this.data_ult_atualizacao = "";
        this.url_photo = "";
        this.tipo_veiculo = "M"; //M moto | T Tuc tuc | C Carro
        this.sn_disponivel = "S";

        this.isAtendimento = "N";
        this.atendSolicitacao_Id = 0;
        this.atendSolicitacao_UsuarioId = 0;
        this.atendSolicitacao_StatusSol = "";

    }


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public Integer getId_servidor() {
        return id_servidor;
    }
    public void setId_servidor(Integer id_servidor) {
        this.id_servidor = id_servidor;
    }

    public String getNome() {
        return Nome;
    }
    public void setNome(String nome) {
        Nome = nome;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }
    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getSexo() {
        return sexo;
    }
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getUrl_photo() {
        return url_photo;
    }
    public void setUrl_photo(String url_photo) {
        this.url_photo = url_photo;
    }


    public Double getLatitude_atual() {
        return latitude_atual;
    }
    public void setLatitude_atual(Double latitude_atual) {
        this.latitude_atual = latitude_atual;
    }

    public Double getLongitude_atual() {
        return longitude_atual;
    }
    public void setLongitude_atual(Double longitude_atual) { this.longitude_atual = longitude_atual;  }

    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getSn_aceitou_termo() {
        return sn_aceitou_termo;
    }
    public void setSn_aceitou_termo(String sn_aceitou_termo) { this.sn_aceitou_termo = sn_aceitou_termo; }

    public String getSn_mototaxi() {
        return sn_mototaxi;
    }
    public void setSn_mototaxi(String sn_mototaxi) {
        this.sn_mototaxi = sn_mototaxi;
    }


    public String getBairro() {
        return bairro;
    }
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public Integer getMototaxi_id() {
        return mototaxi_id;
    }
    public void setMototaxi_id(Integer mototaxi_id) {
        this.mototaxi_id = mototaxi_id;
    }

    public String getData_cadastro() { return data_cadastro; }
    public void setData_cadastro(String data_cadastro) { this.data_cadastro = data_cadastro; }

    public String getData_ult_atualizacao() { return data_ult_atualizacao; }
    public void setData_ult_atualizacao(String data_ult_atualizacao) { this.data_ult_atualizacao = data_ult_atualizacao; }


    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getData_nasc() {
        return data_nasc;
    }

    public void setData_nasc(String data_nasc) {
        this.data_nasc = data_nasc;
    }

    public String getCnh() {
        return cnh;
    }

    public void setCnh(String cnh) {
        this.cnh = cnh;
    }

    public String getCnh_validade() {
        return cnh_validade;
    }

    public void setCnh_validade(String cnh_validade) {
        this.cnh_validade = cnh_validade;
    }

    public String getCrlv() {
        return crlv;
    }

    public void setCrlv(String crlv) {
        this.crlv = crlv;
    }

    public String getPlaca_moto() {
        return placa_moto;
    }

    public void setPlaca_moto(String placa_moto) {
        this.placa_moto = placa_moto;
    }

    public String getTipo_veiculo() {
        return tipo_veiculo;
    }

    public void setTipo_veiculo(String tipo_veiculo) {
        this.tipo_veiculo = tipo_veiculo;
    }

    public String getSn_disponivel() { return sn_disponivel; }

    public void setSn_disponivel(String sn_disponivel) { this.sn_disponivel = sn_disponivel; }

    public String getIsAtendimento() {
        return isAtendimento;
    }

    public void setIsAtendimento(String isAtendimento) {
        this.isAtendimento = isAtendimento;
    }

    public Integer getAtendSolicitacao_Id() {
        return atendSolicitacao_Id;
    }

    public void setAtendSolicitacao_Id(Integer atendSolicitacao_Id) {
        this.atendSolicitacao_Id = atendSolicitacao_Id;
    }

    public Integer getAtendSolicitacao_UsuarioId() {
        return atendSolicitacao_UsuarioId;
    }

    public void setAtendSolicitacao_UsuarioId(Integer atendSolicitacao_UsuarioId) {
        this.atendSolicitacao_UsuarioId = atendSolicitacao_UsuarioId;
    }

    public String getAtendSolicitacao_StatusSol() {
        return atendSolicitacao_StatusSol;
    }

    public void setAtendSolicitacao_StatusSol(String atendSolicitacao_StatusSol) {
        this.atendSolicitacao_StatusSol = atendSolicitacao_StatusSol;
    }



}
