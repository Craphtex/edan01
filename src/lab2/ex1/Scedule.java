package lab2.ex1;

import org.jacop.constraints.Sum;
import org.jacop.constraints.SumWeight;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.PrintOutListener;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;
import org.jacop.search.SimpleSelect;
import org.jacop.search.SmallestDomain;

public class Scedule {
    private Store store;

    private IntVar[] week;

    public static void main(String[] args) {
        long T1, T2, T;
        T1 = System.currentTimeMillis();

        new Scedule();

        T2 = System.currentTimeMillis();
        T = T2 - T1;
        System.out.println("\n\t*** Execution time = " + T + " ms");
    }

    public Scedule() {
        store = new Store();

        IntVar Mo = new IntVar(store, "Monday", 0, 30);
        IntVar Tu = new IntVar(store, "Tuesday", 0, 30);
        IntVar We = new IntVar(store, "Wednesday", 0, 30);
        IntVar Th = new IntVar(store, "Thursday", 0, 30);
        IntVar Fr = new IntVar(store, "Friday", 0, 30);
        IntVar Sa = new IntVar(store, "Saturday", 0, 30);
        IntVar Su = new IntVar(store, "Sunday", 0, 30);

        week = new IntVar[]{Mo, Tu, We, Th, Fr, Sa, Su};

        IntVar sumMoFull = new IntVar(store, 0, 100);
        store.impose(new Sum(new IntVar[]{Mo, Su, Sa, Fr, Th}, sumMoFull));

        IntVar[] fullSum = new IntVar[7];
        for (int i = 0; i < week.length; i++) {
            fullSum[i] = worker(i, 5);
        }

        IntVar[] partSum = new IntVar[7];
        for (int i = 0; i < week.length; i++) {
            partSum[i] = worker(i, 2);
        }

        int[] workers = new int[]{17, 13, 15, 19, 14, 16, 11};
        for (int i = 0; i < week.length; i++) {
            IntVar sum = new IntVar(store, workers[i], 100);
            store.impose(new Sum(new IntVar[]{fullSum[i], partSum[i]}, sum));
        }

        IntVar cost = createCost(fullSum, partSum, 100, 150);

        System.out.println("Number of variables: " + store.size()
                + "\nNumber of constraints: " + store.numberConstraints());

        Search<IntVar> label = new DepthFirstSearch<IntVar>();
        SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(week,
                new SmallestDomain<IntVar>(), new IndomainMin<IntVar>());
        label.setSolutionListener(new PrintOutListener<IntVar>());
        boolean Result = label.labeling(store, select, cost);

        if (Result) {
            System.out.println("\n*** Yes");
            System.out.println("Solution : " + java.util.Arrays.asList(week));
            System.out.println("Solution : " + java.util.Arrays.asList(fullSum));
            System.out.println("Solution : " + java.util.Arrays.asList(partSum));
            System.out.println("Cost : " + cost);

        } else
            System.out.println("\n*** No");

    }

    private IntVar worker(int start, int length) {
        IntVar[] temp = new IntVar[length];
        for (int i = 0; i < length; i++) {
            temp[i] = week[(start - i + week.length) % week.length];
        }
        String name = length == 5 ? "Full" : "Part";
        String day = "";
        switch (start) {
            case 0:
                day = "Monday";
                break;
            case 1:
                day = "Tuesday";
                break;
            case 2:
                day = "Wednesday";
                break;
            case 3:
                day = "Thursday";
                break;
            case 4:
                day = "Friday";
                break;
            case 5:
                day = "Saturday";
                break;
            case 6:
                day = "Sunday";
                break;
        }
        IntVar sum = new IntVar(store, name + "-" + day, 0, 100);
        store.impose(new Sum(temp, sum));
        return sum;
    }

    private IntVar createCost(IntVar[] full, IntVar[] part, int fullCost, int partCost) {
        IntVar sum = new IntVar(store, "Cost", 0, 50000);
        int[] cost = new int[full.length + part.length];
        IntVar[] total = new IntVar[cost.length];
        for (int i = 0; i < full.length; i++) {
            total[i] = full[i];
            cost[i] = fullCost;
        }
        for (int i = 0; i < part.length; i++) {
            total[full.length + i] = part[i];
            cost[full.length + i] = partCost;
        }
        store.impose(new SumWeight(total, cost, sum));
        return sum;
    }
}
