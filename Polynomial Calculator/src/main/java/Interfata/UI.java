package Interfata;

import Controller.UIController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;


public class UI extends JFrame {
    private static JLabel label1, label2, labelResult;
    private static JTextField text1;
    private static JTextField text2;
    private static JTextField textResult;
    private static JButton butonAd, butonScad, butonInmu, butonImpa, butonDeri, butonInte;

    public UI() {
        setTitle("Calculator Polinomial");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(400, 300));

        label1 = new JLabel("Polinom 1:");
        label1.setFont(new Font("Arial", Font.PLAIN, 14));
        label1.setForeground(Color.WHITE);
        text1 = new JTextField(20);

        label2 = new JLabel("Polinom 2:");
        label2.setFont(new Font("Arial", Font.PLAIN, 14));
        label2.setForeground(Color.WHITE);
        text2 = new JTextField(20);

        labelResult = new JLabel("Rezultat:");
        labelResult.setFont(new Font("Arial", Font.PLAIN, 14));
        labelResult.setForeground(Color.WHITE);
        textResult = new JTextField(20);
        textResult.setEditable(false);


        // Set up buttons
        butonAd = new JButton("Adunare");
        butonAd.setBackground(new Color(255, 192, 203));

        butonScad = new JButton("Scadere");
        butonScad.setBackground(new Color(224, 192, 255));

        butonInmu = new JButton("Inmultire");
        butonInmu.setBackground(new Color(192, 255, 227));

        butonImpa = new JButton("Impartire");
        butonImpa.setBackground(new Color(255, 240, 192));

        butonDeri = new JButton("Derivare");
        butonDeri.setBackground(new Color(255, 211, 192));

        butonInte = new JButton("Integrare");
        butonInte.setBackground(new Color(249, 255, 192)); // Set button color


        // adaug textele si etichetele
        JPanel panelLabels = new JPanel(new GridLayout(3, 2, 10, 10));
        panelLabels.setBorder(new EmptyBorder(20, 20, 0, 20));
        panelLabels.add(label1);
        panelLabels.add(text1);
        panelLabels.add(label2);
        panelLabels.add(text2);
        panelLabels.add(labelResult);
        panelLabels.add(textResult);

        // adaugam butoanele
        JPanel panelButtons = new JPanel(new GridLayout(2, 3, 10, 10));
        panelButtons.setBorder(new EmptyBorder(10, 20, 20, 20));
        panelButtons.add(butonAd);
        panelButtons.add(butonScad);
        panelButtons.add(butonInmu);
        panelButtons.add(butonImpa);
        panelButtons.add(butonDeri);
        panelButtons.add(butonInte);

        // le aranjam
        add(panelLabels, BorderLayout.NORTH);
        add(panelButtons, BorderLayout.CENTER);

        // facem background negru ca e mai fain
        Color bgColor = new Color(0, 0, 0); // fac culoarea negru
        getContentPane().setBackground(bgColor); // Setez fundalul cu negru
        panelLabels.setBackground(bgColor); // setez fundalul panoului labels cu negru
        panelButtons.setBackground(bgColor); // la fel cu butoane

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new UI();
        UIController demo = new UIController(text1, text2, textResult, butonAd, butonScad, butonInmu, butonImpa, butonDeri, butonInte, labelResult);
    }

    public void aduButtonListener(ActionListener AduButtonListener) {
        System.out.println("CEVA");
        butonAd.addActionListener(AduButtonListener);
    }

    public void scaButtonListener(ActionListener ScaButtonListener) {
        butonScad.addActionListener(ScaButtonListener);
    }

    public void inmuButtonListener(ActionListener InmuButtonListener) {
        butonInmu.addActionListener(InmuButtonListener);
    }

    public void impaButtonListener(ActionListener ImpaButtonListener) {
        butonImpa.addActionListener(ImpaButtonListener);
    }

    public void deriButtonListener(ActionListener DeriButtonListener) {
        butonDeri.addActionListener(DeriButtonListener);
    }

    public void inteButtonListener(ActionListener InteButtonListener) {
        butonInte.addActionListener(InteButtonListener);
    }
}

