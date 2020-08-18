
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
public class Formula {
    private ArrayList<Clause> clauses;
    private int n;//number of variables
    private int m;//number of clauses
    private boolean[][] possibilitiesAsBooleans;
    private int[][] possibilitiesAsIntegers;
    private int[] solution;
    public Formula(int n,int m,ArrayList<Clause> clauses) {
        this.m=m;
        this.n=n;
        this.clauses = clauses;
        possibilitiesAsBooleans=new boolean[(int)Math.pow(2,n)][n];
        possibilitiesAsIntegers=new int[(int)Math.pow(2,n)][n];
    }
    
    public ArrayList<Clause> getClauses() {
        return clauses;
    }
    //this is the number of clauses
    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }
    //this is the number of variables
    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }
    
    public void setClauses(ArrayList<Clause> clauses) {
        this.clauses = clauses;
    }

    //method used to check an assignemnet to a given formula
    public static boolean checkAssignement(Formula f, boolean[] inputs){
        //System.out.println("Formula.checkAssignement");
        if(f.getN()==inputs.length){
            ArrayList<Clause> clauseList= f.getClauses();
            for (Clause clause : clauseList) {//it depends
                if(!(f.getClauseValue(clause, inputs))){
                    return false;//formula is not true
                }
            }
            return true;//formula has been proven by the input
        }
        else{
            System.out.println("The input does not match with the variable number!");
            return false;
        }
    }
    
    /**Method to check whether a formula is an instance of 2SAT
     * @param f is the formula to be checked
     * @return a boolean indicating whether the formula is a 2SAT instance or not
     */
    public static boolean is2SAT(Formula f){
        //System.out.println("Formula.is2SAT");
        for (Clause c : f.getClauses()) {
            if(!(c.getLiterals().size()==2)){
                return false;//a clause does not have two literals 
            }
        }
        return true;//all clauses have excactly 2 literals
    
    }
    
    /**Method to check whether a formula is an instance of HornSAT
     * @param f is the formula to be checked
     * @return a boolean indicating whether the formula is a horn instance or not
     */
    public static boolean isHornSAT(Formula f){
        //way 1
        /*for (Clause c : f.getClauses()) {
            if(!HornClause.isHornClause(c))
                return false;
            }*/
        //way 2
        for (Clause clause : f.getClauses()) {
            int posCount=0;
            for (Integer l : clause.getLiterals()) {
                if(l>0){//here we have found a positive literal
                    posCount++;
                }
            }
            if(posCount>1)
                return false;//the clause has more than 1 positive literal 
        }//end of clauses iteration
        return true;//all clauses have excactly 1 or 0 literals
    }
    
    private boolean getClauseValue(Clause c, boolean[] inputs){
        //System.out.println("Formula.getClauseValue");
        ArrayList<Integer> listOfLiteralsAsIntegers=c.getLiterals();
        /*for (Integer listOfLiteralsAsInteger : listOfLiteralsAsIntegers) {
            System.out.print(listOfLiteralsAsInteger+"  ");
        }
        System.out.println();*/
        ArrayList<Literal> listOfLiteralsAsBooleans=new ArrayList<>();
        for (Integer i : listOfLiteralsAsIntegers) {//for each variable in a clause
            //check the correspondig values in the inputs array
            if(i<0){
                //add the value to the listOf literals but as boolean this times
                listOfLiteralsAsBooleans.add(new Literal(!inputs[-i-1]));
            }
            else{
                listOfLiteralsAsBooleans.add(new Literal(inputs[i-1]));
            }
        }
        /*for (Literal l : listOfLiteralsAsBooleans) {
            System.out.print(l.getLiteral()+"  ");
        }*/
        //System.out.println();
        return c.getClauseValue(listOfLiteralsAsBooleans);
        
    }
    
    /**Method to return the solution
     *@return the solution as an array of 0s and 1s
     */
    public int[] getSolution(){
        return solution;
    }
    
    /**Used for the general SAT, solve general SAT should be called before this is called
     *@param i is the index of the  solution in the 2D array holding all possibilities
     *@return the solution as an array of 0s and 1s. 
     */
    public int[] getSolution(int i){
        return possibilitiesAsIntegers[i];
    }
    
    /**Method used to set the solution of a formula
     *@param sol is the solution
     */
    public void setSolution(int[] sol){
        solution=sol;
    }
    
    /**Method to generate all possibilities for solutions. This method is optimized it will 
     *iterate until it finds a solution. If there is no solution it will iterate 2^n times
     *and check all possibilities.
     *@returns the index of the solution in the integer array containing all the possibilities
     * or -1 if no solution is found 
     */
    public int generatePossibilities() 
    { 
        int n=this.getN();//number of variables
        //boolean[][] possibleInputs= new boolean[(int)Math.pow(2, n)][n];
        int solutionIndex=-1;
        for (int i = 0; i < (int)Math.pow(2, n); i++) {//runs 2^n times
            decimalToToBoolean(i);//cost log_2(i)
            if(checkAssignement(this, possibilitiesAsBooleans[i])){//we find a solution
                solution=possibilitiesAsIntegers[i];//we set the solution
                solutionIndex=i;
                break;
            }
            
        }
        return solutionIndex;
        /*for (int i=0; i< (int)Math.pow(2, n) ; i++) {
            
            for (boolean j : possibilitiesAsBooleans[i]) {
                System.out.print(j+", ");
            }
            System.out.print(" -> ");
            for (int k : possibilitiesAsIntegers[i]) {
                System.out.print(k+", ");
            }
            System.out.println();
        }*/
        //return possibleInputs;
    }
    
   
    
    /**Method to list all possibilities for solution of a certain formula in boolean and
     * binary form
     *@param number- the current number to be converted to a Binary number
     */
    //total cost of a call log_2 (n)*c=log_2 (n)
    private void decimalToToBoolean(int number){
        int k=number;
        //boolean[] booleans=new boolean[numOfDigits];//c
        //int[] integers=new int[numOfDigits];//c
        int i=getN()-1;//c
        while(number>0){//log n times
            //booleans[i]=(number%2)==1?true:false;//c
            //integers[i]=number%2;//c
            possibilitiesAsIntegers[k][i]=number%2;//c
            possibilitiesAsBooleans[k][i]=(number%2)==1?true:false;
            number=number/2;//decrease the number and record the remainder-c work
            i--;//decrease the index c- work
        }
        //return booleans;
    }
    
    /**Method to find a solution for a general SAT problem
     *@param f- formula to be solved
     *@return the index of the solution in the array of possibilities,
     * if -1 is returned than there is no solution at all
     */
    public static int solveGeneralSAT(Formula f){
        //int solution= -1;
        return f.generatePossibilities();
        /*for (int i=0; i< f.possibilitiesAsBooleans.length; i++){// runs 2^n times
            if(checkAssignement(f, f.possibilitiesAsBooleans[i])){//cost of this is m x avg(no of literals per clause), number of clauses
                solution=i;
                break;//we found a solution and we are ready to output it
            }
        }*/
        
        //return solution;
    }
    
    
    /**Method to find a solution for a certain formula that is an instance of Horn SAT
     *@param f is the formula that we want to solve
     *@return a boolean indicating whether a solution is found or not
     */
    public static boolean solveHornSAT(Formula f){
        HornSAT hornSat=convertToHorn(f);//we first convert the formula to a horn sat formula
        boolean isSolvable=HornSAT.solveHornSAT(hornSat);//check if there is solution
        if(isSolvable){
            f.setSolution( hornSat.getSolution());//set the solution if found
        }
        return isSolvable; 
        
    }
    
    /**Method to convert a general formula to a horn formula that is convertible in 
     * polynomial time.
     * @param f- is the formula to be converted
     * @return a horn formula
     */
    public static HornSAT convertToHorn(Formula f){
        ArrayList<HornClause> hornClauses= new ArrayList<>();
        for (Clause clause : f.getClauses()) {
            HornClause horn= new HornClause(clause.getLiterals());
            hornClauses.add(horn);
        }
        HornSAT hornSatFormula= new HornSAT(f.getN(), f.getM(), hornClauses);
        return hornSatFormula;
    }
    
}
