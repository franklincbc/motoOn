package com.franklincbc.motoon.model;

import java.io.Serializable;

/**
 * Created by Priscila on 19/01/2017.
 */

public class Chat implements Serializable {

    private Integer solicitacao_usuario_id;
    private Integer solicitacao_id;

    private Integer usuario_id;
    private String data_hora;
    private String nome;
    private String texto;

    public Integer getSolicitacao_usuario_id() {
        return solicitacao_usuario_id;
    }

    public void setSolicitacao_usuario_id(Integer solicitacao_usuario_id) {
        this.solicitacao_usuario_id = solicitacao_usuario_id;
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

    public String getData_hora() {
        return data_hora;
    }

    public void setData_hora(String data_hora) {
        this.data_hora = data_hora;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

}
