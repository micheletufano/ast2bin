package my.pack.test;

import java.util.*;
import java.lang.*;

public class Test {
   

    public static void main(String[] args) {
    	int a, b = 6;
    	try{
    		a = b;
    	} catch(IndexOutOfBoundException e){
    		b = a;
    	} catch(Exception ee){
    		b = a;
    	} finally{
    		b = a;
    	}
    }
    
    public void doSomething(final String name){
    	int z = 5;
    }
}
