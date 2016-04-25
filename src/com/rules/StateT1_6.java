package com.rules;

import com.XPath.PathParser.ASTPath;
import com.XPath.PathParser.ASTPreds;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by qin on 2015/10/10.
 */
public class StateT1_6 extends StateT1{
    protected  State  _q3;//��� preds
    protected  State  _q1;//������ path
    protected StateT1_6(ASTPath path,State q3,State q1){
        super(path);
        _q3=q3;
        _q1=q1;
        _pathstack =new Stack();
    }

    public static State TranslateState(ASTPath path){//���´���T1-6
        State q3=StateT3.TranslateStateT3(path.getFirstStep().getPreds());
        State q1=StateT1.TranslateStateT1(path.getRemainderPath());
        return new StateT1_6(path,q3,q1);//Ȼ��ѹ��ջ
    }
    @Override
    public List startElementDo(String tag,int layer,Stack currstack) {// layer �ǵ�ǰ tag �Ĳ���
        if((getLevel() == layer)  && (tag.equals(_test))){//Ӧ��ƥ��Ĳ��� getLevel������ ��ǰ�������
            _q1.setLevel(getLevel() + 1);
            _q3.setLevel(getLevel() + 1);
            if(this._pathstack.isEmpty()){//��ǰ����� tag ��xpath �е� test �ڵ���ƥ��
                list.add(_pathstack);
                currstack.push(_q3);//  q'(x1)
                _pathstack.push(_q1);//q''(x1)
            }
            else{
                currstack.push(_q3);//  q'(x1)
                _pathstack.push(_q1);//q''(x1)
            }
        }
        return list;
    }

    public List endElementDo(String tag,int layer,Stack currstack){
        if(layer==getLevel()-1){
            currstack.pop();
            if(currstack.isEmpty())
                list.remove(this._pathstack);
        }
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
