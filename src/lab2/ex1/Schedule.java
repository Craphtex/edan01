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
	static Store store;

	private static final int FULL_TIME_WORKER_COST_PER_DAY = 100;
	private static final int PART_TIME_WORKER_COST_PER_DAY = 150;
	private static final String[] weekdays = { "Monday     ", "Tuesday     ",
			"Wednesday", "Thursday", "Friday     ", "Saturday", "Sunday    " };
	private static final int[] DAILY_MINIMAL_WORKFORCE = new int[] { 5, 7, 7, 10, 16, 18, 12 };

	public static void main(String[] args) {
		long T1, T2, T;
		T1 = System.currentTimeMillis();

		schedule();

		T2 = System.currentTimeMillis();
		T = T2 - T1;
		System.out.println("\n\t*** Execution time = " + T + " ms");
	}

	static void schedule() {
		store = new Store();

		IntVar FullTimeMonday = new IntVar(store, "Heltid Måndag", 0, 300);
		IntVar FullTimeTuesday = new IntVar(store, "Heltid Tisdag", 0, 300);
		IntVar FullTimeWednesday = new IntVar(store, "Heltid Onsdag", 0, 300);
		IntVar FullTimeThursday = new IntVar(store, "Heltid Torsdag", 0, 300);
		IntVar FullTimeFriday = new IntVar(store, "Heltid Fredag", 0, 300);
		IntVar FullTimeSaturday = new IntVar(store, "Heltid Lördag", 0, 300);
		IntVar FullTimeSunday = new IntVar(store, "Heltid Söndag", 0, 300);

		IntVar FullTimeWorkers = new IntVar(store, "Alla heltidare", 0, 2100);
		IntVar[] FullTimeWorkerGroup = new IntVar[] { FullTimeMonday,
				FullTimeTuesday, FullTimeWednesday, FullTimeThursday,
				FullTimeFriday, FullTimeSaturday, FullTimeSunday };
		store.impose(new Sum(FullTimeWorkerGroup, FullTimeWorkers));

		IntVar PartTimeMonday = new IntVar(store, "Deltid Måndag", 0, 30);
		IntVar PartTimeTuesday = new IntVar(store, "Deltid Tisdag", 0, 30);
		IntVar PartTimeWednesday = new IntVar(store, "Deltid Onsdag", 0, 30);
		IntVar PartTimeThursday = new IntVar(store, "Deltid Torsdag", 0, 30);
		IntVar PartTimeFriday = new IntVar(store, "Deltid Fredag", 0, 30);
		IntVar PartTimeSaturday = new IntVar(store, "Deltid Lördag", 0, 30);
		IntVar PartTimeSunday = new IntVar(store, "Deltid Söndag", 0, 30);

		IntVar PartTimeWorkers = new IntVar(store, "Alla deltidare", 0, 210);
		IntVar[] PartTimeWorkerGroup = new IntVar[] { PartTimeMonday,
				PartTimeTuesday, PartTimeWednesday, PartTimeThursday,
				PartTimeFriday, PartTimeSaturday, PartTimeSunday };
		store.impose(new Sum(PartTimeWorkerGroup, PartTimeWorkers));

		IntVar[] allWorkers = new IntVar[] { FullTimeMonday, FullTimeTuesday,
				FullTimeWednesday, FullTimeThursday, FullTimeFriday,
				FullTimeSaturday, FullTimeSunday, PartTimeMonday,
				PartTimeTuesday, PartTimeWednesday, PartTimeThursday,
				PartTimeFriday, PartTimeSaturday, PartTimeSunday };

		IntVar[] sumOfADays = new IntVar[7];


		for (int i = 0; i < 7; i++) {
			IntVar tempFull = new IntVar(store, 0, 100);
			IntVar tempPart = new IntVar(store, 0, 100);
			store.impose(numberOfWorkersOnDay(FullTimeWorkerGroup, tempFull, i,
					5));
			store.impose(numberOfWorkersOnDay(PartTimeWorkerGroup, tempPart, i,
					2));
			IntVar dayLabour = new IntVar(store, DAILY_MINIMAL_WORKFORCE[i], 100);
			sumOfADays[i] = dayLabour;
			store.impose(new Sum(new IntVar[] { tempFull, tempPart }, dayLabour));
		}

		IntVar cost = new IntVar(store, 0, 25000);
		store.impose(new SumWeight(new IntVar[] { FullTimeWorkers,
				PartTimeWorkers }, new int[] {
				5 * FULL_TIME_WORKER_COST_PER_DAY,
				2 * PART_TIME_WORKER_COST_PER_DAY }, cost));

		System.out.println("Number of variables: " + store.size()
				+ "\nNumber of constraints: " + store.numberConstraints());

		Search<IntVar> label = new DepthFirstSearch<IntVar>();
		SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(allWorkers,
				new SmallestDomain<IntVar>(), new IndomainMin<IntVar>());
		label.setSolutionListener(new PrintOutListener<IntVar>());
		boolean Result = label.labeling(store, select, cost);

		if (Result) {
			System.out.println("\n*** Solution found");

			System.out.println("\t\tFull\tPart\tWorkers on given Day");
			for (int i = 0; i < 7; i++) {
				System.out.printf("%s\t%d\t%d\t%d\n", weekdays[i],
						FullTimeWorkerGroup[i].value(),
						PartTimeWorkerGroup[i].value(), sumOfADays[i].value());
			}
			System.out.println("Cost: " + cost.value());

		} else
			System.out.println("\n*** No solution");
	}

	/**
	 * Creates a constraint that sums up a "duration" long list of week days
	 * 
	 * @param week
	 *            list of weekdays
	 * @param sum
	 *            the sum of workers working on "weekday"
	 * @param weekday
	 *            the day in focus
	 * @param duration
	 *            how many days back it shall look
	 * @return a constraint with the sum of the days equals the "sum" variable
	 */
	private static Constraint numberOfWorkersOnDay(IntVar[] week, IntVar sum,
			int weekday, int duration) {
		IntVar[] temp = new IntVar[duration];
		for (int i = 0; i < duration; i++) {
			temp[i] = week[(weekday - i + week.length) % week.length];
		}
		return new Sum(temp, sum);
	}
}
