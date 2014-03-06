package lab2.ex3;

import java.util.ArrayList;

import org.jacop.constraints.Diff2;
import org.jacop.constraints.Linear;
import org.jacop.constraints.Max;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.InputOrderSelect;
import org.jacop.search.PrintOutListener;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;
import org.jacop.search.SimpleSelect;
import org.jacop.search.SmallestDomain;

public class JobshopScheduling {

	private static int BIG_NUMBER = 218;
	private static int[][] data = new int[][] {
			{ 2, 1, 0, 3, 1, 6, 3, 7, 5, 3, 4, 6 },
			{ 1, 8, 2, 5, 4, 10, 5, 10, 0, 10, 3, 4 },
			{ 2, 5, 3, 4, 5, 8, 0, 9, 1, 1, 4, 7 },
			{ 1, 5, 0, 5, 2, 5, 3, 3, 4, 8, 5, 9 },
			{ 2, 9, 1, 3, 4, 5, 5, 4, 0, 3, 3, 1 },
			{ 1, 3, 3, 3, 5, 9, 0, 10, 4, 4, 2, 1 } };
	private static Store store;
	private static ArrayList<IntVar> vars = new ArrayList<IntVar>();

	public static void main(String[] args) {
		long T1, T2, T;
		T1 = System.currentTimeMillis();

		jobshopScheduling();

		T2 = System.currentTimeMillis();
		T = T2 - T1;
		System.out.println("\n\t*** Execution time = " + T + " ms");
	}

	private static void jobshopScheduling() {
		store = new Store();

		Machine[] machines = new Machine[6];

		for (int i = 0; i < machines.length; i++) {
			machines[i] = new Machine(i);
		}
		Job[] jobs = new Job[data.length];

		for (int job = 0; job < jobs.length; job++) {
			jobs[job] = new Job(job);
			int[] row = data[job];

			for (int i = 0; i < row.length; i += 2) {
				Machine m = machines[row[i]];
				int duration = row[i + 1];
				jobs[job].addTask(m, duration, (i / 2) + 1);
			}
			jobs[job].setConstraints();
		}

		for (int i = 0; i < machines.length; i++) {
			machines[i].setConstraints();
		}

		IntVar[] v = vars.toArray(new IntVar[0]);

		IntVar[] lastTimes = new IntVar[jobs.length];
		for (int i = 0; i < jobs.length; i++) {
			lastTimes[i] = jobs[i].getLastEndTime();
		}
		
		IntVar lastJob = new IntVar(store,"lastJob",0,2500);
		
		store.impose(new Max(lastTimes,lastJob));
		
		// search for a solution and print results
		Search<IntVar> search = new DepthFirstSearch<IntVar>();
		
		SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(v, 
			    new SmallestDomain<IntVar>(), 
			    new IndomainMin<IntVar>()); 
		
		search.setSolutionListener(new PrintOutListener<IntVar>());
//		SelectChoicePoint<IntVar> select = new InputOrderSelect<IntVar>(store,
//				v, new IndomainMin<IntVar>());
		boolean result = search.labeling(store, select);

		if (result) {
			new Graph(machines);
			System.out.println("Solution:");
			for (int i = 0; i < jobs.length; i++) {
				System.out.println(jobs[i]);
			}
			for (int i = 0; i < machines.length; i++) {
				machines[i].printTasks();
			}
			
			System.out.printf("\n\nLast job ends at %d\n",lastJob.value());
		} else
			System.out.println("No solution");

	}

	static class Machine {
		int n;
		ArrayList<Task> tasks = new ArrayList<Task>();
		Rectangles rect = new Rectangles();
		
		
		public Machine(int n) {
			this.n = n;
		}

		public void setConstraints() {
			store.impose(new Diff2(rect.getRectangles()));
		}

		public void addTask(Task t,int duration) {
			tasks.add(t);
			rect.addRectangle(t.start, duration);
		}

		@Override
		public String toString() {
			return "M" + n;
		}
		
		public void printTasks() {
			System.out.println(toString() + tasks);
		}
	}

	private static class Job {

		ArrayList<Task> tasks = new ArrayList<Task>();
		int job;

		public Job(int job) {
			this.job = job+1;
		}

		public void addTask(Machine m, int duration, int n) {
			Task t = new Task(duration, n, m,this);
			tasks.add(t);
		}

		public void setConstraints() {
			for (int i = 1; i < tasks.size(); i++) {
				store.impose(new Linear(store, new IntVar[] {
						tasks.get(i).start, tasks.get(i - 1).end }, new int[] {
						1, -1 }, ">=", 0));
			}
		}
		
		public IntVar getLastEndTime() {
			return tasks.get(tasks.size()-1).end;
		}

		@Override
		public String toString() {
			return "Job" + job + ": " + tasks.toString();
		}

	}

	private static class Task {
		IntVar start;
		IntVar end;
		int n;
		Machine m;
		Job j;

		public Task(int duration, int n, Machine m,Job j) {
			this.m = m;
			this.n = n;
			this.j = j;
			start = new IntVar(store,"J"+j.job+" T"+n, 0, BIG_NUMBER);
			end = new IntVar(store, 0, BIG_NUMBER);
			vars.add(start);
			vars.add(end);
			m.addTask(this,duration);
			store.impose(new Linear(store, new IntVar[] { start, end },
					new int[] { -1, 1 }, "=", duration));
		}

		@Override
		public String toString() {
			return "Task" + n + " (" + start.value() + "," + end.value() + ")["
					+ m + ",J"+j.job+"]";
		}
	}
	
	static class Rectangles {
		private ArrayList<IntVar[]> rectangles;

		public Rectangles() {
			rectangles = new ArrayList<IntVar[]>();
		}

		public void addRectangle(IntVar starts, int length) {
			IntVar y = new IntVar(store, 1, 1);
			IntVar dy = new IntVar(store, 1, 1);
			IntVar dx = new IntVar(store, length, length);
			IntVar[] rect = new IntVar[] { starts, y, dx, dy };
			rectangles.add(rect);
		}

		public IntVar[][] getRectangles() {
			return rectangles.toArray(new IntVar[0][0]);
		}
	}

}
