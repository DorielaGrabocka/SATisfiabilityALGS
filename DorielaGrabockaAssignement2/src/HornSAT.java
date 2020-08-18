
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Doriela
 */
public class HornSAT {
    ArrayList<HornClause> clauses;
    private int n;//number of variables
    private int m;//number of clauses
    private int[] solution;//solution
    
    public HornSAT(int n, int m, ArrayList<HornClause> clauses) {
        this.m=m;
        this.n=n;
        this.clauses = clauses;
    }

    public ArrayList<HornClause> getClauses() {
        return clauses;
    }

    public int[] getSolution() {
        return solution;
    }

    public void setSolution(int[] solution) {
        this.solution = solution;
    }

    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }
    
       
    /**The SAT algorithm. Runs in linear time in terms of the number of literals.
     *@param f is the horn instance to be solved
     *@return whether the formula has a solution or not
     */
    public static boolean solveHornSAT(HornSAT f){
        
        ArrayList<HornClause> allClauses= f.getClauses();//all clauses of the formula
        ArrayList<HornClause> singletons=new ArrayList<>();//array to hold singletons
        ArrayList<HornClause> implications= new ArrayList<>();//arraylist to keep all implications
        ArrayList<HornClause> negatives= new ArrayList<>();//arraylist to keep the negative clauses
        boolean isSolvable=true;
        boolean[] inputAsBool=new boolean[f.getN()];//all variables to false
        int[] inputAsInt=new int[f.getN()];//all variables to 0
        //phase 1: splitting input into implications and negative clauses
        //this phase will run m times(m is the number of clauses)
        for (HornClause c : allClauses) {
            if(isClauseNegative(c)){
                negatives.add(c);//c
            }
            else if(isClauseSingleton(c)){
                singletons.add(c);//c
            }
            else{
                implications.add(c);//c
            }
            
        }
        //implications.addAll(singletons);//adding all singletons at the end of implications and search implications from  end
        
        
        /*for (Clause implication : implications) {
            System.out.println("Clause in implication:");
            for (int i : implication.getLiterals()) {
                System.out.print(i+" ");
            }
            System.out.println();
        }
        for (Clause implication : negatives) {
            System.out.println("Clause in negatives:");
            for (int i : implication.getLiterals()) {
                System.out.print(i+" ");
            }
            System.out.println();
        }*/
        
        for (HornClause singleton : singletons) {
            if(!singleton.getClauseValue(inputAsBool)){
                int literalAtindex=singleton.getLiteral(0);//since it has only one literal
                int index;
                if(literalAtindex<0){
                    index=-literalAtindex-1;
                }
                else{
                    index=literalAtindex-1;
                }
                inputAsBool[index]=true;//satisfying the singletons
                inputAsInt[index]=1;
            }
        }
        //phase 2: find if the assignement satisfies all implications
        //everything inside the for loop runs in c time except of if
        //total cost will be m(number of clauses) x avg(no Of literals per clause)=total number of literals
        for (HornClause c: implications) {
            
            /*System.out.println("Inside phase 2:");
            for (int i : c.getLiterals()) {
                System.out.print(i+" ");
            }
            System.out.println();*/
            if(!c.getClauseValue(inputAsBool)){// will have a cost of th enumber of literals whose value will be checked
            //here we have found an implication whose valuse is not satisfied by the current assignement
                int lastIndex=c.getLiterals().size()-1;//get the last index of the clause
                //int literalAtLastIndex= c.getLiterals().get(lastIndex);//get the last item in the clause
                int literalAtLastIndex=c.getRightPart().get(0);
                if(literalAtLastIndex<0){
                    literalAtLastIndex=-literalAtLastIndex;
                }
                //System.out.println(literalAtLastIndex);
                boolean value=inputAsBool[literalAtLastIndex-1];
                inputAsBool[literalAtLastIndex-1]=!value;//here we swicth the value of the last variable
                inputAsInt[literalAtLastIndex-1]=(value==false)?1:0;//here we swicth the value of the last variable
            }
        }
        //phase 3: check if the new  assignmenet satisfies all negative clauses
        //cost here is the number of negative cluases x avg(no of literals per clause)
        for (Clause c : negatives) {
            if(!c.getClauseValue(inputAsBool)){//depends on the number of literals per clause
                isSolvable=false;
                break;//no solution at all
            }
        }
        if(isSolvable){//c
            f.setSolution(inputAsInt);//here we set the solution of the class as input - c
        }
        return isSolvable;
    
    }

    
    
    /**Method that indicates whether a clause has all negative literals or not*/
    private static boolean isClauseNegative(HornClause c){
        /*boolean isNegative=true;
        ArrayList<Integer> literals=c.getLiterals();
        for (Integer l : literals) {
            if(l>0){
                isNegative=false;//we found a positive literal
                break;
            }
        }*/
        return c.getRightPart().isEmpty();
        //return isNegative;
    }
    
    
    
    /**Method that indicates whether a clause has is singleton and not negative*/
    private static boolean isClauseSingleton(Clause c){
        
        return c.getLiterals().size()==1;
    }
    
    
    
}
