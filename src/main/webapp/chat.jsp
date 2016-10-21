<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-CN">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
        <title>聊天室</title>
        <link href="bootstrap/css/bootstrap.css" rel="stylesheet">
        <link href="bootstrap/css/bootstrap-theme.css" rel="stylesheet">
        <script type="text/javascript" src="jquery/jquery.js"></script>
        <script type="text/javascript" src="bootstrap/js/bootstrap.js"></script>
    </head>
    <body style="padding-top: 70px;padding-bottom: 70px">
        <!-- 导航-->
        <nav class="navbar navbar-inverse navbar-fixed-top navbar-default">
            <div class="container">
                <div class="navbar-header">
                    <button id="navbarBtn" type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                        <span class="sr-only"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a id="nickname" class="navbar-brand" href="javascript:void(0)">${nickname}</a>
                </div>
                <div id="navbar" class="collapse navbar-collapse">
                    <ul class="nav navbar-nav">
                        <li id="updateNicknameLi">
                            <a href="javascript:void(0)" data-toggle="modal" data-target="#updateNicknameModal">修改昵称</a>
                        </li>
                        <li><a href="javascript:void(0)" data-toggle="modal" data-target="#chatRecordModal">聊天记录</a></li>
                        <!-- 管理员统计-->
                        <c:choose>
                            <c:when test="${isAdmin==1}">
                                <li><a href="javascript:void(0)" data-toggle="modal" data-target="#userReportModal">公司统计</a></li>
                                <li><a href="javascript:void(0)" data-toggle="modal" data-target="#dailyReportModal">每日统计</a></li>
                                </c:when>
                            </c:choose>
                        <li><a href="javascript:void(0)" onclick="signOut()">退出聊天室</a></li>
                    </ul>
                </div>
            </div>
        </nav>
        <!--修改昵称对话框-->
        <div id="updateNicknameModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="updateNicknameModalLabel">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-body">
                        <form onsubmit="return updateNickname()">
                            <div class="form-group">
                                <p>新昵称:</p>
                                <input id="updateNicknameText" type="text" class="form-control" placeholder="输入新昵称" required="required">
                            </div>
                            <div class="text-right">
                                <button id="updateNicknameCloseBtn" type="button" class="btn btn-default btn-sm" data-dismiss="modal">关闭</button>
                                <button type="submit" class="btn btn-default btn-sm">保存</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <!--发送图片对话框-->
        <div id="sendImgModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="sendImgModalLabel">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-body">
                        <form onsubmit="return sendImgMessage()">
                            <div class="text-right">
                                <input id="imgUpload" type="file" accept="image/jpeg" name="imgUpload" required="required" onchange="previewImg()">
                            </div>
                            <div class="text-center">
                                <canvas id="imgCanvas"></canvas>
                            </div>
                            <div class="text-right">
                                <button id="sendImgCloseBtn" type="button" class="btn btn-default btn-sm" data-dismiss="modal">关闭</button>
                                <button type="submit" class="btn btn-default btn-sm">发送</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <!--聊天记录对话框-->
        <div id="chatRecordModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="chatRecordModalLabel">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-body">
                        <!-- 聊天记录-->
                        <div id="chatRecordList"> </div>
                        <div class="text-right">
                            <button type="button" class="btn btn-default btn-sm" data-dismiss="modal">关闭</button>
                            <button type="button" class="btn btn-default btn-sm" onclick="chatRecord()">查看</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--用户统计对话框-->
        <div id="userReportModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="userReportModalLabel">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-body">
                        <div id="userReportList"> </div>
                        <div class="text-right">
                            <button type="button" class="btn btn-default btn-sm" data-dismiss="modal">关闭</button>
                            <button type="button" class="btn btn-default btn-sm" onclick="userReport()">查看</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--每日统计对话框-->
        <div id="dailyReportModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="dailyReportModalLabel">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-body">
                        <div id="dailyReportList"> </div>
                        <div class="text-right">
                            <button type="button" class="btn btn-default btn-sm" data-dismiss="modal">关闭</button>
                            <button type="button" class="btn btn-default btn-sm" onclick="dailyReport()">查看</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- 聊天内容-->
        <div id="messageList" class="container"></div>
        <!--聊天工具栏-->
        <nav class="navbar navbar-default navbar-fixed-bottom">
            <div class="container" style="background-color: whitesmoke">
                <!--工具栏-->
                <div class="row" style="margin-top: 5px;margin-bottom: 5px" >
                    <div class="col-lg-3 col-md-4">
                        <!--在线用户列表-->
                        <select id="userList" style="height: 27px;width: 100px" onchange="toggleAdminBtn()">
                            <option value="all">全部</option>
                        </select>
                        <!--管理员按钮组-->
                        <c:choose>
                            <c:when test="${isAdmin==1}">
                                <button name="adminBtn" class="btn btn-default btn-sm" type="button" style="display: none" onclick="rejectUser()">拉黑</button>
                                <button name="adminBtn" class="btn btn-default btn-sm" type="button" style="display: none" onclick="removeUser()">移除</button>
                            </c:when>
                        </c:choose>
                    </div>
                </div>
                <!--文本框-->
                <div class="row" style="margin-bottom: 2px">
                    <div class="col-lg-12">
                        <form onsubmit="return sendTextMessage()">
                            <div class="input-group">
                                <span class="input-group-btn">
                                    <button class="btn btn-default" type="button" data-toggle="modal" data-target="#sendImgModal">
                                        <span class="glyphicon glyphicon-picture" aria-hidden="true"></span>
                                    </button>
                                </span>
                                <input id="messageContent" type="text" class="form-control" placeholder="输入内容..." aria-describedby="sizing-addon2" required="required">
                                <span class="input-group-btn">
                                    <button class="btn btn-default" type="submit">
                                        <span class="glyphicon glyphicon-comment" aria-hidden="true"></span>
                                    </button>
                                </span>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </nav>
        <!--JS-->
        <%@include file="chat_js.jsp"%>
    </body>
</html>