public abstract class VehiculoAereo {
    protected String identificador;

    public VehiculoAereo(String identificador) {
        this.identificador = identificador;
    }

    public String getIdentificador() {
        return identificador;
    }

    // Método abstracto: cada vehículo define su forma de despegar
    public abstract void despegar();
}
