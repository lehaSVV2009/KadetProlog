package com.kadet.prolog.datastructure;

import java.util.ArrayList;

/**
 * Класс, описывающий предикат
 *
 * @author Dudal Sergey
 */
public class Atom {

    /**
     * Имя предиката
     */
    private String name = "";

    /**
     * Список параметров предиката
     */
    private ArrayList<String> vars = new ArrayList<String>();

    public Atom () {

    }

    /**
     * @param name Имя предиката
     */
    public Atom (String name) {
        this.setName(name);
    }

    /**
     * @param name Имя предиката
     * @param vars Список параметров предиката
     */
    public Atom (String name, ArrayList<String> vars) {
        this.setName(name);
        this.setVars(vars);
        if (this.getVars() == null) {
            this.setVars(new ArrayList<String>());
        }
    }

    public String toString () {
        String res = getName() + "(";
        for (int i = 0; i < getVars().size(); i++) {
            res = res + getVars().get(i) + ",";
        }
        res = res.substring(0, res.length() - 1);
        return res + ")";
    }

    public boolean equals (Object ob) {
        if (ob instanceof Atom) {
            Atom pred = (Atom) ob;
            if (this.getName().equals(pred.getName())
                    && this.getVars().equals(pred.getVars())) {
                return true;
            } else
                return false;
        } else
            return false;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getName () {
        return name;
    }

    public void setVars (ArrayList<String> vars) {
        this.vars = vars;
    }

    public ArrayList<String> getVars () {
        return vars;
    }
}