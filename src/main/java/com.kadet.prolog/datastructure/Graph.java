package com.kadet.prolog.datastructure;

import java.util.ArrayList;

/**
 * Класс, описывающий неориентированный граф, вершинами которого
 * являются строки символов
 *
 */
public class Graph {
    /**
     * Список узлов графа
     */
    private ArrayList<String> nodes = new ArrayList<String>();
    /**
     * Метки пройденных узлов при обходе графа
     */
    private int[] uses = null;
    /**
     * Список ребер графа
     */
    private ArrayList<ArrayList<String>> links = new ArrayList<ArrayList<String>>();
    /**
     * Список компонент связности графа
     */
    private ArrayList<ArrayList<String>> groupList = new ArrayList<ArrayList<String>>();

    /**
     * Добавление списка вершин в граф
     *
     * @param nodes Список вершин
     */
    public void addAll(ArrayList<String> nodes) {
        for (int i = 0; i < nodes.size(); i++) {
            this.addNode(nodes.get(i));
        }
    }

    /**
     * Добаление вершины в граф
     *
     * @param node Вершина
     */
    public void addNode(String node) {
        if (!nodes.contains(node)) {
            nodes.add(node);
            links.add(new ArrayList<String>());
        }
    }

    /**
     * Добавление ребра в граф
     *
     * @param node1 Первая вершина
     * @param node2 Вторая вершина
     */
    public void addArc(String node1, String node2) {
        if (node1.equals(node2))
            return;
        int ind = nodes.indexOf(node1);
        if (!links.get(ind).contains(node2)) {
            links.get(ind).add(node2);
        }

        ind = nodes.indexOf(node2);
        if (!links.get(ind).contains(node1)) {
            links.get(ind).add(node1);
        }
    }

    /**
     * Проверка наличия ребра между двумя узлами
     *
     * @param node1 Первый узел
     * @param node2 Второй узел
     * @return true Если ребро имеется
     */
    public boolean hasArc(String node1, String node2) {
        int ind = nodes.indexOf(node1);
        return links.get(ind).contains(node2);
    }

    public String toString() {
        String s = "";
        s = s + nodes.toString() + "\n";
        for (int i = 0; i < nodes.size(); i++) {
            s = s + nodes.get(i) + " -> " + links.get(i) + "\n";
        }
        return s;
    }

    /**
     * Формирование компонент связности графа
     *
     * @return Множество компонент связности
     */
    public ArrayList<ArrayList<String>> formGroups() {
        groupList = new ArrayList<ArrayList<String>>();
        uses = new int[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            if (uses[i] == 0) {
                groupList.add(new ArrayList<String>());
                search(i, groupList.size() - 1);
            }
        }
        return groupList;
    }

    /**
     * Рекурсивная функция для поиска компонент связности
     *
     * @param ind Индекс вершины
     * @param g Номер компоненты связности
     */
    private void search(int ind, int g) {
        uses[ind] = 1;
        groupList.get(g).add(nodes.get(ind));
        for (int i = 0; i < nodes.size(); i++) {
            if (uses[i] == 0 && this.hasArc(nodes.get(ind), nodes.get(i))) {
                search(i, g);
            }
        }
    }
}