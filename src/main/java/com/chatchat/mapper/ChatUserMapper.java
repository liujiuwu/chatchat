package com.chatchat.mapper;

import com.chatchat.entity.ChatUser;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

/**
 *
 * @author Li
 */
public interface ChatUserMapper {

    @Select("select user_id,nickname,is_admin from jo_user where user_id = #{userid} and is_rejective = #{isRejectiveOrNot}")
    @Results(value = {
        @Result(id = true, property = "userid", column = "user_id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
        @Result(property = "nickname", column = "nickname", javaType = String.class, jdbcType = JdbcType.VARCHAR),
        @Result(property = "isAdmin", column = "is_admin", javaType = Integer.class, jdbcType = JdbcType.TINYINT)
    })
    public ChatUser getUser(@Param("userid") Integer userid, @Param("isRejectiveOrNot") Integer isRejectiveOrNot);

    @Select("select user_id,nickname from jo_user")
    @Results(value = {
        @Result(id = true, property = "userid", column = "user_id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
        @Result(property = "nickname", column = "nickname", javaType = String.class, jdbcType = JdbcType.VARCHAR)
    })
    public List<ChatUser> getAllUser();

    @Select("select count(*) from jo_user where nickname = #{nickname}")
    public int verifyNickname(@Param("nickname") String nickname);

    @Update("update jo_user set nickname = #{nickname} where user_id = #{userid}")
    public void updateNickname(@Param("userid") Integer userid, @Param("nickname") String nickname);

    @Update("update jo_user set is_rejective = #{rejectiveOrNot} where user_id = #{userid}")
    public void rejectUser(@Param("userid") Integer userid, @Param("rejectiveOrNot") Integer rejectiveOrNot);
}
