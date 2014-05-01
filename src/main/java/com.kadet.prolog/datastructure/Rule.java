package com.kadet.prolog.datastructure;

import java.util.ArrayList;

/**
 * Класс, описывающий правила
 *
 */
public class Rule {

    /**
     * Заключение правила
     */
    private Atom result = null;

    /**
     * Множество посылок правила
     */
    private ArrayList<Atom> sources = new ArrayList<Atom>();

    /**
     * @param result  Заключение правила
     * @param sources Множество посылок правила
     */
    public Rule (Atom result, ArrayList<Atom> sources) {
        super();
        this.setResult(result);
        this.setSources(sources);
    }

    public String toString () {
        String res = getResult().toString() + "<-";
        for (int i = 0; i < getSources().size(); i++) {
            res = res + getSources().get(i);
        }
        return res;
    }

    public void setResult (Atom result) {
        this.result = result;
    }

    public Atom getResult () {
        return result;
    }

    public void setSources (ArrayList<Atom> sources) {
        this.sources = sources;
    }

    public ArrayList<Atom> getSources () {
        return sources;
    }

}
