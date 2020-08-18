
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
public class TwoSAT {
    public int[] literals;
    public boolean[] visited;
    public int[] reverseLiterals;//will keep the order of reverse dfs
    public int[] solution;
    public ArrayList<Integer>[] edges;
    public ArrayList<Integer>[] reverseEdges;
    public int[] pre;
    public int[] ccnum;
    int cc;
    int k;
    int numberOfVariablesInFormula;
    
    /**Method to create the graph by the given formula
     *@param f- is the formula that we will create the graph from it.
     */
    public TwoSAT(Formula f) {
        cc=1;
        k=0;
        //constructing the literals
        int numOfvariables=f.getN();
        numberOfVariablesInFormula=f.getN();
        literals= new int[2*numOfvariables];
        reverseLiterals= new int[2*numOfvariables];
        solution= new int[numOfvariables];
        ccnum= new int[2*numOfvariables];
        visited= new boolean[2*numOfvariables];
        for (int i = 0; i < literals.length; i++) {
            if(i>=numOfvariables)
                literals[i]=-(i%numOfvariables)-1;
            else
                literals[i]=i%numOfvariables+1;
            
        }
        //constructing the edges
        int numOfclauses=f.getM();
        edges= new ArrayList[2*numOfvariables];
        reverseEdges= new ArrayList[2*numOfvariables];
        ArrayList<Clause> clauses=f.getClauses();
        ArrayList<Clause> implications= new ArrayList<>();
        for (int i = 0; i < numOfclauses; i++) {
            ArrayList<Integer> lits=clauses.get(i).getLiterals();//this will return an arraylist with 2 inetgers only
            ArrayList<Integer> newImplication1=new ArrayList<>();
            ArrayList<Integer> newImplication2=new ArrayList<>();
            int first=-lits.get(0);//this is the first literal of the clause
            int second=lits.get(1);//this is the second literal of the clause
            newImplication1.add(first);
            newImplication1.add(second);
            Clause one=new Clause(newImplication1);//here we consruct -a->b
            first=-first;//restore the value of the first
            second=-second;//negate the second
            newImplication2.add(second);
            newImplication2.add(first);
            Clause two = new Clause(newImplication2);//here we construct -b->a
            implications.add(one);//adding the first implication
            implications.add(two);//adding the second implication
        }
        
        //System.out.println();
        //System.out.println("Printing all the implications added: ");
        //printImplications(implications);
        
        //initializing the lists
        for (int i = 0; i < edges.length; i++) {
            edges[i]=new ArrayList<>();
            
        } 
        //initializing the lists
        for (int i = 0; i < reverseEdges.length; i++) {
            reverseEdges[i]=new ArrayList<>();
            
        }
        
        //here we construct the adj list of each variable
        for (int i = 0; i < implications.size(); i++) {//implications size is 2m
            Clause c= implications.get(i);//save the current clause
            int firstLiteral=c.getLiterals().get(0);//get the first literal of current clause
            int secondLiteral=c.getLiterals().get(1);//get the second literal of current clasue
            //System.out.println("Clause "+i+" "+"First: "+firstLiteral+"  Second: "+secondLiteral);
            int index=firstLiteral;
            if(index<0){
                //System.out.println("The index:"+ (-index+numOfvariables-1)+" adding "+secondLiteral );
                edges[-index+numOfvariables-1].add(secondLiteral);//add into the arraylist of the first literal the second one
                //System.out.println("Added: "+edges[-index+numOfvariables-1].get(edges[-index+numOfvariables-1].size()-1) );
            }
            else{
                //System.out.println("The index:"+ (index-1) );
                edges[index-1].add(secondLiteral);//add into the arraylist of the first literal the second one
                //System.out.println("Added: "+edges[index-1].get(edges[index-1].size()-1) );
            }
        }
        
    }
    
    
    /**Method to reverse the graph*/
    public void reverseGraph(){
        
        for (int i = 0; i < edges.length; i++) {
            for (int j = 0; j < edges[i].size(); j++) {
                int destination=edges[i].get(j);//get the item at the list  
                int source=i;
                if(destination<0){
                    destination=-destination+edges.length/2-1;//shift the position
                }
                else{
                    destination=destination-1;
                }
                if(source>=edges.length/2){
                    source=-(i%(edges.length/2))-1;
                    reverseEdges[destination].add(source);
                }
                else{
                    reverseEdges[destination].add(source+1);
                }
            }
        }
        
    }
    /**Method to perform the depth first search and assign ccnums to the literals
     */
    public void dfs(){
        k=0;
        //phase 1: Reverse the graph
        reverseGraph();//reverse the graph
        
        //phase 2: running dfs on the revesed graph
        for (int i = 0; i < visited.length; i++) {
            visited[i]=false;
        }
        for (int literal : literals) {
            int indexOfLiteral= indexOfLiteral(literal);
            //System.out.println("literal: "+literal+"---index: "+indexOfLiteral);
            if(!visited[indexOfLiteral]){
                explore(indexOfLiteral);
            }
        }
        //System.out.println();
        //System.out.println("Inside reverseLiterals: ");
        //phase 3: finding the Strongly connected components
        for (int literal=reverseLiterals.length-1; literal>=0; literal-- ) {
            //System.out.print(reverseLiterals[literal]);
            int indexOfLiteral=indexOfLiteral(reverseLiterals[literal]);
            //int lt=literalOfIndex(literal);
            //System.out.println("literal: "+lt+"---index: "+indexOfLiteral);
            if(ccnum[indexOfLiteral]==0){//this means that the literal is not visited yet cc start from 1
                findingSCCs(indexOfLiteral);
                cc++;
            }
            //cc++;//increment the connected component number
            
        }
    
    }
    
