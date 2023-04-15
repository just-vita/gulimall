package top.vita.gulimall.member.dao;

import top.vita.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author vita
 * @email 1254581982@gmail.com
 * @date 2022-07-31 15:42:36
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
