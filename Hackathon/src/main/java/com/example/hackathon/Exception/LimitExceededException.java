package com.sparkyconsulting.aihub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class LimitExceededException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    private String limitType;
    private long currentUsage;
    private long maxLimit;
    
    public LimitExceededException(String limitType, long currentUsage, long maxLimit) {
        super(String.format("%s limit exceeded: current usage is %d, maximum allowed is %d", 
                           limitType, currentUsage, maxLimit));
        this.limitType = limitType;
        this.currentUsage = currentUsage;
        this.maxLimit = maxLimit;
    }
    
    public String getLimitType() {
        return limitType;
    }
    
    public long getCurrentUsage() {
        return currentUsage;
    }
    
    public long getMaxLimit() {
        return maxLimit;
    }
}
