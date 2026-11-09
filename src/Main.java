import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;


public class Main extends JFrame{

    private JTextField txtPatente;
    private JTextField txtKilometros;
    private JTextField txtLitros;

    private JButton btnAgregar;
    private JButton btnRegistros;
    private JButton btnEstadisticas;
    private JButton btnFiltrar;

    private JTextArea txtArea;

    private ArrayList<String> listaPatentes = new ArrayList<>();
    private ArrayList<Double> listaKilometros = new ArrayList<>();
    private ArrayList<Double> listaLitros = new ArrayList<>();
    private ArrayList<Double> listaRendimientos = new ArrayList<>();

    private final String ARCHIVO = "productos.txt";

    public Main () {

        setTitle("Control de Consumo");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // PANEL PRINCIPAL
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10,10));

        //PANEL SUPERIOR
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(0, 0, 10,0));

        JPanel panelDatos = new JPanel(new GridLayout(3,2, 10,10));
        panelDatos.setBorder(BorderFactory.createTitledBorder("Datos del Vehículo \n"));

        panelDatos.add(new JLabel("Patente del vehículo: "));
        txtPatente = new JTextField(10);
        panelDatos.add(txtPatente);

        panelDatos.add(new JLabel("Kilometros (Km)"));
        txtKilometros = new JTextField(10);
        panelDatos.add(txtKilometros);

        panelDatos.add(new JLabel("Litros (L)"));
        txtLitros = new JTextField(10);
        panelDatos.add(txtLitros);


        //PANEL DE BOTONES
        JPanel panelBotones = new JPanel(new GridLayout(4,1, 0, 10));

        btnAgregar = new JButton("Agregar");
        btnRegistros = new JButton("Ver Registros");
        btnEstadisticas = new JButton("Calcular Estadísticas");
        btnFiltrar = new JButton("Filtrar");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnRegistros);
        panelBotones.add(btnEstadisticas);
        panelBotones.add(btnFiltrar);

        panelSuperior.add(panelDatos, BorderLayout.WEST);
        panelSuperior.add(panelBotones, BorderLayout.EAST);

        //AREA DE TEXTO
        txtArea = new JTextArea();
        txtArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        txtArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(txtArea);
        scroll.setBorder(BorderFactory.createTitledBorder("Listado y resultados"));
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(scroll, BorderLayout.CENTER);


        //FUNCION DE LOS BOTONES
        btnAgregar.addActionListener(e -> {
            agregarVehiculo();
        });

        btnRegistros.addActionListener(e ->{
            mostrarRegistro();
        } );

        btnEstadisticas.addActionListener(e -> {
            calcularEstadisticas();
        });

        btnFiltrar.addActionListener(e -> {
            filtrarDatos();
        });

        cargarRegistro();

        add(panelPrincipal);
        setVisible(true);
    }

    //FUNCIONES
    // AGREGAR
    public void agregarVehiculo () {

        String patente = txtPatente.getText().trim();
        String kilometros = txtKilometros.getText().trim();
        String litros = txtLitros.getText().trim();

        if (patente.isEmpty() || kilometros.isEmpty() || litros.isEmpty()){
            JOptionPane.showMessageDialog(this,"Debe completar todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double valorKilometros = Double.parseDouble(kilometros);
            double valorLitros = Double.parseDouble(litros);

            if (valorKilometros <= 0 && valorLitros <= 0){
                JOptionPane.showMessageDialog(this, "Kilometros y Litros deben ser mayores a 0", "Error", JOptionPane.ERROR_MESSAGE);
                return;

            }else if (valorKilometros <= 0){
                JOptionPane.showMessageDialog(this, "Kilometros deben ser mayores a 0", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (valorLitros <= 0) {
                JOptionPane.showMessageDialog(this, "Litros deben ser mayores a 0", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double rendimiento = valorKilometros / valorLitros;

            listaPatentes.add(patente);
            listaKilometros.add(valorKilometros);
            listaLitros.add(valorLitros);
            listaRendimientos.add(rendimiento);

            guardarRegistro (patente, valorKilometros, valorLitros, rendimiento);

            mostrarRegistro ();
            limpiarCampos();

            JOptionPane.showMessageDialog(this, "Vehículo guardado correctamente");

        }catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,"Kilometros y Litros deben ser números (para decimales usar punto (Ej: 9.5)", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

    }

    // GUARDAR
    public void guardarRegistro (String patente, double kilometros, double litros, double rendimiento){
        try {

            FileWriter archivo = new FileWriter(ARCHIVO, true);
            BufferedWriter escribir = new BufferedWriter(archivo);

            escribir.write(patente + ";" + kilometros + ";" + litros + ";" + rendimiento + ";");
            escribir.newLine();

            escribir.close();

        }catch (IOException e){
            JOptionPane.showMessageDialog(this, "Error al guardar vehículo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    //LEER
    public void cargarRegistro (){

        File archivo = new File(ARCHIVO);

        if (!archivo.exists()){
            return;
        }

        try {

            FileReader lectorArchivo = new FileReader(ARCHIVO);
            BufferedReader lector = new BufferedReader(lectorArchivo);

            String linea;

            while ((linea = lector.readLine()) != null){

                String [] datos = linea.split(";");

                if (datos.length == 4) {

                 String patente = datos[0];
                 double kilometros = Double.parseDouble(datos[1]);
                 double litros = Double.parseDouble(datos[2]);
                 double rendimiento = Double.parseDouble(datos[3]);

                 listaPatentes.add(patente);
                 listaKilometros.add(kilometros);
                 listaLitros.add(litros);
                 listaRendimientos.add(rendimiento);

                }

            } lector.close();

        }catch (IOException e){
            JOptionPane.showMessageDialog(this, "Error al leer registro: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }catch (NumberFormatException e){
            JOptionPane.showMessageDialog(this, "El archivo contiene datos con formato inválido");
        }

    }

    //MOSTRAR DATOS
    //---TABLA---
    public String encabezadoTabla (){
        return String.format("%-4s %-10s %-10s %-10s %-10s \n", "N°","Patente","Km","Litros","Km/L")
        +"----------------------------------------------\n";
    }

    public String filaTabla (int i){
        return String.format("%-4d %-10s %-10.1f %-10.1f %-10.2f\n", (i+1),
        listaPatentes.get(i),listaKilometros.get(i), listaLitros.get(i), listaRendimientos.get(i));
    }

    // ---VER DATOS---
    public void mostrarRegistro (){

        txtArea.setText("");

        if (listaPatentes.size() == 0){
            txtArea.setText("No hay registro de consumo");
            return;
        }

        txtArea.append(encabezadoTabla());

        for (int i = 0; i < listaPatentes.size(); i++ ){
            txtArea.append(filaTabla(i));
        }

    }

    //CALCULAR ESTADISTICAS
    public void calcularEstadisticas (){

        if (listaPatentes.size() == 0){
            JOptionPane.showMessageDialog(this, "No hay registros para mostrar");
            return;
        }

        double total = 0;
        int minimo = 0;
        int maximo = 0;

        for (int i = 0; i < listaPatentes.size(); i++) {
            total = total + listaRendimientos.get(i);

            if (listaRendimientos.get(i) < listaRendimientos.get(minimo)) {
                minimo = i;
            }

            if (listaRendimientos.get(i) > listaRendimientos.get(maximo)) {
                maximo = i;
            }
        }

        double promedio = total / listaRendimientos.size();

        txtArea.setText("- - - - - ESTADÍSTICAS - - - - - \n");
        txtArea.append("Cantidad de registros: " + listaRendimientos.size()+ "\n");
        txtArea.append("Promedio: " + String.format("%.2f", promedio) + "km/L\n");
        txtArea.append("Mínimo: " + String.format("%.2f", listaRendimientos.get(minimo)) + " km/L (" + listaPatentes.get(minimo) + ")\n");
        txtArea.append("Máximo: " + String.format("%.2f", listaRendimientos.get(maximo)) + " km/L (" + listaPatentes.get(maximo) + ")\n");

    }

    public void filtrarDatos (){

        if (listaRendimientos.size() == 0){
            JOptionPane.showMessageDialog(this, "No hay registros para filtrar", "AVISO", JOptionPane.WARNING_MESSAGE);
            return;
        }

        txtArea.setText("- - - RENDIMIENTO BAJO (menos de 10 km/L) - - -\n");
        txtArea.append(encabezadoTabla());

        int contador = 0;

        for (int i = 0 ; i < listaRendimientos.size(); i++){
            if (listaRendimientos.get(i) < 10){
                txtArea.append(filaTabla(i));
                contador ++;
            }
        }

        if (contador == 0){
            txtArea.setText("No hay vehículos con rendimiento inferior a 10 km/L");
        }
    }


    public void limpiarCampos (){

        txtPatente.setText("");
        txtKilometros.setText("");
        txtLitros.setText("");

    }

    public static void main(String[] args) {
        //MEJORAS DE LA UI

        try {
            UIManager.put("control", new Color(45,45,45));
            UIManager.put("text", new Color(255, 255, 255));
            UIManager.put("nimbusLightBackground", new Color(69, 69, 69));
            UIManager.put("nimbusBase", new Color(253, 110, 0));
            UIManager.put("nimbusBlueGrey", new Color(124, 84, 66));
            UIManager.put("nimbusSelectionBackground", new Color(45,115,180));


            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            UIManager.put("Button.font", new Font(Font.SANS_SERIF, Font.BOLD, 12));

        }catch (Exception e) {
            System.out.println("No se puede aplicar UI");
        }
        new Main();

    }

}
