package br.udesc.ddm.condominioapp.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatter {

    private static final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    private static final NumberFormat numberFormatter = NumberFormat.getNumberInstance(new Locale("pt", "BR"));

    public static String formatCurrency(double value) {
        return currencyFormatter.format(value);
    }

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