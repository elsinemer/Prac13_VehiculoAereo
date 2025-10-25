public class App {
    public static void main(String[] args) {
        // Look & Feel del sistema (opcional)
        try {
            javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        javax.swing.SwingUtilities.invokeLater(() -> {
            new VentanaTorreControl().setVisible(true);
        });
    }
}
