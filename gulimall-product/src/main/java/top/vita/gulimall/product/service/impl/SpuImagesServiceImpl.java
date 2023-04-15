package top.vita.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.vita.common.utils.PageUtils;
import top.vita.common.utils.Query;

import top.vita.gulimall.product.dao.SpuImagesDao;
import top.vita.gulimall.product.entity.SpuImagesEntity;
import top.vita.gulimall.product.service.SpuImagesService;


@Service("spuImagesService")
public class SpuImagesServiceImpl extends ServiceImpl<SpuImagesDao, SpuImagesEntity> implements SpuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuImagesEntity> page = this.page(
                new Query<SpuImagesEntity>().getPage(params),
                new QueryWrapper<SpuImagesEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveImages(Long spuInfoId, List<String> images) {
        if (images != null && images.size() > 0) {
            List<SpuImagesEntity> imagesEntities = images.stream().map(item -> {
                SpuImagesEntity spuImagesEntity = new SpuImagesEntity();
                spuImagesEntity.setSpuId(spuInfoId);
                spuImagesEntity.setImgUrl(item);
                return spuImagesEntity;
            }).collect(Collectors.toList());
            saveBatch(imagesEntities);
        }
    }

}