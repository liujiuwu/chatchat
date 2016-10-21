package com.chatchat.service;

import com.chatchat.config.SessionFactory;
import static com.chatchat.constant.ChatConstant.CONTENT;
import static com.chatchat.constant.ChatConstant.FORMAT;
import static com.chatchat.constant.ChatConstant.FROM;
import static com.chatchat.constant.ChatConstant.PRIVATE_CHAT;
import static com.chatchat.constant.ChatConstant.PUBLIC_CHAT;
import static com.chatchat.constant.ChatConstant.TO;
import static com.chatchat.constant.ChatConstant.TYPE;
import com.chatchat.entity.ChatRecord;
import com.chatchat.mapper.ChatRecordMapper;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;

/**
 *
 * @author Li
 */
public class ChatRecordService {

    public void saveRecord(JSONObject obj, Date time) {
        try (SqlSession session = SessionFactory.getSessionFactory().openSession()) {
            ChatRecordMapper mapper = session.getMapper(ChatRecordMapper.class);
            if (obj.getString(TYPE).equals(PUBLIC_CHAT)) {
                mapper.savePublicRecord(obj.getString(TYPE), obj.getString(FORMAT), time, obj.getInt(FROM), obj.getString(CONTENT));
            }
            if (obj.getString(TYPE).equals(PRIVATE_CHAT)) {
                mapper.savePrivateRecord(obj.getString(TYPE), obj.getString(FORMAT), time, obj.getInt(FROM), obj.getInt(TO), obj.getString(CONTENT));
            }
            session.commit();
        }
    }

    public List<ChatRecord> getRecord(Integer userid, Integer start, Integer end, String publicChat, String privateChat) {
        List<ChatRecord> list;
        try (SqlSession session = SessionFactory.getSessionFactory().openSession()) {
            ChatRecordMapper mapper = session.getMapper(ChatRecordMapper.class);
            list = mapper.getChatRecord(userid, start, end, publicChat, privateChat);
        }
        return list;
    }

    public List<Map<String, Object>> getUserReport() {
        List<Map<String, Object>> list;
        try (SqlSession session = SessionFactory.getSessionFactory().openSession()) {
            ChatRecordMapper mapper = session.getMapper(ChatRecordMapper.class);
            list = mapper.getUserReport();
        }
        return list;
    }

    public List<Map<String, Object>> getDailyReport() {
        List<Map<String, Object>> list;
        try (SqlSession session = SessionFactory.getSessionFactory().openSession()) {
            ChatRecordMapper mapper = session.getMapper(ChatRecordMapper.class);
            list = mapper.getDailyReport();
        }
        return list;
    }
}
