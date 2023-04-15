package top.vita.gulimall.product.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.vita.common.to.SkuReductionTo;
import top.vita.common.to.SpuBoundTo;
import top.vita.common.utils.R;

@FeignClient("gulimall-coupon")
public interface CouponFeignService {
    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    @PostMapping("/saveinfo")
    R saveSkuReduction(SkuReductionTo skuReductionTo);
}
