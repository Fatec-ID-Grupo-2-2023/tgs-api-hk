package br.com.springboot.tgs.controllers;

public class ValidateController {

    /**
     * Validador de Cpf, se estiver correto. Devolve "true",
     * e se estiver falso, retorna "false".
     * 
     * @param cpf o Cpf.
     * 
     * @return cpf
     */
    public static boolean validateCPF(String cpf) {
        if (cpf.length() == 14) {
            return cpf.matches("[0-9]{3}.[0-9]{3}.[0-9]{3}.[0-9]{2}");
        }

        return false;
    }

    /**
     * Validador de nome,se estiver correto. Devolve "true",
     * e se estiver falso, retorna "false".
     * 
     * @param
     *
     * @return nome
     */
    public static boolean validateName(String name) {
        // verificar nome
        if (name.length() == 50) {
            return name.matches("[A-Za-z]");
        }
        return false;
    }

    public static boolean validateCep(String cep) {

        if (cep.length() == 8) {
            return cep.matches("\\d{5}-\\d{3}");
        }
        return false;
    }

    public static boolean validateRg(String rg) {

        if (rg.length() == 8) {
            return rg.matches("\\d{5}-\\d{3}");
        }
        return false;

    }

    public static boolean validateTelephone(String telephone) {

        if (telephone.length() == 8) {
            return telephone.matches("\\((d{2}))\\d{4}-\\d{4}");
        }
        return false;
    }

    public static boolean validateCellphone(String cellphone) {

        if (cellphone.length() == 8) {
            return cellphone.matches("\\((d{2}))\\d{5}-\\d{4}");
        }
        return false;
    }

    public static boolean validateEmail(String email) {

        if (email.length() == 30) {
            return email.matches("^[_A-Za-z0-9-\\\\+]+(\\\\.[_A-Za-z0-9-]+)*@\"\r\n"
                    + "        + \"[A-Za-z0-9-]+(\\\\.[A-Za-z0-9]+)*(\\\\.[A-Za-z]{2,})$\"");
            /*
             * return email.matches("\"^[\\\\w\\\\.-]+@([\\\\w\\\\-]+\\\\.)+[A-Z]{2,4}$\"");
             */
        }
        return false;
    }

    public static boolean validateCro(String cro) {

        if (cro.length() == 30) {
            return cro.matches("\\d{2})\\.(\\d{3})$");
        }
        return false;
    }

}
