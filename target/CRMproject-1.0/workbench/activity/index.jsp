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
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css"
          rel="stylesheet"/>
    <link href="jquery/bs_pagination/jquery.bs_pagination.min.css" rel="stylesheet">

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

    <script type="text/javascript">

        $(function () {
            // 页面加载完成后获取活动列表
            pageList(1, 4);

            // 查询按钮 查询活动
            $('#search').click(function () {

                // 每次查询前，将搜索框中的值赋予隐藏域
                $('#hidden-name').val($.trim($('#search-name').val()));
                $('#hidden-owner').val($.trim($('#search-owner').val()));
                $('#hidden-startDate').val($.trim($('#search-startDate').val()));
                $('#hidden-endDate').val($.trim($('#search-endDate').val()));

                pageList(1, $('#activityPage').bs_pagination('getOption', 'rowsPerPage'));
            });

            // 创建按钮 打开创建操作的模态框
            $('#addBtn').click(function () {

                // 在模态框打开之前获取用户数据 并在下拉框中铺值
                $.ajax({
                    url: "workbench/activity/getNames.do",
                    type: "get",
                    dataType: "json",
                    success: function (data) {
                        $.each(data, function (index, v) {
                            var cmAOchilds = $('#create-owner').children();

                            for (let i = 0; i < cmAOchilds.length; i++) {
                                if (cmAOchilds.eq(i).val() === v.id) {
                                    return;
                                }
                            }

                            $('#create-owner').append("<option value=" + v.id + ">" + v.name + "</option>")
                        })
                        $('#create-owner').val("${user.id}");
                    }
                });

                // 在模态框打开之前 为日期框铺值
                $('.time').datetimepicker({
                    minView: "mouth",
                    language: "zh-CN",
                    format: "yyyy-mm-dd",
                    autoclose: true,
                    todayBtn: true,
                    pickerPosition: "bottom-left"
                });

                $('#createActivityModal').modal('show');
            });

            // 保存按钮 执行添加操作
            $('#saveBtn').click(function () {

                $.ajax({
                    url: "workbench/activity/save.do",
                    data: {
                        "owner": $.trim($('#create-owner').val()),
                        "name": $.trim($('#create-name').val()),
                        "startDate": $.trim($('#create-startDate').val()),
                        "endDate": $.trim($('#create-endDate').val()),
                        "cost": $.trim($('#create-cost').val()),
                        "description": $.trim($('#create-description').val())
                    },
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        // 回调 返回添加操作是否成功
                        if (data.success) {
                            // 添加成功
                            console.log("添加成功")
                            // 刷新市场活动列表 局部刷新
                            pageList(1, $('#activityPage').bs_pagination('getOption', 'rowsPerPage'));
                            // 清空添加操作模态框
                            $('#clearAddForm').trigger("click");

                            // 关闭添加操作模态框
                            $('#createActivityModal').modal('hide');
                        } else {
                            // 添加失败
                            console.log('添加失败')
                        }
                    }
                });
            });

            // 修改按钮 打开修改操作的模态框
            $('#editBtn').click(function () {
                // 获取已选dataList的id
                var $checkedDataList = $('input[name=checkedDataList]:checked');
                console.log($checkedDataList.length);
                if ($checkedDataList.length === 0) {
                    alert("请选择要修改的记录")
                    return false;
                } else if ($checkedDataList.length > 1) {
                    alert("只能选择一条记录")
                    return false;
                }
                var activityID = $checkedDataList.val();

                // 在打开之前铺值
                $.ajax({
                    url: "workbench/activity/getUserAndActivity.do",
                    data: {
                        "activityID": activityID
                    },
                    type: "get",
                    dataType: "json",
                    success: function (data) {
                        console.log(data)
                        // 铺值
                        // 遍历铺值用户名
                        var edAOchilds = $('#edit-owner').children();
                        $.each(data.userList, function (i, v) {
                            if (edAOchilds.eq(i).val() != v.id) {
                                $('#edit-owner').append("<option value=" + v.id + ">" + v.name + "</option>")
                            }
                        });
                        // 选项默认为该活动所有者
                        $('#edit-owner').val(data.activity.owner);

                        // 信息铺值
                        $('#edit-name').val(data.activity.name);
                        $('#edit-startDate').val(data.activity.startDate);
                        $('#edit-endDate').val(data.activity.endDate);
                        $('#edit-cost').val(data.activity.cost);
                        $('#edit-description').val(data.activity.description);

                    }
                });
                $('#editActivityModal').modal('show');
            });

            // 更新按钮 更新修改后的市场活动
            $('#updateBtn').click(function () {
                if (!confirm("是否更新该市场活动")) {
                    return false;
                }
                var activityID = $('input[name=checkedDataList]:checked').val();

                $.ajax({
                    url: "workbench/activity/updateActivity.do",
                    data: {
                        "id": activityID,
                        "owner": $.trim($('#edit-owner').val()),
                        "name": $.trim($('#edit-name').val()),
                        "startDate": $.trim($('#edit-startDate').val()),
                        "endDate": $.trim($('#edit-endDate').val()),
                        "cost": $.trim($('#edit-cost').val()),
                        "description": $.trim($('#edit-description').val()),
                        "editBy": "${user.id}"
                    },
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        if (data.success) {
                            console.log("更新成功");
                            pageList($('#activityPage').bs_pagination('getOption', 'currentPage'),
                                $('#activityPage').bs_pagination('getOption', 'rowsPerPage'));
                            $('#editActivityModal').modal('hide');
                        }
                    }
                });
            });

            // 删除按钮 删除市场活动
            $('#deleteBtn').click(function () {
                // 找到所有勾上的dataList复选框
                var $checkedDataList = $('input[name=checkedDataList]:checked');

                if ($checkedDataList.length === 0) {
                    alert("请选择要删除的记录")
                } else {

                    if (!confirm("是否删除")) {
                        return false;
                    }

                    console.log("开始执行删除操作")
                    // 拼接选择的记录id
                    var param = "";
                    for (let i = 0; i < $checkedDataList.length; i++) {
                        param += "id=" + $checkedDataList[i].value;

                        if (i < $checkedDataList.length - 1)
                            param += "&";
                    }
                    // 在url中传递参数
                    $.ajax({
                        url: "workbench/activity/deleteActivity.do",
                        data: param,
                        type: "post",
                        dataType: "json",
                        success: function (data) {
                            if (data.success) {
                                pageList(1, $('#activityPage').bs_pagination('getOption', 'rowsPerPage'));
                                alert("删除成功")
                            } else {
                                alert("删除失败")
                            }
                        }
                    });
                }

            })

            // 数据列表多选框 全选与反选
            $('#dataListCheckBox').click(function () {
                $('input.dataListCheckBoxs').prop("checked", this.checked)
            });

            // 动态生成的元素需要使用on方法绑定事件
            $('#activityListBody').on('click', $('input[name=checkedDataList]'), function () {
                var checkF = $('input[name=checkedDataList]').length == $('input[name=checkedDataList]:checked').length;
                $('#dataListCheckBox').prop("checked", checkF);

            })

        });

        // pageNo 页码 pageSize 每页展示的记录数
        // 发出ajax请求，从后台获取最新的市场活动信息列表数据，并局部刷新展示
        // 什么时候调用该方法
        // 1.获取市场活动模块时 2.添加、修改、删除操作后 3.点击查询按钮时 4.点击分页时
        function pageList(pageNo, pageSize) {
            // 将全选改为未选中
            $('#dataListCheckBox').prop("checked", false);

            // 每次查询前取出隐藏域中保存的信息，并重新赋予搜索框中
            $('#search-name').val($.trim($('#hidden-name').val()));
            $('#search-owner').val($.trim($('#hidden-owner').val()));
            $('#search-startDate').val($.trim($('#hidden-startDate').val()));
            $('#search-endDate').val($.trim($('#hidden-endDate').val()));

            $.ajax({
                url: "workbench/activity/getActivityList.do",
                data: {
                    "pageNo": pageNo,
                    "pageSize": pageSize,
                    "name": $.trim($('#search-name').val()),
                    "owner": $.trim($('#search-owner').val()),
                    "startDate": $.trim($('#search-startDate').val()),
                    "endDate": $.trim($('#search-endDate').val())
                },
                type: "get",
                dataType: "json",
                success: function (data) {
                    /*
                        data
                            活动列表json
                                {{活动1},{活动2}}
                            分页插件
                                查询的总记录数
                                {"total":xx}
                            {"total":10, "dataList":[{活动1},{活动2}]}
                     */
                    console.log(data);
                    var dataHtml = "";
                    $.each(data.dataList, function (i, v) {
                        dataHtml += '<tr class="active">';
                        dataHtml += '<td><input type="checkbox" name="checkedDataList" class="dataListCheckBoxs" value="' + v.id + '" /></td>';
                        dataHtml += '<td><a style="text-decoration: none; cursor: pointer;' +
                            '"onclick="window.location.href=\'workbench/activity/detail.jsp\';">' + v.name + '</a></td>';
                        dataHtml += '<td>' + v.owner + '</td>';
                        dataHtml += '<td>' + v.startDate + '</td>';
                        dataHtml += '<td>' + v.endDate + '</td>';
                        dataHtml += '</tr>';
                    });
                    $('#activityListBody').html(dataHtml);

                    // 数据处理完毕后 结合前端使用分页
                    var totalPages = Math.ceil(data.total / pageSize);
                    $('#activityPage').bs_pagination({
                        currentPage: pageNo, // 页码
                        rowsPerPage: pageSize,// 每页显示的记录条数
                        maxRowsPerPage: 20, // 每页最多显示的记录条数
                        totalPages: totalPages, // 总页数
                        totalRows: data.total, // 总记录条数
                        visiblePageLinks: 3, // 显示几个卡片

                        showGoToPage: true,
                        showRowsPerPage: true,
                        showRowsInfo: true,
                        showRowsDefaultInfo: true,
                        onChangePage: function (event, data) {
                            pageList(data.currentPage, data.rowsPerPage);
                        }

                    });
                }
            })
        }


    </script>
