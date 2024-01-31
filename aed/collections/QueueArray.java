package aed.collections;
import java.util.Iterator;
import java.util.Arrays;

public class QueueArray<Item> implements Iterable<Item> {

    private final int max_size;
    private Item[] queue;
    private int size;
    private int first;
    private int last;

    @SuppressWarnings("unchecked")
    public QueueArray(int max_size) {
        this.max_size = max_size;
        this.queue = (Item[]) new Object[max_size];
        this.size = 0;
        this.first = 0;
        this.last = -1;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (size >= max_size) {
            throw new OutOfMemoryError();
        }
        size++;
        last = (last + 1) % max_size;
        queue[last] = item;
    }

    public Item dequeue() {
        if (isEmpty()) {
            return null;
        }
        Item top = queue[first];
        queue[first] = null;
        first = (first + 1) % max_size;
        size--;

        return top;

    }

    public Item peek() {
        if (isEmpty()) {
            return null;
        }
        Item top = this.queue[first];
        return top;
    }

    public boolean isEmpty() {
        return size == 0;
    }


    public int size() {
        return this.size;
    }

    public QueueArray<Item> shallowCopy() {
        QueueArray<Item> new_array = new QueueArray<>(max_size);

        Item[] newArray = Arrays.copyOf(queue, max_size);
        new_array.queue = newArray;
        new_array.size = size;
        new_array.first = first;
        new_array.last = last;
        return new_array;
    }


    private class QueueIterator implements Iterator<Item> {

        private int i=0;
        @Override
        public boolean hasNext(){
            return i<size;
        }

        @Override
        public Item next(){
            Item result=queue[(first+i)%max_size];
            i++;
            return result;
        }

        @Override
        public void remove(){
            throw new UnsupportedOperationException("Iterator doesn't support removal");
        }
    }

    public Iterator<Item> iterator() {
        return new QueueIterator();
    }


    public static void main(String[] args){



    }
}



