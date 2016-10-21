package com.chachat.test;

import com.chatchat.config.SessionFactory;
import static com.chatchat.constant.ChatConstant.PRIVATE_CHAT;
import static com.chatchat.constant.ChatConstant.PUBLIC_CHAT;
import com.chatchat.entity.ChatRecord;
import com.chatchat.entity.ChatUser;
import com.chatchat.mapper.ChatUserMapper;
import java.util.Date;
import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;
import com.chatchat.mapper.ChatRecordMapper;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Li
 */
public class TestSQL {

    private SqlSession session;

//    @Before
    public void before() {
        session = SessionFactory.getSessionFactory().openSession();
    }

//    @Test
    public void test1() {
//        ChatRecordMapper mapper = session.getMapper(ChatRecordMapper.class);
//        ChatRecord message = new ChatRecord();
//        message.setTime(new Date());
//        message.setFromUserid(2);
//        message.setToUserid(2);
//        mapper.saveRecord(message);
//        session.commit();
//        session.close();
    }

//    @Test
    public void test2() {
        ChatUserMapper mapper = session.getMapper(ChatUserMapper.class);
        mapper.updateNickname(2, "哈哈");
        session.commit();
        session.close();
    }

//    @Test
    public void test3() {
        ChatUserMapper mapper = session.getMapper(ChatUserMapper.class);
        Integer i = mapper.verifyNickname("xx");
        System.out.println(i);
    }

//    @Test
    public void test4() {
        ChatRecordMapper recordMapper = session.getMapper(ChatRecordMapper.class);
        List<ChatRecord> r = recordMapper.getChatRecord(1, 0, 10, PUBLIC_CHAT, PRIVATE_CHAT);
//        for (ChatRecord cr : r) {
//            System.out.println(cr.toString());
//        }
        ChatUserMapper userMapper = session.getMapper(ChatUserMapper.class);
        List<ChatUser> u = userMapper.getAllUser();
//        for (ChatUser cu : u) {
//            System.out.println(cu.toString());
//        }
        for (ChatRecord cr : r) {
            for (ChatUser cu : u) {
                if (Objects.equals(cr.getFrom(), cu.getUserid())) {
                    cr.setFromNickname(cu.getNickname());
                }
                if (Objects.equals(cr.getTo(), cu.getUserid())) {
                    cr.setToNickname(cu.getNickname());
                }
            }
        }
        for (ChatRecord cr : r) {
            System.out.println(cr.toString());
        }
        session.close();
    }

//    @Test
    public void test5() {
        ChatRecordMapper recordMapper = session.getMapper(ChatRecordMapper.class);
        List<Map<String, Object>> list = recordMapper.getUserReport();
        for (Map<String, Object> map : list) {
            System.out.println(map.get("nickname"));
            System.out.println(map.get("total"));
        }
    }
}