</head>
<body>

<%-- 使用隐藏域存储查询关键字 --%>
<input type="hidden" id="hidden-name"/>
<input type="hidden" id="hidden-owner"/>
<input type="hidden" id="hidden-startDate"/>
<input type="hidden" id="hidden-endDate"/>

<!-- 创建市场活动的模态窗口 -->
<div class="modal fade" id="createActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
            </div>
            <div class="modal-body">

                <form id="activityAddForm" class="form-horizontal" role="form">

                    <div class="form-group">
                        <label for="create-owner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-owner">

                            </select>
                        </div>
                        <label for="create-name" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-name">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-startDate" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="create-startDate">
                        </div>
                        <label for="create-endDate" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="create-endDate">
                        </div>
                    </div>
                    <div class="form-group">

                        <label for="create-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-cost">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="create-description" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="create-description"></textarea>
                        </div>
                    </div>


            </div>
            <div class="modal-footer">
                <button type="reset" class="btn btn-danger" id="clearAddForm">清空</button>
                </form>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="saveBtn">保存</button>
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
                <h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" role="form">

                    <div class="form-group">
                        <label for="edit-owner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-owner">

                            </select>
                        </div>
                        <label for="edit-name" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-name" value="">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-startDate" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-startDate" value="">
                        </div>
                        <label for="edit-endDate" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-endDate" value="">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-cost" value="">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-description" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="edit-description"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="updateBtn">更新</button>
            </div>
        </div>
    </div>
