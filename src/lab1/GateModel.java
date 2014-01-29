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
        IntVar[] v = new IntVar[]{a,b,c, ab, s};

        store.impose(new AndBool(new IntVar[]{a,b}, ab));
        store.impose(new AndBool(new IntVar[]{ab,c},s));


        // search for a solution and print results
        Search<IntVar> search = new DepthFirstSearch<IntVar>();
        search.setSolutionListener(new PrintOutListener<IntVar>());
        SelectChoicePoint<IntVar> select = new InputOrderSelect<IntVar>(store, v,
                new IndomainMin<IntVar>());
        search.getSolutionListener().searchAll(true);
        search.getSolutionListener().recordSolutions(true);
        boolean result = search.labeling(store, select);


        if (result) {
            System.out.println("a  b  c  ab s");
            for (int i=1; i<=search.getSolutionListener().solutionsNo(); i++){
                for (int j=0; j<search.getSolution(i).length; j++)
                    System.out.print(search.getSolution(i)[j]+"  ");
                System.out.println();
            }

        } else System.out.println("No solution");
    }
}
