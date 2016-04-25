package com.rules;

import com.XPath.PathParser.ASTPreds;

import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by qin on 2015/10/10.
 */
public class StateT3_2 extends StateT3{
    protected  State _q31;//检查 preds
    protected  State _q32;//检查preds'
    protected  State _q2;//检查【child::test preds】

    protected  StateT3_2(ASTPreds preds,State q31,State q32,State q2){
        super(preds);
        _q31=q31;
        _q32=q32;
        _q2=q2;
        _predstack=new Stack();
        _singlestack=new Stack();
    }

    public static StateT3 TranslateState(ASTPreds preds){//重新创建T3-2
        State q31=StateT3.TranslateStateT3(preds.getFirstStep().getPreds());
        State q32=StateT3.TranslateStateT3(preds.getRemainderPreds());
        State q2= StateT2.TranslateStateT2(StateT3.getSinglePred(preds));
        return new StateT3_2(preds,q31,q32,q2);
    }
    public List startElementDo(String tag,int layer,Stack currstack) throws CloneNotSupportedException {
        if (getLevel() == layer) {
            _q2.setLevel(getLevel());//检查【child::test preds】
            _q31.setLevel(getLevel()+1);//检查preds
            _q32.setLevel(getLevel());//检查preds'
            if (tag.equals(_test)) {
                currstack.push(_q31);

                if(this._predstack.isEmpty()){
                    list.add(_predstack);
                    this._predstack.push(_q32);
                }
                if(this._singlestack.isEmpty()){
                    list.add(_singlestack);
                    this._singlestack.push(_q2);//q'''(X2)
                }
                ((State)this._predstack.peek()).startElementDo(tag, layer, this._predstack);
            }
            else {
                FWaitState  fwaitQ=new FWaitState();
                fwaitQ.setLevel(this.getLevel());
                currstack.pop();
                currstack.push(fwaitQ);

                fwaitQ._singlestack.push(_q2);
                fwaitQ._predstack.push(_q32);
                ((State)fwaitQ._predstack.peek()).startElementDo(tag, layer, fwaitQ._predstack);
            }
        }
        return list;
    }

    public List endElementDo(String tag,int layer,Stack currstack){
        if(layer==getLevel()-1){//q3-2 自己遇到上层结束标签，表示自己已检查失败
            FWaitState  fwaitQ=new FWaitState();
            fwaitQ.setLevel(this.getLevel());
            fwaitQ._predstack=this._predstack;
            fwaitQ._singlestack=this._singlestack;
            currstack.pop();
            currstack.push(fwaitQ);
            ((State)currstack.peek()).endElementDo(tag, layer, currstack);
        }
        return list;
    }

    public State copy() throws CloneNotSupportedException {
        return (State)this.clone();
    }
}
