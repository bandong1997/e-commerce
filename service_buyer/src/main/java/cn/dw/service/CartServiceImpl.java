package cn.dw.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;

import cn.dw.dao.item.ItemDao;
import cn.dw.pojo.entity.BuyerCart;
import cn.dw.pojo.item.Item;
import cn.dw.pojo.order.OrderItem;
import cn.dw.util.Contents;

/**
 * bandong
 * 2020年3月17日下午9:31:01
 * 	购物车service层接口实现类
 */
@Service
@Transactional
public class CartServiceImpl implements CartService {

	//注入商品dao层接口
	@Autowired
	private ItemDao itemDao;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	
	//将商品加入到这个人现有的购物车列表中
	@Override
	public List<BuyerCart> addItemToCartList(List<BuyerCart> cartList, Long itemId, Integer num) {
		// 1.根据商品SKU ID查询SKU商品信息 
		Item item = itemDao.selectByPrimaryKey(itemId);
		
		// 2.判断商品是否存在，不存在抛异常
		if(item == null) {
			throw new RuntimeException("此产品不存在");// 运行时异常
		}
		// 3.判断商品状态是否为1，已审核的，状态不对抛异常 
		if(!"1".equals(item.getStatus())) {
			throw new RuntimeException("此商品未通过审核");
		}
		// 4.获取商家ID
		String sellerId = item.getSellerId();
		// 5.根据商家ID查询购物车列表中是否存在该商家的购物车
		BuyerCart buyerCart = findBuyerCartBySellerId(cartList, sellerId);
		// 6.判断如果购物车列表中不存在该商家的购物车
		if(buyerCart == null) {
			// 6.a.1新建购物车对象 	
			buyerCart = new BuyerCart();
			// 设置新创建的购物车的卖家Id
			buyerCart.setSellerId(sellerId);
			// 设置新创建的购物车对象的卖家名称
			buyerCart.setSellerName(item.getSeller());
			// 创建购物项集合
			List<OrderItem> orderItemList = new ArrayList<OrderItem>();
			// 创建购物项
			OrderItem orderItem = createOrderItem(item, num);
			// 将购物项添加到购物项集合中
			orderItemList.add(orderItem);
			//把购物项集合添加到购物车中
			buyerCart.setOrderItemList(orderItemList);
			// 6.a.2将新建的购物车对象添加到购物车列表
			cartList.add(buyerCart);
		}else {
			// 6.b.1如果购物车列表存在该商家的购物车(查询购物车明细列表中是否存在该商品) 
			List<OrderItem> orderItemList = buyerCart.getOrderItemList();
			OrderItem orderItem = findOrderItemByItemId(orderItemList, itemId);
			// 6.b.2判断购物车明细列表是否为空
			if(orderItem == null) {
				// 6.b.3为空，新增购物车明细
				orderItem = createOrderItem(item, num);
				//添加到集合中
				orderItemList.add(orderItem);
			}else {
				// 6.b.4不为空，在原购物车明细上添加数量，更改金额 
				orderItem.setNum(orderItem.getNum() + num);
				orderItem.setTotalFee(orderItem.getPrice().multiply(new BigDecimal(orderItem.getNum())));
				// 6.b.5如果购物车明细中操作后小于等于0，则从集合中移除
				if(orderItem.getNum() <= 0) {
					orderItemList.remove(orderItem);
				}
				// 6.b.6如果购物车中的购物车明细列表为空，则从购物车移除
				if(orderItemList.size() <= 0) {
					cartList.remove(orderItemList);
				}
			}
		}
		// 7.返回购物车列表对象
		return cartList;
	}

	//根据卖家id,判断此购物车集合中有没有这个卖家的购物车对象，如果有返回，如果没有返回null
	private BuyerCart findBuyerCartBySellerId(List<BuyerCart> cartList,String sellerId ) {
		if(cartList != null) {
			for (BuyerCart buyerCart : cartList) {
				//判断卖家id是否相同
				if(buyerCart.getSellerId().equals(sellerId)) {
					return buyerCart;
				}
			}
		}
		return null;
	}
	//创建购物项对象
	private OrderItem createOrderItem(Item item,Integer num) {
		if(num <= 0) {
			throw new RuntimeException("购买数量非法");
		}
		OrderItem orderItem = new OrderItem();
		// 设置购买数量
		orderItem.setNum(num);
		// 设置商品id
		orderItem.setGoodsId(item.getGoodsId());
		// 设置库存id
		orderItem.setItemId(item.getId());
		// 设置示例图片
		orderItem.setPicPath(item.getImage());
		// 设置单价
		orderItem.setPrice(item.getPrice());
		// 设置卖家id
		orderItem.setSellerId(item.getSellerId());
		// 设置库存标题
		orderItem.setTitle(item.getTitle());
		// 设置总价=单价*购买数量
 		orderItem.setTotalFee(item.getPrice().multiply(new BigDecimal(num)));
 		
		return orderItem;
	}
	//从当前购物项集合中判断是否存在这个商品，存在则返回这个购物项对象，不存在则返回null
	private OrderItem findOrderItemByItemId(List<OrderItem> orderItemList,Long itemId) {
		if(orderItemList != null) {
			for (OrderItem orderItem : orderItemList) {
				if(orderItem.getItemId().equals(itemId)) {
					return orderItem; //信息存在
				}
			}
		}
		return null;
	}
	
	
	//将购物车列表存储到redis中
	@Override
	public void setCartListToRedis(String userName, List<BuyerCart> cartList) {
		redisTemplate.boundHashOps(Contents.CART_LIST_REDIS).put(userName, cartList);;
	}

	//从redis获取购物车列表
	@Override
	public List<BuyerCart> getCartListFromRedis(String userName) {
		List<BuyerCart> buyerCartList = (List<BuyerCart>) redisTemplate.boundHashOps(Contents.CART_LIST_REDIS).get(userName);
		if(buyerCartList == null) {
			buyerCartList = new ArrayList<>();
		}
		return buyerCartList;
	}

	
	//将cookie中的数据和redis中的数据进行合并
	@Override
	public List<BuyerCart> mergeCookirCartListToRedisCartList(List<BuyerCart> cookieCartList,
			List<BuyerCart> redisCartList) {
		if(cookieCartList != null) {
			for (BuyerCart cookirCart : cookieCartList) {
				//购物项
				for (OrderItem cookieOrderItem : cookirCart.getOrderItemList()) {
					redisCartList = addItemToCartList(redisCartList, cookieOrderItem.getItemId(), 
							cookieOrderItem.getNum());
				}
			}
		}
		return redisCartList;
	}

	
	
	
	
	
	
	
	
	
	
	
	
}
