<%@ page import="com.alsw.crm.workbench.domain.Activity" %>
<%@ page import="com.alsw.crm.settings.domain.User" %>
<%@ page import="java.util.List" %>
<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%
    Activity activity = (Activity) request.getAttribute("activity");
    List<User> userList = (List<User>) request.getAttribute("userList");
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

    <script type="text/javascript">
        //默认情况下取消和保存按钮是隐藏的
        var cancelAndSaveBtnDefault = true;
        $(function () {

            showRemarkList();

            $("#remarkTextArea").focus(function () {
                if (cancelAndSaveBtnDefault) {
                    //设置remarkDiv的高度为130px
                    $("#remarkDiv").css("height", "130px");
                    //显示
                    $("#cancelAndSaveBtn").show("2000");
                    cancelAndSaveBtnDefault = false;
                }
            });

            $("#cancelBtn").click(function () {
                //显示
                $("#cancelAndSaveBtn").hide();
                //设置remarkDiv的高度为130px
                $("#remarkDiv").css("height", "90px");
                cancelAndSaveBtnDefault = true;
            });

            $(".remarkDiv").mouseover(function () {
                $(this).children("div").children("div").show();
            });

            $(".remarkDiv").mouseout(function () {
                $(this).children("div").children("div").hide();
            });

            $(".myHref").mouseover(function () {
                $(this).children("span").css("color", "red");
            });

            $(".myHref").mouseout(function () {
                $(this).children("span").css("color", "#E6E6E6");
            });

            $('#remarkBody').on('mouseover', ".remarkDiv", function () {
                $(this).children("div").children("div").show();
            })

            $('#remarkBody').on('mouseout', ".remarkDiv", function () {
                $(this).children("div").children("div").hide();

            })

            $('#remarkBody').on('mouseover', ".myHref", function () {
                $(this).children("span").css("color", "red");
            })

            $('#remarkBody').on('mouseout', ".myHref", function () {
                $(this).children("span").css("color", "#E6E6E6");
            })

            // 保存备注按钮
            $('#saveRemarkBtn').click(function () {
                var remarkText = $('#remarkTextArea').val();

                if (remarkText === '' || remarkText == null) {
                    return false;
                }

                $.ajax({
                    url: "workbench/activity/saveRemark.do",
                    data: {
                        "text": remarkText,
                        "activityID": "${activity.id}",
                        "createBy": "${user.name}"
                    },
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        if (data.success) {
                            $('#remarkTextArea').val('');
                            showNewRemark(data.remark);
                        } else {
                            alert("保存失败")
                        }
                    }
                })


            });

            // 更新备注按钮
            $('#updateRemarkBtn').click(function () {
                if (!confirm("是否更新该备注")) {
                    return false;
                }

                var remarkId = $('#edit-remarkId').val();
                var newRemarkText = $.trim($('#edit-noteContent').val());

                if (newRemarkText === "" || newRemarkText == null) {
                    alert("新的备注内容不能为空");
                    return false;
                }

                $.ajax({
                    url: "workbench/activity/updateRemark.do",
                    data: {
                        "remarkID": remarkId,
                        "newRemarkText": newRemarkText,
                        "editBy": "${user.name}"
                    },
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        if (data.success) {
                            $('#editRemarkModal').modal('hide');
                            removeRemarkDiv(data.remark.id, data.remark);
                        } else {
                            alert("更新失败")
                        }
                    }
                });

            });

        });

        // 在页面加载时给备注列表铺值
        function showRemarkList() {
            $.ajax({
                url: "workbench/activity/getActivityRemarkList.do",
                data: {
                    "id": "${activity.id}"
                },
                type: "get",
                dataType: "json",
                success: function (data) {
                    var $remarkDiv = $('#remarkDiv')
                    var activityName = "${activity.name}";
                    $.each(data, function (i, v) {
                        var remarkHtml = "";
                        remarkHtml += '<div id="' + v.id + '" class="remarkDiv" style="height: 60px;">'
                        remarkHtml += '<img title="' + v.createBy + '" src="image/user-thumbnail.png" style="width: 30px; height:30px;">'
                        remarkHtml += '<div style="position: relative; top: -40px; left: 40px;">'
                        remarkHtml += '<h5>' + v.noteContent + '</h5>'
                        remarkHtml += '<font color="gray">市场活动</font> <font color="gray">-</font> <b>' + activityName + '</b> <small style="color: gray;">'
                        remarkHtml += (v.editFlag == 0 ? v.createTime : v.editTime) + ' 由 ' + (v.editFlag == 0 ? v.createBy : v.editBy) + '</small>'
                        remarkHtml += '<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">'
                        remarkHtml += '<a class="myHref" href="javascript:void(0);" onclick="editRemark(\'' + v.id + '\')">' +
                            '<span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>'
                        remarkHtml += '&nbsp;&nbsp;&nbsp;&nbsp;' +
                            '<a class="myHref" href="javascript:void(0);" onclick="deleteRemark(\'' + v.id + '\' , \'' + v.id + '\')">' +
                            '<span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>'
                        remarkHtml += '</div>'
                        remarkHtml += '</div>'
                        remarkHtml += '</div>'
                        $remarkDiv.before(remarkHtml);
                    })
                }

            });
        }

        function showNewRemark(remark) {
            var $remarkDiv = $('#remarkDiv2')
            var activityName = "${activity.name}";

            var remarkHtml = "";
            remarkHtml += '<div id="' + remark.id + '" class="remarkDiv" style="height: 60px;">'
            remarkHtml += '<img title="' + remark.createBy + '" src="image/user-thumbnail.png" style="width: 30px; height:30px;">'
            remarkHtml += '<div style="position: relative; top: -40px; left: 40px;">'
            remarkHtml += '<h5>' + remark.noteContent + '</h5>'
            remarkHtml += '<font color="gray">市场活动</font> <font color="gray">-</font> <b>' + activityName + '</b> <small style="color: gray;">'
            remarkHtml += (remark.editFlag == 0 ? remark.createTime : remark.editTime) + ' 由 ' + (remark.editFlag == 0 ? remark.createBy : remark.editBy) + '</small>'
            remarkHtml += '<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">'
            remarkHtml += '<a class="myHref" href="javascript:void(0);" onclick="editRemark(\'' + remark.id + '\')">' +
                '<span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>'
            remarkHtml += '&nbsp;&nbsp;&nbsp;&nbsp;' +
                '<a class="myHref" href="javascript:void(0);" onclick="deleteRemark(\'' + remark.id + '\' , \'' + remark.id + '\')">' +
                '<span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>'
            remarkHtml += '</div>'
            remarkHtml += '</div>'
            remarkHtml += '</div>'

            $remarkDiv.after(remarkHtml);

            showRemarkDiv(remark.id);

        }

        function editRemark(remarkId) {

            // 为修改备注模态框铺值
            $.ajax({
                url: "workbench/activity/getActivityRemark.do",
                data: {
                    "remarkID": remarkId
                },
                type: "get",
                dataType: "json",
                success: function (data) {
                    $('#edit-remarkId').val(data.id);
                    $('#edit-noteContent').val(data.noteContent);
                    // 打开修改备注模态框
                    $('#editRemarkModal').modal('show');
                }
            })


        }

        function deleteRemark(divId, id) {
            if (!confirm("是否删除该备注")) {
                return false;
            }
            $.ajax({
                url: "workbench/activity/deleteRemark.do",
                data: {
                    "id": id
                },
                type: "post",
                dataType: "json",
                success: function (data) {
                    if (data.success) {
                        removeRemarkDiv(divId);
                    } else {
                        alert("删除失败")
                    }

                }
            });
        }

        function showRemarkDiv(divId) {
            $('#' + divId + '').css("opacity", "0")
            $('#' + divId + '').css("height", "0px")

            $('#' + divId + '').animate({
                opacity: '1',
                height: '60px'
            }, 300);
        }

        function removeRemarkDiv(divId, remark) {
            $('#' + divId + '').animate({
                height: '0px',
                opacity: '0'
            }, 300, function () {
                $('#' + divId + '').css('display', 'none');
                $('#' + divId + '').empty();
                showNewRemark(remark);
            });
        }

    </script>

    <style>
        .remarkDiv {
            margin: 20px;
        }
    </style>

