package com.rules;

/**
 * Created by toy on 15-9-18.
 */
public class NodeTest {

    protected NodeTest(){

    }

    public static NodeTest x = new NodeTest();

    public NodeTest X1(){
        return x;
    }

    public NodeTest TestX1(){
        return x;
    }

    public NodeTest X2(){
        return x;
    }
}
