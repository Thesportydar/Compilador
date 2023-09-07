package compi;

public class TransitionMatrix<T> {
    private final int rows;
    private final int columns;
    private final T[][] matrix;

    @SuppressWarnings("unchecked")
    public TransitionMatrix(int rows, int columns) {   
        this.rows = rows;
        this.columns = columns;
        matrix = (T[][]) new Object[rows][columns];
    }

    public void set(int row, int column, T value) {
        matrix[row][column] = value;
    }

    public T get(int row, int column) {
        return matrix[row][column];
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    @Override
    // returns a string representation of the matrix
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int row = 0; row < rows; row++) {
            stringBuilder.append(matrix[row][0]);

            for (int column = 1; column < columns; column++)
                stringBuilder.append(",").append(matrix[row][column]);

            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
    
    public static void main(String[] args) {
        TransitionMatrix<Integer> matrix = new TransitionMatrix<>(3, 3);

        matrix.set(0, 0, 1);
        matrix.set(0, 1, 2);
        matrix.set(0, 2, 3);
        matrix.set(1, 0, 4);
        matrix.set(1, 1, 5);
        matrix.set(1, 2, 6);
        matrix.set(2, 0, 7);
        matrix.set(2, 1, 8);
        matrix.set(2, 2, 9);

        System.out.println(matrix);
    }
}
