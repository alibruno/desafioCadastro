package dominio;

public enum Sexo {
    MACHO("Macho"),
    FEMEA("Femea");

    private String genero;

    Sexo(String genero) {
        this.genero = genero;
    }

    public static Sexo converterStringToSexo(String sexoString) {
        for (Sexo sexo : values()) {
            if (sexoString.equalsIgnoreCase(String.valueOf(sexo))) {
                return sexo;
            }
        }
        throw new PetAtributoInvalidoExeception("Sexo inválido. Digite: \"Macho\" ou \"Femea\".");
    }

    @Override
    public String toString() {
        return this.genero;
    }
}