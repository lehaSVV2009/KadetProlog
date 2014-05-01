package com.kadet.prolog.solver;

import java.util.ArrayList;

import com.kadet.prolog.datastructure.*;

/**
 * Основной класс, отвечающий за осуществление логического вывода
 *
 */
public class Solver {

    /**
     * Множество логических переменных
     */
    private ArrayList<String> vars = new ArrayList<String>();
    /**
     * Множество предикатов в базе знаний
     */
    private ArrayList<Atom> preds = new ArrayList<Atom>();
    /**
     * Множество пар отношений подобия в базе знаний
     */
    private ArrayList<Relation> sims = new ArrayList<Relation>();
    /**
     * Множество правил вывода в базе знаний
     */
    private ArrayList<Rule> rules = new ArrayList<Rule>();
    /**
     * Множество предикатов, рассмотренных при выводе
     */
    private ArrayList<Atom> done = new ArrayList<Atom>();
    /**
     * Множество целей
     */
    private ArrayList<Atom> goals = new ArrayList<Atom>();

    public Solver (Base base) {
        // Инициализируем списки данными, получеными при разборе базы и целей
        this.vars = base.getVars();
        this.preds = base.getPreds();
        // Проверяем арность предикат
        if (!checkAtoms())
            return;
        this.sims = base.getSims();
        this.rules = base.getRules();
        goals = base.getGoals();
    }

    /**
     * Начало вывода.
     *
     * @return
     */
    public String solve () {
        if (goals.size() > 1) {
            return "Нет цели";
        }
        this.createVarSims();
        this.reverseSims();
        Result result = new Result();
        for (int i = 0; i < goals.size(); i++) {
            result = result.link(nextStep(goals.get(i), ""));
        }
        return result.getResult();
    }

    /**
     * Проверка предикатов
     *
     * @return true Если несоответствия не обнаружены
     */
    private boolean checkAtoms () {
        boolean res = true;
        for (int i = 0; i < preds.size(); i++) {
            for (int j = 0; j < preds.size(); j++) {
                // Если есть ошибки в арностях предикатов
                if (preds.get(i).getName().equals(preds.get(j).getName())
                        && preds.get(i).getVars().size() != preds.get(j)
                        .getVars().size()) {
                    System.out.println("Несоответствие арностей предикатов: "
                            + preds.get(i) + " and " + preds.get(j));
                    res = false;
                }
            }
        }
        return res;
    }

    /**
     * Поиск второго элемента пары указанного оношения подобия
     *
     * @param s   Первый элемент
     * @param sim Отношение подобия
     * @return Второй элемент
     */
    private String findSim (String s, String sim) {
        for (int i = 0; i < sims.size(); i++) {
            if (sims.get(i).getName().equals(sim)
                    && sims.get(i).getVars().get(0).equals(s)) {
                return sims.get(i).getVars().get(1);
            }
        }
        return null;
    }

    /**
     * Создание предиката, связанного отношением подобия с данным
     *
     * @param pred Предикат
     * @param sim  Отношение подобия
     * @return Созданный предикат
     */
    private Atom createSim (Atom pred, String sim) {
        ArrayList<String> vars = new ArrayList<String>();
        for (int i = 0; i < pred.getVars().size(); i++) {
            vars.add(findSim(pred.getVars().get(i), sim));
        }
        return new Atom(findSim(pred.getName(), sim), vars);
    }

    /**
     * Проверка наличия предиката подобного данному (с учетом всех элементов)
     *
     * @param pred Предикат
     * @param sim  Отношение подобия
     * @return true Если подобный предикат существует
     */
    private boolean checkSim (Atom pred, String sim) {
        // Ищем предикат подобный данному
        String n = findSim(pred.getName(), sim);
        if (n == null)
            return false;
        for (int i = 0; i < pred.getVars().size(); i++) {
            if (Character.isUpperCase(pred.getVars().get(i).charAt(0)))
                continue;
            // Если текущая переменная предиката является константой
            // ищем подобную ей
            String n1 = findSim(pred.getVars().get(i), sim);
            if (n1 == null)
                return false;

        }
        return true;
    }

