package com.kadet.prolog.frame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
/*
import com.alee.laf.filechooser.FilesToChoose;
import com.alee.laf.filechooser.SelectionMode;*/
import com.alee.laf.filechooser.WebFileChooser;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.splitpane.WebSplitPane;
import com.kadet.prolog.solver.SolveManager;
import com.kadet.prolog.util.FileIO;

/**
 * Класс, определяющий основной Frame приложения
 *
 */
public class MainFrame extends JFrame {

    private JTextArea base = null;
    private JTextArea target = null;
    private JTextArea result = null;
    private SolveManager solver = null;
    private WebFileChooser fileChooser = null;

    public MainFrame (String title, int height, int width) {
        this.setTitle(title);
        this.setSize(height, width);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(WebLookAndFeel.class.getCanonicalName());
        } catch (Exception e) {
        }
        setupFrame();
        setupMenu();
    }

    private void setupFrame () {
        solver = new SolveManager();
        base = new JTextArea();
        target = new JTextArea();
        result = new JTextArea();
        base.setEditable(false);
        target.setEditable(false);
        result.setEditable(false);
        WebScrollPane baseScroll = new WebScrollPane(base);
        WebScrollPane targetScroll = new WebScrollPane(target);
        WebScrollPane resultScroll = new WebScrollPane(result);
        WebSplitPane hsplit = new WebSplitPane(WebSplitPane.HORIZONTAL_SPLIT,
                baseScroll, targetScroll);
        hsplit.setResizeWeight(0.5);
        hsplit.setContinuousLayout(true);
        WebSplitPane vsplit = new WebSplitPane(WebSplitPane.VERTICAL_SPLIT,
                hsplit, resultScroll);
        vsplit.setResizeWeight(0.5);
        vsplit.setContinuousLayout(true);
        this.add(vsplit, BorderLayout.CENTER);
    }

    private void setupMenu () {
        JMenuBar menu = new JMenuBar();
        JMenu fileMenu = new JMenu("Файл");
        JMenu actionMenu = new JMenu("Действия");

        JMenuItem close = new JMenuItem("Выход");
        close.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed (ActionEvent e) {
                System.exit(0);
            }
        });

        JMenuItem openBase = new JMenuItem("Открыть базу");
        JMenuItem openTarget = new JMenuItem("Открыть цель");

        fileMenu.add(openBase);
        fileMenu.add(openTarget);
        fileMenu.add(close);

        openBase.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed (ActionEvent e) {
                fileChooser = new WebFileChooser(/*new File("Resource/Base")*//*null, "Выберите файл с базой"*/);
                File file = fileChooser.showOpenDialog();
                if (file != null) {
                    solver.setBaseFile(file.getAbsolutePath());
                    FileIO.print(base, file.getAbsolutePath());
                }

            }
        });

        openTarget.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed (ActionEvent e) {
                fileChooser = new WebFileChooser(/*new File("Resource/Target")*//*null, "Выберите файл с базой"*/);
                File file = fileChooser.showOpenDialog();
                if (file != null) {
                    solver.setTargetFile(file.getAbsolutePath());
                    FileIO.print(target, file.getAbsolutePath());
                }
            }
        });

        JMenuItem analogy = new JMenuItem("Вывод");

        analogy.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed (ActionEvent e) {
                solver.solve(result);
            }
        });

        actionMenu.add(analogy);

        menu.add(fileMenu);
        menu.add(actionMenu);

        JMenu help = new JMenu("?");
        JMenuItem about = new JMenuItem("О программе");
        about.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed (ActionEvent e) {
                About a = new About();
                a.setVisible(true);
            }
        });
        help.add(about);
        menu.add(help);
        setJMenuBar(menu);
    }
}
