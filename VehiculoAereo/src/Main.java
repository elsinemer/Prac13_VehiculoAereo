public class Main {
    public static void main(String[] args) {
        // Arreglo polimórfico: contiene Avion y Helicoptero como VehiculoAereo
        VehiculoAereo[] colaDespegue = {
                new Avion("AR123", 180),
                new Helicoptero("H-BLACK", true),
                new Avion("UY450", 90),
                new Helicoptero("RESCUE-7", false)
        };

        System.out.println("Orden de despegue según la posición en el arreglo:\n");
        for (int i = 0; i < colaDespegue.length; i++) {
            System.out.print((i + 1) + ") ");
            colaDespegue[i].despegar(); // despacho polimórfico
        }
    }
}
