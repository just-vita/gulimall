package top.vita.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.vita.common.utils.PageUtils;
import top.vita.gulimall.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author vita
 * @email 1254581982@gmail.com
 * @date 2022-07-31 15:42:36
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