</head>
<body>

<!-- 修改市场活动备注的模态窗口 -->
<div class="modal fade" id="editRemarkModal" role="dialog">
    <%-- 备注的id --%>
    <input type="hidden" id="edit-remarkId">
    <div class="modal-dialog" role="document" style="width: 40%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title">修改备注</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label for="edit-describe" class="col-sm-2 control-label">内容</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="edit-noteContent"></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="updateRemarkBtn">更新</button>
            </div>
        </div>
    </div>
</div>

<!-- 修改市场活动的模态窗口 -->
<div class="modal fade" id="editActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title">修改市场活动</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" role="form">

                    <div class="form-group">
                        <label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-marketActivityOwner">
                                <option>zhangsan</option>
                                <option>lisi</option>
                                <option>wangwu</option>
                            </select>
                        </div>
                        <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-startTime" value="2020-10-10">
                        </div>
                        <label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-endTime" value="2020-10-20">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-cost" value="5,000">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="edit-describe">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" data-dismiss="modal">更新</button>
            </div>
        </div>
    </div>
</div>

<!-- 返回按钮 -->
<div style="position: relative; top: 35px; left: 10px;">
    <a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left"
                                                                         style="font-size: 20px; color: #DDDDDD"></span></a>
</div>

<!-- 大标题 -->
<div style="position: relative; left: 40px; top: -30px;">
    <div class="page-header">
        <h3>市场活动-${activity.name}<small> ${activity.startDate} ~ ${activity.endDate}</small></h3>
    </div>
    <div style="position: relative; height: 50px; width: 250px;  top: -72px; left: 700px;">
        <button type="button" class="btn btn-default" data-toggle="modal" data-target="#editActivityModal"><span
                class="glyphicon glyphicon-edit"></span> 编辑
        </button>
        <button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
    </div>
