package cn.dw.pojo.entity;

import java.io.Serializable;
import java.util.List;

import cn.dw.pojo.specification.Specification;
import cn.dw.pojo.specification.SpecificationOption;

/**
 *  规格和规格选项集合实体
 * @author INS
 *
 */
public class SpecEntity implements Serializable{
	//规格对象
	private Specification specification;
	//规格选项集合
	private List<SpecificationOption> specificationOptionList;
	public Specification getSpecification() {
		return specification;
	}
	public void setSpecification(Specification specification) {
		this.specification = specification;
	}
	public List<SpecificationOption> getSpecificationOptionList() {
		return specificationOptionList;
	}
	public void setSpecificationOptionList(List<SpecificationOption> specificationOptionList) {
		this.specificationOptionList = specificationOptionList;
	}
	
}
