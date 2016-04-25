package com.rules;

import com.XPath.PathParser.ASTPreds;

import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by qin on 2015/10/10.
 */
public class StateT2_2 extends StateT2{
    protected  State _q3;

    protected  StateT2_2(ASTPreds preds,State q3){
        super(preds);
        _q3=q3;
    }

    public static StateT2 TranslateState(ASTPreds preds){//重新创建T2-2
        State q3=StateT3.TranslateStateT3(preds.getFirstStep().getPreds());
        return new StateT2_2(preds,q3);
    }
    public List startElementDo(String tag,int layer,Stack currstack) {
        if((getLevel()==layer) && (tag.equals(_test))){
            _q3.setLevel(getLevel() + 1);
            currstack.push(_q3);//  q'(x1)
        }
        return list;
    }

    //谓词要是能自己遇见上层结束标签，则表明自己是检查失败的
    public List endElementDo(String tag,int layer,Stack currstack){
        if(layer==getLevel()-1){
            if(currstack.size()>1){
                currstack.pop();
                State currQ=(State)currstack.peek();
                Object object=outlist.get(outlist.size()-1);
                if(object instanceof  List){
                    if(currQ instanceof StateT1_2)
                        outlist.remove(((List) object).size() - 1);
                    else if(currQ instanceof StateT1_6)
                        ((List)object).clear();
                }
                else{
                    if(currQ instanceof StateT1_2)
                        outlist.remove(outlist.size() - 1);
                    else if(currQ instanceof StateT1_6)
                        outlist.clear();
                }
            }
        }
        return list;
    }

    public State copy() throws CloneNotSupportedException {
        return (State)this.clone();
    }}