</div>

<!-- 详细信息 -->
<div style="position: relative; top: -70px;">
    <div style="position: relative; left: 40px; height: 30px;">
        <div style="width: 300px; color: gray;">所有者</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;">
            ${activity.owner}
        </div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">名称</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${activity.name}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>

    <div style="position: relative; left: 40px; height: 30px; top: 10px;">
        <div style="width: 300px; color: gray;">开始日期</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activity.startDate}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">结束日期</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${activity.endDate}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 20px;">
        <div style="width: 300px; color: gray;">成本</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activity.cost}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 30px;">
        <div style="width: 300px; color: gray;">创建者</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;">
            <b>${activity.createBy}&nbsp;&nbsp;</b><small
                style="font-size: 10px; color: gray;">${activity.createTime}</small></div>
        <div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 40px;">
        <div style="width: 300px; color: gray;">修改者</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>
            ${activity.editBy}
            &nbsp;&nbsp;</b><small
                style="font-size: 10px; color: gray;">
            ${activity.editTime}
        </small></div>
        <div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 50px;">
        <div style="width: 300px; color: gray;">描述</div>
        <div style="width: 630px;position: relative; left: 200px; top: -20px;">
            <b>
                ${activity.description}
            </b>
        </div>
        <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
</div>

<!-- 备注 -->
<div id="remarkBody" style="position: relative; top: 30px; left: 40px;">
    <div id="remarkDiv2" class="page-header">
        <h4>备注</h4>
    </div>

    <div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
        <form role="form" style="position: relative;top: 10px; left: 10px;">
            <textarea id="remarkTextArea" class="form-control" style="width: 850px; resize : none;" rows="2"
                      placeholder="添加备注..."></textarea>
            <p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
                <button id="cancelBtn" type="button" class="btn btn-default">取消</button>
                <button type="button" class="btn btn-primary" id="saveRemarkBtn">保存</button>
            </p>
        </form>
    </div>
</div>
<div style="height: 200px;"></div>
</body>
</html>