package top.vita.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.vita.common.utils.PageUtils;
import top.vita.gulimall.product.entity.SpuInfoDescEntity;

import java.util.Map;

/**
 * spu信息介绍
 *
 * @author vita
 * @email 1254581982@gmail.com
 * @date 2022-07-31 15:22:59
 */
public interface SpuInfoDescService extends IService<SpuInfoDescEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfoDesc(SpuInfoDescEntity spuInfoDescEntity);
}

