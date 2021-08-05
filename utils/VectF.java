package com.tea.free.utils;

public class VectF {

	float x, y, hypot, theta, thetaD, slope;

	/**
	 * Constructor creates an empty vector
	 * 
	 */
	public VectF() {

	}

	public VectF(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void add(float x, float y) {

		this.x = this.x + x;
		this.y = this.y + y;
	}

	public void add(VectF v) {
		x += v.getX();
		y += v.getY();
		calculate();
	}

	public void subtract(VectF v) {
		x -= v.getX();
		y -= v.getY();
		calculate();
	}

	/**
	 * retunrs a new Vector created by subtracting b from a
	 * @param a
	 * @param b
	 * @return
	 */
	public static VectF subtract(VectF a, VectF b) {
		a.subtract(b);
		a.calculate();
		return a;
	}

	/**
	 * changes the vector to a vector with parameter x and y values
	 * 
	 * @param x
	 *            the x value to set the vector to
	 * @param y
	 *            the y value to set the vector to
	 */

	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Makes this vector identical to the parameter
	 * 
	 * @param v
	 *            The vector to set this vector to.
	 */
	public void set(VectF v) {
		x = v.getX();
		y = v.getY();
		calculate();
	}

	public float resolveX(float theta, float hypot) {
		float x = (float) (hypot * Math.cos(theta));
		return x;
	}

	public float resolveY(float theta, float hypot) {
		float y = (float) (hypot * Math.sin(theta));
		return y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setHypot(float hypot) {
		this.hypot = hypot;
		x = resolveX(theta, hypot);
		y = resolveY(theta, hypot);
	}

	/**
	 * sets the angle in radians
	 * 
	 * @param theta
	 *            sets the angle in radians
	 */
	public void setTheta(float theta) {
		this.theta = theta;
		x = resolveX(theta, hypot);
		y = resolveY(theta, hypot);
		thetaD = (float) Math.toDegrees(theta);
	}

	/**
	 * sets the angle of the vector in degrees
	 * 
	 * @param thetaD
	 *            sets the angle of the vector
	 */
	public void setThetaD(float thetaD) {
		this.thetaD = thetaD;
		theta = (float) Math.toRadians(thetaD);
		x = resolveX(theta, hypot);
		y = resolveY(theta, hypot);
	}

	/**
	 * adds amount z to the hypotenuse
	 * 
	 * @param z
	 *            amount to add to the hypotenuse
	 */
	public void addToHypot(float z) {
		hypot += z;
		x = (float) (hypot * Math.cos(theta));
		y = (float) (hypot * Math.sin(theta));
	}

	private void calculate() {
		slope = y / x;
		theta = (float) -Math.atan(y / x);
		thetaD = (float) Math.toDegrees(theta);
		hypot = (float) Math.hypot(x, y);
	}

	public float getThetaD() {
		return thetaD;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getTheta() {
		return theta;
	}

	public float getHypot() {
		return hypot;
	}
}