    /**
     * Рекурсивная функция, осуществляющая построение и обход дерева вывода
     *
     * @param pred Текущий узел
     * @param sim  Текущее используемое отношение подобия ("" если аналогия пока
     *             не используется)
     * @return Множество пар значений переменных полученных на данном этапе
     */
    private Result nextStep (Atom pred, String sim) {
        Result res = new Result();
        //System.out.print("Next step - atom = " + pred.toString() + " sims = " + sim.toString());
        if (done.contains(pred)) {
            //System.out.println(" -----");
            return res;
        }
        //System.out.println(" +++++");

        // добавляем текущий предикат в список просмотренных
        done.add(pred);
        // Если есть текущее отношение подобия и у данного предиката нет предикат
        // связанный с ним отношением подобия возвращяем пустой результат
        if (!sim.equals("") && !checkSim(pred, sim)) {
            return res;
        }
        // получаем список унифицированных предикат
        ArrayList<Atom> mismatches = this.unifyBase(pred);
    /*	System.out.println("Atom = " + pred.toString() + "Sim = " +
				sim.toString() + "Mistmatches size = " + mismatches.size());
		for(Atom a : mismatches){
			System.out.println(a.toString());

		}*/
        // анализируем предикаты с таким же именем как и у текущего
        for (int i = 0; i < mismatches.size(); i++) {
            ArrayList<Pair> line = new ArrayList<Pair>();
            // если для текущего предиката нет связанных отношением подобия предикат
            if (!sim.equals("") && !checkSim(mismatches.get(i), sim)) {
                continue;
            }
            // если список предикат не содержит предиката связанного с ним ОП
            // пропускаем итерацию
            if (!sim.equals("")
                    && !preds.contains(createSim(mismatches.get(i), sim))) {
                continue;
            }
            // просматриваем переменные предиката
            for (int j = 0; j < pred.getVars().size(); j++) {
                String s = "";
                s = mismatches.get(i).getVars().get(j);
                if (!sim.equals("")) {
                    // ищем переменную связаную с текущей ОП
                    s = findSim(s, sim);
                }
                Pair pair = new Pair(pred.getVars().get(j), s);
                if (!line.contains(pair)) {
                    // добавляем новую пару в список пар
                    line.add(pair);
                }
            }
            res.addPairList(line);
        }
        for (int i = 0; i < rules.size(); i++) {
            // для каждого правила проводим унификацию
            ArrayList<Atom> next = this.unifyRule(pred, rules.get(i));
			/*System.out.println("Atom = " + pred.toString() + "Rule = " +
					rules.get(i).toString() + "Rules size = " + next.size());
			for(Atom a : next){
				System.out.println(a.toString());

			}*/
            Result nextRes = new Result();
            Result oneRes = new Result();
            for (int j = 0; j < next.size(); j++) {
                // рекурсивно запускаем анализ нового предиката
                oneRes = nextStep(next.get(j), sim);
                if (oneRes.isEmpty()) {
                    nextRes.clear();
                    break;
                }
                // объединяем результаты
                nextRes = nextRes.link(oneRes);
            }
            nextRes.checkPairToAtom(pred);
            res.addAll(nextRes);
        }
        // если в текущий момент отношение подобие не используется
        // то ищем предикаты, связанные с текщим отношением подобия
        // и рекурсивно вызываем функцию его анализа
        if (sim.equals("")) {
            ArrayList<SimPair> simList = this.findSimsAtoms(pred);
            Result nextSim = new Result();
            for (int j = 0; j < simList.size(); j++) {
                nextSim = nextStep(simList.get(j).getPred(), simList.get(j)
                        .getSim());
                nextSim.returnSims(sims, vars);
                res.addAll(nextSim);
            }
        }
        res.recheck();
		/*System.out.println("Результат анализа предиката " + pred.toString() +
				"с отношением подобия s = " + sim.toString() + "\n"
				+ res.getResult() + "\n");
		*/
        return res;
    }

