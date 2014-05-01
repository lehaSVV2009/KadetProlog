package com.kadet.prolog.run;

import com.kadet.prolog.frame.MainFrame;

/**
 * Запуск приложения
 *
 */
public class Main {

    public static void main(String [] args){
        MainFrame mf = new MainFrame("Лоис. Лабораторная работа 1", 800, 640);
        mf.setVisible(true);
    }
}
