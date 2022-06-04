package br.com.springboot.tgs.controllers;

public class ValidateController {

    // CPF
    /**
     * Validador de Cpf, se estiver correto, devolve "true".
     * Se estiver falso, retorna "false".
     * 
     * @param cpf Cpf a ser validado
     * @return true or false
     */
    public static boolean validateCPF(String cpf) {
        if (cpf.length() == 11) {
            return cpf.matches("[0-9]{3}[0-9]{3}[0-9]{3}[0-9]{2}");
        }
        return false;
    }

    // CEP
    /**
     * Validador de Cep, se estiver correto. Devolve "true",
     * e se estiver falso, retorna "false".
     * 
     * @param cep Cep a ser validado.
     * @return true or false
     */
    public static boolean validateCEP(String cep) {

        if (cep.length() == 8) {
            return cep.matches("\\d{5}\\d{3}");
        }
        return false;
    }

    // TEXTO

    /**
     * Validador de Texto, se estiver correto. Devolve "true",
     * e se estiver falso, retorna "false".
     * 
     * @param text Texto a ser validado.
     * @return true ou false.
     */
    public static boolean validateText(String text) {
        // return text.matches("([A-Z][a-z]* [A-Z][a-z]*)|([A-Z][a-z]*)");
        // return text.matches("[a-zA-Z]*");

        return text.matches("[a-zA-ZáàâãéèêíóôõúçñÁÀÂÃÉÈÍÓÔÕÚÇ]*");
    }

    // RG
    /**
     * Validador de Rg, se estiver correto. Devolve "true",
     * e se estiver falso, retorna "false".
     * 
     * @param rg Rg a ser validado.
     * @return true or false
     */
    public static boolean validateRG(String rg) {

        if (rg.length() == 9) {
            return rg.matches("[0-9]{2}[0-9]{3}[0-9]{3}[0-9]{1}");
        }
        return false;

    }

    // TELEFONE
    /**
     * Validador de Telefone,se estiver correto. Devolve "true",
     * e se estiver falso, retorna "false".
     * 
     * @param telephone Telefone a ser validado.
     * @return true or false
     */
    public static boolean validateTelephone(String telephone) {

        if (telephone.length() == 10) {
            return telephone.matches("[0-9]{2}[0-9]{4}[0-9]{4}");
        }
        return false;
    }

    // CELULAR
    /**
     * Validar número de celular, se estiver correto, devolve "true".
     * e estiver errado, retorna "false".
     * 
     * @param cellphone Número de celular, a ser validado.
     * @return true or false
     */
    public static boolean validateCelular(String cellphone) {

        if (cellphone.length() == 11) {
            return cellphone.matches("\\d{2}\\d{5}\\d{4}");
        }
        return false;
    }

    // E-MAIL
    /**
     * Validador de e-mail, se estiver correto,devolve "true".
     * Se estiver errado, retorna "false".
     * 
     * @param email E-mail a ser validado.
     * @return true or false.
     */
    public static boolean validateEmail(String email) {
        return email.matches(
                "(((\"\")(\"\".+?\"\"@)|(([0-9a-zA-Z]((\\.(?!\\.))|[-!#%&'*+/=?^`{}|~\\w])*)([0-9a-zA-Z])@))((\\[)(\\[(\\d{1,3}\\.){3}\\d{1,3}])|(([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,6})))");
        // return email.matches("[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}");

    }

    // CRO
    /**
     * Validador de Cro, se estiver correto, devolve "true".
     * Se estiver errado, retorna "false".
     * 
     * @param cro Cro a ser validado.
     * @return true or false.
     */
    public static boolean validateCRO(String cro) {

        if (cro.length() == 5) {
            return cro.matches("\\d{2}\\d{3}");
        }
        return false;
    }

}
