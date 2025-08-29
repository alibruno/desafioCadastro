package dominio;

public enum Sexo {
    MASCULINO,
    FEMININO;

    public static Sexo converterStringToSexo(String sexoString) {
        for (Sexo sexo : values()) {
            if (sexoString.equalsIgnoreCase(String.valueOf(sexo))) {
                return sexo;
            }
        }
        throw new PetAtributoInvalidoExeception("Sexo inválido. Digite: \"MASCULINO\" ou \"FEMININO\".");
    }
}