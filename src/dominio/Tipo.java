package dominio;

public enum Tipo {
    CACHORRO("Cachorro"),
    GATO("Gato");

    private final String nomeAnimal;

    Tipo(String nomeAnimal) {
        this.nomeAnimal = nomeAnimal;
    }

    public static Tipo converterStringToTipo(String tipoString) {
        for (Tipo tipo : values()) {
            if (tipoString.equalsIgnoreCase(String.valueOf(tipo))) {
                return tipo;
            }
        }
        throw new PetAtributoInvalidoExeception("Tipo inválido. Tipos possíveis de cadastrar: " + tiposDisponiveis());
    }

    public static String tiposDisponiveis() {
        StringBuilder tiposValidos = new StringBuilder();
        for (Tipo tipo : values()) {
            tiposValidos.append("\"").append(tipo.toString()).append("\"; ");
        }
        return tiposValidos.toString();
    }

    @Override
    public String toString() {
        return this.nomeAnimal;
    }
}
