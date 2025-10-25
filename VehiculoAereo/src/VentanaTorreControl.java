import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;

public class VentanaTorreControl extends JFrame {
    // Modelo de dominio: cola de despegue
    private final Deque<VehiculoAereo> cola = new ArrayDeque<>();

    // Componentes UI
    private final JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Avión", "Helicóptero"});
    private final JTextField txtIdentificador = new JTextField(10);
    private final JSpinner spPasajeros = new JSpinner(new SpinnerNumberModel(100, 1, 600, 1));
    private final JCheckBox chkPatines = new JCheckBox("Patines");
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> listCola = new JList<>(listModel);
    private final JTextArea txtLog = new JTextArea(10, 40);

    public VentanaTorreControl() {
        super("Torre de Control - Despegues");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        // Panel de alta de vehículos
        JPanel panelAlta = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        c.gridx = 0; c.gridy = row; panelAlta.add(new JLabel("Tipo:"), c);
        c.gridx = 1; c.gridy = row++; panelAlta.add(cbTipo, c);

        c.gridx = 0; c.gridy = row; panelAlta.add(new JLabel("Identificador:"), c);
        c.gridx = 1; c.gridy = row++; panelAlta.add(txtIdentificador, c);

        c.gridx = 0; c.gridy = row; panelAlta.add(new JLabel("Pasajeros (Avión):"), c);
        c.gridx = 1; c.gridy = row++; panelAlta.add(spPasajeros, c);

        c.gridx = 0; c.gridy = row; panelAlta.add(new JLabel("Opciones (Helicóptero):"), c);
        c.gridx = 1; c.gridy = row++; panelAlta.add(chkPatines, c);

        JButton btnAgregar = new JButton("Agregar a cola");
        c.gridx = 0; c.gridy = row; c.gridwidth = 2;
        panelAlta.add(btnAgregar, c);

        // Panel de cola y controles
        JPanel panelCola = new JPanel(new BorderLayout(8, 8));
        panelCola.add(new JLabel("Cola de despegue (orden):"), BorderLayout.NORTH);
        listCola.setVisibleRowCount(8);
        panelCola.add(new JScrollPane(listCola), BorderLayout.CENTER);

        JPanel panelAcciones = new JPanel(new GridLayout(1, 3, 8, 8));
        JButton btnDespegarSiguiente = new JButton("Despegar siguiente");
        JButton btnDespegarTodos = new JButton("Despegar todos");
        JButton btnVaciar = new JButton("Vaciar cola");
        panelAcciones.add(btnDespegarSiguiente);
        panelAcciones.add(btnDespegarTodos);
        panelAcciones.add(btnVaciar);
        panelCola.add(panelAcciones, BorderLayout.SOUTH);

        // Panel de log
        JPanel panelLog = new JPanel(new BorderLayout(8, 8));
        panelLog.add(new JLabel("Registro de operaciones:"), BorderLayout.NORTH);
        txtLog.setEditable(false);
        panelLog.add(new JScrollPane(txtLog), BorderLayout.CENTER);

        // Layout principal
        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        content.add(panelAlta, BorderLayout.WEST);
        content.add(panelCola, BorderLayout.CENTER);
        content.add(panelLog, BorderLayout.SOUTH);
        setContentPane(content);

        // Interacciones
        cbTipo.addActionListener(e -> toggleCampos());
        toggleCampos();

        btnAgregar.addActionListener(e -> onAgregar());
        btnDespegarSiguiente.addActionListener(e -> onDespegarSiguiente());
        btnDespegarTodos.addActionListener(e -> onDespegarTodos());
        btnVaciar.addActionListener(e -> onVaciar());
    }

    private void toggleCampos() {
        boolean esAvion = cbTipo.getSelectedItem().toString().equals("Avión");
        spPasajeros.setEnabled(esAvion);
        chkPatines.setEnabled(!esAvion);
    }

    private void onAgregar() {
        String id = txtIdentificador.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un identificador.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        VehiculoAereo v;
        if (cbTipo.getSelectedItem().toString().equals("Avión")) {
            int pax = (Integer) spPasajeros.getValue();
            v = new Avion(id, pax);
            listModel.addElement("Avión " + id + " (pax " + pax + ")");
        } else {
            boolean patines = chkPatines.isSelected();
            v = new Helicoptero(id, patines);
            listModel.addElement("Helicóptero " + id + (patines ? " [patines]" : ""));
        }

        cola.addLast(v);
        txtIdentificador.setText("");
        txtIdentificador.requestFocus();
        log("Agregado a cola: " + v.getClass().getSimpleName() + " " + id);
    }

    private void onDespegarSiguiente() {
        if (cola.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay vehículos en cola.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        VehiculoAereo v = cola.removeFirst();
        if (!listModel.isEmpty()) listModel.remove(0);
        // Mostrar cómo despega (polimorfismo)
        capturarDespegue(v);
    }

    private void onDespegarTodos() {
        if (cola.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay vehículos en cola.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        while (!cola.isEmpty()) {
            VehiculoAereo v = cola.removeFirst();
            if (!listModel.isEmpty()) listModel.remove(0);
            capturarDespegue(v);
        }
    }

    private void onVaciar() {
        cola.clear();
        listModel.clear();
        log("Cola vaciada.");
    }

    // Captura la salida “despegar()” en el log
    private void capturarDespegue(VehiculoAereo v) {
        // Redirigimos la impresión a log
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.PrintStream ps = new java.io.PrintStream(baos);
        java.io.PrintStream oldOut = System.out;
        System.setOut(ps);
        try {
            v.despegar(); // usa tu implementación actual
        } finally {
            System.out.flush();
            System.setOut(oldOut);
        }
        log(baos.toString().trim());
    }

    private void log(String msg) {
        txtLog.append(msg + "\n");
        txtLog.setCaretPosition(txtLog.getDocument().getLength());
    }
}
