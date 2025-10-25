public class Helicoptero extends VehiculoAereo {
    private boolean tienePatines;

    public Helicoptero(String identificador, boolean tienePatines) {
        super(identificador);
        this.tienePatines = tienePatines;
    }

    @Override
    public void despegar() {
        System.out.println("Helicóptero " + identificador +
                " despegando en vertical" + (tienePatines ? " con patines." : "."));
    }
}
