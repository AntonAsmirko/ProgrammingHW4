package queue;

import javax.swing.text.html.HTMLDocument;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractQueue implements Queue, Iterable {
    protected int size = 0;

    protected abstract void makeNext(Object item);

    protected abstract Object destroyFirst();

    //Prev: size > 0 && fnc != null
    //Post: returns new queue[size]
    @Override
    public AbstractQueue map(Function fnc) {
        AbstractQueue newQueue = createNewDataSource();
        for (Object item : this) {
            newQueue.enqueue(fnc.apply(item));
        }
        return newQueue;
    }

    //Prev: size > 0 && fnc != null
    //Post: returns new queue
    @Override
    public AbstractQueue filter(Predicate predicate){
        AbstractQueue newQueue = createNewDataSource();
        for (Object item : this) {
            if (predicate.test(item))
                newQueue.enqueue(item);
        }
        return newQueue;
    }

    @Override
    // Pre: size > 0
    /* Post: size-- && returns ringBufferElements[front]*/
    public Object dequeue() {
        assert size > 0;
        size--;
        return destroyFirst();
    }

    // Pre: item != null
    /* Post: size' = size + 1 && ringBufferElements[size' - 1] = element*/;
    public void enqueue(Object item) {
        assert item != null;
        makeNext(item);
        size++;
    }

    //Prev: nothing
    //Post: queue immutable
    @Override
    public int size() {
        return size;
    }

    //Prev: nothing
    //Post: queue immutable
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Object[] toArray(){
        int resultArrCurPos = 0;
        Object[] resultArr = new Object[size];
        for(Object item: this){
            resultArr[resultArrCurPos] = item;
            resultArrCurPos++;
        }
        return resultArr;
    }

    //Prev: nothing
    //Post: queue immutable
    protected abstract AbstractQueue createNewDataSource();
}
