
import java.io.File;
import java.io.FileNotFoundException;
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
public class FileRead {
    Scanner input;
    File data;
    
    public void openFile(String path){//opening the file from which we will read
       //System.out.println(path+"inside FIleRead");
        try{
            data=new File(path);
            input=new Scanner(data);
        }
        catch(FileNotFoundException e){
            System.err.println("File not found!");
            System.exit(1);
        }
    }
    /**Method that reads the formula from a file and constructs the clauses
     *@param clauses - will hold the clauses of the formula since it is passed by reference
     *@return - an array of 2 integers holding the number of variables and the number of clauses
     */
    public int[] readElements(ArrayList<Clause> clauses){//reading the contnts of the file
        int[] numbers= new int[2];//will hold the number of variables and claues
        
        try{
            int noOfvariables=Integer.parseInt(input.nextLine());//number of variables
            int noOfclauses=Integer.parseInt(input.nextLine());//number of clauses
            numbers[0]=noOfvariables;
            numbers[1]=noOfclauses;
            while(input.hasNext()){
               String clause= input.nextLine();
               String[] tokens= clause.split(", ");
               ArrayList<Integer> literals = new ArrayList<>();
                for (String token : tokens) {
                    literals.add(Integer.parseInt(token));
                }
                Clause c= new Clause(literals);
                clauses.add(c);
            }
            
        }
        catch(Exception e){
           System.out.println("Error reading from file!");
           System.exit(1);
        }
        return numbers;
    }
    
    public void closeFile(){//closing the file from which we read
       if(input!=null){
          input.close();
       }
    }
    
}
