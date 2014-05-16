package com.ignatieff.newton;

public class Polynomial {
	public double[] terms;
	
	public Polynomial(double[] terms){
		this.terms = terms;
	}
	
	@Override
	public String toString(){
		StringBuilder s = new StringBuilder();
		
		for(int i=terms.length; i>0; i--){
			if(i!=terms.length){s.append(" + ");}
			if(i-1 == 0){
				s.append(terms[(i-1)]);
				continue;
			}
			if(i-1 == 1){
				s.append(terms[(i-1)] + " * x");
				continue;
			}
			s.append(terms[(i-1)] + " * x^"+(i-1));
		}
		
		return s.toString();
	}
	
	public Polynomial derivative(){
		if(terms == null)return null;
		double[] newTerms = new double[terms.length-1];
		
		for(int i=0; i<newTerms.length; i++){
			newTerms[i] = terms[i+1] * (i+1);
		}
		
		return new Polynomial(newTerms);
	}
	
	public static Polynomial random(int length){
		double[] terms = new double[length];
		
		for(int i=0; i<length; i++){
			terms[i] = (int)(Math.random()*10);
		}
		
		return new Polynomial(terms);
	}
	
	public Complex getValue(Complex x){
		Complex y = Complex.ZERO;
		for(int i=0; i<terms.length; i++){
			if(i==0){
				y = y.add(terms[0]);
				continue;
			}
			if(terms[i] == 0)continue;
			y = y.add(
					x.pow(i).multiply(terms[i]));
		}
		return y;
	}
	
	public double getValue(double x){
		double y = 0;
		for(int i=0; i<terms.length; i++){
			if(i==0){
				y += terms[0];
				continue;
			}
			if(terms[i]==0)continue;
			y += terms[i] * Math.pow(x, i);
		}
		return y;
	}
}
