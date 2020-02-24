package cn.dw.pojo.entity;

import java.io.Serializable;
/**
 * 执行结果实体
 * @author INS
 *
 */
public class Result implements Serializable{
	private boolean success;
	private String message;
	public Result(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
