package com.kadet.prolog.frame;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JDialog;

import com.kadet.prolog.util.FileIO;


import com.alee.laf.button.WebButton;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextPane;


/**
 * Класс, выводящий модальное окно с описанием данной программы
 *
 */
public class About extends JDialog {

    public About () {
        setTitle("О программе");
        setSize(600, 600);
        setModal(true);
        WebTextPane text = new WebTextPane();
        WebButton close = new WebButton("Закрыть");

        Font f = new Font("Verdana", Font.PLAIN, 14);
        text.setFont(f);
        text.setEditable(false);
        WebPanel panel = new WebPanel();
        panel.add(close);

        WebScrollPane pane = new WebScrollPane(text);
        String s = "";
        add(pane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        ArrayList<String> data = FileIO.readData(
                new File("Resource/about"));
        for (String str : data) {
            s += str + "\n";
        }
        text.setText(s);

        close.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent event) {
                setVisible(false);
            }
        });
    }
}