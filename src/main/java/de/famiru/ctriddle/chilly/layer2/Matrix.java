package de.famiru.ctriddle.chilly.layer2;

public class Matrix {
    private final int[][] entries;
    private final String[][] paths;
    private final String[] descriptions;

    public Matrix(int dimension) {
        if (dimension <= 0) {
            throw new IllegalArgumentException("dimension must be > 0.");
        }

        entries = new int[dimension][dimension];
        paths = new String[dimension][dimension];
        descriptions = new String[dimension];

        for (int i = 0; i < dimension; i++) {
            descriptions[i] = "";
            for (int j = 0; j < dimension; j++) {
                paths[i][j] = "";
            }
        }
    }

    public int getDimension() {
        return entries.length;
    }

    public void setDescription(int i, String description) {
        descriptions[i] = description;
    }

    public String getDescription(int i) {
        return descriptions[i];
    }

    public String getPath(int i, int j) {
        return paths[i][j];
    }

    public void setEntry(int i, int j, int value) {
        entries[i][j] = value;
    }

    public void setPath(int i, int j, String path) {
        paths[i][j] = path;
    }

    public void swapRows(int i1, int i2) {
        int[] entryBuf = entries[i1];
        entries[i1] = entries[i2];
        entries[i2] = entryBuf;

        String[] pathBuf = paths[i1];
        paths[i1] = paths[i2];
        paths[i2] = pathBuf;
    }

    int getEntry(int i, int j) {
        return entries[i][j];
    }
}
