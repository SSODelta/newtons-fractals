package com.ignatieff.newton;

public class Complex {
	
	double re, im;
	
	public static Complex ZERO = new Complex(0,0);
	
	public Complex(double re, double im){
		this.re = re;
		this.im = im;
	}
	
	public double mod(){
		return mod(this);
	}
	
	public double arg(){
		return arg(this);
	}
	
	public Complex add(double n){
		return new Complex( n + re,
							im);
	}
	
	public Complex subtract(double n){
		return new Complex( re - n,
							im);
	}
	
	public Complex multiply(double n){
		return new Complex( n * re,
							n * im);
	}
	
	public Complex divide(double n){
		return new Complex( re / n,
							im / n);
	}

	public Complex pow(double n){
		return pow(this, n);
	}
	
	public Complex add(Complex a){
		return add(this, a);
	}
	
	public Complex subtract(Complex a){
		return subtract(this, a);
	}
	
	public Complex multiply(Complex a){
		return multiply(this, a);
	}
	
	public Complex divide(Complex a){
		return divide(this, a);
	}
	
	public static Complex add(Complex a, Complex b){
		return new Complex( a.re + b.re,
							a.im + b.im);
	}
	
	public static Complex subtract(Complex a, Complex b){
		return new Complex( a.re - b.re,
							a.im - b.im);
	}
	
	public static Complex multiply(Complex a, Complex b){
		return new Complex( a.re * b.re - a.im * b.im,
							a.re * b.re + a.im * b.im);
	}
	
	public static Complex divide(Complex a, Complex b){
		double k = Math.pow(b.im, 2) + Math.pow(b.re, 2);
		return new Complex( (a.re * b.re + a.im * b.im) / k,
							(a.im * b.re - a.re * b.im) / k);
	}
	
	public static double mod(Complex a){
		return Math.sqrt(
				Math.pow(a.re, 2) +
				Math.pow(a.im, 2));
	}
	
	public static double arg(Complex a){
		return Math.atan2(a.im, a.re);
	}
	
	//(a + bi)^n = |(a + bi)| * (cos(n * theta) + i * sin(n * theta)
	public static Complex pow(Complex a, double b){
		double m = Math.pow(a.mod(), b);
		double r = b * a.arg();
		return new Complex( Math.cos(r) * m,
							Math.sin(r) * m);
	}
	
	@Override
	public String toString(){
		return re + " + i*"+im;
	}
}
