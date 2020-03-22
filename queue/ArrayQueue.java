package queue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class ArrayQueue extends AbstractQueue implements Iterable {

    private int INITIAL_CAPACITY = 8;
    private Object[] ringBufferElements = new Object[INITIAL_CAPACITY];
    private int rear = -1;
    private int front = 0;

    private ArrayQueue(int initialCapacity){
        INITIAL_CAPACITY = initialCapacity;
        ringBufferElements = new Object[initialCapacity];
    }

    public ArrayQueue(){

    }

    @Override
    protected void makeNext(Object item) {
        if (size >= ringBufferElements.length || ringBufferElements.length == 0) {
            expandBuffer();
        }
        rear = (rear + 1) % ringBufferElements.length;
        ringBufferElements[rear] = item;
    }

    @Override
    protected Object destroyFirst() {
        Object deQueuedElement = ringBufferElements[front];
        ringBufferElements[front] = null;
        front = (front + 1) % ringBufferElements.length;
        return deQueuedElement;
    }

    //Pre: size > 0 && item != null
    /*Post: ringBufferElements[front] = item && ∀i >= 1 'ringBufferElements[i] = ringBufferElements[i-1]*/
    public void push(Object item) {
        assert size > 0 && item != null;
        if (size < ringBufferElements.length) {
            front = front - 1;
            if (front < 0) {
                front += ringBufferElements.length;
            }
        } else {
            expandBuffer();
            front = front - 1 + ringBufferElements.length;
        }
        ringBufferElements[front] = item;
        size++;
    }

    //Pre: size > 0
    //Post: ℝ = ringBufferElements[rear] && queue immutable
    public Object peek() {
        assert size > 0;
        if (rear < 0)
            rear += ringBufferElements.length;
        return ringBufferElements[rear];
    }

    //Pre: size > 0
    //Post ℝ = ringBufferElements[rear] ∧ ∀i=front..rear-1 : ringBufferElements[i]' = ringBufferElements[i] && size--
    public Object remove() {
        assert size > 0;
        Object result;
        if (rear < 0)
            rear += ringBufferElements.length;
        result = ringBufferElements[rear];
        ringBufferElements[rear] = null;
        rear = rear - 1;
        size--;
        if (rear < 0 && size != 0) {
            rear += ringBufferElements.length;
        }
        return result;
    }

    //Prev: nothing
    //Post: size = 0 && queue empty
    public void clear() {
        ringBufferElements = new Object[INITIAL_CAPACITY];
        size = 0;
        front = 0;
        rear = -1;
    }

    @Override
    protected AbstractQueue createNewDataSource() {
        return new ArrayQueue(size);
    }

    // Pre: size > 0
    //Post: queue immutable
    public Object element() {
        return ringBufferElements[front];
    }

    private void expandBuffer() {
        Object[] ringBufferElementsExpanded = new Object[2 * (size != 0? size : 8)];
        ringBufferElements = orderArray(ringBufferElementsExpanded);
        front = 0;
        rear = size - 1;
    }

    //Pre: ringBufferElements != null
    //Post: queue immutable
    public String toStr() {
        assert ringBufferElements != null;
        if (size == 0) {
            return "[]";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        for (int i = front; i < front + size; i++) {
            stringBuilder.append(ringBufferElements[i % ringBufferElements.length]);
            stringBuilder.append(", ");
        }
        stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), "");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    //Pre: ringBufferElements != null
    //Post: return new array[size] with all elements of ringBufferElements front ... rear
    public Object[] toArray() {
        assert ringBufferElements != null;
        if (size == 0) {
            return new Object[0];
        }
        return orderArray(new Object[size]);
    }


    private Object[] orderArray(Object[] arr) {
        if (front == 0 || front <= rear) {
            System.arraycopy(ringBufferElements, front, arr, 0, size);
        } else {
            System.arraycopy(ringBufferElements, front, arr, 0, ringBufferElements.length - front);
            System.arraycopy(ringBufferElements, 0, arr, ringBufferElements.length - front, rear + 1);
        }
        return arr;
    }

    //Prev: head != null
    //Post: queue immutable
    @Override
    public Iterator iterator() {
        return Arrays.stream(orderArray(new Object[ringBufferElements.length])).filter(Objects::nonNull).iterator();
    }
}
