package jopt.mp.spi;

/**
 * Exception throw when structure of matrix is determined to be invalid 
 */
public class MatrixInvalidException extends Exception {
	public MatrixInvalidException(String msg) {
		super(msg);
	}
}