    /**Method to find the orders of the literals that will enter in the dfs on the second phase*/
    private void explore(int indexOfLiteral){//running dfs on the reverse graph
        //System.out.println("Literal :"+literalOfIndex(indexOfLiteral));
        visited[indexOfLiteral]=true;
        //previsit(indexOfLiteral);
        //System.out.println("Visited size: "+visited.length);
        for (int i = 0; i < reverseEdges[indexOfLiteral].size(); i++) {
            int indexOfNeighbour=indexOfLiteral(reverseEdges[indexOfLiteral].get(i));
            if(!visited[indexOfNeighbour]){
                //System.out.println("At"+literalOfIndex(indexOfLiteral)+"                neighbour: "+reverseEdges[indexOfLiteral].get(i)+"---index: "+indexOfNeighbour);    
                explore(indexOfNeighbour);
                
            }
                
        }
        //System.out.println("k="+k+"  adding="+literalOfIndex(indexOfLiteral));
        reverseLiterals[k++]=literalOfIndex(indexOfLiteral);//here we add the elemnts in reverse order in the array
        
    
    }
    
    /**Method to find the SCCs of the variables*/
    private void findingSCCs(int indexOfLiteral){
        //visited[indexOfLiteral]=true;
        //System.out.println("Inside exploreReverse Literal :"+literalOfIndex(indexOfLiteral)+" index: "+indexOfLiteral);
        previsit(indexOfLiteral);//setting the connected compoent number
        for (int i = 0; i < edges[indexOfLiteral].size(); i++) {
            int indexOfNeighbour=indexOfLiteral(edges[indexOfLiteral].get(i));
                if(ccnum[indexOfNeighbour]==0){//the neighbours has not been visited yet
                   //System.out.println("At"+literalOfIndex(indexOfLiteral)+"                neighbour: "+edges[indexOfLiteral].get(i)+"---index: "+indexOfNeighbour);    
                   findingSCCs(indexOfNeighbour); 
            }
        }
        //cc++;
    
    }
    /**Method to solve an instance of 2SAT.It checks the connected components 
     *numbers of opposite literals to find if they are on the same scc or not.If yes,
     *the formula has no solution.Otherwise if the scc of a positive literal is greater 
     *than that of a negative literal it means that it is reached first in the linearization
     *so  it should be assigned false. 
     *@return if the formula is solvable or not
     */
    public boolean solve2SAT(){
        boolean isSolved=true;
        dfs();//O(l+e) in terms of literals and implications
        //O(n)- runs n times where n is the number of variables n<=l/2, everything insie is constant
        for (int i = 0; i < ccnum.length/2; i++) {//we needto check only half of the values since the second half holds the negations 
            if(ccnum[i]==ccnum[i+numberOfVariablesInFormula]){//a variable and its negation are part of the same scc-no solution
                isSolved=false;
                break;
            }
            else if(ccnum[i]>ccnum[i+numberOfVariablesInFormula]){//we reah first the nodes with the hghest scc numbers
                solution[i]=0;//put false for the variable
            }
            else{
                solution[i]=1;//put true to the variable
            }
        }
        
        return isSolved;
    
    }
    
    
    public int[] getSolution(){
        return solution;
    }
    
