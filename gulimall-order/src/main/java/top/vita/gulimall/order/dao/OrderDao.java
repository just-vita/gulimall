package top.vita.gulimall.order.dao;

import top.vita.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author vita
 * @email 1254581982@gmail.com
 * @date 2022-07-31 15:38:55
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
