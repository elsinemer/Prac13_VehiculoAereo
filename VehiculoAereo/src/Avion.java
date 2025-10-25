public class Avion extends VehiculoAereo {
    private int pasajeros;

    public Avion(String identificador, int pasajeros) {
        super(identificador);
        this.pasajeros = pasajeros;
    }

    @Override
    public void despegar() {
        System.out.println("Avión " + identificador +
                " acelerando por pista. Pasajeros: " + pasajeros + ".");
    }
}
