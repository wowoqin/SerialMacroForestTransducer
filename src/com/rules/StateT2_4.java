package com.rules;

import com.XPath.PathParser.ASTPreds;

import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by qin on 2015/10/10.
 */
public class StateT2_4 extends StateT2 implements Cloneable{
    protected  State _q3;//��� preds

    protected  StateT2_4(ASTPreds preds,State q3){
        super(preds);
        _q3=q3;
        _predstack=new Stack();
    }

    public static StateT2 TranslateState(ASTPreds preds){//���´���T2-4
        State q3=StateT3.TranslateStateT3(preds.getFirstStep().getPreds());
        return new StateT2_4(preds,q3);
    }
    public List startElementDo(String tag,int layer,Stack currstack) throws CloneNotSupportedException{
        while ((layer >= getLevel()) && (tag.equals(_test))) {
            _q3.setLevel(layer + 1);
            if(this._predstack.isEmpty()){
                list.add(_predstack);
                this._predstack.push(_q3);//  q'(x1)
            }
            else{
                this._predstack.push(_q3.copy());//  q'(x1)
                State currQ=(State)this._predstack.peek();
                currQ.setLevel(layer + 1);
            }

        }
        return list;
    }

    public List endElementDo(String tag,int layer,Stack currstack){
        if(tag.equals(this._test)){ //�����Լ��Ľ�����ǩ�����ж��Լ����ɹ���
            State currQ=(State)this._predstack.peek();
            if(currQ instanceof TState){//ν��ջ���ɹ�
//                while(!this._predstack.isEmpty())
//                    this._predstack.pop();
                list.remove(this._predstack);
                TState trueQ=new TState(true);
                trueQ.setLevel(this.getLevel());
                currstack.pop();
                currstack.push(trueQ);
            }
            else if(currQ instanceof TWaitState || currQ instanceof FWaitState
                                                  || currQ instanceof StateT3_3||currQ instanceof StateT3_4){
                currQ.endElementDo(tag, layer, this._predstack);
                if(currQ instanceof TState){//�Ƶ�֮��ν�ʼ��ɹ�
//                    while(!this._predstack.isEmpty())
//                        this._predstack.pop();
                    list.remove(this._predstack);
                    TState trueQ=new TState(true);
                    trueQ.setLevel(this.getLevel());
                    currstack.pop();
                    currstack.push(trueQ);
                }
                else  //�Ƶ�֮���Ǽ��ʧ��
                    this._predstack.pop();
            }
            else{//ν��ջ�����ͼ��ʧ��
                this._predstack.pop();
            }
        }
        else if(layer==getLevel()-1){//�Լ������ϲ������ǩ����ʾ q2-4 ���ʧ��
                if(currstack.size()>1){
                    currstack.pop();
                    if(currstack.isEmpty())
                        list.remove(this._predstack);
                    else{
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
            }
        return list;
    }

    public State copy() throws CloneNotSupportedException {
        return (State)this.clone();
    }
}