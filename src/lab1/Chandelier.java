package lab1;

import org.jacop.constraints.Alldiff;
import org.jacop.constraints.AndBool;
import org.jacop.constraints.Not;
import org.jacop.constraints.OrBool;
import org.jacop.constraints.Sum;
import org.jacop.constraints.XeqY;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.InputOrderSelect;
import org.jacop.search.PrintOutListener;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;

public class Chandelier {
    public static void main(String[] args) {
        // Something to start from
        Store store = new Store(); // define FD store
        // define finite domain variables
        IntVar a = new IntVar(store,"a",1,9);
        IntVar b = new IntVar(store,"b",1,9);
        IntVar c = new IntVar(store,"c",1,9);
        IntVar d = new IntVar(store,"d",1,9);
        IntVar e = new IntVar(store,"e",1,9);
        IntVar f = new IntVar(store,"f",1,9);
        IntVar g = new IntVar(store,"g",1,9);
        IntVar h = new IntVar(store,"h",1,9);
        IntVar i = new IntVar(store,"i",1,9);
        
        IntVar[] v = new IntVar[]{a,b,c,d,e,f,g,h,i};
        
        IntVar[] abc = new IntVar[]{a,b,c};
        IntVar[] def = new IntVar[]{d,e,f};
        IntVar[] ghi = new IntVar[]{g,h,i};
        
        IntVar bajs = new IntVar(store,"bajs",1,9);
        new Sum(abc,bajs);
        
        
        store.impose(new Alldiff(v));
        store.impose(new XeqY(bajs,d));
        
        // search for a solution and print results
        Search<IntVar> search = new DepthFirstSearch<IntVar>();
        search.setSolutionListener(new PrintOutListener<IntVar>());
        SelectChoicePoint<IntVar> select = new InputOrderSelect<IntVar>(store, v,
                new IndomainMin<IntVar>());
        boolean result = search.labeling(store, select);
        
        if (result) {
            System.out.println("Solution:");
        } else System.out.println("No solution");
    }
}
