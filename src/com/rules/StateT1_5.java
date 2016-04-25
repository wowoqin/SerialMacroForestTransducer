package com.rules;

import com.XPath.PathParser.ASTPath;

import java.util.List;
import java.util.Stack;

/**
 * Created by qin on 2015/10/10.
 */
public class StateT1_5 extends StateT1{
    protected  State  _q1;//检查 后续 path

    protected StateT1_5(ASTPath path,State q1){
        super(path);
        _q1=q1;
    }

    public static State TranslateState(ASTPath path){//重新创建T1-5
        State q1=StateT1.TranslateStateT1(path.getRemainderPath());
        return new StateT1_5(path,q1);
    }

    public List startElementDo(String tag,int layer,Stack currstack) {
        if((getLevel() == layer) && (tag.equals(_test))) {//应该匹配的层数 getLevel（）和 当前层数相等
            _q1.setLevel(getLevel() + 1); //q1 检查后续 path，肯定是当前标签的子孙中检查
            currstack.push(_q1);
        }
        return list;
    }

    public List endElementDo(String tag,int layer,Stack currstack){
        if(layer==getLevel()-1)
            currstack.pop();
        return list;
    }

    public List checkOthers(String tag, int layer,Stack currstack) {
        if(!outlist.isEmpty())
            System.out.println(outlist);
        return list;
    }

    public State copy() throws CloneNotSupportedException {
        return (State)this.clone();
    }
}
