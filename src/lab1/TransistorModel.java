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
		
		vdd = new IntVar(store,"vdd",1,1);
		gnd = new IntVar(store,"gnd",0,0);
		
		IntVar A = new IntVar(store, "A", 0, 1);
		IntVar B = new IntVar(store, "B", 0, 1);
		IntVar C = new IntVar(store, "C", 0, 1);
		IntVar Coi = new IntVar(store, "Coi", 0, 1);
		IntVar Si = new IntVar(store, "Si", 0, 1);

		IntVar T3 = new IntVar(store, "T3", 0, 1);
		IntVar S = new IntVar(store, "S", 0, 1);
		IntVar Carry = new IntVar(store, "Carry", 0, 1);
		IntVar[] v = new IntVar[] { In1, In2, C, T1, T2, T3, S, Carry };

		Constraint x1 = new XorBool(new IntVar[] { In1, In2 }, T1);
		Constraint x2 = new XorBool(new IntVar[] { T1, C }, S);
		Constraint a1 = new AndBool(new IntVar[] { In1, In2 }, T2);
		Constraint a2 = new AndBool(new IntVar[] { T1, C }, T3);
		Constraint o1 = new OrBool(new IntVar[] { T2, T3 }, Carry);

		store.impose(x1);
		store.impose(x2);
		store.impose(a1);
		store.impose(a2);
		store.impose(o1);

		// search for a solution and print results
		Search<IntVar> search = new DepthFirstSearch<IntVar>();
		search.setSolutionListener(new PrintOutListener<IntVar>());
		SelectChoicePoint<IntVar> select = new InputOrderSelect<IntVar>(store,
				v, new IndomainMin<IntVar>());
		search.getSolutionListener().searchAll(true);
		search.getSolutionListener().recordSolutions(true);
		boolean result = search.labeling(store, select);

		if (result) {
			System.out.println("In1 In2 C   T1  T2  T3  S   Carry");
			for (int i = 1; i <= search.getSolutionListener().solutionsNo(); i++) {
				for (int j = 0; j < search.getSolution(i).length; j++)
					System.out.print(search.getSolution(i)[j] + "   ");
				System.out.println();
			}

		} else
			System.out.println("No solution");
	}

	public static Constraint ntrans(Store store, IntVar x, IntVar y, IntVar b) {
		return new IfThen(new XeqY(b, gnd), new XeqY(x,
				y));
	}

	public static Constraint ptrans(Store store, IntVar x, IntVar y, IntVar b) {
		return new IfThen(new XeqY(b, vdd), new XeqY(x,
				y));
	}
	
	public static Constraint inv(Store store, IntVar in, IntVar out) {
		
		
		return null;
	}

}
