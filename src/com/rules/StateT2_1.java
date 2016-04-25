package com.rules;

import com.XPath.PathParser.ASTPath;
import com.XPath.PathParser.ASTPreds;

import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by qin on 2015/10/10.
 */
public class StateT2_1 extends StateT2 {

    protected  StateT2_1(ASTPreds preds){
        super(preds);
    }

    public static StateT2 TranslateState(ASTPreds preds){//���´���T2-1
        return new StateT2_1(preds);
    }

    public List startElementDo(String tag,int layer,Stack currstack) {// layer �ǵ�ǰ tag �Ĳ���
        if((getLevel()==layer) && (tag.equals(_test))){
            TState trueQ=new TState(true);
            trueQ.setLevel(this.getLevel());
            currstack.pop();
            currstack.push(trueQ);
       }
        return list;
    }

    //ν��Ҫ�����Լ������ϲ������ǩ��������Լ��Ǽ��ʧ�ܵ�
    public List endElementDo(String tag,int layer,Stack currstack){//�Լ������ϲ������ǩ��q2-1 ���ʧ��
        if(layer==getLevel()-1){
            if(currstack.size()>1){
                currstack.pop();
                State currQ=(State)currstack.peek();
                Object object=outlist.get(outlist.size()-1);
                if(object instanceof  List){
                    if(currQ instanceof StateT1_2)
                        ((List) object).remove(((List) object).size() - 1);
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
    }

}
