package com.ignatieff.newton;

public class Approximator {
	
	public static int PRECISION = 5;
	
	private Polynomial p, q;
	private Complex guess;
	
	public Approximator(Polynomial p, Complex guess){
		this.p = p;
		this.q = p.derivative();
		this.guess = guess;
	}
	
	private double improveGuess(){
		Complex newGuess = guess.subtract(
								p.getValue(guess).divide(q.getValue(guess)));
		double dx = newGuess.difference(guess);
		guess = newGuess;
		return dx;
	}
	
	public RootTime getRoot(){
		double dx = 100;
		int i = 0;
		while(dx > 10E-15 && i<100){
			i++;
			dx = improveGuess();
		}
		//System.out.println("	"+i+": " + dx);
		return new RootTime(guess.update(), i);
	}
}
