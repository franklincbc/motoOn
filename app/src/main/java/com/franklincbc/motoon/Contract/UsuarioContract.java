package com.franklincbc.motoon.Contract;

import android.provider.BaseColumns;

/**
 * Created by Priscila on 17/10/2016.
 */

public interface UsuarioContract extends BaseColumns {

    //Nome da tabela no banco de dados
    String TABLE_NAME = "Usuario";

    //Colunas do banco de dados
    String COL_ID_SERVIDOR = "id_Servidor";
    String COL_MOTOTAXI_ID = "mototaxi_id";
    String COL_NOME = "nome";
    String COL_EMAIL = "email";
    String COL_DATA_CADASTRO = "data_cadastro";
    String COL_URL_PHOTO = "url_photo";
    String COL_LATITUDE_ATUAL = "latitude_atual";
    String COL_LONGITUDE_ATUAL = "longitude_atual";
    String COL_ESTADO = "estado";
    String COL_CIDADE = "cidade";
    String COL_BAIRRO = "bairro";
    String COL_CELULAR = "celular";
    String COL_SEXO = "sexo";

    String COL_CPF = "cpf";
    String COL_DATA_NASC = "data_nasc";
    String COL_CNH = "cnh";
    String COL_CNH_VALIDADE = "cnh_validade";
    String COL_CRLV = "crlv";
    String COL_PLACA_MOTO = "placa_moto";
    String COL_TIPO_VEICULO = "tipo_veiculo";

    String COL_SN_ACEITOU_TERMO = "sn_aceitou_termo";
    String COL_SN_MOTOTAXI = "sn_mototaxi";
    String COL_SN_DISPONIVEL = "sn_disponivel";
    String COL_DATA_ULT_ATUALIZACAO = "data_ult_atualizacao";

    String COL_IS_ATENDIMENTO = "is_atendimento";
    String COL_ATEND_SOLICITACAO_ID = "atend_solicitacao_id";
    String COL_ATEND_SOLICITACAO_USUARIO_ID = "atend_solicitacao_usuario_id";
    String COL_ATEND_SOLICITACAO_sTATUS_SOL = "atend_solicitacao_status_sol";


}
