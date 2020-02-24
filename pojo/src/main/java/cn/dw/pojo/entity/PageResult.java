package cn.dw.pojo.entity;

import java.io.Serializable;
/**
 * 	分页实体
 * @author INS
 *
 */
import java.util.List;
public class PageResult implements Serializable {

	private Long total;//总条数
	private List rows; //数据集合
	//
	public PageResult(Long total, List rows) {
		super();
		this.total = total;
		this.rows = rows;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public List getRows() {
		return rows;
	}
	public void setRows(List rows) {
		this.rows = rows;
	}
	
}
