package com.kadet.prolog.util;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTextArea;

/**
 * Класс для работы с файлами
 *
 */
public class FileIO {

    /**
     * Считать данные
     *
     * @param file
     * @return
     */
    public static ArrayList<String> readData (File file) {
        if (file == null || !file.exists()) {
            return new ArrayList();
        }
        FileReader fr = null;
        ArrayList<String> data = new ArrayList();
        try {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String st = "";
            while ((st = br.readLine()) != null) {
                data.add(st);
            }
        } catch (IOException ex) {
            Logger.getLogger(FileIO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(FileIO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return data;
    }

    /**
     * Добавить новое слово в алфавит
     *
     * @param file
     * @param word
     */
    public static void addWord (File file, String word) {
        FileWriter fw = null;
        try {
            ArrayList<String> data = readData(file);
            data.add(word);
            fw = new FileWriter(file);
            for (String s : data) {
                fw.write(s + "\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(FileIO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(FileIO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void print (JTextArea text, String fileName) {
        ArrayList<String> data = readData(new File(fileName));
        StringBuffer sb = new StringBuffer();
        sb.append("----------------------\nFile name:\n" +
                fileName + "\n----------------------\n");
        for (String s : data) {
            sb.append(s + "\n");
        }
        text.setText(sb.toString());
    }
}