</div>


<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>市场活动列表</h3>
        </div>
    </div>
</div>
<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">

        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">名称</div>
                        <input class="form-control" type="text" id="search-name">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input class="form-control" type="text" id="search-owner">
                    </div>
                </div>


                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">开始日期</div>
                        <input class="form-control" type="text" id="search-startDate"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">结束日期</div>
                        <input class="form-control" type="text" id="search-endDate"/>
                    </div>
                </div>
                <button type="button" class="btn btn-default" id="search">查询</button>
            </form>
        </div>
        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-primary" id="addBtn">
                    <span class="glyphicon glyphicon-plus"></span> 创建
                </button>
                <button type="button" class="btn btn-default" id="editBtn"><span
                        class="glyphicon glyphicon-pencil"></span> 修改
                </button>
                <button type="button" class="btn btn-danger" id="deleteBtn"><span
                        class="glyphicon glyphicon-minus"></span> 删除
                </button>
            </div>

        </div>
        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" id="dataListCheckBox"/></td>
                    <td>名称</td>
                    <td>所有者</td>
                    <td>开始日期</td>
                    <td>结束日期</td>
                </tr>
                </thead>
                <tbody id="activityListBody">
                </tbody>
            </table>
        </div>

        <div style="height: 50px; position: relative;top: 30px;">
            <div id="activityPage">

            </div>
        </div>

    </div>

</div>
</body>
</html>