    /**
     * Формирование графа соответствия переменных и констант для двух предикатов
     *
     * @param pred1 Первый предикат
     * @param pred2 Второй предикат
     * @return Сформированный граф
     */
    private Graph createGraph (Atom pred1, Atom pred2) {
        Graph g = new Graph();
        // Добавляем все переменные как вершины графа
        g.addAll(pred1.getVars());
        g.addAll(pred2.getVars());
        // Между связаными вершинами строим дугу
        for (int i = 0; i < pred1.getVars().size(); i++) {
            g.addArc(pred1.getVars().get(i), pred2.getVars().get(i));
        }
        return g;
    }

    /**
     * Унификация предиката по базе (поиск несоответствий)
     *
     * @param pred Предикат
     * @return Множество предикатов, полученных в результате унификации
     */
    private ArrayList<Atom> unifyBase (Atom pred) {
        ArrayList<Atom> list = new ArrayList<Atom>();
        for (int i = 0; i < preds.size(); i++) {
            Atom pr = preds.get(i);
            // Пропускаем итерацию если текущий предикат не совпадает с заданным
            if (!pred.getName().equals(pr.getName())) {
                continue;
            }
            // строим граф где вершинами будут параметры двух предикатов
            // связываем вершины
            // и формируем компоненты связнасти
            ArrayList<ArrayList<String>> groupList = createGraph(pred, pr)
                    .formGroups();
            // создаем копию переменных предиката
            ArrayList<String> vList = new ArrayList<String>(pred.getVars());
            ArrayList<Pair> replList = new ArrayList<Pair>();
            boolean unifFlag = false;
            // просматриваем список компонент связности
            for (int j = 0; j < groupList.size(); j++) {
                ArrayList<String> conl = new ArrayList<String>();
                ArrayList<String> varl = new ArrayList<String>();
                // Ищем пременные и константы в компоненте связности
                for (int k = 0; k < groupList.get(j).size(); k++) {
                    if (Character
                            .isLowerCase(groupList.get(j).get(k).charAt(0))) {
                        conl.add(groupList.get(j).get(k));
                    } else {
                        varl.add(groupList.get(j).get(k));
                    }
                }
                // Если констант больше одной
                if (conl.size() > 1) {
                    unifFlag = true;
                }
                // Если одна константа
                if (conl.size() == 1) {
                    for (int ii = 0; ii < varl.size(); ii++) {
                        // создаем множество пар где первым элементом будет переменная
                        // а вторая константа
                        replList.add(new Pair(varl.get(ii), conl.get(0)));
                    }
                }
                // Если нет констант
                if (conl.size() == 0) {
                    for (int ii = 1; ii < varl.size(); ii++) {
                        replList.add(new Pair(varl.get(ii), varl.get(0)));
                    }
                }
            }
            if (unifFlag)
                continue;
            // просатриваем список переменных констант
            for (int k = 0; k < vList.size(); k++) {
                for (int ii = 0; ii < replList.size(); ii++) {
                    // если текущая переменная совпадает с переменной в созданном списке
                    // то заменяем переменную на константу
                    if (vList.get(k).equals(replList.get(ii).getFrom())) {
                        vList.set(k, replList.get(ii).getTo());
                    }
                }
            }
            // записываем в список инифицированный предикат
            list.add(new Atom(pred.getName(), vList));
        }
        return list;
    }

