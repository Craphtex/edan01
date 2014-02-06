package lab2.ex5;

import java.util.ArrayList;

import org.jacop.constraints.Diff2;
import org.jacop.constraints.Linear;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.PrintOutListener;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;
import org.jacop.search.SimpleSelect;
import org.jacop.search.SmallestDomain;

public class ARF {

	private static final int NBR_ADD = 13;
	private static final int NBR_MUL = 1;
	private static final int TIME_ADD = 1;
	private static final int TIME_MUL = 2;

	public static void main(String[] args) {
		new ARF();

	}

	public ARF() {
		Store store = new Store();

		ArrayList<IntVar> all = new ArrayList<IntVar>();

		Add a = new Add(store, all, "A");
		Add b = new Add(store, all, "B");
		Mul c = new Mul(store, all, "C", a, b);
		Add d = new Add(store, all, "D", c, a);

		store.impose(new Diff2(Add.getRectangles()));
		store.impose(new Diff2(Mul.getRectangles()));
		
		
		Search<IntVar> label = new DepthFirstSearch<IntVar>();
		SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(
				all.toArray(new IntVar[0]), new SmallestDomain<IntVar>(),
				new IndomainMin<IntVar>());
		label.setSolutionListener(new PrintOutListener<IntVar>());
		boolean Result = label.labeling(store, select, d.ends);

		if (Result) {
			System.out.println("\n*** Yes");
			System.out.println("Solution : " + java.util.Arrays.asList(all));
		} else
			System.out.println("\n*** No");

	}

	private static abstract class Operand {

		protected Store store;
		
		protected IntVar starts;
		protected IntVar ends;
		
		public Operand(Store store, ArrayList<IntVar> all, String name,
				Operand... parents) {
			this.store = store;
			starts = new IntVar(store, name + "-start", 0, 1000);
			ends = new IntVar(store, name + "-end", 0, 1000);
			all.add(starts);
			all.add(ends);
			addRectangle();
			store.impose(new Linear(store, new IntVar[] { starts, ends },
					new int[] { -1, 1 }, "=", cost()));
			for (Operand p : parents)
				store.impose(new Linear(store, new IntVar[] { starts, p.ends },
						new int[] { 1, -1 }, ">=", 0));

		}

		protected abstract int cost();

		protected abstract void addRectangle();
	}

	private static class Add extends Operand {

		private static Rectangles rectangles;

		public Add(Store store, ArrayList<IntVar> all, String name,
				Operand... parents) {
			super(store, all, name, parents);
		}

		@Override
		protected int cost() {
			return TIME_ADD;
		}

		@Override
		protected void addRectangle() {
			if (rectangles == null)
				rectangles = new Rectangles(store);
			rectangles.addRectangle(starts, TIME_ADD, NBR_ADD);
		}

		protected static  IntVar[][] getRectangles() {
			return rectangles.getRectangles();
		}

	}

	private static class Mul extends Operand {

		private static Rectangles rectangles;

		public Mul(Store store, ArrayList<IntVar> all, String name,
				Operand... parents) {
			super(store, all, name, parents);
		}

		@Override
		protected int cost() {
			return TIME_MUL;
		}

		@Override
		protected void addRectangle() {
			if (rectangles == null)
				rectangles = new Rectangles(store);
			rectangles.addRectangle(starts, TIME_MUL, NBR_MUL);
		}

		protected static IntVar[][] getRectangles() {
			return rectangles.getRectangles();
		}

	}

	private static class Rectangles {
		private Store store;
		private ArrayList<IntVar[]> rectangles;
		
		
		public Rectangles(Store store) {
			this.store = store;
			rectangles = new ArrayList<IntVar[]>();
		}

		public void addRectangle(IntVar starts, int length, int recources) {
			IntVar y = new IntVar(store,0,recources-1);
			IntVar dy = new IntVar(store,1,1);
			IntVar dx = new IntVar(store,length,length);
			IntVar[] rect = new IntVar[]{starts,y,dx,dy};
			rectangles.add(rect);
		}
		
		public IntVar[][] getRectangles() {
			return rectangles.toArray(new IntVar[0][0]);
		}
	}
}
