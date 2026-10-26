package net.eventos_facu.eventos.utils;

import java.util.regex.Pattern;

public final class CpfUtils {

    // Aceita somente dígitos (12345678909) ou formatado (123.456.789-09)
    private static final Pattern CPF_PATTERN = Pattern.compile("\\d{11}|\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");

    private CpfUtils() {
    }

    public static boolean isValid(String cpf) {
        if (cpf == null || !CPF_PATTERN.matcher(cpf).matches()) {
            return false;
        }

        String digits = sanitize(cpf);

        // Sequências repetidas (111.111.111-11) passam no cálculo, mas não são CPFs válidos
        if (digits.chars().distinct().count() == 1) {
            return false;
        }

        return checkDigit(digits, 9) == Character.getNumericValue(digits.charAt(9))
                && checkDigit(digits, 10) == Character.getNumericValue(digits.charAt(10));
    }

    // Remove pontos e traço: 529.982.247-25 -> 52998224725
    public static String sanitize(String cpf) {
        if (cpf == null) {
            return null;
        }
        return cpf.replace(".", "").replace("-", "");
    }

    private static int checkDigit(String digits, int length) {
        int sum = 0;
        for (int i = 0; i < length; i++) {
            sum += Character.getNumericValue(digits.charAt(i)) * (length + 1 - i);
        }
        int remainder = (sum * 10) % 11;
        return remainder == 10 ? 0 : remainder;
    }
}
