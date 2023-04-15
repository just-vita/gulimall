package top.vita.common.to;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SpuBoundTo {
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
    private Long spuId;
}
