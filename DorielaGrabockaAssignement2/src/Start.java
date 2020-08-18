
import java.util.ArrayList;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Doriela
 */
public class Start {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here      
       /* boolean[] inputs2={true, true, false,false};
        Formula f4= readFromFile("formula8.txt");
        System.out.println("Input validates the formula: "+checkAssignement(f4, inputs2));
        System.out.println("is2SAT: "+is2SAT(f4));
        System.out.println("isHornSAT: "+isHornSAT(f4));
        System.out.println("Solving f4 with solveGeneralSAT");
        solveGeneralSAT(f4);
        System.out.println("Solving f4 with hornSAT2");
        solveHornSAT(f4);
        System.out.println("Solving f4 with 2SAT");
        solve2SAT(f4);*/
        
        
        System.out.println("Enter a file path: ");
        String path= new Scanner(System.in).nextLine();
        Formula f = readFromFile(path);
        System.out.println("Formula created succefully! Select an option from the menu");
        System.out.println("1. Check Assignmnet");
        System.out.println("2. Check Horn");
        System.out.println("3. Check 2-SAT");
        System.out.println("4. Solve with general SAT (all formulas)");
        System.out.println("5. Solve with Horn SAT (Horn formulas only)");
        System.out.println("6. Solve with 2-SAT (2-SAT formulas only)");
        System.out.println("-1.Exit");
        
        int choice=new Scanner(System.in).nextInt();
        while(choice!=-1){
            switch (choice){
                case 1:
                    System.out.println("Please enter the assignment as 1(true) or 0(false) for the variables");
                    Scanner inp= new Scanner(System.in);
                    boolean[] assign= new boolean[f.getN()];
                    for(int i=0; i< f.getN();i++){
                        assign[i]=(inp.nextInt()==1)?true:false;
                    }
                    /*for (boolean b : assign) {
                        System.err.println(b);
                    }*/
                    System.out.println(checkAssignement(f, assign));
                    break;
                case 2:
                    System.out.println(isHornSAT(f));
                    break;
                case 3:
                    System.out.println(is2SAT(f));
                    break;
                case 4:
                    solveGeneralSAT(f);
                    break;
                case 5:
                    solveHornSAT(f);
                    break;
                case 6:
                    solve2SAT(f);
                    break;
                default:
                    System.exit(0);//terminate the program
            }
            System.out.println("1. Check Assignmnet");
            System.out.println("2. Check Horn");
            System.out.println("3. Check 2-SAT");
            System.out.println("4. Solve with general SAT (all formulas)");
            System.out.println("5. Solve with Horn SAT (Horn formulas only)");
            System.out.println("6. Solve with 2-SAT (2-SAT formulas only)");
            System.out.println("-1.Exit");
            choice=new Scanner(System.in).nextInt();        
        }
        
    }
    
    
    /**Method to find if a certain assignment of values is a solution 
     * for the formula or not
     *@param f- is the formula to be checked
     *@param inputs[]- the array of the assignments
     *@return boolean- whether the input is a solution or not
     */
    private static boolean checkAssignement(Formula F, boolean[] inputs){
        return Formula.checkAssignement(F, inputs);
    }
    
    /**Method to find if a formula is an instance of 2SAT or not
     *@param f- is the formula to be checked
     *@return the result
     */
    private static boolean is2SAT(Formula f){
        return Formula.is2SAT(f);
    }
    
    /**Method to find if a formula is an instance of HornSAT or not
     *@param f- is the formula to be checked
     *@return the result
     */
    private static boolean isHornSAT(Formula f){
        return Formula.isHornSAT(f);
    }
    
    /**Method to solve a given formula
     *@param f- the formula to be solved
     */
    private static void solveGeneralSAT(Formula f){
        int solutionIndex=Formula.solveGeneralSAT(f);
        if(solutionIndex==-1){
            System.out.println("no");
        }
        else{
            int[] solution= f.getSolution(solutionIndex);
            for (int i : solution) {
                System.out.print(i+", ");
            }
        }
        System.out.println();
    }

    /**Method to solve a given formula using Horn SAT
     *@param f- the formula to be solved
     */
    private static void solveHornSAT(Formula f){
        
        if(Formula.isHornSAT(f)){//check if f is an instance of Hor SAT
            if(Formula.solveHornSAT(f)){//here is the call the real algorithm
                int[] solution=f.getSolution();
                System.out.println("The solution is: ");
                for (int i : solution) {
                    System.out.print(i+", ");
                }
                System.out.println();
            }
            else{
                System.out.println("no");
            }
        }
        else{
            System.out.println("This is not an instance of Horn-SAT");
        }
        
    }
    /**Method to run the 2SAT algorithm in order to solve the formula
     *@param f is the formula to be solved
     */
    private static void solve2SAT(Formula f){        
        if(Formula.is2SAT(f)){//here we check if the formula is an insatnceof 2SAT or not
            TwoSAT ins= new TwoSAT(f);//creating a two sat formula
            if(ins.solve2SAT()){//this is the real algorithm
                int[] solution= ins.getSolution();
                for (int i : solution) {
                    System.out.print(i+", ");
                }
                System.out.println();
            }
            else{
                System.out.println("no");
            }
        }
        else{
            System.out.println("This is not an instance of 2-SAT");
        }
    }
    
    /**Method to read data from file and create the array*/
    private static Formula readFromFile(String path){
        FileRead reader= new FileRead();
        reader.openFile(path);
        ArrayList<Clause> clauses= new ArrayList<>();
        int[] data= reader.readElements(clauses);
        Formula f= new Formula(data[0],data[1],clauses);
        return f;
    }
}
