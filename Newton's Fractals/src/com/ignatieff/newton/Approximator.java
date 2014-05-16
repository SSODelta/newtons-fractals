package com.ignatieff.newton;

public class Approximator {
	
	public static int PRECISION = 5;
	private double DX_CUTOFF;;
	
	private Polynomial p, q;
	private Complex guess;
	
	public Approximator(Polynomial p, Complex guess){
		this.p = p;
		this.q = p.derivative();
		this.guess = guess;
		DX_CUTOFF = Math.pow(10, -PRECISION);
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
		while(dx > DX_CUTOFF){
			i++;
			dx = improveGuess();
		}
		return new RootTime(guess.update(), i);
	}
}
