package top.vita.gulimall.coupon.dao;

import top.vita.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author vita
 * @email 1254581982@gmail.com
 * @date 2022-07-31 15:43:44
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
