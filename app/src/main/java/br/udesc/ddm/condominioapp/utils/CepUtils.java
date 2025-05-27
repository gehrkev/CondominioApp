package br.udesc.ddm.condominioapp.utils;

import java.util.regex.Pattern;

public class CepUtils {

    private static final Pattern CEP_PATTERN = Pattern.compile("^\\d{8}$");
    private static final Pattern CEP_FORMATTED_PATTERN = Pattern.compile("^\\d{5}-\\d{3}$");

    public static String limparCep(String cep) {
        if (cep == null) return "";
        return cep.replaceAll("[^0-9]", "");
    }

    public static String formatarCep(String cep) {
        String cepLimpo = limparCep(cep);
        if (isValidCep(cepLimpo)) {
            return cepLimpo.substring(0, 5) + "-" + cepLimpo.substring(5);
        }
        return "";
    }

    public static boolean isValidCep(String cep) {
        if (cep == null) return false;
        String cepLimpo = limparCep(cep);
        return CEP_PATTERN.matcher(cepLimpo).matches();
    }

    public static boolean isFormatted(String cep) {
        if (cep == null) return false;
        return CEP_FORMATTED_PATTERN.matcher(cep).matches();
    }

    public static String prepararParaApi(String cep) {
        String cepLimpo = limparCep(cep);
        return isValidCep(cepLimpo) ? cepLimpo : null;
    }
}