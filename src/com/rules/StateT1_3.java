package com.rules;

import com.XPath.PathParser.ASTPath;

import java.util.List;
import java.util.Stack;

/**
 * Created by qin on 2015/10/10.
 */
public class StateT1_3 extends StateT1 implements Cloneable{

    protected StateT1_3(ASTPath path){
        super(path);
    }

    public static State TranslateState(ASTPath path){//重新创建T1-3
        return new StateT1_3(path);
    }

    public List startElementDo(String tag,int layer,Stack currstack) throws CloneNotSupportedException {
        if ((layer >= getLevel()) &&(tag .equals(_test))) {//当前层数大于等于应该匹配的层数 getLayer（）就可以
            if(outlist.isEmpty()){
                outlist.add(_test);
            }
            else{
                if(outlist.get(outlist.size()-1) instanceof List)
                    ((List)(outlist.get(outlist.size() - 1))).add(_test);
                else
                    outlist.add(_test);
            }
        }
        return list;
   }


    @Override
    public List endElementDo(String tag,int layer,Stack currstack){
        State currQ= ((State) currstack.peek());// q1-3
        if(layer == currQ.getLevel()-1)
            currstack.pop();
        return list;
    }



    public List checkOthers(String tag, int layer,Stack currstack) {
        if(!outlist.isEmpty()){
            int i=outlist.size()-1;
            System.out.println(outlist.get(i));
            outlist.remove(i);
        }
        return list;
    }

    public State copy() throws CloneNotSupportedException {
        return (State)this.clone();
    }
}
