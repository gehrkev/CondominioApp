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
            // Remove tudo que não for número, vírgula ou ponto
            String cleanValue = value.replaceAll("[^0-9.,]", "");

            // Troca a vírgula por ponto para o decimal
            cleanValue = cleanValue.replace(",", ".");

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