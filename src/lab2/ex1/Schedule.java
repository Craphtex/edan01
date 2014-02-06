package lab2.ex1;

import org.jacop.constraints.Constraint;
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

public class Schedule {
    private Store store;
    private IntVar[] week;

    private static final int FULL_TIME_COST = 100;
    private static final int PART_TIME_COST = 150;

    private static final int FULL_TIME_DURATION = 5;
    private static final int PART_TIME_DURATION = 2;

    public static void main(String[] args) {
        long T1, T2, T;
        T1 = System.currentTimeMillis();

        new Schedule();

        T2 = System.currentTimeMillis();
        T = T2 - T1;
        System.out.println("\n\t*** Execution time = " + T + " ms");
    }

    public Schedule() {
        store = new Store();

        // Variables for number of workers starting on each day of the week.
        IntVar Monday = new IntVar(store, "Monday", 0, 30);
        IntVar Tuesday = new IntVar(store, "Tuesday", 0, 30);
        IntVar Wednesday = new IntVar(store, "Wednesday", 0, 30);
        IntVar Thursday = new IntVar(store, "Thursday", 0, 30);
        IntVar Friday = new IntVar(store, "Friday", 0, 30);
        IntVar Saturday = new IntVar(store, "Saturday", 0, 30);
        IntVar Sunday = new IntVar(store, "Sunday", 0, 30);

        // Creating a vector representing all the workers for a week.
        week = new IntVar[]{Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday};

        // Putting up workers constraints
        int[] requiredAmountOfWorkers = new int[]{5, 7, 7, 10, 16, 18, 12};
        int[] costs = new int[week.length + week.length];
        IntVar[] workers = new IntVar[week.length + week.length];
        for (int weekday = 0; weekday < week.length; weekday++) {
            // For full time workers
            IntVar fullTimers = new IntVar(store, "Full timer: " + weekday, 0, 100);
            store.impose(numberOfWorkers(fullTimers, weekday, FULL_TIME_DURATION));
            workers[2 * weekday] = fullTimers;
            costs[2 * weekday] = FULL_TIME_COST;

            // For part time workers
            IntVar partTimers = new IntVar(store, "Part timer: " + weekday, 0, 100);
            store.impose(numberOfWorkers(partTimers, weekday, PART_TIME_DURATION));
            workers[2 * weekday + 1] = partTimers;
            costs[2 * weekday + 1] = PART_TIME_COST;

            // Apply constraints
            IntVar sum = new IntVar(store, requiredAmountOfWorkers[weekday], 100);
            store.impose(new Sum(new IntVar[]{fullTimers, partTimers}, sum));
        }

        // Calculating total cost
        IntVar cost = new IntVar(store, "Cost", 0, 10000);
        store.impose(new SumWeight(workers, costs, cost));

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
            System.out.println("Cost : " + cost);

        } else
            System.out.println("\n*** No");

    }

    private Constraint numberOfWorkers(IntVar var, int weekday, int duration) {
        IntVar[] temp = new IntVar[duration];
        System.out.print(weekday + ":");
        for (int i = 0; i < duration; i++) {
            temp[i] = week[(weekday - i + week.length) % week.length];
            System.out.print(" " + (weekday - i + week.length) % week.length);
        }
        System.out.println();
        return new Sum(temp, var);
    }
}
