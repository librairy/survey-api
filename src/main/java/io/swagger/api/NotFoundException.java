package io.swagger.api;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2016-11-22T13:07:43.858Z")

public class NotFoundException extends ApiException {
	private int code;
	public NotFoundException(int code, String msg) {
		super(code, msg);
		this.code = code;
	}
}
