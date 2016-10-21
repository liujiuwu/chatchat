package com.chatchat.service;

import com.chatchat.config.SessionFactory;
import static com.chatchat.constant.ChatConstant.IS_NOT_REJECTIVE;
import static com.chatchat.constant.ChatConstant.NICKNAME;
import static com.chatchat.constant.ChatConstant.USERID;
import com.chatchat.entity.ChatUser;
import com.chatchat.mapper.ChatUserMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.websocket.Session;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author Li
 */
public class ChatUserService {

    public ChatUser getUser(Integer userid, Integer rejectiveOrNot) {
        ChatUser user;
        try (SqlSession session = SessionFactory.getSessionFactory().openSession()) {
            ChatUserMapper mapper = session.getMapper(ChatUserMapper.class);
            user = mapper.getUser(userid, IS_NOT_REJECTIVE);
        }
        return user;
    }

    public List<ChatUser> getAllUser() {
        List<ChatUser> list;
        try (SqlSession session = SessionFactory.getSessionFactory().openSession()) {
            ChatUserMapper mapper = session.getMapper(ChatUserMapper.class);
            list = mapper.getAllUser();
        }
        return list;
    }

    public void rejectUser(Integer userid, Integer rejectiveOrNot) {
        try (SqlSession session = SessionFactory.getSessionFactory().openSession()) {
            ChatUserMapper mapper = session.getMapper(ChatUserMapper.class);
            mapper.rejectUser(userid, rejectiveOrNot);
            session.commit();
        }
    }

    public void updateNickname(Integer userid, String nickname) {
        try (SqlSession session = SessionFactory.getSessionFactory().openSession()) {
            ChatUserMapper mapper = session.getMapper(ChatUserMapper.class);
            mapper.updateNickname(userid, nickname);
            session.commit();
        }
    }

    public int verifyNickname(String nickname) {
        int count;
        try (SqlSession session = SessionFactory.getSessionFactory().openSession()) {
            ChatUserMapper mapper = session.getMapper(ChatUserMapper.class);
            count = mapper.verifyNickname(nickname);
        }
        return count;
    }

    public static List<ChatUser> getUserList(Map<Integer, Session> USER_SESSIONS) {
        List<ChatUser> list = new ArrayList<>();
        ChatUser user;
        for (Session session : USER_SESSIONS.values()) {
            user = new ChatUser();
            user.setUserid(Integer.parseInt(session.getUserProperties().get(USERID).toString()));
            user.setNickname(session.getUserProperties().get(NICKNAME).toString());
            list.add(user);
        }
        return list;
    }
}
