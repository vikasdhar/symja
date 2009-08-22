package org.matheclipse.core.eval.exception;

/**
 * Memory exceeded exception
 * 
 */
public class PoolMemoryExceededException extends RuntimeException {
	private String fObjectType;

	private int fSize;

	public PoolMemoryExceededException(String objectType, int size) {
		super();
		this.fObjectType = objectType;
		this.fSize = size;
	}

	@Override
	public String getMessage() {
		return "Evaluation interrupted. Pool memory limit exceeded: " + fSize
				+ " Object-Type: " + fObjectType + "\n";
	}
}