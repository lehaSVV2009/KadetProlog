package com.kadet.prolog.datastructure;

import java.util.ArrayList;

/**
 * Класс, описывающий множество результатов вывода
 *
 */
public class Result {

    /**
     * Список списков пар результатов
     */
    private ArrayList<ArrayList<Pair>> values = new ArrayList<ArrayList<Pair>>();

    public boolean contains (Result res) {


        return true;
    }

    /**
     * Добавление нового списка пар результатов
     *
     * @param line Добавляемый список пар результатов
     */
    public void addPairList (ArrayList<Pair> line) {
        if (!this.values.contains(line)) {
            this.values.add(line);
        }
    }

    /**
     * Соединение отношения с другим отношением
     *
     * @param other Другое отношение
     * @return Результат соединения
     */
    public Result link (Result other) {
        Result res = new Result();
        // Если исходный список пуст, то возвращаем копию передаваемого
        if (values.size() == 0) {
            res.values = new ArrayList<ArrayList<Pair>>(other.values);
            return res;
        }
        // Если передаваемый список пуст, то возвращаем копию исходного
        if (other.values.size() == 0) {
            res.values = new ArrayList<ArrayList<Pair>>(values);
            return res;
        }
        // Соединяем отношения
        for (int i = 0; i < this.values.size(); i++) {
            for (int j = 0; j < other.values.size(); j++) {
                ArrayList<Pair> l = this.link(values.get(i), other.values
                        .get(j));
                if (l != null)
                    res.addPairList(l);
            }
        }
        return res;
    }

    /**
     * Соединение двух списков пар значений переменных
     *
     * @param list1 Первый список
     * @param list2 Второй список
     * @return Результат соединения
     */
    private ArrayList<Pair> link (ArrayList<Pair> list1, ArrayList<Pair> list2) {
        ArrayList<Pair> res = new ArrayList<Pair>();
        ArrayList<Pair> list = new ArrayList<Pair>(list2);
        res.addAll(list1);
        for (int i = 0; i < list1.size(); i++) {
            Pair pair1 = list1.get(i);
            for (int j = 0; j < list.size(); j++) {
                Pair pair2 = list.get(j);
                // Если текущая пара первого списка соответсвует текущей паре второго
                // списка, то удаляем данный элемент из второго списка
                if (pair1.getFrom().equals(pair2.getFrom())
                        && pair1.getTo().equals(pair2.getTo())) {
                    list.remove(j);
                    j--;
                    continue;
                }
                // Если первые элементы текущих пар совпадают, а вторые - нет
                // возвращаем null
                if (pair1.getFrom().equals(pair2.getFrom())
                        && !pair1.getTo().equals(pair2.getTo())) {
                    return null;
                }
            }
        }
        // Добавляем к парам из первого списка оставщиеся из второго
        res.addAll(list);
        return res;
    }

    /**
     * Проверяем пары на соответствие предикату
     *
     * @param pred Предикат
     */
    public void checkPairToAtom (Atom pred) {
        for (int i = 0; i < values.size(); i++) {
            for (int j = 0; j < values.get(i).size(); j++) {
                Pair pair1 = values.get(i).get(j);
                // Если начало пары не совпадает с переменными предиката
                // удаляем такую пару
                if (!pred.getVars().contains(pair1.getFrom())) {
                    values.get(i).remove(j);
                    j--;
                }
            }
        }
    }

    /**
     * Замена подобных переменных в списке отношений на их прообразы
     *
     * @param sims Множество пар отношений подобия
     * @param vars Множество переменных
     */
    public void returnSims (ArrayList<Relation> sims,
                            ArrayList<String> vars) {
        for (int i = 0; i < values.size(); i++) {
            for (int j = 0; j < values.get(i).size(); j++) {
                Pair pair = values.get(i).get(j);
                if (!vars.contains(pair.getFrom())) {
                    for (int k = 0; k < sims.size(); k++) {
                        if (pair.getFrom().equals(
                                sims.get(k).getVars().get(0)
                                        + sims.get(k).getName())) {
                            pair.setFrom(sims.get(k).getVars().get(0));
                        }
                    }
                }
            }
        }
    }

    /**
     * Удаление повторяющихся значений в списке пар
     */
    public void recheck () {
        ArrayList<ArrayList<Pair>> values1 = new ArrayList<ArrayList<Pair>>();
        for (int i = 0; i < values.size(); i++) {
            if (!values1.contains(values.get(i))) {
                values1.add(values.get(i));
            }
        }
        values = new ArrayList<ArrayList<Pair>>(values1);
    }

    /**
     * Добавляем в текущее множество пар, пары с другого множества
     *
     * @param set Другое множество
     */
    public void addAll (Result set) {
        values.addAll(set.values);
    }

    public String toString () {
        return values.toString();
    }

    public String getResult () {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < values.size(); i++) {
            sb.append(values.get(i) + ";\n");
        }
        return sb.toString();
    }

    /**
     * Проверка соответствия строк списка значений списку переменных предиката
     *
     * @param pred
     */
    public void checkPred (Atom pred) {
        // Определяем количество переменных
        int count = pred.getVars().size();
        for (int i = 0; i < pred.getVars().size(); i++) {
            if (!Character.isUpperCase(pred.getVars().get(i).charAt(0))) {
                count--;
            }
        }
        for (int i = 0; i < values.size(); i++) {
            // Если число пар в текущем списке не соответствует
            // числу переменных в цели, то удаляем данные пары
            if (values.get(i).size() != count) {
                values.remove(i);
                i--;
            }
        }
    }

    /**
     * @return true Если список пуст
     */
    public boolean isEmpty () {
        return values.isEmpty();
    }

    /**
     * Очистка списка
     */
    public void clear () {
        values.clear();
    }
}