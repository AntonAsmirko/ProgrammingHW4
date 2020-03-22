package queue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.function.Consumer;

public class LinkedQueue extends AbstractQueue implements Iterable {
    private Node head;
    private Node tail;

    // Pre: size > 0
    //Post: queue immutable
    @Override
    public Object element() {
        assert size > 0;
        return head.value;
    }

    @Override
    protected void makeNext(Object element) {
        Node temp = tail;
        tail = new Node(element, null);
        if (size == 0)
            head = tail;
        else
            temp.next = tail;
    }

    @Override
    protected Object destroyFirst() {
        Object first = head.value;
        head = head.next;
        return first;
    }

    @Override
    protected AbstractQueue createNewDataSource() {
        return new LinkedQueue();
    }

    //Prev: nothing
    //Post: size = 0 && queue empty
    @Override
    public void clear()  {
        size = 0;
        tail = null;
        head = null;
    }

    //Prev: head != null
    //Post: queue immutable
    @Override
    public Iterator iterator() {
        return new NodeIterator(head);
    }

    private class Node {
        private Object value;
        private Node next;

        public Node(Object value, Node next) {
            assert value != null;

            this.value = value;
            this.next = next;
        }
    }

    private class NodeIterator implements Iterator{

        private Node currentNode;

        public NodeIterator(Node node){
            currentNode = node;
        }

        @Override
        public boolean hasNext() {
            return currentNode!=null;
        }

        @Override
        public Object next() {
            Object currentValue = currentNode.value;
            currentNode = currentNode.next;
            return currentValue;
        }
    }
}
