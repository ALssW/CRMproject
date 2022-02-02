<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

    <script>
        $(function () {

            if (window.top != window) {
                window.top.location = window.location;
            }

            // 页面加载完毕后清空输入框
            $('#loginAct').val("");

            $("#loginAct").focus();

            $('#submitBtn').click(function () {
                login();
            })

            // 登录页面登录回车键盘事件
            $(window).keydown(function (event) {
                if (event.keyCode === 13) {
                    // $('#submitBtn')
                    login();
                }
            })
        })

        function login() {
            console.log("开始执行登录操作");

            // 获取账号密码
            var loginAct = $.trim($('#loginAct').val());
            var loginPwd = $.trim($('#loginPwd').val());
            // 账号密码不能为空
            if (loginAct === "" || loginPwd === "") {
                $('#msg').html("账号密码不能为空")
                return false;
            }

            // 开始向后台发送ajax请求开始验证
            $.ajax({
                url: "settings/user/login.do",
                data: {
                    "loginAct": loginAct,
                    "loginPwd": loginPwd
                },
                type: "post",
                dataType: "json",
                success: function (data) {
                    /*
                        验证后返回一个标识代表验证是否成功
                            "success" : true/false
                        返回失败类型
                            "msg" : 错误类型
                     */
                    if (data.success) {
                        // 登录成功 跳转工作台初始页
                        console.log("登录成功")
                        window.location.href = "workbench/index.jsp";
                    } else {
                        console.log("登录失败")
                        console.log(data.msg)
                        $('#msg').html(data.msg);
                    }
                }

            })
        }

    </script>

</head>
<body>
<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
    <img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
</div>
<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
    <div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">
        CRM &nbsp;<span style="font-size: 12px;">&copy;2022&nbsp;ALsW</span></div>
</div>

<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
    <div style="position: absolute; top: 0px; right: 60px;">
        <div class="page-header">
            <h1>登录</h1>
        </div>
        <form action="workbench/index.jsp" class="form-horizontal" role="form">
            <div class="form-group form-group-lg">
                <div style="width: 350px;">
                    <input id="loginAct" class="form-control" type="text" placeholder="用户名">
                </div>
                <div style="width: 350px; position: relative;top: 20px;">
                    <input id="loginPwd" class="form-control" type="password" placeholder="密码">
                </div>
                <div class="checkbox" style="position: relative;top: 30px; left: 10px; color: red;">

                    <span id="msg"></span>

                </div>
                <button id="submitBtn" type="button" class="btn btn-primary btn-lg btn-block"
                        style="width: 350px; position: relative;top: 45px;">登录
                </button>
            </div>
        </form>
    </div>
</div>
</body>
</html>