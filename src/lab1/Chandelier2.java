package lab1;

import java.util.ArrayList;

import org.jacop.constraints.Alldiff;
import org.jacop.constraints.Linear;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.InputOrderSelect;
import org.jacop.search.PrintOutListener;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;

public class Chandelier2 {
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
        
        
        store.impose(new Alldiff(new IntVar[]{a,b,c,d,e,f,g,h,i}));
        ArrayList<IntVar> p1w = new ArrayList<IntVar>();
        p1w.add(a);
        p1w.add(b);
        p1w.add(c);
        ArrayList<Integer> p1p = new ArrayList<Integer>();
        p1p.add(2);
        p1p.add(-1);
        p1p.add(-2);
        store.impose(new Linear(store, p1w, p1p, "=", 0));
        
        ArrayList<IntVar> p2w = new ArrayList<IntVar>();
        p2w.add(d);
        p2w.add(e);
        p2w.add(f);
        ArrayList<Integer> p2p = new ArrayList<Integer>();
        p2p.add(2);
        p2p.add(1);
        p2p.add(-1);
        store.impose(new Linear(store, p2w, p2p, "=", 0));
        
        ArrayList<IntVar> p3w = new ArrayList<IntVar>();
        p3w.add(g);
        p3w.add(h);
        p3w.add(i);
        ArrayList<Integer> p3p = new ArrayList<Integer>();
        p3p.add(2);
        p3p.add(1);
        p3p.add(-3);
        store.impose(new Linear(store, p3w, p3p, "=", 0));
        
        
        ArrayList<IntVar> p0w = new ArrayList<IntVar>();
        p0w.add(a);
        p0w.add(b);
        p0w.add(c);
        p0w.add(d);
        p0w.add(e);
        p0w.add(f);
        p0w.add(g);
        p0w.add(h);
        p0w.add(i);
        
        ArrayList<Integer> p0p = new ArrayList<Integer>();
        p0p.add(3);
        p0p.add(3);
        p0p.add(3);
        
        p0p.add(-2);
        p0p.add(-2);
        p0p.add(-2);
        
        p0p.add(-3);
        p0p.add(-3);
        p0p.add(-3);
        
        
        store.impose(new Linear(store, p0w, p0p, "=", 0));
        
        
        
        
        
        
        IntVar[] v = new IntVar[]{a,b,c,d,e,f,g,h,i };
        
        // search for a solution and print results
        Search<IntVar> search = new DepthFirstSearch<IntVar>();
        search.setSolutionListener(new PrintOutListener<IntVar>());
        SelectChoicePoint<IntVar> select = new InputOrderSelect<IntVar>(store, v,
                new IndomainMin<IntVar>());
        boolean result = search.labeling(store, select);
        
        if (result) {
            System.out.println("Solution:");
            System.out.println("a:" + a.value());
            System.out.println("b:" + b.value());
            System.out.println("c:" + c.value());
            System.out.println("d:" + d.value());
            System.out.println("e:" + e.value());
            System.out.println("f:" + f.value());
            System.out.println("g:" + g.value());
            System.out.println("h:" + h.value());
            System.out.println("i:" + i.value());
            
        } else System.out.println("No solution");
    }
}
