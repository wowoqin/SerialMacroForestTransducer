package com.rules;

import com.XPath.PathParser.ASTPreds;
import com.XPath.PathParser.AxisType;

import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by qin on 2015/10/10.
 */
public class StateT2 extends  State implements Cloneable{

    protected ASTPreds _preds;
    protected String _test;

    protected Stack _predstack;

    protected StateT2(ASTPreds preds){
        _preds=preds;
        _test=_preds.getFirstStep().getNodeTest().toString();
    }

    public String getNodeTest(){//�õ���ǰ preds �Ĳ��Խڵ�
        return _preds.getFirstStep().getNodeTest().toString();
    }

    public static StateT2 TranslateStateT2(ASTPreds preds){
        //����������ѡ���Եĵ���T2����

        if(preds.getFirstStep().getAxisType()== AxisType.PC)
        {
            if (preds.getFirstStep().getPreds().toString().equals(""))
                return StateT2_1.TranslateState(preds);//�޺���ν��
            return StateT2_2.TranslateState(preds);//�к���ν��
        }
        if (preds.getFirstStep().getPreds().toString().equals("")) //AD
            return StateT2_3.TranslateState(preds);//�޺���ν��
        return StateT2_4.TranslateState(preds);//�к���ν��
    }


    public State copy() throws CloneNotSupportedException {
        return (State)this.clone();
    }

    @Override
    public List startElementDo(String tag, int layer,Stack currstack) throws CloneNotSupportedException {
        return list;
    }

    @Override
    public List endElementDo(String tag,int layer,Stack currstack){
        if(layer == getLevel()-1)
            currstack.pop();
        return list;
    }

    @Override
    public List checkOthers(String tag, int layer, Stack currstack) {
        return list;
    }
}
