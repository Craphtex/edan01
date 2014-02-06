package lab2.ex1;

import org.jacop.constraints.Constraint;
import org.jacop.constraints.Sum;
import org.jacop.core.IntVar;
import org.jacop.core.Store;

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

		week = new IntVar[] { Mo, Tu, We, Th, Fr, Sa, Su };

		IntVar sumMoFull = new IntVar(store, 0, 100);
		store.impose(new Sum(new IntVar[] { Mo, Su, Sa, Fr, Th }, sumMoFull));

		IntVar cost = new IntVar(store, "cost", 0, 100);
		store.impose(new Sum(week, cost));

	}

	private Constraint worker(int start, int length) {
		IntVar[] temp = new IntVar[length];
		for (int i = 0; i < length; i++) {
			temp[i] = week[(start - i) % week.length];
		}
		IntVar sum = new IntVar(store, 0, 100);
		return new Sum(temp, sum);

	}
}
