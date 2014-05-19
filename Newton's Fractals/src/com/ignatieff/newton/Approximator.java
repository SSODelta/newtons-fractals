package com.ignatieff.newton;

public class Approximator {
	
	public static int PRECISION = 5;
	
	private Polynomial p, q;
	private Complex guess;
	
	/**
	 * Approximates a guess x_0 using Newton's method.
	 * 
	 * x_(n+1) = x_n - f(x_n) / f'(x_n)
	 * 
	 * @param p The polynomial in question.
	 * @param guess The initial guess.
	 */
	public Approximator(Polynomial p, Complex guess){
		this.p = p;
		this.q = p.derivative();
		this.guess = guess;
	}
	
	/**
	 * Improves the guess. Calculate x_(n+1) using Newton's method.
	 * @return The new, improved guess.
	 */
	private double improveGuess(){
		Complex newGuess = guess.subtract(
								p.getValue(guess).divide(q.getValue(guess)));
		double dx = newGuess.difference(guess);
		guess = newGuess;
		return dx;
	}
	
	/**
	 * Approximates the root which this complex number maps to.
	 * @return A RootTime object which is the root mapped to by the initial guess.
	 */
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
