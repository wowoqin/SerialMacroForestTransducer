package com.rules;

import com.XPath.PathParser.ASTPath;

import java.util.List;
import java.util.Stack;

/**
 * Created by qin on 2015/10/10.
 */
public class StateT1_2 extends StateT1 {
    protected State _q3;//��� preds

    protected StateT1_2(ASTPath path, State q3) {
        super(path);
        _q3 = q3;
    }

    public static State TranslateState(ASTPath path) {//���´���T1-2
        State q3 = StateT3.TranslateStateT3(path.getFirstStep().getPreds());
        return new StateT1_2(path, q3);
    }

    public List startElementDo(String tag, int layer,Stack currstack) {// layer ��ʾ��ǰ��ǩ tag �Ĳ���
        if((getLevel() == layer) && (tag.equals(_test))) {//Ӧ��ƥ��Ĳ��� getLayer������ ��ǰ��ǩ tag �Ĳ������
            _q3.setLevel(getLevel() + 1);//����ν�ʵĲ����϶��ǵ�ǰӦ��ƥ���������Ӧ�ı�ǩ������Ĳ���
            currstack.push(_q3);

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

    public List checkOthers(String tag, int layer,Stack currstack) {
        if(!outlist.isEmpty())
            System.out.println(outlist);
        return list;
    }


    public State copy() throws CloneNotSupportedException {
        return (State)this.clone();
    }

}
