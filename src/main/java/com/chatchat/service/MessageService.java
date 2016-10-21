package com.chatchat.service;

import static com.chatchat.constant.ChatConstant.CONTENT;
import static com.chatchat.constant.ChatConstant.STATUS;
import static com.chatchat.constant.ChatConstant.TIME;
import static com.chatchat.constant.ChatConstant.TYPE;
import com.chatchat.entity.ChatRecord;
import com.chatchat.entity.ChatUser;
import org.json.JSONObject;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Li
 */
public class MessageService {

    public static String toMessage(String status, String type, String time, Object content) {
        JSONObject object = new JSONObject();
        object.put(STATUS, status);
        object.put(TYPE, type);
        object.put(TIME, time);
        object.put(CONTENT, content);
        return object.toString();
    }

    public static String toMessage(String status, String type, Object content) {
        JSONObject object = new JSONObject();
        object.put(STATUS, status);
        object.put(TYPE, type);
        object.put(CONTENT, content);
        return object.toString();
    }

    public static String toMessage(JSONObject object, String status, Object content) {
        object.put(STATUS, status);
        object.put(CONTENT, content);
        return object.toString();
    }

    public static String toMessage(JSONObject object, String status, String time) {
        object.put(STATUS, status);
        object.put(TIME, time);
        return object.toString();
    }

    public static String toMessage(JSONObject object, String status) {
        object.put(STATUS, status);
        return object.toString();
    }

    public List<ChatRecord> getRecord(Integer userid, Integer start, Integer end, String publicChat, String privateChat) {
        List<ChatRecord> recordList = new ChatRecordService().getRecord(userid, start, end, publicChat, privateChat);
        List<ChatUser> userList = new ChatUserService().getAllUser();
        for (ChatRecord cr : recordList) {
            for (ChatUser cu : userList) {
                if (Objects.equals(cr.getFrom(), cu.getUserid())) {
                    cr.setFromNickname(cu.getNickname());
                }
                if (Objects.equals(cr.getTo(), cu.getUserid())) {
                    cr.setToNickname(cu.getNickname());
                }
            }
        }
        return recordList;
    }
}
