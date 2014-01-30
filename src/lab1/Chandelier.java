package lab1;

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
        
        
        //store.impose(new Alldiff(new IntVar[]{a,b,c,d,e,f,g,h,i}));

        IntVar x = new IntVar(store,"x",1,100);
        IntVar y = new IntVar(store,"y",1,100);
        IntVar z = new IntVar(store,"z",1,100);
        
        
        store.impose(new Sum(new IntVar[]{a,b,c},y));
        store.impose(new Sum(new IntVar[]{d,e,f},z));
        store.impose(new Sum(new IntVar[]{g,h,i},x));
        
        IntVar yy = new IntVar(store,"yy",0,100);
        store.impose(new Sum(new IntVar[]{y,y,y},yy));
        IntVar xz = new IntVar(store,"xz",0,100);
        store.impose(new Sum(new IntVar[]{z,z,x,x,x},xz));
        store.impose(new XeqY(yy,xz));
        
        IntVar bc = new IntVar(store,"bc",0,100);
        store.impose(new Sum(new IntVar[]{b,c,c},bc));
        IntVar aa = new IntVar(store,"aa",0,100);
        store.impose(new Sum(new IntVar[]{a,a},aa));
        store.impose(new XeqY(bc,aa));
                
        IntVar de = new IntVar(store,"de",0,100);
        store.impose(new Sum(new IntVar[]{d,d,e},de));
        IntVar ff = new IntVar(store,"ff",0,100);
        store.impose(new Sum(new IntVar[]{f,f},ff));
        store.impose(new XeqY(de,ff));
        
        IntVar gh = new IntVar(store,"gh",0,100);
        store.impose(new Sum(new IntVar[]{g,g,h},gh));
        IntVar ii = new IntVar(store,"ii",0,100);
        store.impose(new Sum(new IntVar[]{i,i,i},ii));
        store.impose(new XeqY(gh,ii));
        
        
        IntVar[] v = new IntVar[]{a,b,c,d,e,f,g,h,i,x,y,z,bc,aa,de,ff,gh,ii,yy,xz };
        
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
