package com.rules;

import com.XPath.PathParser.ASTPreds;

import java.util.List;
import java.util.Stack;

/**
 * Created by qin on 2015/10/10.
 */
public class StateT3_1 extends StateT3{
    protected  State _q3;//���preds'
    protected  State _q2;//��顾child::test preds��

    protected  StateT3_1(ASTPreds preds,State q3,State q2){
        super(preds);
        _q3=q3;
        _q2=q2;
    }

    public static StateT3 TranslateState(ASTPreds preds){//���´���T3-1
        State q3=StateT3.TranslateStateT3(preds.getRemainderPreds());
        State q2=StateT2.TranslateStateT2(StateT3.getSinglePred(preds));
        return new StateT3_1(preds,q3,q2);
    }
    public List startElementDo(String tag,int layer,Stack currstack) throws CloneNotSupportedException {
        //���� q3-1 �о�Ӧ�ö� preds' ͬʱ���м��
        if(getLevel() == layer) {//Ӧ��ƥ��Ĳ��� getLayer������ ��ǰ��ǩ tag �Ĳ������
            _q2.setLevel(getLevel() );//q2 ��顾child::test preds����Ӧ��ƥ��ı�ǩ�Ĳ��� ����
            _q3.setLevel(getLevel());// q3 ���preds'��Ӧ��ƥ��ı�ǩ�Ĳ����뵱ǰ [test] ͬһ��
            if (tag .equals(_test)){
                TWaitState twaitQ=new TWaitState();
                twaitQ.setLevel(this.getLevel());
                currstack.pop();
                currstack.push(twaitQ);
                twaitQ._predstack.push(_q3);
                ((State)twaitQ._predstack.peek()).startElementDo(tag, layer, twaitQ._predstack);
            }
            else {
                FWaitState  fwaitQ=new FWaitState();
                fwaitQ.setLevel(this.getLevel());
                currstack.pop();
                currstack.push(fwaitQ);

                fwaitQ._singlestack.push(_q2);
                fwaitQ._predstack.push(_q3);
                ((State) fwaitQ._predstack.peek()).startElementDo(tag, layer, fwaitQ._predstack);
            }
        }
        return list;
    }

    //���۳ɰܣ�q3-1 �����滻������ q3-1 ���������ϲ������ǩ


    public State copy() throws CloneNotSupportedException {
        return (State)this.clone();
    }
}

