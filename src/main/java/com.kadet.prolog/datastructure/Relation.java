package com.kadet.prolog.datastructure;


import java.util.ArrayList;

/**
 * Класс, описывающий отношение подобия
 *
 */
public class Relation extends Atom {

    /**
     * @param name Имя отношения
     * @param vars Пара подобных элементов
     */
    public Relation(String name, ArrayList<String> vars) {
        this.setName(name);
        this.setVars(vars);
        if (this.getVars() == null) {
            this.setVars(new ArrayList<String>());
        }
    }
}
