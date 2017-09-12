/*
Rodrigo Rogel-Perez
10/21/2016
Algorithms
*/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamic.programming;
import java.io.*;
/**
 *
 * @author Rodrigo
 */
public class EditDistance {
    //returns min
    public static int min(int a, int b, int c){
        if (a<=b && a<=c){ //in case of tie swapping is preferred over deleting over inserting
            return a;
        }else if (b<=a && b<=c){
            return b;
        }else{
            return c;
        }
    }
    //Checks if character input is a vowel
    public static boolean isVowel(char c){
	String vowels = "aeiouAEIOU";
	return vowels.contains(Character.toString(c));
    }
    //Checks if character input is consonant
    public static boolean isConsanant(char c){
            String cons = "bcdfghjklmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ";
            return cons.contains(Character.toString(c));
    }
    //returns penalty score for swapping two characters. Penalty score is determined by consonant-vowel pair
    public static int delta(char i, char j, int delta1, int delta2){
        int delta;
        if (i == j){
            delta = 0;
        }
        else if (isVowel(i) && isVowel(j)){
                delta = delta2;
        }
        else if (isConsanant(i) && isConsanant(j)){
                delta = delta2;
        }else{
                delta = delta1;
        }
        return delta;
    }
    //Appends strings and prints string after swapping characters
    public static String printChange(char i, char j, String s, int index){
        if (i == j){//If characters are the same we ignore
            System.out.print("Ignore " + i +".                  " + s);
        }else{
            char[] ch = s.toCharArray();
            ch[index-1] = j;
            s = new String(ch);
            System.out.print("Change " + i + " to " + j +".             " + s);
        }
        return s;
    }
    //Inserts a character to sring input and prints string afterwards
    public static String printInsert(char c, String str, int i){
        if (i==0){//Base case: Inserting char infront of string
            str = c + str.substring(0);
        }
        else if (i==str.length()){ //Inserting char at the end of string
            str = str.substring(0,str.length()) + c;
        }else{ //Inserting char somewhere in between
            str = str.substring(0,i) + c + str.substring(i);
        }
        System.out.print("Insert " + c +".                  " + str);
        return str;
    }
    //Deletes a char from the string input and prints string afterwards
    public static String printDelete(char i, String s, int index){
        if (index==0){ //Base case: Delete first letter of string
            s = s.substring(1);
        }
        else if (index==s.length()){ //delete last letter of string
            s = s.substring(0,index-1);
        }else{//delete a letter somewhere in between
           s = s.substring(0,index-1) + s.substring(index);
        }
        System.out.print("Delete " + i +".                  " + s);
        return s;
    }
    //Traces back matrix to get the min cost sequence of edits
    public static void traceback(String str1, String str2, int[][] a, int[][][] b, String[][] c){
        int x = a.length-1;
        int y = a[0].length-1;
        String sampleS = str1.substring(1);
        while(a[x][y]>0){
            switch (c[x][y]) {
                case "change":
                    sampleS = printChange(str1.charAt(x),str2.charAt(y),sampleS,x);
                    break;
                case "insert":
                    sampleS = printInsert(str2.charAt(y),sampleS,x);
                    break;
		case "delete":
                    sampleS = printDelete(str1.charAt(x),sampleS,x);
                    break;
                default:
                    System.out.println("edit type not recognized.");
                    return;
            }
            int i = b[x][y][0];
            int j = b[x][y][1];
            x = i;
            y = j;
 
            System.out.println();
        }
    }
    //prints matrix
    public static void printMatrix(int[][] a){
        for (int[] a1 : a) {
            for (int j = 0; j<a[0].length; j++) {
                System.out.print(a1[j] + " ");
            }
            System.out.println();
        }
    }
    //Fills matrix
    public static int Alignment(String x, String y, int delta1, int delta2, int alpha){
        x = "_" + x;
        y = "_" + y;
        int[][] OPT = new int[x.length()][y.length()];//array to hold opt values
        int[][][] indices = new int[x.length()][y.length()][2]; //array to record indices of suboptimal problems
        String[][] edits = new String[x.length()][y.length()]; //array to hold type of edit
        for (int i=0; i<OPT.length; i++){
                OPT[i][0] = (alpha*i);
                indices[i][0][0] = i-1;
                indices[i][0][1] = 0;
                edits[i][0] = "delete";
        }
        for (int i=0; i<OPT[0].length; i++){
                OPT[0][i] = (alpha*i);
                indices[0][i][0] = 0;
                indices[0][i][1] = i-1;
                edits[0][i] = "insert";
        }
        for (int j=1; j<y.length(); j++){
            int a, b, c;
            for (int i=1; i<x.length(); i++){
                OPT[i][j] = min(a = delta(x.charAt(i), y.charAt(j), delta1, delta2) + OPT[i-1][j-1], b = alpha + OPT[i-1][j], c = alpha + OPT[i][j-1]);
/***************************************************************/
                if (OPT[i][j] == a){
                    indices[i][j][0] = i-1;
                    indices[i][j][1] = j-1;
                    edits[i][j] = "change";
                }
                else if (OPT[i][j] == b){
                    indices[i][j][0] = i-1;
                    indices[i][j][1] = j;
                    edits[i][j] = "delete";
                }
                else if (OPT[i][j] == c){
                    indices[i][j][0] = i;
                    indices[i][j][1] = j-1;
                    edits[i][j] = "insert";
                }
				else{
					System.out.println("No clear edit");
				}
/***************************************************************/
            }
        }
//        printMatrix(OPT);
        traceback(x,y,OPT,indices,edits);
        return OPT[x.length()-1][y.length()-1];
    }
    
    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException{
        BufferedReader Buff = new BufferedReader(new FileReader("inputs.txt"));
        int lines = 0;
        String line;
        
        int delta1; //to hold penalty score for swapping a consonant and vowel
        int delta2; //to hold penalty score for swapping 2 of the same kind
        int alpha; //to hold penalty score for deleting/inserting
        
        String s1 = ""; //to hold first string of the two for each set of inputs
        String s2 = ""; //Naturally this holds the second
        while( (line=Buff.readLine()) != null){
            lines++;
            if (lines == 1){
                String[] strings = line.split(" ");
                s1 = strings[0];
                s2 = strings[1];
                System.out.println(s1 + " " + s2);
            }
            else if (lines == 2){
                String[] scores = line.split(" ");
                alpha  = Integer.parseInt(scores[0]);
                delta1 = Integer.parseInt(scores[1]);
                delta2 = Integer.parseInt(scores[2]);
                System.out.println(alpha + " " + delta1 + " " + delta2);
                System.out.println("Start!" + "                     " + s1);
                int lowcost = Alignment(s1,s2,delta1,delta2,alpha);
                System.out.println("\nEdit Distance: " + lowcost + "\n");
                System.out.println("---------------------------------------------\n");
            }else{
                lines = 0;
            }
        }
    } 
}
