package queue;

import java.io.IOException;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Queue {
        void enqueue(Object element);
    Object element();
    Object dequeue();
    int size();
    boolean isEmpty();
    void clear() throws IOException;
    AbstractQueue map(Function fnc);
    AbstractQueue filter(Predicate predicate);
    Object[] toArray();
}