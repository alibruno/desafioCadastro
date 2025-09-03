package dominio;

public enum Tipo {
    CACHORRO("Cachorro"),
    GATO("Gato");

    private String nomeAnimal;
    Tipo(String nomeAnimal) {
        this.nomeAnimal = nomeAnimal;
    }

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

    @Override
    public String toString() {
        return this.nomeAnimal;
    }
}
