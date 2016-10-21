package com.chatchat.server;

import static com.chatchat.constant.ChatConstant.CHAT_RECORD;
import static com.chatchat.constant.ChatConstant.CONTENT;
import static com.chatchat.constant.ChatConstant.DAILY_REPORT;
import static com.chatchat.constant.ChatConstant.DELETE_USER;
import static com.chatchat.constant.ChatConstant.END;
import static com.chatchat.constant.ChatConstant.ERROR;
import static com.chatchat.constant.ChatConstant.FROM;
import static com.chatchat.constant.ChatConstant.IS_REJECTIVE;
import static com.chatchat.constant.ChatConstant.MAX_TEXT_MESSAGE_BUFFER_SIZE;
import static com.chatchat.constant.ChatConstant.NICKNAME;
import static com.chatchat.constant.ChatConstant.PRIVATE_CHAT;
import static com.chatchat.constant.ChatConstant.SUCCESS;
import com.chatchat.entity.ChatUser;
import com.chatchat.service.MessageService;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.json.JSONObject;
import static com.chatchat.constant.ChatConstant.PUBLIC_CHAT;
import static com.chatchat.constant.ChatConstant.REJECT_USER;
import static com.chatchat.constant.ChatConstant.REMOVE_USER;
import static com.chatchat.constant.ChatConstant.SIGN_IN;
import static com.chatchat.constant.ChatConstant.SIGN_OUT;
import static com.chatchat.constant.ChatConstant.START;
import static com.chatchat.constant.ChatConstant.TO;
import static com.chatchat.constant.ChatConstant.TYPE;
import static com.chatchat.constant.ChatConstant.UPDATE_NICKNAME;
import static com.chatchat.constant.ChatConstant.UPDATE_USER_LIST;
import static com.chatchat.constant.ChatConstant.USERID;
import static com.chatchat.constant.ChatConstant.USER_REPORT;
import com.chatchat.entity.ChatRecord;
import com.chatchat.service.ChatRecordService;
import com.chatchat.service.ChatUserService;
import javax.websocket.CloseReason;
import javax.websocket.OnError;

/**
 *
 * @author Li
 */
@ServerEndpoint("/connect/{userid}/{nickname}")
public class ChatServer {

    private static final Map<Integer, Session> USER_SESSIONS = Collections.synchronizedMap(new HashMap<Integer, Session>());

    @OnOpen
    public void open(Session session, @PathParam("userid") Integer userid, @PathParam("nickname") String nickname) throws IOException {
        Map<String, Object> map = session.getUserProperties();
        map.put(USERID, userid);
        map.put(NICKNAME, nickname);
        session.setMaxTextMessageBufferSize(MAX_TEXT_MESSAGE_BUFFER_SIZE);
        USER_SESSIONS.put(userid, session);
        String time = new Date().toLocaleString();
        List<ChatUser> list = ChatUserService.getUserList(USER_SESSIONS);
        for (Session s : USER_SESSIONS.values()) {
            s.getBasicRemote().sendText(MessageService.toMessage(SUCCESS, SIGN_IN, time, nickname));
            s.getBasicRemote().sendText(MessageService.toMessage(SUCCESS, UPDATE_USER_LIST, list));
        }
    }

    @OnClose
    public void close(Session session, CloseReason reason) throws IOException {
        Map<String, Object> map = session.getUserProperties();
        Integer userid = (Integer) map.get(USERID);
        String nickname = (String) map.get(NICKNAME);
        USER_SESSIONS.remove(userid);
        String time = new Date().toLocaleString();
        for (Session s : USER_SESSIONS.values()) {
            s.getBasicRemote().sendText(MessageService.toMessage(SUCCESS, SIGN_OUT, time, nickname));
            s.getBasicRemote().sendText(MessageService.toMessage(SUCCESS, DELETE_USER, time, userid));
        }
    }

    @OnError
    public void error(Session session, Throwable throwable) throws IOException {
        session.getBasicRemote().sendText(MessageService.toMessage(ERROR, ERROR, ERROR));
    }

    @OnMessage
    public void message(Session session, String message) throws IOException {
        JSONObject object = new JSONObject(message);
        Date time = new Date();
        String timeString = time.toLocaleString();
        //群聊或私聊
        if (object.getString(TYPE).equals(PUBLIC_CHAT) || object.getString(TYPE).equals(PRIVATE_CHAT)) {
            new ChatRecordService().saveRecord(object, time);
            for (Session s : USER_SESSIONS.values()) {
                s.getBasicRemote().sendText(MessageService.toMessage(object, SUCCESS, timeString));
            }
        }
        //修改昵称
        if (object.getString(TYPE).equals(UPDATE_NICKNAME)) {
            String content = object.getString(CONTENT);
            Integer count = new ChatUserService().verifyNickname(content);
            Integer id = object.getInt(FROM);
            if (count == 0) {
                new ChatUserService().updateNickname(id, content);
                USER_SESSIONS.get(id).getUserProperties().put(NICKNAME, content);
                session.getBasicRemote().sendText(MessageService.toMessage(object, SUCCESS));
                List<ChatUser> list = ChatUserService.getUserList(USER_SESSIONS);
                for (Session s : USER_SESSIONS.values()) {
                    s.getBasicRemote().sendText(MessageService.toMessage(SUCCESS, UPDATE_USER_LIST, list));
                }
            } else {
                session.getBasicRemote().sendText(MessageService.toMessage(object, ERROR));
            }
        }
        //移除用户
        if (object.getString(TYPE).equals(REMOVE_USER)) {
            Integer toUserid = object.getInt(TO);
            USER_SESSIONS.get(toUserid).getBasicRemote().sendText(MessageService.toMessage(object, SUCCESS));
        }
        //拉黑用户
        if (object.getString(TYPE).equals(REJECT_USER)) {
            Integer toUserid = object.getInt(TO);
            new ChatUserService().rejectUser(toUserid, IS_REJECTIVE);
            session.getBasicRemote().sendText(MessageService.toMessage(object, SUCCESS));
        }
        //查看聊天记录
        if (object.getString(TYPE).equals(CHAT_RECORD)) {
            List<ChatRecord> list = new MessageService().getRecord(object.getInt(FROM), object.getInt(START), object.getInt(END), PUBLIC_CHAT, PRIVATE_CHAT);
            session.getBasicRemote().sendText(MessageService.toMessage(object, SUCCESS, list));
        }
        //查看统计
        if (object.getString(TYPE).equals(USER_REPORT)) {
            List<Map<String, Object>> list = new ChatRecordService().getUserReport();
            session.getBasicRemote().sendText(MessageService.toMessage(SUCCESS, USER_REPORT, list));
        }
        //查看统计
        if (object.getString(TYPE).equals(DAILY_REPORT)) {
            List<Map<String, Object>> list = new ChatRecordService().getDailyReport();
            session.getBasicRemote().sendText(MessageService.toMessage(SUCCESS, DAILY_REPORT, list));
        }
    }

}
