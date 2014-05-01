package com.kadet.prolog.datastructure;

import java.util.ArrayList;

/**
 * Класс, описывающий базу знаний
 *
 */

public class Base {
    public static ArrayList<String> consts = new ArrayList<String>();
    public static ArrayList<String> vars = new ArrayList<String>();
    public static ArrayList<Atom> preds = new ArrayList<Atom>();
    public static ArrayList<Atom> goals = new ArrayList<Atom>();
    public static ArrayList<Relation> sims = new ArrayList<Relation>();
    public static ArrayList<Rule> rules = new ArrayList<Rule>();

    public Base (ArrayList<String> consts, ArrayList<String> vars,
                 ArrayList<Atom> preds, ArrayList<Atom> goals,
                 ArrayList<Relation> sims, ArrayList<Rule> rules) {
        this.consts = consts;
        this.vars = vars;
        this.preds = preds;
        this.goals = goals;
        this.sims = sims;
        this.rules = rules;
    }

    public String getInfo () {
        StringBuffer sb = new StringBuffer();
        sb.append("Список переменных");
        sb.append(vars.toString());
        sb.append("\n" + "Список констант");
        sb.append(consts.toString());
        sb.append("\n" + "Список предикатов" + "\n");
        sb.append(preds.toString());
        sb.append("\n" + "Список отношений" + "\n");
        sb.append(sims.toString());
        sb.append("\n" + "Список правил" + "\n");
        sb.append(rules.toString());
        sb.append("\n" + "Цели" + "\n");
        sb.append(goals.toString());
        return sb.toString();
    }

    public ArrayList<String> getConsts () {
        return consts;
    }

    public void setConsts (ArrayList<String> consts) {
        Base.consts = consts;
    }

    public ArrayList<String> getVars () {
        return vars;
    }

    public void setVars (ArrayList<String> vars) {
        Base.vars = vars;
    }

    public ArrayList<Atom> getPreds () {
        return preds;
    }

    public void setPreds (ArrayList<Atom> preds) {
        Base.preds = preds;
    }

    public ArrayList<Atom> getGoals () {
        return goals;
    }

    public void setGoals (ArrayList<Atom> goals) {
        Base.goals = goals;
    }

    public ArrayList<Relation> getSims () {
        return sims;
    }

    public void setSims (ArrayList<Relation> sims) {
        Base.sims = sims;
    }

    public ArrayList<Rule> getRules () {
        return rules;
    }

    public void setRules (ArrayList<Rule> rules) {
        Base.rules = rules;
    }


}
