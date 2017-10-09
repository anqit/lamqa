package com.anqit.util.lamqa;

import java.util.function.Function;

import com.anqit.util.lamqa.function.ThrowingVarFunction;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InstantiationException, IllegalAccessException
    {
    		Object f = Function.class.newInstance();
        System.out.println(f);
        
        ThrowingVarFunction<Number> sums = (a) -> 7l;
    }
}
