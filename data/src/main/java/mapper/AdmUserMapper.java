package mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import entity.AdmUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmUserMapper extends BaseMapper<AdmUser> {

    @Select("select * from adm_user where username = #{username}")
    AdmUser loadUserByUsername(@Param("username") String username);
}
