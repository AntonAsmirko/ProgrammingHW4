package queue;

public class Test {
    public static void main(String[] args) {
        AbstractQueue arrayQueue = new ArrayQueue();
        arrayQueue.enqueue("1");
        arrayQueue.enqueue("2");
        arrayQueue.enqueue("3");
        arrayQueue.enqueue("4");
        for (Object item: arrayQueue.map(String::valueOf)){
            System.out.println(item);
        }
    }
}
