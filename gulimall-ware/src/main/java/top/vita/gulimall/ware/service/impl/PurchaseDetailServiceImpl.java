package top.vita.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.StringUtils;
import top.vita.common.constant.WareConstant;
import top.vita.common.utils.PageUtils;
import top.vita.common.utils.Query;

import top.vita.gulimall.ware.dao.PurchaseDetailDao;
import top.vita.gulimall.ware.entity.PurchaseDetailEntity;
import top.vita.gulimall.ware.service.PurchaseDetailService;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        /**
         * status: 0,//状态
         *    wareId: 1,//仓库id
         */

        QueryWrapper<PurchaseDetailEntity> queryWrapper = new QueryWrapper<PurchaseDetailEntity>();

        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            //purchase_id  sku_id
            queryWrapper.and(w->{
                w.eq("purchase_id",key).or().eq("sku_id",key);
            });
        }

        String status = (String) params.get("status");
        if(!StringUtils.isEmpty(status)){
            //purchase_id  sku_id
            queryWrapper.eq("status",status);
        }

        String wareId = (String) params.get("wareId");
        if(!StringUtils.isEmpty(wareId)){
            //purchase_id  sku_id
            queryWrapper.eq("ware_id",wareId);
        }

        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<PurchaseDetailEntity> getDetailByPurchaseId(Long id) {
        LambdaQueryWrapper<PurchaseDetailEntity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(PurchaseDetailEntity::getPurchaseId, id);
        List<PurchaseDetailEntity> purchaseDetailEntities = list(lqw);
        List<PurchaseDetailEntity> purchaseDetailList = purchaseDetailEntities.stream().map(detail -> {
            PurchaseDetailEntity entity = new PurchaseDetailEntity();
            entity.setId(detail.getId());
            entity.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
            return entity;
        }).collect(Collectors.toList());
        return purchaseDetailList;
    }

}