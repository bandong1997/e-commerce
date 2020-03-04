package cn.dw.service;

import cn.dw.pojo.entity.GoodsEntity;
import cn.dw.pojo.entity.PageResult;
import cn.dw.pojo.good.Goods;

/** 
 * 添加商品service层接口
 * @author INS
 *
 */
public interface GoodsService {
	//添加商品
	void save(GoodsEntity goodsEntity);
	//分页高级
	PageResult searchGoods(Goods goods, Integer page, Integer rows);
	//根据商品id查询商品信息页面回显
	GoodsEntity getOne(Long id);
	//修改商品
	void updateGoodsEntity(GoodsEntity goodsEntity);
	//批删
	void del(Long[] ids);
	//审核商品
	void updateStayus(Long[] ids, String status);

}
