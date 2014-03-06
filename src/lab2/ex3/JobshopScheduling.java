package lab3;

import org.jacop.core.Store;

public class JobshopScheduling {
    static Store store;

    public static void main(String[] args) {
	long T1, T2, T;
	T1 = System.currentTimeMillis();
		
	jobshopScheduling();
		
	T2 = System.currentTimeMillis();
	T = T2 - T1;		
	System.out.println("\n\t*** Execution time = " + T + " ms");
    }

	private static void jobshopScheduling() {
		
	}
    
    
}
