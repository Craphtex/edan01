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

	private static final int NBR_ADD = 1;
	private static final int NBR_MUL = 1;
	private static final int TIME_ADD = 1;
	private static final int TIME_MUL = 2;

	public static void main(String[] args) {
		new ARF();

	}

	public ARF() {
		Store store = new Store();

		ArrayList<IntVar> all = new ArrayList<IntVar>();

		Operand a = new Mul(store, all, "*2");
		Operand b = new Mul(store, all, "*3");
		Operand c = new Mul(store, all, "*4");
		Operand d = new Mul(store, all, "*5");
		Operand e = new Add(store, all, "+9", a, b);
		Operand f = new Add(store, all, "+10", c, d);
		Operand g = new Add(store, all, "+12", e);
		Operand h = new Add(store, all, "+13", f);
		Operand i = new Mul(store, all, "*14", g);
		Operand j = new Mul(store, all, "*15", h);
		Operand k = new Mul(store, all, "*16", g);
		Operand l = new Mul(store, all, "*17", h);
		Operand m = new Add(store, all, "+18", i, j);
		Operand n = new Add(store, all, "+19", k, l);
		Operand o = new Mul(store, all, "*6");
		Operand p = new Mul(store, all, "*7");
		Operand q = new Mul(store, all, "*22", m);
		Operand r = new Mul(store, all, "*20", m);
		Operand s = new Mul(store, all, "*23", n);
		Operand t = new Mul(store, all, "*21", n);
		Operand u = new Mul(store, all, "*1");
		Operand v = new Mul(store, all, "*0");
		Operand w = new Add(store, all, "+11", o, p);
		Operand x = new Add(store, all, "+25", q, s);
		Operand y = new Add(store, all, "+24", r, t);
		Operand z = new Add(store, all, "+8", u, v);

		Operand ans1 = new Add(store, all, "+27", w, x);
		Operand ans2 = new Add(store, all, "+26", y, z);

		Operand res = new Result(store, all, "Result", ans1, ans2);

		store.impose(new Diff2(Add.getRectangles()));
		store.impose(new Diff2(Mul.getRectangles()));

		Search<IntVar> label = new DepthFirstSearch<IntVar>();
		SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(
				all.toArray(new IntVar[0]), new SmallestDomain<IntVar>(),
				new IndomainMin<IntVar>());
		label.setSolutionListener(new PrintOutListener<IntVar>());
		boolean Result = label.labeling(store, select, res.ends);

		if (Result) {
			System.out.println("\n*** Yes");
			System.out.println("Solution : " + java.util.Arrays.asList(all));
			System.out.println(ans1);
			System.out.println(ans2);
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

		@Override
		public String toString() {
			return "[" + starts.toString() + "," + ends.toString() + "]";
		}

		protected abstract int cost();

		protected abstract void addRectangle();
	}

	private static class Result extends Operand {

		public Result(Store store, ArrayList<IntVar> all, String name,
				Operand... parents) {
			super(store, all, name, parents);
		}

		@Override
		protected int cost() {
			return 0;
		}

		@Override
		protected void addRectangle() {
			// Does nothing
		}

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

		protected static IntVar[][] getRectangles() {
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
			IntVar y = new IntVar(store, 0, recources - 1);
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
