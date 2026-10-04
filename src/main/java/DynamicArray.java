public class DynamicArray {
    private int[] data;
    private int size;
    public Metrics metrics = new Metrics();

    public DynamicArray() {
        this.data = new int[10];
        this.size = 0;
    }

    public void add(int x) {
        if (size == data.length) {
            resize();
        }
        data[size++] = x;
        metrics.moves++;
    }

    public void add(int index, int x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (size == data.length) {
            resize();
        }
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
            metrics.moves++;
            metrics.steps++;
        }
        data[index] = x;
        size++;
        metrics.moves++;
    }

    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
            metrics.moves++;
            metrics.steps++;
        }
        size--;
    }

    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        metrics.steps++;
        return data[index];
    }

    public boolean contains(int x) {
        for (int i = 0; i < size; i++) {
            metrics.steps++;
            metrics.comparisons++;
            if (data[i] == x) {
                return true;
            }
        }
        return false;
    }

    private void resize() {
        int[] newData = new int[data.length * 2];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
            metrics.moves++;
            metrics.steps++;
        }
        data = newData;
    }

    public int size() {
        return size;
    }
}