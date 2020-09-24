package mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import entity.AdmUser;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
@CacheNamespace
public interface AdmUserMapper extends BaseMapper<AdmUser> {

    @Select("select * from adm_user where username = #{username}")
    AdmUser loadUserByUsername(@Param("username") String username);

    @Options
    @Select("select * from adm_user where id = #{id}")
    AdmUser getById(@Param("id") Integer id);
}
