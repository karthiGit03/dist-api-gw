package com.model;

public class RateLimitResponse {

    private boolean allowed;

    private long remainingTokens;
    
    private long refillTime;

    public RateLimitResponse(
            boolean allowed,
            long remainingTokens,
            long refillTime) {

        this.allowed = allowed;
        this.remainingTokens = remainingTokens;
        this.refillTime = refillTime;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public long getRemainingTokens() {
        return remainingTokens;
    }

	public long getRefillTime() {
		return refillTime;
	}

	public void setRefillTime(long refillTime) {
		this.refillTime = refillTime;
	}
}