package com.chatchat.server;

import static com.chatchat.constant.ChatConstant.CHAT_PAGE;
import static com.chatchat.constant.ChatConstant.CONTENT;
import static com.chatchat.constant.ChatConstant.ERROR_PAGE;
import static com.chatchat.constant.ChatConstant.IS_ADMIN;
import static com.chatchat.constant.ChatConstant.IS_NOT_REJECTIVE;
import static com.chatchat.constant.ChatConstant.NICKNAME;
import static com.chatchat.constant.ChatConstant.USERID;
import com.chatchat.entity.ChatUser;
import com.chatchat.service.ChatUserService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/signin")
public class SignInServer extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        Integer userid = Integer.parseInt(req.getParameter(USERID));
        ChatUser user = new ChatUserService().getUser(userid, IS_NOT_REJECTIVE);
        if (user != null) {
            req.setAttribute(USERID, userid);
            req.setAttribute(NICKNAME, user.getNickname());
            req.setAttribute(IS_ADMIN, user.getIsAdmin());
            req.getRequestDispatcher(CHAT_PAGE).forward(req, resp);
        } else {
            req.setAttribute(CONTENT, "用户不存在或已被禁止访问");
            req.getRequestDispatcher(ERROR_PAGE).forward(req, resp);
        }
    }
}
