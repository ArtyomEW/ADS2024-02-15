package by.it.group351004.zalivako.lesson09;

import java.util.*;

public class ListC<E> implements List<E> {

    private static final int DEFAULT_CAPACITY = 10;
    private static final Object[] DEFAULT_EMPTY_ARRAY = {};
    private int size = 0;
    private Object[] elementData;

    private class Itr implements Iterator<E> {
        int cursor;       // индекс текущего элемента
        int lastRet = -1; // индекс предыдущего возвращенного элемента, -1 если такого нет

        Itr () {};
        public boolean hasNext() {
            return cursor != size;
        }

        @SuppressWarnings("unchecked")
        public E next() {
            int i = cursor;
            if (i >= size)
                throw new NoSuchElementException();
            cursor = i + 1;
            return (E) ListC.this.elementData[lastRet = i];
        }
    }

    private class ListItr extends Itr implements ListIterator<E> {
        ListItr(int i) {
            super();
            cursor = i;
        }
        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            ListC.this.remove(lastRet);
            cursor = lastRet;
            lastRet = -1;
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        @SuppressWarnings("unchecked")
        public E previous() {
            if (cursor < 1)
                throw new NoSuchElementException();
            cursor--;
            return (E) ListC.this.elementData[lastRet = cursor];
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        public void set(E e) {
            if (lastRet < 0)
                throw new IllegalStateException();
            cursor = lastRet;
            ListC.this.set(lastRet, e);
        }

        public void add(E e) {
            int i = cursor;
            ListC.this.add(i, e);
            cursor = i + 1;
            lastRet = -1;
        }
    }

    public ListC(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elementData = DEFAULT_EMPTY_ARRAY;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+ initialCapacity);
        }
        this.size = initialCapacity;
    }

    public ListC() {
        this.elementData = DEFAULT_EMPTY_ARRAY;
    }

    @Override
    public String toString() {
        ListIterator<E> it = listIterator();
        if (!it.hasNext())
            return "[]";
        StringBuilder result = new StringBuilder();
        result.append("[");
        while (it.hasNext()) {
            result.append(it.next());
            if (it.hasNext())
                result.append(", ");
        }
        return result + "]";
    }

    private Object[] grow(int minCapacity) {
        int oldCapacity = elementData.length;
        if (oldCapacity > 0) {
            int newCapacity = oldCapacity << 1;
            return elementData = Arrays.copyOf(elementData, newCapacity);
        } else {
            return elementData = new Object[Math.max(DEFAULT_CAPACITY, minCapacity)];
        }
    }

    @Override
    public boolean add(E e) {
        if (elementData == null || size == elementData.length)
            elementData = grow(size + 1);
        elementData[size] = e;
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        Objects.checkIndex(index, size); //Проверяет, находится ли индекс в заданных пределах, если не находится, то выбрасывается исключение outOfBoundsCheckIndex
        E oldValue = (E) elementData[index];
        int newSize = size - 1;
        if (newSize != index)
            System.arraycopy(elementData, index + 1, elementData, index, newSize - index);
        elementData[size = newSize] = null;
        return oldValue;
    }

    @Override
    public int size() {
        return size;
    }

    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + size;
    }

    @Override
    public void add(int index, E element) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        if (elementData == null || size == elementData.length)
            elementData = grow(size + 1);
        System.arraycopy(elementData, index, elementData, index + 1, size - index);
        elementData[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        Iterator<E> it = listIterator();
        int index = 0;
        while (it.hasNext()) {
            if (o.equals(it.next())) {
                remove(index);
                break;
            }
            index++;
        }
        return index != size;
    }

    @Override
    public E set(int index, E element) {
        Objects.checkIndex(index, size);
        E oldValue = (E) elementData[index];
        elementData[index] = element;
        return oldValue;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        for (int i = 0; i < size; i++)
            elementData[i] = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        Iterator<E> it = listIterator();
        int index = 0;
        while (it.hasNext()) {
            if (o.equals(it.next()))
                return index;
            index++;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        Objects.checkIndex(index, size);
        return (E) elementData[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        ListIterator<E> it = listIterator(size);
        int index = size - 1;
        while (it.hasPrevious()) {
            if (o.equals(it.previous()))
                return index;
            index--;
        }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c)
            if (!contains(o))
                return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c)
            add(e);
        return !c.isEmpty();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (elementData.length < size + c.size())
            grow(size + c.size());
        System.arraycopy(elementData, index, elementData, index + c.size(), size - index);
        int insIndex = index;
        for (E e : c) {
            elementData[insIndex] = e;
            insIndex++;
        }
        size += c.size();
        return !c.isEmpty();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean isChanged = false;
        ListIterator<E> it;
        for (Object o : c) {
            it = listIterator();
            while (it.hasNext())
                if (o.equals(it.next()))
                    it.remove();
            isChanged = true;
        }
        return isChanged;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        int deletedCount = 0;
        int fullSize = size;
        for (int i = 0; i < fullSize; i++) {
            if (!c.contains(elementData[i - deletedCount])) {
                remove(i - deletedCount);
                deletedCount++;
            }
        }
        return deletedCount != 0;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ListItr(index);
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

}
