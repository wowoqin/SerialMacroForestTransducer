package com.rules;

import com.XPath.PathParser.ASTPreds;

import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by qin on 2015/10/10.
 */
public class StateT3_4 extends StateT3{
    protected  State _q31;//��� preds
    protected  State _q32;//���preds'
    protected  State _q2;//��顾desc_or_self::test preds��

    protected StateT3_4(ASTPreds preds, State q31, State q32, State q2){
        super(preds);
        _q31=q31;
        _q32=q32;
        _q2=q2;

        _predstack=new Stack();
        list.add(_predstack);

        _singlestack=new Stack();
        list.add(_singlestack);
    }

    public static StateT3 TranslateState(ASTPreds preds){//���´���T3-4
        State q31=StateT3.TranslateStateT3(preds.getFirstStep().getPreds());
        State q32=StateT3.TranslateStateT3(preds.getRemainderPreds());
        State q2= StateT2.TranslateStateT2(StateT3.getSinglePred(preds));
        return new StateT3_4(preds,q31,q32,q2);//Ȼ��ѹ��ջ
    }

    public List startElementDo(String tag,int layer,Stack currstack) throws CloneNotSupportedException {
       if(layer >= getLevel())  {
            _q2.setLevel(getLevel());
            _q32.setLevel(getLevel());
            _q31.setLevel(layer+1 );
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
        if(layer==getLevel()-1) {//q3-4�Լ������ϲ������ǩ����ʾ�Լ��Ѽ��ʧ��
            FWaitState fwaitQ = new FWaitState();
            fwaitQ.setLevel(this.getLevel());
            fwaitQ._predstack = this._predstack;
            fwaitQ._singlestack = this._singlestack;
            currstack.pop();
            currstack.push(fwaitQ);
            ((State)currstack.peek()).endElementDo(tag, layer, currstack);
        }
        return list;
    }

    public State copy() throws CloneNotSupportedException {
        return (State)this.clone();
    }}

