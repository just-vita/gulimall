package top.vita.gulimall.ware.dao;

import org.apache.ibatis.annotations.Param;
import top.vita.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品库存
 * 
 * @author vita
 * @email 1254581982@gmail.com
 * @date 2022-07-31 15:37:56
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    void addStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("skuNum") Integer skuNum);

    Long getSkusStock(@Param("item") Long item);
}
