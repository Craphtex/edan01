package lab2.ex5;

import org.jacop.constraints.Linear;
import org.jacop.core.IntVar;
import org.jacop.core.Store;

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
		
		
		Add a = new Add(store);
		Add b = new Add(store);
		Mul c = new Mul(store,a,b);
		Add d = new Add(store,c,a);
		
		
		
		
	}
	
	
	
	private static abstract class Operand{
		protected Operand[] parents;
		
		protected Store store;
		protected IntVar starts;
		protected IntVar ends;
		
		public Operand(Store store, Operand... parents) {
			this.store = store;
			this.parents = parents;
			starts = new IntVar(store,0,1000);
			ends = new IntVar(store,0,1000);
			store.impose(new Linear(store, new IntVar[]{starts,ends}, new int[]{-1,1}, "=", cost()));
			for (Operand p : parents) {
				store.impose(new Linear(store, new IntVar[]{starts,p.ends}, new int[]{1,-1}, ">=", 0));
			}
		}
		
		protected abstract int cost();
	}
	
	private static class Add extends Operand {

		public Add(Store store, Operand... parents) {
			super(store, parents);
		}

		@Override
		protected int cost() {
			return TIME_ADD;
		}
	}
	
	private static class Mul extends Operand {

		public Mul(Store store, Operand... parents) {
			super(store, parents);
		}

		@Override
		protected int cost() {
			return TIME_MUL;
		}
	}
	
	
}
