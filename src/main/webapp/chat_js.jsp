<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String path = request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<script type="text/javascript">
    //检测是否支持WebSocket
    window.onload = function () {
        if (!("WebSocket" in window)) {
            alert("你的浏览器版本过低");
            window.location.replace("about:blank");
        }
    };

    //全局变量
    var userid = "${userid}";
    var nickname = "${nickname}";
    var upload = document.getElementById("imgUpload");
    var canvas = document.getElementById("imgCanvas");
    var context = canvas.getContext("2d");

    //建立连接
    var socket = new WebSocket("ws://<%=path%>/connect/" + "${userid}" + "/" + "${nickname}");

    //关闭连接
    socket.onclose = function () { };

    //错误
    socket.onerror = function (evt) {
        alert("系统繁忙");
        window.location.replace("about:blank");
    };

    //检查是否自言自语
    function areYouCrazy() {
        var val = $("#userList > option:selected").val();
        if (val === userid) {
            alert("自言自语是不好滴");
            return false;
        } else {
            return true;
        }
    }

    //获取消息对象
    function getMessage() {
        var val = $("#userList > option:selected").val();
        var message = new Object();
        message.from = userid;
        message.fromNickname = nickname;
        if (val === "all") {
            message.type = "public";
        } else {
            message.type = "private";
            message.to = val;
            message.toNickname = $("#userList > option:selected").text();
        }
        return message;
    }

    //发送文本消息
    function sendTextMessage() {
        var check = areYouCrazy();
        if (check) {
            var message = getMessage();
            message.format = 'text';
            message.content = $("#messageContent").val();
            socket.send(JSON.stringify(message));
            $("#messageContent").val("");
        }
        return false;
    }

    //图片预览
    function previewImg() {
        var reader = new FileReader();
        reader.onload = function (event) {
            var img = new Image();
            img.onload = function () {
                var width = img.width <= 175 ? img.width : 175;
                var height = img.height <= 175 ? img.width : 175;
                canvas.width = width;
                canvas.height = height;
                context.drawImage(img, 0, 0, width, height);
            };
            img.src = event.target.result;
        };
        reader.readAsDataURL(upload.files[0]);
    }

    //发送图片消息
    function sendImgMessage() {
        var check = areYouCrazy();
        if (check) {
            var content = canvas.toDataURL("image/jpeg", 1.0);
            if (content.length > 1024 * 32) {
                alert("图片超出最大允许大小,发送失败");
            } else {
                var message = getMessage();
                message.format = 'img';
                message.content = content;
                socket.send(JSON.stringify(message));
                $("#sendImgCloseBtn").click();
            }
        }
        return false;
    }

    //修改昵称
    function updateNickname() {
        $("#updateNicknameCloseBtn").click();
        $("#navbarBtn").click();
        $("#updateNicknameLi").hide();
        var message = new Object();
        message.type = "updateNickname";
        message.from = userid;
        message.fromNickname = nickname;
        message.content = $("#updateNicknameText").val();
        socket.send(JSON.stringify(message));
        return false;
    }

    //用户统计
    function userReport() {
        var message = new Object();
        message.type = "userReport";
        message.from = userid;
        socket.send(JSON.stringify(message));
    }

    //每日统计
    function dailyReport() {
        var message = new Object();
        message.type = "dailyReport";
        message.from = userid;
        socket.send(JSON.stringify(message));
    }

    //切换管理员按钮组
    function toggleAdminBtn() {
        if ($("#userList > option:selected").val() === "all") {
            $("button[name='adminBtn']").hide();
        } else {
            $("button[name='adminBtn']").show();
        }
    }

    //登出
    function signOut() {
        socket.close();
        window.location.replace("about:blank");
    }

    //移除用户
    function removeUser() {
        var message = new Object();
        message.type = "removeUser";
        message.from = userid;
        message.fromNickname = nickname;
        message.to = $("#userList > option:selected").val();
        message.toNickname = $("#userList > option:selected").text();
        socket.send(JSON.stringify(message));
    }

    //拉黑用户
    function rejectUser() {
        var message = new Object();
        message.type = "rejectUser";
        message.from = userid;
        message.fromNickname = nickname;
        message.to = $("#userList > option:selected").val();
        message.toNickname = $("#userList > option:selected").text();
        socket.send(JSON.stringify(message));
    }

    //聊天记录
    function chatRecord() {
        var message = new Object();
        message.type = "chatRecord";
        message.from = userid;
        message.fromNickname = nickname;
        message.start = 0;
        message.end = 10;
        socket.send(JSON.stringify(message));
    }

    //滚动屏幕
    function scrollWindow() {
        $(window).scrollTop($(window).height() + $("#messageList > p:last").height());
    }

    //接收消息
    socket.onmessage = function (evt) {
        var message = JSON.parse(evt.data);
        //错误
        if (message.status === "error" && message.type === "error") {
            alert("系统繁忙");
            window.location.replace("about:blank");
        }
        //登录
        if (message.status === "success" && message.type === "signIn") {
            $("#messageList").append('<p class="text-center text-info">' + message.content + '加入了聊天室' + '</p>');
            scrollWindow();
        }
        //更新在线用户列表
        if (message.status === "success" && message.type === "updateUserList") {
            $.each(message.content, function (i, n) {
                if ($("#userList > option[value=" + n.userid + "]").length === 0) {
                    $("#userList").append('<option value=' + n.userid + '>' + n.nickname + '</option>');
                }
                if ($("#userList > option[value=" + n.userid + "]").length === 1) {
                    if ($("#userList > option[value=" + n.userid + "]").text() !== n.nickname) {
                        $("#userList > option[value=" + n.userid + "]").text(n.nickname);
                    }
                }
            });
        }
        //登出
        if (message.status === "success" && message.type === "signOut") {
            $("#messageList").append('<p class="text-center text-info">' + message.content + '离开了聊天室' + '</p>');
            scrollWindow();
        }
        //删除用户列表中指定用户
        if (message.status === "success" && message.type === "deleteUser") {
            if ($("#userList > option[value=" + message.content + "]").length === 1) {
                $("#userList > option[value=" + message.content + "]").remove();
                toggleAdminBtn();
            }
        }
        //群聊
        if (message.status === "success" && message.type === "public") {
            if (message.format === 'text') {
                $("#messageList").append('<p>' + message.fromNickname + '说:' + message.content + '</p>');
                scrollWindow();
            }
            if (message.format === 'img') {
                $("#messageList").append('<p>' + message.fromNickname + '说:' + '<img src=' + message.content + ' />' + '</p>');
                scrollWindow();
            }
        }
        //私聊
        if (message.status === "success" && message.type === "private") {
            if (message.format === 'text') {
                $("#messageList").append('<p class="text-danger">' + message.fromNickname + '说:' + message.content + '</p>');
                scrollWindow();
            }
            if (message.format === 'img') {
                $("#messageList").append('<p class="text-danger">' + message.fromNickname + '说:' + '<img src=' + message.content + ' />' + '</p>');
                scrollWindow();
            }
        }
        //修改昵称成功
        if (message.status === "success" && message.type === "updateNickname") {
            $("#nickname").text(message.content);
            nickname = message.content;
            $("#messageList").append('<p class="text-center text-danger">昵称修改成功</p>');
            $("#updateNicknameLi").show();
            scrollWindow();
        }
        //修改昵称失败
        if (message.status === "error" && message.type === "updateNickname") {
            $("#messageList").append('<p class="text-center text-danger">昵称修改失败,昵称已经存在</p>');
            $("#updateNicknameLi").show();
            scrollWindow();
        }
        //移除用户
        if (message.status === "success" && message.type === "removeUser") {
            signOut();
        }
        //拉黑用户
        if (message.status === "success" && message.type === "rejectUser") {
            $("#messageList").append('<p class="text-center text-danger">' + message.toNickname + '已经被加入黑名单' + '</p>');
            scrollWindow();
        }
        //聊天记录
        if (message.status === "success" && message.type === "chatRecord") {
            $("#chatRecordList").text("");
            $.each(message.content, function (i, n) {
                if (n.type === "public" && n.format === "text") {
                    $("#chatRecordList").append('<p>' + n.fromNickname + '说:' + n.content + '</p>');
                }
                if (n.type === "public" && n.format === "img") {
                    $("#chatRecordList").append('<p>' + n.fromNickname + '说:' + '<img src=' + n.content + ' />' + '</p>');
                }
                if (n.type === "private" && n.format === "text") {
                    $("#chatRecordList").append('<p class="text-danger">' + n.fromNickname + '说:' + n.content + '</p>');
                }
                if (n.type === "private" && n.format === "img") {
                    $("#chatRecordList").append('<p class="text-danger">' + n.fromNickname + '说:' + '<img src=' + n.content + ' />' + '</p>');
                }
            });
        }
        //用户统计
        if (message.status === "success" && message.type === "userReport") {
            $("#userReportList").text("");
            $.each(message.content, function (i, n) {
                $("#userReportList").append('<p>用户名:' + n.nickname + '   消息数量:' + n.total + '</p>');
            });
        }
        //每日统计
        if (message.status === "success" && message.type === "dailyReport") {
            $("#dailyReportList").text("");
            $.each(message.content, function (i, n) {
                $("#dailyReportList").append('<p>日期:' + n.date + '   消息数量:' + n.total + '</p>');
            });
        }
    };
</script>
