package lab1;

import org.jacop.constraints.XneqY;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.*;

public class TransistorModel {
    public static void main(String[] args) {
        // Something to start from
        Store store = new Store(); // define FD store
        int size = 9;
        // define finite domain variables
        IntVar[] v = new IntVar[size * size];
        for (int i = 0; i < size * size; i++)
            v[i] = new IntVar(store, "v" + i, 1, size);
        // define setup
        //  _ 2 _ _ 4 _ _ _ 5
        //  _ 5 8 _ _ _ _ _ _
        //  _ 1 _ 8 _ _ 4 _ _
        //  7 _ _ _ _ 8 _ 4 _
        //  _ _ 1 9 _ 5 7 _ _
        //  _ 3 _ 7 _ _ _ _ 2
        //  _ _ 4 _ _ 3 _ 1 _
        //  _ _ _ _ _ _ 9 6 _
        //  2 _ _ _ 1 _ _ 5 _
        v[1].setDomain(2, 2);
        v[4].setDomain(4, 4);
        v[8].setDomain(5, 5);
        v[10].setDomain(5, 5);
        v[11].setDomain(8, 8);
        v[19].setDomain(1, 1);
        v[21].setDomain(8, 8);
        v[24].setDomain(4, 4);
        v[27].setDomain(7, 7);
        v[32].setDomain(8, 8);
        v[34].setDomain(4, 4);
        v[38].setDomain(1, 1);
        v[39].setDomain(9, 9);
        v[41].setDomain(5, 5);
        v[42].setDomain(7, 7);
        v[46].setDomain(3, 3);
        v[48].setDomain(7, 7);
        v[53].setDomain(2, 2);
        v[56].setDomain(4, 4);
        v[59].setDomain(3, 3);
        v[61].setDomain(1, 1);
        v[69].setDomain(9, 9);
        v[70].setDomain(6, 6);
        v[72].setDomain(2, 2);
        v[76].setDomain(1, 1);
        v[79].setDomain(5, 5);
        // define constraints
        // rows
        for (int row = 0; row < 9; row++) {
            for (int i = 0; i < 9; i++) {
                for (int j = i + 1; j < 9; j++) {
                    store.impose(new XneqY(v[row * 9 + i], v[row * 9 + j]));
                }
            }
        }
        // columns
        for (int col = 0; col < 9; col++) {
            for (int i = 0; i < 9; i++) {
                for (int j = i + 1; j < 9; j++) {
                    store.impose(new XneqY(v[i * 9 + col], v[j * 9 + col]));
                }
            }
        }
        // squares
        for (int col = 0; col < 3; col++) {
            for (int row = 0; row < 3; row++) {
                for (int i = 0; i < 9; i++) {
                    for (int j = i + 1; j < 9; j++) {
                        store.impose(new XneqY(v[(row * 3 + i / 3) * 9 + col * 3 + i % 3], v[(row * 3 + j / 3) * 9 + col * 3 + j % 3]));
                    }
                }
            }
        }
        store.impose(new XneqY(v[0], v[1]));
        store.impose(new XneqY(v[0], v[2]));
        store.impose(new XneqY(v[1], v[2]));
        store.impose(new XneqY(v[1], v[3]));
        store.impose(new XneqY(v[2], v[3]));
        // search for a solution and print results
        Search<IntVar> search = new DepthFirstSearch<IntVar>();
        SelectChoicePoint<IntVar> select = new InputOrderSelect<IntVar>(store, v,
                new IndomainMin<IntVar>());
        boolean result = search.labeling(store, select);
        if (result) {
            System.out.println("Solution:");
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    System.out.print(v[row * 9 + col].value() + " ");
                }
                System.out.println();
            }
        } else System.out.println("No solution");

    }
}