    /**
     * Унификация предиката по правилу
     *
     * @param pred Предикат
     * @param rule Правило
     * @return Множество предикатов, полученных в результате унификации
     */
    private ArrayList<Atom> unifyRule (Atom pred, Rule rule) {
        ArrayList<Atom> list = new ArrayList<Atom>();
        Atom pr = rule.getResult();

        // Если наименования предикатов не совпадают возвращаем пустой список
        if (!pred.getName().equals(pr.getName())) {
            return list;
        }
        // Создаем граф и формируем множество компонент связности
        ArrayList<ArrayList<String>> groupList = createGraph(pred, pr)
                .formGroups();
        ArrayList<Pair> replList = new ArrayList<Pair>();
        boolean unifFlag = false;
        for (int j = 0; j < groupList.size(); j++) {
            // Заполням константы и параметры
            ArrayList<String> conl = new ArrayList<String>();
            ArrayList<String> varl = new ArrayList<String>();
            for (int k = 0; k < groupList.get(j).size(); k++) {
                if (Character.isLowerCase(groupList.get(j).get(k).charAt(0))) {
                    conl.add(groupList.get(j).get(k));
                } else {
                    varl.add(groupList.get(j).get(k));
                }
            }
            if (conl.size() > 1) {
                unifFlag = true;
            }
            if (conl.size() == 1) {
                for (int ii = 0; ii < varl.size(); ii++) {
                    replList.add(new Pair(varl.get(ii), conl.get(0)));
                }
            }
            if (conl.size() == 0) {
                for (int ii = 1; ii < varl.size(); ii++) {
                    replList.add(new Pair(varl.get(ii), varl.get(0)));
                }
            }
        }
        if (unifFlag)
            return list;
        // просматриваем все предикаты правила
        for (int jj = 0; jj < rule.getSources().size(); jj++) {
            Atom pr1 = rule.getSources().get(jj);
            ArrayList<String> vList = new ArrayList<String>(pr1.getVars());
            for (int k = 0; k < vList.size(); k++) {
                for (int ii = 0; ii < replList.size(); ii++) {
                    if (vList.get(k).equals(replList.get(ii).getFrom())) {
                        vList.set(k, replList.get(ii).getTo());
                    }
                }
            }
            list.add(new Atom(pr1.getName(), vList));
        }
        return list;
    }

    /**
     * Поиск предикатов, подобных указанному
     *
     * @param pred Предикат
     * @return Множество подобных предикатов
     */
    private ArrayList<SimPair> findSimsAtoms (Atom pred) {
        ArrayList<SimPair> list = new ArrayList<SimPair>();
        for (int i = 0; i < sims.size(); i++) {
            // Если начало переменной в отношении подобия соответствует
            // названию предиката
            if (sims.get(i).getVars().get(0).equals(pred.getName())) {
                ArrayList<String> vars = new ArrayList<String>();
                boolean flag = false;
                for (int j = 0; j < pred.getVars().size(); j++) {
                    // Ищем переменную, связаную текущим отношением подобия
                    // с текущим параметром предиката
                    String var = this.findSim(pred.getVars().get(j), sims
                            .get(i).getName());
                    if (var == null) {
                        flag = true;
                        break;
                    }
                    vars.add(var);
                }
                // Если хотя бы для
                if (flag)
                    continue;
                list.add(new SimPair(new Atom(sims.get(i).getVars().get(1),
                        vars), sims.get(i).getName()));
            }
        }
        return list;
    }

    /**
     * Применение правила коммутативности для связок отношений подобия
     */
    private void reverseSims () {
        ArrayList<Relation> list = new ArrayList<Relation>();
        for (int i = 0; i < sims.size(); i++) {
            Relation sim = sims.get(i);
            ArrayList<String> list1 = new ArrayList<String>();
            for (int j = 0; j < sim.getVars().size(); j++) {
                list1.add(sim.getVars().get(sim.getVars().size() - j - 1));
            }
            list.add(new Relation(sim.getName(), list1));
        }
        // обавляем новый список отношений с измененным порядком следования параметров
        sims.addAll(list);
    }

    /**
     * Создание пар подобия для логических переменных
     */
    private void createVarSims () {
        // Создаем список названий отношений
        ArrayList<String> simNames = new ArrayList<String>();
        for (int i = 0; i < sims.size(); i++) {
            if (!simNames.contains(sims.get(i).getName())) {
                simNames.add(sims.get(i).getName());
            }
        }
        for (int i = 0; i < simNames.size(); i++) {
            for (int j = 0; j < vars.size(); j++) {
                ArrayList<String> vlist = new ArrayList<String>();
                vlist.add(vars.get(j));
                vlist.add(vars.get(j) + simNames.get(i));
                // Добавляем новое отношение, к примеру s(W, Ws)
                sims.add(new Relation(simNames.get(i), vlist));
            }
        }
    }
}
