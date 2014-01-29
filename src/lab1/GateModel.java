package lab1;

import org.jacop.constraints.And;
import org.jacop.constraints.AndBool;
import org.jacop.constraints.XeqY;
import org.jacop.constraints.XneqY;
import org.jacop.core.BooleanVar;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.*;

public class GateModel {
    public static void main(String[] args) {
        // Something to start from
        Store store = new Store(); // define FD store
        // define finite domain variables
        IntVar a = new IntVar(store,"a",0,1);
        IntVar b = new IntVar(store,"b",0,1);
        IntVar s = new IntVar(store,"s",0,1);
        IntVar[] v = new IntVar[]{a,b};

        store.impose(new AndBool(v,s));

        // search for a solution and print results
        Search<IntVar> search = new DepthFirstSearch<IntVar>();
        SelectChoicePoint<IntVar> select = new InputOrderSelect<IntVar>(store, v,
                new IndomainMin<IntVar>());
        boolean result = search.labeling(store, select);
        if (result) {
            System.out.println("Solution:");
            System.out.println(a + " & " + b + " => " + s);
        } else System.out.println("No solution");

    }
}
