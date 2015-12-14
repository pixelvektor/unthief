package data;

// Source: http://www.theorie.physik.uni-goettingen.de/~honecker/rb07/Complex.java

public class Complex {
	private final double re;
	private final double im;
	
	public Complex(final double re, final double im) {
		this.re = re;
		this.im = im;
	}
	
	public Complex(final double re) {
		this.re = re;
		this.im = 0.;
	}
	
	public double getRe() {
		return re;
	}
	
	public double getIm() {
		return im;
	}
	
	public double getAbs() {
		return Math.sqrt(re * re + im * im);
	}
	
	public Complex add(final Complex c) {
		return new Complex(re + c.getRe(), im + c.getIm());
	}
	
	public Complex subtract(final Complex c) {
		return new Complex(re - c.getRe(), im - c.getIm());
	}
	
	public Complex times(final Complex c) {
		return new Complex(re * c.getRe() - im * c.getIm(),
				re * c.getIm() + im * c.getRe());
	}
	
	public Complex times(final double d) {
		return new Complex(re * d, im * d);
	}
	
	public Complex divide(final Complex c) {
		return this.times(c.conjugate())
				.divide(c.getRe() * c.getRe() + c.getIm() * c.getIm());
	}
	
	public Complex divide(final double d) {
		if (d == 0) {
			System.err.println("Complex: Division by 0");
		}
		return new Complex(re / d, im / d);
	}
	
	public Complex conjugate() {
		return new Complex(re, -im);
	}
}
