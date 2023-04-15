package top.vita.gulimall.product.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import top.vita.common.constant.ProductConstant;
import top.vita.common.es.SkuEsModel;
import top.vita.common.es.SkuHasStockVo;
import top.vita.common.to.SkuReductionTo;
import top.vita.common.to.SpuBoundTo;
import top.vita.common.utils.PageUtils;
import top.vita.common.utils.Query;

import top.vita.common.utils.R;
import top.vita.gulimall.product.dao.SpuInfoDao;
import top.vita.gulimall.product.entity.*;
import top.vita.gulimall.product.feign.CouponFeignService;
import top.vita.gulimall.product.feign.SearchFeignService;
import top.vita.gulimall.product.feign.WareFeignSerivce;
import top.vita.gulimall.product.service.*;
import top.vita.gulimall.product.vo.*;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    SpuImagesService imagesService;

    @Autowired
    AttrService attrService;

    @Autowired
    ProductAttrValueService attrValueService;

    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    SkuImagesService skuImagesService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    CouponFeignService couponFeignService;

    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    WareFeignSerivce wareFeignSerivce;

    @Autowired
    SearchFeignService searchFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo spuInfo) {
        // 保存spu_info表信息
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuInfo, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.saveSpuInfo(spuInfoEntity);
        // 保存成功后会更新ID，保存ID其他表要用
        Long spuInfoId = spuInfoEntity.getId();

        // 保存spu_info_desc表信息
        List<String> descripts = spuInfo.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoId);
        spuInfoDescEntity.setDecript(String.join(",", descripts));
        spuInfoDescService.saveSpuInfoDesc(spuInfoDescEntity);

        // 保存spu_images表信息
        List<String> images = spuInfo.getImages();
        imagesService.saveImages(spuInfoId,images);

        // 保存product_attr_value表信息
        List<BaseAttrs> baseAttrs = spuInfo.getBaseAttrs();
        List<ProductAttrValueEntity> attrValueEntities = baseAttrs.stream().map(item -> {
            ProductAttrValueEntity valueEntity = new ProductAttrValueEntity();
            valueEntity.setAttrId(item.getAttrId());
            AttrEntity attrEntity = attrService.getById(item.getAttrId());
            valueEntity.setAttrName(attrEntity.getAttrName());
            valueEntity.setSpuId(spuInfoId);
            valueEntity.setQuickShow(item.getShowDesc());
            return valueEntity;
        }).collect(Collectors.toList());
        attrValueService.saveAttrValue(attrValueEntities);

        // 保存spu的积分信息；gulimall_sms->sms_spu_bounds
        Bounds bounds = spuInfo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds, spuBoundTo);
        spuBoundTo.setSpuId(spuInfoId);
        couponFeignService.saveSpuBounds(spuBoundTo);

        // 保存当前spu对应的所有sku信息
        List<Skus> skus = spuInfo.getSkus();
        skus.forEach(item->{
            String defaultImg = "";
            List<Images> imagesList = item.getImages();
            for (Images img : imagesList) {
                if (img.getDefaultImg() == 1){
                    defaultImg = img.getImgUrl();
                }
            }

            // sku的基本信息；pms_sku_info
            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
            BeanUtils.copyProperties(item, skuInfoEntity);
            skuInfoEntity.setSpuId(spuInfoId);
            skuInfoEntity.setBrandId(spuInfo.getBrandId());
            skuInfoEntity.setCatalogId(spuInfo.getCatalogId());
            skuInfoEntity.setSaleCount(0L);
            skuInfoEntity.setSkuDefaultImg(defaultImg);
            skuInfoService.saveSkuInfo(skuInfoEntity);
            // 保存skuId，方便后续使用
            Long skuId = skuInfoEntity.getSkuId();

            // sku的图片信息；pms_sku_image
            List<SkuImagesEntity> skuImagesEntities = item.getImages().stream().map(img -> {
                SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                skuImagesEntity.setImgUrl(img.getImgUrl());
                skuImagesEntity.setSkuId(skuId);
                skuImagesEntity.setDefaultImg(img.getDefaultImg());
                return skuImagesEntity;
            })
                    .filter(img-> StringUtils.isNotBlank(img.getImgUrl())) // true为需要
                    .collect(Collectors.toList());
            skuImagesService.saveImage(skuImagesEntities);

            // sku的销售属性信息：pms_sku_sale_attr_value
            List<Attr> attrs = item.getAttr();
            List<SkuSaleAttrValueEntity> saleAttrValueEntities = attrs.stream().map(attr -> {
                SkuSaleAttrValueEntity attrValueEntity = new SkuSaleAttrValueEntity();
                BeanUtils.copyProperties(attr, attrValueEntities);
                attrValueEntity.setSkuId(skuId);
                return attrValueEntity;
            }).collect(Collectors.toList());
            skuSaleAttrValueService.saveAttrValue(saleAttrValueEntities);

            // sku的优惠、满减等信息；gulimall_sms->sms_sku_ladder\sms_sku_full_reduction\sms_member_price
            SkuReductionTo skuReductionTo = new SkuReductionTo();
            BeanUtils.copyProperties(item,skuReductionTo);
            skuReductionTo.setSkuId(skuId);
            if(skuReductionTo.getFullCount() >0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal("0")) == 1){
                R r1 = couponFeignService.saveSkuReduction(skuReductionTo);
                if(r1.getCode() != 0){
                    log.error("远程保存sku优惠信息失败");
                }
            }
        });


    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {

        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            wrapper.and((w)->{
                w.eq("id",key).or().like("spu_name",key);
            });
        }
        // status=1 and (id=1 or spu_name like xxx)
        String status = (String) params.get("status");
        if(!StringUtils.isEmpty(status)){
            wrapper.eq("publish_status",status);
        }

        String brandId = (String) params.get("brandId");
        if(!StringUtils.isEmpty(brandId)&&!"0".equalsIgnoreCase(brandId)){
            wrapper.eq("brand_id",brandId);
        }

        String catelogId = (String) params.get("catelogId");
        if(!StringUtils.isEmpty(catelogId)&&!"0".equalsIgnoreCase(catelogId)){
            wrapper.eq("catalog_id",catelogId);
        }

        /**
         * status: 2
         * key:
         * brandId: 9
         * catelogId: 225
         */

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void up(Long spuId) {
        List<SkuInfoEntity> skus = skuInfoService.getSkuInfoBySpuId(spuId);
        List<Long> skuIds = skus.stream().map(SkuInfoEntity::getSpuId).collect(Collectors.toList());
        List<ProductAttrValueEntity> attrValueList = attrValueService.baseAttrlistforspu(spuId);
        List<Long> ids = attrValueList.stream()
                .map(item -> item.getAttrId())
                .collect(Collectors.toList());
        List<Long> searchAttrs = attrService.selectSearchAttrs(ids);

        // 配置信息
        HashSet<Long> idSet = new HashSet<>(searchAttrs);
        List<SkuEsModel.Attrs> attrsList = attrValueList.stream().filter
                (item -> idSet.contains(item.getAttrId())).map(item -> {
            SkuEsModel.Attrs attrs = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(item, attrs);
            return attrs;
        }).collect(Collectors.toList());
        Map<Long, Boolean> hasStockMap = null;
        try{
            List<SkuHasStockVo> data = (List<SkuHasStockVo>) wareFeignSerivce.hasStock(skuIds).get("data");
            hasStockMap = data.stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, item -> item.getHasStock()));
        }catch (Exception e){
        }

        Map<Long, Boolean> finalHasStockMap = hasStockMap;
        List<SkuEsModel> product = skus.stream().map(item -> {
            SkuEsModel skuEsModel = new SkuEsModel();
            BeanUtils.copyProperties(item, skuEsModel);
            skuEsModel.setSkuPrice(item.getPrice());
            skuEsModel.setSkuImg(item.getSkuDefaultImg());

            // 热度评分
            skuEsModel.setHotScore(0L);

            if (finalHasStockMap == null){
                skuEsModel.setHasStock(true);
            }else{
                skuEsModel.setHasStock(finalHasStockMap.get(item.getSkuId()));
            }

            // 品牌、分类
            BrandEntity brand = brandService.getById(item.getBrandId());
            skuEsModel.setBrandName(brand.getName());
            skuEsModel.setBrandImg(brand.getLogo());

            CategoryEntity caregory = categoryService.getById(item.getCatalogId());
            skuEsModel.setCatalogId(caregory.getCatId());
            skuEsModel.setCatalogName(caregory.getName());

            skuEsModel.setAttrs(attrsList);

            return skuEsModel;
        }).collect(Collectors.toList());

        //TODO 5、将数据发给es进行保存：gulimall-search
        R r = searchFeignService.productStatusUp(product);

        if (r.getCode() == 0) {
            //远程调用成功
            //TODO 6、修改当前spu的状态
            this.baseMapper.updaSpuStatus(spuId, ProductConstant.ProductStatusEnum.SPU_UP.getCode());
        } else {
            //远程调用失败
            //TODO 7、重复调用？接口幂等性:重试机制
        }

    }

    @Override
    public void saveSpuInfo(SpuInfoEntity spuInfoEntity) {
        baseMapper.insert(spuInfoEntity);
    }

}