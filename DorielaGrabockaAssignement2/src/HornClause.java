
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
public class HornClause extends Clause{
    ArrayList<Integer> leftPart;//negatve literals
    ArrayList<Integer> rightPart;//positive literals
    ArrayList<Integer> allLiterals;
    public HornClause(ArrayList<Integer> literals) {
        super(literals);
        leftPart= new ArrayList<>();
        rightPart= new ArrayList<>();
        allLiterals= new ArrayList<>();
        for (Integer i : literals) {
            if(i>0){
                rightPart.add(i);//positives to the right
            }
            else{
                leftPart.add(i);//negatives to the left
            }
        }
        allLiterals.addAll(leftPart);
        allLiterals.addAll(rightPart);//the positive to be at the end
    }

    public ArrayList<Integer> getLeftPart() {
        return leftPart;
    }

    public ArrayList<Integer> getRightPart() {
        return rightPart;
    }
    
    public static boolean isHornClause(Clause c){
        HornClause h=new HornClause(c.getLiterals());
        return h.rightPart.size()<=1;//if the number of positive literals is 1 or 0 it is a horn clause
    }
    
    @Override
    public ArrayList<Integer> getLiterals() {
       return allLiterals;
    }
    
}
