package lab2.ex5;

import java.util.ArrayList;

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

	private static final int NBR_ADD = 2;
	private static final int NBR_MUL = 2;
	private static final int TIME_ADD = 1;
	private static final int TIME_MUL = 2;

	public static void main(String[] args) {
		new ARF();

	}

	public ARF() {
		Store store = new Store();
		
		ArrayList<IntVar> all = new ArrayList<IntVar>();
		
		Add a = new Add(store,all);
		Add b = new Add(store,all);
		Mul c = new Mul(store,all,a,b);
		Add d = new Add(store,all,c,a);
		
		
		
		Search<IntVar> label = new DepthFirstSearch<IntVar>(); 
		SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(all.toArray(new IntVar[0]), 
							    new SmallestDomain<IntVar>(), 
							    new IndomainMin<IntVar>()); 
		label.setSolutionListener(new PrintOutListener<IntVar>());
		boolean Result = label.labeling(store, select, d.ends);
			
		if (Result) {
		    System.out.println("\n*** Yes");
		    System.out.println("Solution : "+ java.util.Arrays.asList(all));
		}
		else System.out.println("\n*** No");
	    
	}


	private static abstract class Operand {
		protected Operand[] parents;

		protected Store store;
		protected IntVar starts;
		protected IntVar ends;

		public Operand(Store store, ArrayList<IntVar> all, Operand... parents) {
			this.store = store;
			this.parents = parents;
			starts = new IntVar(store, 0, 1000);
			ends = new IntVar(store, 0, 1000);
			all.add(starts);
			all.add(ends);
			store.impose(new Linear(store, new IntVar[] { starts, ends },
					new int[] { -1, 1 }, "=", cost()));
			for (Operand p : parents) {
				store.impose(new Linear(store, new IntVar[] { starts, p.ends },
						new int[] { 1, -1 }, ">=", 0));
			}
		}
		protected abstract int cost();
	}

	private static class Add extends Operand {

		public Add(Store store, ArrayList<IntVar> all, Operand... parents) {
			super(store,all, parents);
		}

		@Override
		protected int cost() {
			return TIME_ADD;
		}
	}

	private static class Mul extends Operand {

		public Mul(Store store, ArrayList<IntVar> all, Operand... parents) {
			super(store, all, parents);
		}

		@Override
		protected int cost() {
			return TIME_MUL;
		}
	}

}
