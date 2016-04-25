package com.rules;

import com.XPath.PathParser.ASTPath;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by qin on 2015/10/10.
 */
public class StateT1_1 extends StateT1 {

    protected StateT1_1(ASTPath path) {
        super(path);
    }

    public static State TranslateState(ASTPath path) {//重新创建T1-1
        return new StateT1_1(path);
    }

    @Override
    public List startElementDo(String tag, int layer, Stack currstack) {
        if ((getLevel() == layer) && (tag.equals(_test))) {//应该匹配的层数-->getLayer（）和 当前标签-->tag 的层数相等
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
        if(layer==getLevel()-1)
            currstack.pop();
        return list;
    }

    public List checkOthers(String tag, int layer, Stack currstack) {
        if(!outlist.isEmpty())
            System.out.println(outlist);
        return list;
    }

    public State copy() throws CloneNotSupportedException {
        return (State)this.clone();
    }
}