    /**Method to get the index of a literal
     *@param literal is the literal whose position we want
     *@return the literal of that particular index
     */
    private int indexOfLiteral(int literal){
        //System.out.println("Number of variables"+numberOfVariablesInFormula);
        int indexOfLiteral;
            if(literal<0){
                indexOfLiteral=-literal+numberOfVariablesInFormula-1;
            }
            else{
                indexOfLiteral=literal-1;
            }
            //System.out.println("Index of literal inside method: "+indexOfLiteral);
            return indexOfLiteral;
    }
    
    /**Method to get the literal at a particular index
     *@param index is the index of the literal we want in an array
     *@return the literal of that particular literal
     */
    private int literalOfIndex(int index){
        int literal;
        if(index>=numberOfVariablesInFormula){
            literal=-(index%numberOfVariablesInFormula)-1;
        }
        else{
            literal=index+1;
        }
        return literal;
    
    }
    
    /**Method used to set the connected component of a literal*/
    private void previsit(int index){
        ccnum[index]=cc;
        //System.out.println("Inside previsit: literal: "+ literalOfIndex(index)+" scc: "+ccnum[index]);
    }
    
    /**Method to print the graph created by a 2SAT formula graph*/
    public void printGraph(){
        for (int i = 0; i < edges.length; i++) {
            int index=i%(edges.length/2)+1;
            if(i>=edges.length/2){
                index=-(i%(edges.length/2))-1;
            }
            System.out.print(index+": ");
            for (Integer integer : edges[i]) {
                System.out.print(integer+", ");
            }
            System.out.println();
        }
    }
    
    /**Method to print the reversed graph created by a 2SAT formula graph*/
    public void printReverseGraph(){
        for (int i = 0; i < reverseEdges.length; i++) {
            int index=i%(reverseEdges.length/2)+1;
            if(i>=reverseEdges.length/2){
                index=-(i%(reverseEdges.length/2))-1;
            }
            System.out.print(index+": ");
            for (Integer integer : reverseEdges[i]) {
                System.out.print(integer+", ");
            }
            System.out.println();
        }
    }
    
    /**Method to print all implications
     *@param implications the arraylist of all implications
     */
    public void printImplications(ArrayList<Clause> implications){
    
        for (Clause c : implications) {
           
            System.out.print(c.getLiteral(0)+"->"+c.getLiteral(1));
            System.out.println();
        }
    }
    
    /**Method to print the list of reversed literals according to which the explore method
     *runs in order to assign SCCs to the variables
     */
    public void printReversedLiterals(){
        System.out.println("Reversed literals: ");
        for (int reverseLiteral : reverseLiterals) {
            System.out.print(reverseLiteral+", ");
        }
    
    }
    
    /**Method to print the connected components of the literals
     */
    public void printConnectedComponents(){
        System.out.println("Connected compontets: ");
        int l=0;
        for (int i : ccnum) {
            System.out.println(literalOfIndex(l)+" in scc: "+i);
            l++;
        }
    }
}
