package top.vita.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.vita.common.utils.PageUtils;
import top.vita.gulimall.product.entity.SkuSaleAttrValueEntity;
import top.vita.gulimall.product.vo.SkuItemSaleAttrVo;

import java.util.List;
import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author vita
 * @email 1254581982@gmail.com
 * @date 2022-07-31 15:22:59
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttrValue(List<SkuSaleAttrValueEntity> saleAttrValueEntities);

    List<SkuItemSaleAttrVo> getSaleAttrBySpuId(Long spuId);
}

