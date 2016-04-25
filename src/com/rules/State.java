package com.rules;

import java.util.*;

/**
 * Created by qin on 2015/10/10.
 */
public  abstract class State implements Cloneable{
    public static List list=new ArrayList();   //存放所有的栈
    public static List outlist=new ArrayList();//用来存放要输出的标签
    public static Stack stack;     //将生成的将要用到的状态压入栈
    protected int level=0;                      //当前应该匹配的标签的层数


    public  abstract List startElementDo(String tag,int layer,Stack currstack) throws CloneNotSupportedException;
    public  abstract List endElementDo(String tag,int layer,Stack currstack);
    public  abstract List checkOthers(String tag, int layer,Stack currstack);
    public  abstract String getNodeTest();

    public int getLevel() {return level;}
    public void setLevel(int level) {
        this.level=level;
    }



    public abstract Object copy() throws CloneNotSupportedException;
}
