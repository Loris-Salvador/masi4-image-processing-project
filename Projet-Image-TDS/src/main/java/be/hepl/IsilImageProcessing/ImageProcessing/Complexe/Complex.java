package be.hepl.IsilImageProcessing.ImageProcessing.Complexe;

public class Complex {
    double real;
    double imag;

    public Complex(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }

    public Complex add(Complex other) {
        return new Complex(this.real + other.real, this.imag + other.imag);
    }

    public Complex multiply(Complex other) {
        double real = this.real * other.real - this.imag * other.imag;
        double imag = this.real * other.imag + this.imag * other.real;
        return new Complex(real, imag);
    }

    @Override
    public String toString() {
        return String.format("(%.2f %s %.2fi)",
                real,
                imag >= 0 ? "+" : "-",
                Math.abs(imag));
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imag;
    }
}
