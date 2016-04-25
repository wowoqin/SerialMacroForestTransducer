package com.rules;

import java.util.*;

/**
 * Created by qin on 2015/10/10.
 */
public  abstract class State implements Cloneable{
    public static List list=new ArrayList();   //������е�ջ
    public static List outlist=new ArrayList();//�������Ҫ����ı�ǩ
    public static Stack stack;     //�����ɵĽ�Ҫ�õ���״̬ѹ��ջ
    protected int level=0;                      //��ǰӦ��ƥ��ı�ǩ�Ĳ���


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
