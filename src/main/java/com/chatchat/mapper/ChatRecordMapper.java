package com.chatchat.mapper;

import com.chatchat.entity.ChatRecord;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

/**
 *
 * @author Li
 */
public interface ChatRecordMapper {

    @Insert({"insert into chat_record (type,format,time,from_userid,content)", "values (#{type},#{format},#{time},#{from},#{content})"})
    public void savePublicRecord(@Param("type") String type, @Param("format") String format, @Param("time") Date time, @Param("from") Integer from, @Param("content") String content);

    @Insert({"insert into chat_record (type,format,time,from_userid,to_userid,content)", "values (#{type},#{format},#{time},#{from},#{to},#{content})"})
    public void savePrivateRecord(@Param("type") String type, @Param("format") String format, @Param("time") Date time, @Param("from") Integer from, @Param("to") Integer to,
            @Param("content") String content);

    @Select("select * from (select type,format,time,from_userid,to_userid,content from chat_record \n"
            + "where type = #{publicChat} or type= #{privateChat} and from_userid = #{userid} or type=  #{privateChat} and to_userid = #{userid} \n"
            + "order by time desc limit #{start},#{end}) cr order by time asc")
    @Results(value = {
        @Result(property = "type", column = "type", javaType = String.class, jdbcType = JdbcType.VARCHAR),
        @Result(property = "format", column = "format", javaType = String.class, jdbcType = JdbcType.VARCHAR),
        @Result(property = "time", column = "time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
        @Result(property = "from", column = "from_userid", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
        @Result(property = "to", column = "to_userid", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
        @Result(property = "content", column = "content", javaType = String.class, jdbcType = JdbcType.VARCHAR)
    })
    public List<ChatRecord> getChatRecord(@Param("userid") Integer userid, @Param("start") Integer start, @Param("end") Integer end,
            @Param("publicChat") String publicChat, @Param("privateChat") String privateChat);

    @Select("select nickname nickname,count(*) total from chat_record join jo_user on from_userid = user_id group by from_userid")
    public List<Map<String, Object>> getUserReport();

    @Select("select date_format(time,'%Y-%m-%d') date,count(*) total from chat_record group by date_format(time,'%Y-%m-%d')")
    public List<Map<String, Object>> getDailyReport();
}
