package com.franklincbc.motoon.Utils;

import static com.franklincbc.motoon.Utils.Util.HoraNow;
import static com.franklincbc.motoon.Utils.Util.truncateDecimal;

/**
 * Created by Priscila on 08/01/2017.
 */

public class CalcularPrecoPresumido {

    public static String PrecoPresumidoMoto(Double distanciaRota){
        Double preco = 0.00;
        String FaixaPreco = "";

        String hora = HoraNow();
        Integer iHora = Integer.parseInt(hora);
        Double fator = 1.0; //De Acordo com o horário

        if(iHora >= 0 && iHora < 6)
            { fator = 2.0; }
        else if(iHora >= 6 && iHora <= 23)
            { fator = 1.0; }

        if(distanciaRota >= 0.000 && distanciaRota < 1.500){
            preco = 2.50 * fator;
            preco = truncateDecimal(preco, 3).doubleValue();
            FaixaPreco = "R$ " + preco + "  à  " + (preco + 1.00);
        }

        else if (distanciaRota >= 1.500 && distanciaRota < 3.500){
            preco = 3.00 * fator;
            preco = truncateDecimal(preco, 3).doubleValue();
            FaixaPreco = "R$ " + preco + "  à  " + (preco + 1.00);
        }

        else if (distanciaRota >= 3.500 && distanciaRota < 6.000){
            preco = 4.00 * fator;
            preco = truncateDecimal(preco, 3).doubleValue();
            FaixaPreco = "R$ " + preco + "  à  " + (preco + 1.00);
        }

        else if (distanciaRota >= 6.000 && distanciaRota < 8.000){
            preco = 6.00 * fator;
            preco = truncateDecimal(preco, 3).doubleValue();
            FaixaPreco = "R$ " + (preco - 1.00) + "  à  " + (preco + 1.00);
        }

        else if (distanciaRota >= 8.000 && distanciaRota < 11.000){
            preco = 8.00 * fator;
            preco = truncateDecimal(preco, 3).doubleValue();
            FaixaPreco = "R$ " + (preco - 1.00) + "  à  " + (preco + 1.00);
        }

        else
        {
            preco = distanciaRota * 0.9 * fator;
            preco = truncateDecimal(preco, 3).doubleValue();
            FaixaPreco = "R$ " + (preco - 1.50) + "  à  " + (preco + 2.50);
        }

        return FaixaPreco;
    }


    public static String PrecoPresumidoTukTuk(Double distanciaRota){
        Double preco = 0.00;
        String FaixaPreco = "";

        String hora = HoraNow();
        Integer iHora = Integer.parseInt(hora);
        Double fator = 1.0; //De Acordo com o horário

        if(iHora >= 0 && iHora < 6)
        { fator = 2.0; }
        else if(iHora >= 6 && iHora <= 23)
        { fator = 1.0; }

        if(distanciaRota >= 0.000 && distanciaRota < 1.500){
            preco = 5.50 * fator;
            preco = truncateDecimal(preco, 3).doubleValue();
            FaixaPreco = "R$ " + preco + "  à  " + (preco + 1.50);
        }

        else if (distanciaRota >= 1.500 && distanciaRota < 3.500){
            preco = 6.00 * fator;
            preco = truncateDecimal(preco, 3).doubleValue();
            FaixaPreco = "R$ " + preco + "  à  " + (preco + 2.00);
        }

        else if (distanciaRota >= 3.500 && distanciaRota < 6.000){
            preco = 7.00 * fator;
            preco = truncateDecimal(preco, 3).doubleValue();
            FaixaPreco = "R$ " + preco + "  à  " + (preco + 2.00);
        }

        else if (distanciaRota >= 6.000 && distanciaRota < 8.000){
            preco = 8.00 * fator;
            preco = truncateDecimal(preco, 3).doubleValue();
            FaixaPreco = "R$ " + (preco - 1.00) + "  à  " + (preco + 2.00);
        }

        else if (distanciaRota >= 8.000 && distanciaRota < 11.000){
            preco = 10.00 * fator;
            preco = truncateDecimal(preco, 3).doubleValue();
            FaixaPreco = "R$ " + (preco - 2.00) + "  à  " + (preco + 2.00);
        }

        else
        {
            preco = distanciaRota * 0.9 * fator;
            preco = truncateDecimal(preco, 3).doubleValue();
            FaixaPreco = "R$ " + (preco - 2.00) + "  à  " + (preco + 3.00);
        }

        return FaixaPreco;
    }


}
