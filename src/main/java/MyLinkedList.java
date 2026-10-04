public class MyLinkedList {
    private static class Node {
        int value;
        Node next;
        Node prev;

        Node(int value) {
            this.value = value;
        }
    }

    private Node head;
    private Node tail;
    private int size;
    public Metrics metrics = new Metrics();

    public void add(int x) {
        Node node = new Node(x);
        if (tail == null) {
            head = tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        size++;
        metrics.moves += 2;
    }

    public void add(int index, int x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (index == size) {
            add(x);
            return;
        }
        Node node = new Node(x);
        if (index == 0) {
            node.next = head;
            head.prev = node;
            head = node;
        } else {
            Node current = getNode(index);
            node.prev = current.prev;
            node.next = current;
            current.prev.next = node;
            current.prev = node;
        }
        size++;
        metrics.moves += 3;
    }

    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node current = getNode(index);
        if (current.prev != null) {
            current.prev.next = current.next;
        } else {
            head = current.next;
        }
        if (current.next != null) {
            current.next.prev = current.prev;
        } else {
            tail = current.prev;
        }
        size--;
        metrics.moves += 2;
    }

    public int get(int index) {
        return getNode(index).value;
    }

    public boolean contains(int x) {
        Node current = head;
        while (current != null) {
            metrics.steps++;
            metrics.comparisons++;
            if (current.value == x) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    private Node getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
            metrics.steps++;
        }
        metrics.steps++;
        return current;
    }

    public int size() {
        return size;
    }
}