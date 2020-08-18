
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
public class Clause {
    private ArrayList<Integer> literals;

    public Clause(ArrayList<Integer> literals) {
        this.literals = literals;
    }

    /**Method to get the literals as Integers
     *@return an arraylist of integers 0 and 1
     */
    public ArrayList<Integer> getLiterals() {
        return literals;
    }
    /**Method to set the literals of a clause
     * @param literals is the arraylist of integers of the literal
     */
    public void setLiterals(ArrayList<Integer> literals) {
        this.literals = literals;
    }
    /**Method to calculate the value of a clause
     *@param literals is the arraylist containing the literals of the clause
     *@return true or false (the value of a clause) 
     */
    public boolean getClauseValue(ArrayList<Literal> literals){
        
        for (Literal l : literals) {
            if(l.getLiteral())
                return true;
        }
        return false;
    }
    public boolean getClauseValue(boolean[] inputValues){
        /*System.out.println("Inside Clause.getValues");
        System.out.println("Input");
        for (boolean inputValue : inputValues) {
            System.out.print(inputValue+"  ");
        }
        System.out.println();*/
        for (Integer i : getLiterals()) {
            //System.out.print(i+"  ");
            if(i<0 && !inputValues[-i-1])
            {
                return true;
            }
            else if(i>0 && inputValues[i-1]){
                //System.out.println("at true");
                return true;
            }
        }
        //System.out.println("at false");
        return false;
    }
    /**Method to return a literal at a certain index as an integer
     *@param index- is the index of the literal we want to retrieve
     *@returns the integer at that index 
     */
    public int getLiteral(int index){
        return getLiterals().get(index);
    
    }
}
