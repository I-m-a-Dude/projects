package Controller;

import Functii.*;
import Polinom.Polinom;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UIController {
    private final JTextField text1;
    private final JTextField text2;
    private final JTextField textResult;
    private final JButton butonAd;
    private final JButton butonScad;
    private final JButton butonInmu;
    private final JButton butonImpa;
    private final JButton butonDeri;
    private final JButton butonInte;
    private final JLabel labelResult;

    public UIController(JTextField text1, JTextField text2, JTextField textResult, JButton butonAd, JButton butonScad, JButton butonInmu, JButton butonImpa, JButton butonDeri, JButton butonInte, JLabel labelResult) {
        this.text1 = text1;
        this.text2 = text2;
        this.butonAd = butonAd;
        this.butonScad = butonScad;
        this.butonInmu = butonInmu;
        this.butonImpa = butonImpa;
        this.butonDeri = butonDeri;
        this.butonInte = butonInte;
        this.labelResult = labelResult;
        this.textResult = textResult;


        butonAd.addActionListener(new AduButtonListener());
        butonScad.addActionListener(new ScaButtonListener());
        butonInmu.addActionListener(new InmuButtonListener());
        butonImpa.addActionListener(new ImpaButtonListener());
        butonDeri.addActionListener(new DeriButtonListener());
        butonInte.addActionListener(new InteButtonListener());
    }


    private class AduButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Polinom polinom1 = Polinom.fromString(text1.getText());
            Polinom polinom2 = Polinom.fromString(text2.getText());
            Polinom result = Adunare.adunare(polinom1, polinom2);
            textResult.setText(String.valueOf(result));

        }
    }

    private class ScaButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Polinom polinom1 = Polinom.fromString(text1.getText());
            Polinom polinom2 = Polinom.fromString(text2.getText());
            Polinom result = Scadere.scadere(polinom1, polinom2);
            textResult.setText(result.toString());
        }
    }

    private class InmuButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Polinom polinom1 = Polinom.fromString(text1.getText());
            Polinom polinom2 = Polinom.fromString(text2.getText());
            Polinom result = Inmultire.inmultire(polinom1, polinom2);
            textResult.setText(result.toString());
        }
    }

    private class ImpaButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Polinom polinom1 = Polinom.fromString(text1.getText());
            Polinom polinom2 = Polinom.fromString(text2.getText());
            // Polinom[] result = Impartire.impartire(polinom1, polinom2);
            textResult.setText("Teapa Cumetre!!! Nu e implementat :))");
        }
    }

    private class DeriButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Polinom polinom1 = Polinom.fromString(text1.getText());
            Polinom result = Derivare.derivare(polinom1);
            textResult.setText(result.toString());
        }
    }

    private class InteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Polinom polinom1 = Polinom.fromString(text1.getText());
            Polinom result = Integrare.integrare(polinom1);
            textResult.setText(result.toString());
        }
    }
}

