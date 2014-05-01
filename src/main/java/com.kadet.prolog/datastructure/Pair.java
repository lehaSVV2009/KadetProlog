package com.kadet.prolog.datastructure;

/**
 * Класс, описывающий пару связаных строковых элементов
 * (между логической переменной и константой)
 *
 */
public class Pair {

    /**
     * Первый элемент связки
     */
    private String from = "";

    /**
     * Второй элемент связки
     */
    private String to = "";

    /**
     * @param from Первый элемент связки
     * @param to Второй элемент связки
     */
    public Pair(String from, String to) {
        this.setFrom(from);
        this.setTo(to);
    }

    public String toString() {
        return getFrom() + "=" + getTo();
    }

    public boolean equals(Object ob) {
        if (ob instanceof Pair) {
            Pair p = (Pair) ob;
            if (this.getFrom().equals(p.getFrom())
                    && this.getTo().equals(p.getTo())) {
                return true;
            } else
                return false;
        } else
            return false;

    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTo() {
        return to;
    }
}
