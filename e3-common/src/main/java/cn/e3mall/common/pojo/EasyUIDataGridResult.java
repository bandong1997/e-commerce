package cn.e3mall.common.pojo;

import java.io.Serializable;
import java.util.List;
/**
 * Easyui中datagrid控件要求的数据生成json格式 
 *
 */
public class EasyUIDataGridResult implements Serializable {
	private long total;//总记录数
	private List rows;//每页显示记录数
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public List getRows() {
		return rows;
	}
	public void setRows(List rows) {
		this.rows = rows;
	}
	
}
