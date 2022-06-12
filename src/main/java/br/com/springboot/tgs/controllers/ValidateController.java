package br.com.springboot.tgs.controllers;

public class ValidateController {

    /**
     * Valida o cpf [formato: 99999999999]
     * 
     * @param cpf - CPF a ser validado
     * @return true|false
     */
    public static boolean validateCPF(String cpf) {
        return ((cpf.length() == 11) && (cpf.matches("[0-9]{11}")));
    }

    /**
     * Valida o cep [formato: 99999999]
     * 
     * @param cep - CEP a ser validado.
     * @return true|false
     */
    public static boolean validateCEP(String cep) {
        return ((cep.length() == 8) && (cep.matches("[0-9]{8}")));
    }

    /**
     * Valida texto
     * 
     * @param text - Texto a ser validado.
     * @return true|false.
     */
    public static boolean validateText(String text) {
        return text.matches("[a-zA-ZáàâãéèêíóôõúçñÁÀÂÃÉÈÍÓÔÕÚÇ ]*");
    }

    /**
     * Valida texto e/ou numero
     * 
     * @param text Texto a ser validado.
     * @return true|false.
     */
    public static boolean validateTextAndNumber(String text) {
        return text.matches("[1-9a-zA-ZáàâãéèêíóôõúçñÁÀÂÃÉÈÍÓÔÕÚÇ ]*");
    }

    /**
     * Valida rg [formato: 999999999]
     * 
     * @param rg - RG a ser validado.
     * @return true|false
     */
    public static boolean validateRG(String rg) {
        return ((rg.length() == 9) && (rg.matches("[0-9]{9}")));
    }

    /**
     * Valida telefone [formato: 9999999999]
     * 
     * @param telephone - Telefone a ser validado.
     * @return true|false
     */
    public static boolean validateTelephone(String telephone) {
        return ((telephone.length() == 10) && (telephone.matches("[0-9]{10}")));
    }

    /**
     * Valida celular [formato: 99999999999]
     * 
     * @param cellphone - Celular a ser validado.
     * @return true|false
     */
    public static boolean validateCellphone(String cellphone) {
        return ((cellphone.length() == 11) && (cellphone.matches("[0-9]{11}")));
    }

    /**
     * Valida e-mail
     * 
     * @param email E-mail a ser validado.
     * @return true|false.
     */
    public static boolean validateEmail(String email) {
        return email.matches(
                "(((\"\")(\"\".+?\"\"@)|(([0-9a-zA-Z]((\\.(?!\\.))|[-!#%&'*+/=?^`{}|~\\w])*)([0-9a-zA-Z])@))((\\[)(\\[(\\d{1,3}\\.){3}\\d{1,3}])|(([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,6})))");
    }

    /**
     * Valida cro [formato: 999999]
     * 
     * @param cro - CRO a ser validado.
     * @return true|false.
     */
    public static boolean validateCRO(String cro) {
        return ((cro.length() == 6) && (cro.matches("[0-9]{6}")));
    }
}
