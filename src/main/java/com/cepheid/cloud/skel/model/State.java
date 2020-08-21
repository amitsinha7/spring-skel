package com.cepheid.cloud.skel.model;

public enum State {
	
	undefined("UNDEFINED"), valid("VALID"), invalid("INVALID");
	
	private String code;
	 
    private State(String code) {
        this.code = code;
    }
 
    public String getCode() {
        return code;
    }
}
