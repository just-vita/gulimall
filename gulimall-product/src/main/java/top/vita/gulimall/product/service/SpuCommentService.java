package top.vita.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.vita.common.utils.PageUtils;
import top.vita.gulimall.product.entity.SpuCommentEntity;

import java.util.Map;

/**
 * 商品评价
 *
 * @author vita
 * @email 1254581982@gmail.com
 * @date 2022-07-31 15:22:59
 */
public interface SpuCommentService extends IService<SpuCommentEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

