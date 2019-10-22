package main;
import traces.Trace;

public class Utility {
	/**
	 * This class contain useful methods for the project
	 */
	
	//return max value of an Array of int
	public static int maxArray(int[] tab) {
		int max = 0;
		for (int i = 0; i < tab.length; i++) {
			if (max < tab[i]) {
				max = tab[i];
			}
		}
		return max;
	}
	
	
	//add a value to an Array of String
    public static String[] stringAdd(String[] originalArray, String newItem){
  		int currentSize = originalArray.length;
  		int newSize = currentSize + 1;
	    String[] tempArray = new String[ newSize ];
 	    for (int i=0; i < currentSize; i++){
    	    tempArray[i] = originalArray [i];
   		}
  		tempArray[newSize - 1] = newItem;
    	return tempArray;   
	}
    
    
    //display a matrix
    public static void displayMatrixInt(double[][] matrix) {
    	System.out.println("matrice : ");
    	for (int i = 0; i < matrix.length; i++) {
        	for (int j = 0; j < matrix.length; j++) {
        		System.out.print(matrix[i][j] + " ");
        	}	
        	System.out.println();
    	}
    	System.out.println("matrice de taille " + matrix.length + "x" + matrix.length);
    }
    
    
    //display an Array of int
    public static void displayArrayInt(int[] clusters) {
    	System.out.print("tab : ");
    	for (int j = 0; j < clusters.length; j++) {
    		System.out.print(clusters[j] + " ");
    	}
    }
    
    
    //add a value to an Array of Trace
    public static Trace[] traceAdd(Trace[] originalArray, Trace newItem) {
    	int currentSize = originalArray.length;
  		int newSize = currentSize + 1;
	    Trace[] tempArray = new Trace[ newSize ];
 	    for (int i=0; i < currentSize; i++){
    	    tempArray[i] = originalArray [i];
   		}
  		tempArray[newSize - 1] = newItem;
    	return tempArray; 
    }
}

