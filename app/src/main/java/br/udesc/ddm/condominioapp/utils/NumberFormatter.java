package br.udesc.ddm.condominioapp.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatter {

    private static final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    private static final NumberFormat numberFormatter = NumberFormat.getNumberInstance(new Locale("pt", "BR"));

    /**
     * Formata um valor monetário
     * @param value Valor a ser formatado
     * @return Valor formatado como moeda (R$ 1.234,56)
     */
    public static String formatCurrency(double value) {
        return currencyFormatter.format(value);
    }

    /**
     * Formata um número com separadores de milhar
     * @param value Valor a ser formatado
     * @return Valor formatado com separadores (1.234,56)
     */
    public static String formatNumber(double value) {
        return numberFormatter.format(value);
    }

    /**
     * Formata um número com um número específico de casas decimais
     * @param value Valor a ser formatado
     * @param decimalPlaces Número de casas decimais
     * @return Valor formatado com o número específico de casas decimais
     */
    public static String formatNumberWithDecimals(double value, int decimalPlaces) {
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
        formatter.setMinimumFractionDigits(decimalPlaces);
        formatter.setMaximumFractionDigits(decimalPlaces);
        return formatter.format(value);
    }

    /**
     * Converte uma string em um valor double
     * @param value String a ser convertida
     * @return Valor double ou 0 se a conversão falhar
     */
    public static double parseDouble(String value) {
        try {
            // Remove os símbolos de moeda e troca vírgula por ponto
            String cleanValue = value
                    .replaceAll("[^0-9,]", "")  // Remove tudo que não for número ou vírgula
                    .replace(",", ".");          // Troca vírgula por ponto

            return Double.parseDouble(cleanValue);
        } catch (Exception e) {
            return 0.0;
        }
    }

    /**
     * Converte uma string em um valor inteiro
     * @param value String a ser convertida
     * @return Valor inteiro ou 0 se a conversão falhar
     */
    public static int parseInt(String value) {
        try {
            // Remove caracteres não numéricos
            String cleanValue = value.replaceAll("[^0-9]", "");

            return Integer.parseInt(cleanValue);
        } catch (Exception e) {
            return 0;
        }
    }
}