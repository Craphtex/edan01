package lab1;

import org.jacop.constraints.*;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.*;
import org.jacop.set.constraints.XeqA;

public class TransistorModel {

	static IntVar vdd;
	static IntVar gnd;

	public static void main(String[] args) {
		// Something to start from
		Store store = new Store(); // define FD store
		// define finite domain variables

		vdd = new IntVar(store, "vdd", 1, 1);
		gnd = new IntVar(store, "gnd", 0, 0);

		IntVar a = new IntVar(store, "A", 0, 1);
		IntVar b = new IntVar(store, "B", 0, 1);
		IntVar c = new IntVar(store, "C", 0, 1);
		IntVar coi = new IntVar(store, "Coi", 0, 1);
		IntVar si = new IntVar(store, "Si", 0, 1);
		IntVar co = new IntVar(store, "co", 0, 1);
		IntVar s = new IntVar(store, "si", 0, 1);
		
		IntVar[] v = new IntVar[] { a, b, c,co,s};

		inv(store, coi,co);
		inv(store,si,s);
		carryModule(store, a, b, c, coi);
		sumModule(store, a, b, c, coi, si);

		Search<IntVar> search = new DepthFirstSearch<IntVar>();
		search.setSolutionListener(new PrintOutListener<IntVar>());
		SelectChoicePoint<IntVar> select = new InputOrderSelect<IntVar>(store,
				v, new IndomainMin<IntVar>());
		search.getSolutionListener().searchAll(true);
		search.getSolutionListener().recordSolutions(true);
		boolean result = search.labeling(store, select);

		if (result) {
			System.out.println("A    B    Cin  Cout Sum");
			for (int i = 1; i <= search.getSolutionListener().solutionsNo(); i++) {
				for (int j = 0; j < search.getSolution(i).length; j++)
					System.out.print(search.getSolution(i)[j] + "    ");
				System.out.println();
			}

		} else
			System.out.println("No solution");
	}

	public static Constraint ntrans(Store store, IntVar x, IntVar y, IntVar b) {
		return new IfThen(new XeqY(b, gnd), new XeqY(x, y));
	}

	public static Constraint ptrans(Store store, IntVar x, IntVar y, IntVar b) {
		return new IfThen(new XeqY(b, vdd), new XeqY(x, y));
	}

	public static void inv(Store store, IntVar in, IntVar out) {
		store.impose(ntrans(store, vdd, out, in));
		store.impose(ptrans(store, out, gnd, in));

	}

	public static void carryModule(Store store, IntVar a, IntVar b, IntVar c,
			IntVar coi) {
		IntVar t1 = new IntVar(store,"",0,1);
		IntVar t2 = new IntVar(store,"",0,1);
		IntVar t3 = new IntVar(store,"",0,1);
		IntVar t4 = new IntVar(store,"",0,1);

		store.impose(ntrans(store,vdd,t1,a));
		store.impose(ntrans(store,vdd,t1,b));
		store.impose(ntrans(store,vdd,t3,b));
		store.impose(ntrans(store,t1,coi,c));
		store.impose(ntrans(store,t3,coi,a));
		
		store.impose(ptrans(store,coi,t2,c));
		store.impose(ptrans(store,coi,t4,a));
		store.impose(ptrans(store,t2,gnd,a));
		store.impose(ptrans(store,t2,gnd,b));
		store.impose(ptrans(store,t4,gnd,b));
		
	}

	public static void sumModule(Store store, IntVar a, IntVar b,IntVar c, IntVar coi,
			IntVar si) {
		IntVar t1 = new IntVar(store,"",0,1);
		IntVar t2 = new IntVar(store,"",0,1);
		IntVar t3 = new IntVar(store,"",0,1);
		IntVar t4 = new IntVar(store,"",0,1);
		IntVar t5 = new IntVar(store,"",0,1);
		IntVar t6 = new IntVar(store,"",0,1);
		
		store.impose(ntrans(store,vdd,t1,a));
		store.impose(ntrans(store,vdd,t1,b));
		store.impose(ntrans(store,vdd,t1,c));
		store.impose(ntrans(store,vdd,t3,a));
		store.impose(ntrans(store,t3,t4,b));
		store.impose(ntrans(store,t4,si,c));
		store.impose(ntrans(store,t1,si,coi));
		
		
		store.impose(ptrans(store,si,t2,coi));
		store.impose(ptrans(store,t2,gnd,a));
		store.impose(ptrans(store,t2,gnd,b));
		store.impose(ptrans(store,t2,gnd,c));
		store.impose(ptrans(store,si,t5,c));
		store.impose(ptrans(store,t5,t6,b));
		store.impose(ptrans(store,t6,gnd,a));
		
		
	}

}
