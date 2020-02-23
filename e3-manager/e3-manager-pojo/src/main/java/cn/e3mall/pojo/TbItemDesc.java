package cn.e3mall.pojo;

import java.io.Serializable;
import java.util.Date;

import cn.e3mall.common.util.E3mallResult;

public class TbItemDesc implements Serializable{
    private Long itemId;

    private Date created;

    private Date updated;

    private String itemDesc;
    
    //强行加入E3mallResult中的status属性
    private E3mallResult e3mallResult;
    
    
    public E3mallResult getE3mallResult() {
		return e3mallResult;
	}

	public void setE3mallResult(E3mallResult e3mallResult) {
		this.e3mallResult = e3mallResult;
	}

	public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc == null ? null : itemDesc.trim();
    }

	@Override
	public String toString() {
		return "TbItemDesc [itemId=" + itemId + ", created=" + created + ", updated=" + updated + ", itemDesc="
				+ itemDesc + "]";
	}
    
}