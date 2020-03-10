package cn.dw.service;
/**
 * bandong
 * 2020年3月9日下午4:33:40
 *   商品上下架根据商品id获取库存数据的service层接口
 */
public interface SolrManagerService {
	//审核通过的商品，根据商品id获取库存数据，放入到solr中
	void saveItemSolr(Long id);

	//根据商品id到solr索引库中删除对用的数据
	void delItemToSolr(Long id);

}
