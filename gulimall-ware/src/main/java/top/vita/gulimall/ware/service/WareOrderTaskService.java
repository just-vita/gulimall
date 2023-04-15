package top.vita.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.vita.common.utils.PageUtils;
import top.vita.gulimall.ware.entity.WareOrderTaskEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author vita
 * @email 1254581982@gmail.com
 * @date 2022-07-31 15:37:57
 */
public interface WareOrderTaskService extends IService<WareOrderTaskEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

