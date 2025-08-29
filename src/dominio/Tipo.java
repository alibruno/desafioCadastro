package dominio;

public enum Tipo {
    CACHORRO,
    GATO;

    public static Tipo converterStringToTipo(String tipoString) {
        for (Tipo tipo : values()) {
            if (tipoString.equalsIgnoreCase(String.valueOf(tipo))) {
                return tipo;
            }
        }
        StringBuilder tiposValidos = new StringBuilder();
        tiposValidos.append("Tipo inválido. Tipos possíveis de cadastrar: ");
        for (Tipo tipo : values()) {
            tiposValidos.append("\"").append(String.valueOf(tipo)).append("\"; ");
        }
        throw new PetAtributoInvalidoExeception(tiposValidos.toString());
    }
}
