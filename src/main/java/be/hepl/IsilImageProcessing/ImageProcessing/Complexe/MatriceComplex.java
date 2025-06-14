package be.hepl.IsilImageProcessing.ImageProcessing.Complexe;

public class MatriceComplex {
    private Complex m[][];
    private int rows;
    private int cols;

    public MatriceComplex(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        m = new Complex[rows][cols];
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < cols; j++)
                m[i][j] = new Complex(0.0, 0.0);
    }

    public void set(int row, int col, Complex complex) {
        m[row][col] = complex;
    }

    public void set(int row, int col, double realPart, double imaginaryPart) {
        Complex c = new Complex(realPart, imaginaryPart);
        set(row, col, c);
    }

    public Complex get(int row, int col) {
        return m[row][col];
    }

    public double[][] getRealPart() {
        double d[][] = new double[rows][cols];
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < cols; j++)
                d[i][j] = m[i][j].getReal();
        return d;
    }

    public double[][] getImaginaryPart() {
        double d[][] = new double[rows][cols];
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < cols; j++)
                d[i][j] = m[i][j].getImaginary();
        return d;
    }

    public double[][] getModule() {
        double d[][] = new double[rows][cols];
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < cols; j++)
                d[i][j] = m[i][j].getModule();
        return d;
    }

    public double[][] getPhase() {
        double d[][] = new double[rows][cols];
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < cols; j++)
                d[i][j] = m[i][j].getPhase();
        return d;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}
