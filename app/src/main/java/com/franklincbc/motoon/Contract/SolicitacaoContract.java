package com.franklincbc.motoon.Contract;

import android.provider.BaseColumns;

/**
 * Created by Priscila on 26/10/2016.
 */

public interface SolicitacaoContract extends BaseColumns {
    //Nome da tabela no banco de dados
    String TABLE_NAME = "Solicitacao";

    //Colunas do banco de dados
    String COL_SOLICITACAO_ID       = "solicitacao_id";
    String COL_USUARIO_ID           = "usuario_id";
    String COL_MOTOTAXI_ID          = "mototaxi_id";
    String COL_SOLICITANTE          = "solicitante";
    String COL_DATA_SOLICITACAO     = "data_solicitacao";
    String COL_LATITUDE             = "latitude";
    String COL_LONGITUDE            = "longitude";
    String COL_LOCAL_ORIGEM         = "local_origem";
    String COL_PONTO_REFERENCIA     = "ponto_referencia";
    String COL_LOCAL_DESTINO        = "local_destino";
    String COL_INFORMACAO_ADICIONAL = "informacao_adicional";
    String COL_STATUS_SOL           = "status_sol";   //A - Aguardanto atendimento  E - Em atendimento   - F- Finalizado  - C- Cancelado
    String COL_CIDADE               = "cidade";
    String COL_BAIRRO               = "bairro";
    String COL_TIPO_VEICULO         = "tipo_veiculo";

    String COL_DATA_ATENDIMENTO     = "data_atendimento";
    String COL_DATA_CANCELAMENTO    = "data_cancelamento";
    String COL_DATA_INI_CORRIDA     = "data_ini_corrida";
    String COL_DATA_FIM_CORRIDA     = "data_fim_corrida";

    String COL_LATITUDE_DESTINO     = "latitude_destino";
    String COL_LONGITUDE_DESTINO    = "longitude_destino";
    String COL_DISTANCIA_PRESUMIDA  = "distancia_presumida";
    String COL_FAIXA_PRECO_PRESUMIDO= "faixa_preco_presumido";
    String COL_DISTANCIA_CALCULADA  = "distancia_calculada";
    String COL_FAIXA_PRECO_CALCULADO= "faixa_preco_calculado";
    String COL_VALOR_CORRIDA        = "valor_corrida";
    String COL_TIPO_DESCONTO_MOTO   = "tipo_desconto_moto"; //0 - Sem desconto  |  1 - 50% desconto  | 2 - 100% desconto


}
