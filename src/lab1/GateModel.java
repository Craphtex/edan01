package lab1;

import org.jacop.constraints.AndBool;
import org.jacop.constraints.Not;
import org.jacop.constraints.OrBool;
import org.jacop.constraints.XeqY;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.InputOrderSelect;
import org.jacop.search.PrintOutListener;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;

public class GateModel {
    public static void main(String[] args) {
        // Something to start from
        Store store = new Store(); // define FD store
        // define finite domain variables
        IntVar a = new IntVar(store,"a",0,1);
        IntVar b = new IntVar(store,"b",0,1);
        IntVar c = new IntVar(store,"c",0,1);
        IntVar ab = new IntVar(store,"ab",0,1);
        IntVar s = new IntVar(store,"s",0,1);
        IntVar[] v = new IntVar[]{a,b,c};

        IntVar[] andAB = new IntVar[]{a,b};
        IntVar[] andABC = new IntVar[]{ab,c};
        
        store.impose(new AndBool(andAB, ab));
        store.impose(new AndBool(andABC,s));

        // search for a solution and print results
        Search<IntVar> search = new DepthFirstSearch<IntVar>();
        search.setSolutionListener(new PrintOutListener<IntVar>());
        SelectChoicePoint<IntVar> select = new InputOrderSelect<IntVar>(store, v,
                new IndomainMin<IntVar>());
        boolean result = search.labeling(store, select);
        
        if (result) {
            System.out.println("Solution:");
            System.out.printf("a:%d b:%d c:%d   a&b&c=%d", a.value(), b.value(),c.value(), s.value());
        } else System.out.println("No solution");
    }
}
