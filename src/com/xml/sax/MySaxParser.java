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
    //主查询栈
    protected Stack stack;
    //此处应有一个list 的引用，引用到State 中的list，因为在此要进行 遍历list中的stack的栈顶的操作
    protected List llist;

    protected List currentlist;

    protected Stack currentstack;

    public MySaxParser(String path_str){
        super();
        qp = new QueryParser();
        path = qp.parseXPath(path_str);
        currentQ = StateT1.TranslateStateT1(path);//将XPath翻译为各个状态
        stack=new Stack();
        stack.push(currentQ);//先将第一个q压入栈
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
        //来一个tag时，遍历qqueue，所有的在当前队列中的stack都要进行operation操作，即在此时就要进行遍历队列
        if (currentlist.size()==1){
            if(!stack.empty()){
                currentQ=(State)stack.peek();
                try {
                    llist=currentQ.startElementDo(qName, layer, stack);//对当前栈顶元素执行operation操作--即进栈操作
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
                    currentQ=(State)currentstack.peek();//找到当前 stack 的栈顶元素
                    try {
                        llist=currentQ.startElementDo(qName, layer, currentstack);//对当前栈顶元素执行operation操作--即进栈操作
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        currentlist=llist;
        layer++;//layer 是表示在 XML 流中的标签的层数
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        layer--;
        if (!stack.isEmpty()) {
            if (stack.size() == 1) {//AD轴
                currentQ = (State) stack.peek();
                if (qName.equals(currentQ.getNodeTest()))//遇到自己的结束标签，做输出
                    llist = currentQ.checkOthers(qName, layer, stack);
                else {//遇到普通的结束标签
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
            else {//PC轴
                int size = currentlist.size();
                for (int i = 0; i < size; i++) {
                    currentstack = (Stack) currentlist.get(i);
                    if (!currentstack.empty()) {
                        currentQ = (State) currentstack.peek();
                        llist = currentQ.endElementDo(qName, layer, currentstack);
                    }
                }
                if (stack.size() == 1) {//遍历完之后看是否是XPath的结束标签，若是，进行检查输出操作
                    currentQ = (State) stack.peek();
                    if (qName.equals(currentQ.getNodeTest()))//遇到自己的结束标签，做输出
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