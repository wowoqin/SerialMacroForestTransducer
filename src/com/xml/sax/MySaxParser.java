package com.xml.sax;

import com.XPath.PathParser.ASTPath;
import com.XPath.PathParser.QueryParser;
import com.rules.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.*;

/**
 * Created by toy on 15-4-27.
 */
public class MySaxParser<T> extends DefaultHandler {

    protected QueryParser qp ;
    protected ASTPath path;
    protected int layer;
    protected State currentQ;
    //����ѯջ
    protected Stack stack;
    //�˴�Ӧ��һ��list �����ã����õ�State �е�list����Ϊ�ڴ�Ҫ���� ����list�е�stack��ջ���Ĳ���
    protected List llist;

    protected List currentlist;

    protected Stack currentstack;

    public MySaxParser(String path_str){
        super();
        qp = new QueryParser();
        path = qp.parseXPath(path_str);
        currentQ = StateT1.TranslateStateT1(path);//��XPath����Ϊ����״̬
        stack=new Stack();
        stack.push(currentQ);//�Ƚ���һ��qѹ��ջ
        State.list.add(stack);//
        currentlist=State.list;
        llist=currentlist;
    }

    @Override
    public void startDocument() throws SAXException {
        System.out.println("----------- Start Document ----------");
        System.out.println(State.list.size());
        layer = 0;
        super.startDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //��һ��tagʱ������qqueue�����е��ڵ�ǰ�����е�stack��Ҫ����operation���������ڴ�ʱ��Ҫ���б�������
        if (currentlist.size()==1){
            if(!stack.empty()){
                currentQ=(State)stack.peek();
                try {
                    llist=currentQ.startElementDo(qName, layer, stack);//�Ե�ǰջ��Ԫ��ִ��operation����--����ջ����
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            int size=currentlist.size();
            for(int i=(size-1);i>=0;i--){
                currentstack=(Stack)currentlist.get(i);
                if(!currentstack.empty()){
                    currentQ=(State)currentstack.peek();//�ҵ���ǰ stack ��ջ��Ԫ��
                    try {
                        llist=currentQ.startElementDo(qName, layer, currentstack);//�Ե�ǰջ��Ԫ��ִ��operation����--����ջ����
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        currentlist=llist;
        layer++;//layer �Ǳ�ʾ�� XML ���еı�ǩ�Ĳ���
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        layer--;
        if (!stack.isEmpty()) {
            if (stack.size() == 1) {//AD��
                currentQ = (State) stack.peek();
                if (qName.equals(currentQ.getNodeTest()))//�����Լ��Ľ�����ǩ�������
                    llist = currentQ.checkOthers(qName, layer, stack);
                else {//������ͨ�Ľ�����ǩ
                    int size = currentlist.size();
                    for (int i = 1; i < size; i++) {
                        currentstack = (Stack) currentlist.get(i);
                        if (!currentstack.empty()) {
                            currentQ = (State) currentstack.peek();
                            llist = currentQ.endElementDo(qName, layer, currentstack);
                        }
                    }
                }
            }
            else {//PC��
                int size = currentlist.size();
                for (int i = 0; i < size; i++) {
                    currentstack = (Stack) currentlist.get(i);
                    if (!currentstack.empty()) {
                        currentQ = (State) currentstack.peek();
                        llist = currentQ.endElementDo(qName, layer, currentstack);
                    }
                }
                if (stack.size() == 1) {//������֮���Ƿ���XPath�Ľ�����ǩ�����ǣ����м���������
                    currentQ = (State) stack.peek();
                    if (qName.equals(currentQ.getNodeTest()))//�����Լ��Ľ�����ǩ�������
                        llist = currentQ.checkOthers(qName, layer, stack);
                }
            }
            currentlist = llist;
            super.endElement(uri, localName, qName);
        }
    }




    @Override
    public void endDocument() throws SAXException{
        System.out.println("----------- End  Document ----------");
        super.endDocument();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String content = new String(ch,start,length);
        super.characters(ch, start, length);
    }

}