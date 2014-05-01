package com.kadet.prolog.datastructure;

/**
 * Класс, описывающий пару, состоящую из предиката и отношения подобия
 *
 */
public class SimPair {

    /**
     * Предикат
     */
    private Atom pred = null;

    /**
     * Отношение подобия
     */
    private String sim = "";

    /**
     * @param pred Предикат
     * @param sim  Отношение подобия
     */
    public SimPair (Atom pred, String sim) {
        super();
        this.setPred(pred);
        this.setSim(sim);
    }

    public String toString () {
        return getPred() + "-s-" + getSim();
    }

    public void setPred (Atom pred) {
        this.pred = pred;
    }

    public Atom getPred () {
        return pred;
    }

    public void setSim (String sim) {
        this.sim = sim;
    }

    public String getSim () {
        return sim;
    }
}
