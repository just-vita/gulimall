package top.vita.gulimall.product.dao;

import top.vita.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author vita
 * @email 1254581982@gmail.com
 * @date 2022-07-31 15:22:59
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
