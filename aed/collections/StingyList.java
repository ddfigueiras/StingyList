package aed.collections;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class StingyList<T> implements Iterable<T> {
    private static final long NULL = 0L;

    private int size;
    private long first;
    private long last;

    public StingyList() {
        this.size = 0;
        this.first = NULL;
        this.last = NULL;
    }

    private static long getBeyond(long node, long fromAddr) {
        return node ^ fromAddr;
    }

    static void updateNodeReference(long node, long oldAddr, long newAddr) {
        if (node == oldAddr) {
            node = newAddr;
        }
        else {
            node ^= oldAddr;
            node ^= newAddr;
        }
    }

    private static void updateBothNodeReferences(long node, long prevAddr, long nextAddr) {
        long prev = getBeyond(node, prevAddr);
        long next = getBeyond(node, nextAddr);
        updateNodeReference(prev, node, next);
        updateNodeReference(next, node, prev);
    }

    public void add(T item) {
        if (item == null)
            throw new IllegalArgumentException();
        size++;
        if (first == NULL)
            first = last = UNode.create_node(item, NULL, NULL);
        else {
            long newNode = UNode.create_node(item, last, NULL);
            UNode.set_prev_next_addr(last, UNode.get_prev_next_addr(last) ^ newNode);
            last = newNode;
        }
    }
    public T remove() {
        if (size == 0)
            throw new IndexOutOfBoundsException();
        long current = last;
        long prev = UNode.get_prev_next_addr(current) ^ NULL;

        if (prev == NULL)
            first = last = NULL;
        else {
            long prevPrev = UNode.get_prev_next_addr(prev) ^ current;
            UNode.set_prev_next_addr(prev, prevPrev);
            last = prev;
        }

        T item = UNode.get_item(current);
        UNode.free_node(current);
        size--;
        return item;
    }

    public T get() {
        if (isEmpty())
            throw new IndexOutOfBoundsException ();
        return UNode.get_item(last);
    }

    public T get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        if (index <= size / 2) {

            long current = this.first;
            long prev = NULL;

            for (int i = 0; i < index; i++) {
                long next = UNode.get_prev_next_addr(current) ^ prev;
                prev = current;
                current = next;
            }

            return UNode.get_item(current);
        }
        else {
            long current = this.last;
            long next = NULL;

            for (int i = size - 1; i > index; i--) {
                long prev = UNode.get_prev_next_addr(current) ^ next;
                next = current;
                current = prev;
            }

            return UNode.get_item(current);
        }
    }

    public T getSlow(int index) {
        if (index < 0 || index >= this.size)
            throw new IndexOutOfBoundsException("Índice fora dos limites");

        long current = this.first;
        long prev = NULL;
        for (int i = 0; i < index; i++) {
            long next = UNode.get_prev_next_addr(current) ^ prev;
            prev = current;
            current = next;
        }
        return UNode.get_item(current);
    }
    public void addAt(int index, T item) {
        if (index < 0 || index > size) 
            throw new IndexOutOfBoundsException();
        if (index == 0) {
            long newNode = UNode.create_node(item, NULL, first);
            if (first != NULL) {
                long prevNextXOR = UNode.get_prev_next_addr(first);
                UNode.set_prev_next_addr(first, newNode ^ prevNextXOR);
            }
            else
                last = newNode;
            first = newNode;
            size++;
            return;
        }

        if (index == size) {
            long newNode = UNode.create_node(item, last, NULL);
            if (last != NULL) {
                long prevNextXOR = UNode.get_prev_next_addr(last);
                UNode.set_prev_next_addr(last, newNode ^ prevNextXOR);
            }
            last = newNode;
            size++;
            return;
        }

        long prevAddr, currentAddr;
        if (index <= size / 2) {
            prevAddr = 0L;
            currentAddr = first;
            for (int i = 0; i < index; i++) {
                long nextAddr = UNode.get_prev_next_addr(currentAddr) ^ prevAddr;
                prevAddr = currentAddr;
                currentAddr = nextAddr;
            }
        } 
        else {
            prevAddr = last;
            currentAddr = 0L;
            for (int i = size; i > index; i--) {
                long prev = UNode.get_prev_next_addr(prevAddr) ^ currentAddr;
                currentAddr = prevAddr;
                prevAddr = prev;
            }
        }
        long newNode = UNode.create_node(item, prevAddr, currentAddr);
        UNode.set_prev_next_addr(prevAddr, newNode ^ UNode.get_prev_next_addr(prevAddr) ^ currentAddr);
        UNode.set_prev_next_addr(currentAddr, newNode ^ UNode.get_prev_next_addr(currentAddr) ^ prevAddr);
        size++;
    }

    public T removeAt(int index) {
        if (index < 0 || index >= size) 
            throw new IndexOutOfBoundsException();
        long current;
        long prevA = NULL;
        long nextA = NULL;
        if (index <= size / 2) {
            current = first;
            for (int i = 0; i < index; i++) {
                long temp = current;
                current = prevA ^ UNode.get_prev_next_addr(current);
                prevA = temp;
            }
            nextA = prevA ^ UNode.get_prev_next_addr(current);
        } 
        else {
            current = last;
            for (int i = size - 1; i > index; i--) {
                long temp = current;
                current = nextA ^ UNode.get_prev_next_addr(current);
                nextA = temp;
            }
            prevA = nextA ^ UNode.get_prev_next_addr(current);
        }

        T removedValue = UNode.get_item(current);
        if (prevA != 0L) {
            long temp = UNode.get_prev_next_addr(prevA);
            UNode.set_prev_next_addr(prevA, temp ^ current ^ nextA);
        }
        if (nextA != 0L) {
            long temp = UNode.get_prev_next_addr(nextA);
            UNode.set_prev_next_addr(nextA, temp ^ current ^ prevA);
        }

        if (current == first)
            first = nextA;
        if (current == last)
            last = prevA;
        UNode.free_node(current);
        size--;
        return removedValue;
    }
    public void reverse() {
        long current = last;
        long prev = NULL;
        while (current != NULL) {
            long next = getBeyond(current, prev);
            updateBothNodeReferences(current, prev, next);
            prev = current;
            current = next;
        }
        long temp = first;
        first = last;
        last = temp;
    }

    public StingyList<T> reversed() {
        StingyList<T> reversedList = new StingyList<>();
        reversedList.size = this.size;
        reversedList.first = this.last;
        reversedList.last = this.first;

        return reversedList;
    }

    public void clear() {
        long current = first;
        long prev = NULL;

        while (current != NULL) {
            long next = UNode.get_prev_next_addr(current) ^ prev;
            UNode.free_node(current);
            prev = current;
            current = next;
        }

        first = NULL;
        last = NULL;
        size = 0;
    }


    public boolean isEmpty() {
        return this.size == 0;
    }

    public int size() {
        return this.size;
    }

    public Object[] toArray() {
        Object[] array = new Object[size];
        long current = first;
        long prev = NULL;

        for (int i = 0; i < size; i++) {
            array[i] = UNode.get_item(current);
            long next = UNode.get_prev_next_addr(current) ^ prev;
            prev = current;
            current = next;
        }

        return array;
    }

    public Iterator<T> iterator() {
        return new StingyListIterator();
    }

    private class StingyListIterator implements Iterator<T> {
        private long atual = first;
        private long ant = NULL;

        @Override
        public boolean hasNext() {
            return atual != NULL;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements to iterate");
            }

            T item = UNode.get_item(atual);
            long nextNodeAddr = UNode.get_prev_next_addr(atual) ^ ant;
            ant = atual;
            atual = nextNodeAddr;
            return item;
        }
    }
    public static void main(String[] args) {
        StingyList<Integer> list = new StingyList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        list.add(8);
        list.add(9);
        list.add(10);
        //list.reverse();
        StingyList<Integer> list1 = list.reversed();
        list1.reverse();
        list.reverse();
        Object[] array = list1.toArray();
        for (Object obj : array) {
            int elemento = (int) obj;
            System.out.println(elemento);
        }
        System.out.println(" ================================= ");
        //list.removeAt(i);
        //System.out.println(removido);
    }
}