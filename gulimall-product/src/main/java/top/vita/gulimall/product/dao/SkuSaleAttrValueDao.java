package top.vita.gulimall.product.dao;

import org.apache.ibatis.annotations.Param;
import top.vita.gulimall.product.entity.SkuSaleAttrValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.vita.gulimall.product.vo.SkuItemSaleAttrVo;

import java.util.List;

/**
 * sku销售属性&值
 * 
 * @author vita
 * @email 1254581982@gmail.com
 * @date 2022-07-31 15:22:59
 */
@Mapper
public interface SkuSaleAttrValueDao extends BaseMapper<SkuSaleAttrValueEntity> {

    List<SkuItemSaleAttrVo> getSaleAttrBySpuId(@Param("spuId") Long spuId);
}
