public class MinHeap {
    private int[] data;
    private int size;
    public Metrics metrics = new Metrics();

    public MinHeap(int capacity) {
        this.data = new int[capacity > 0 ? capacity : 10];
        this.size = 0;
    }

    public void insert(int x) {
        if (size == data.length) {
            resize();
        }
        data[size] = x;
        metrics.moves++;
        bubbleUp(size);
        size++;
    }

    public int peekMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        metrics.steps++;
        return data[0];
    }

    public int extractMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        int min = data[0];
        metrics.steps++;
        data[0] = data[size - 1];
        metrics.moves++;
        size--;
        if (size > 0) {
            bubbleDown(0);
        }
        return min;
    }

    private void bubbleUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            metrics.steps += 2;
            metrics.comparisons++;
            if (data[index] >= data[parent]) {
                break;
            }
            swap(index, parent);
            index = parent;
        }
    }

    private void bubbleDown(int index) {
        while (index * 2 + 1 < size) {
            int left = index * 2 + 1;
            int right = index * 2 + 2;
            int smallest = left;

            metrics.steps += 2;
            if (right < size) {
                metrics.comparisons++;
                if (data[right] < data[left]) {
                    smallest = right;
                }
            }

            metrics.comparisons++;
            if (data[index] <= data[smallest]) {
                break;
            }
            swap(index, smallest);
            index = smallest;
        }
    }

    private void swap(int i, int j) {
        int temp = data[i];
        data[i] = data[j];
        data[j] = temp;
        metrics.moves += 3;
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