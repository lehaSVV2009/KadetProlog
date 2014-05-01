package com.kadet.prolog.solver;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import com.kadet.prolog.datastructure.Base;
import com.kadet.prolog.parser.*;

/**
 * Класс, отвечающий за управление логическим выводом
 * и отображение результата
 *
 */

public class SolveManager {
    private String base_file = "";
    private String target_file = "";

    private Base base = null;
    private Solver analyzer = null;

    public SolveManager () {

    }

    public void solve (JTextArea text) {
        parseData();
        text.setText(text.getText() + "\n" + base.getInfo());
        analyzer = new Solver(base);
        text.setText(text.getText() + "\nРезультат:\n" + analyzer.solve());
    }

    private void parseData () {
        if (base_file.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Загрузите базу");
            return;
        }
        if (target_file.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Загрузите цель");
            return;
        }
        try {
            base = GrammarParser.parse(base_file, target_file);
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    public void setTargetFile (String fileName) {
        target_file = fileName;
    }

    public void setBaseFile (String fileName) {
        base_file = fileName;
    }
}
