package top.vita.gulimall.product.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.vita.common.utils.R;

import java.util.List;

@FeignClient("gulimall-ware")
public interface WareFeignSerivce {
    @PostMapping("/ware/waresku/hasstock")
    R hasStock(@RequestBody List<Long> ids);
}
