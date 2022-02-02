package com.alsw.crm.settings.domain;

public class User {
    /*
        登录
            验证
                1）账号
                2）密码
            sql
                User user = select * from tbl_user where id = ? and loginPwd = ?;

            user为null 账号密码错误 抛出异常
            user不为null 账号密码正确，这时继续验证
                expireTime 失效时间
                lockState 锁定状态
                allowIps 允许访问的ip request.get
     */
    private String id;  // 编号 主键
    private String loginAct;    // 登录账号
    private String name;    // 用户真实姓名
    /*
        密码
            使用MD5加密
            数据库中不存储明文密码 不安全
     */
    private String loginPwd;    // 登录密码
    private String email;   // 邮箱
    /*
        关于字符串中表现的日期及时间，常见的有两种方式
        日期：年月日 10位字符串
            yyyy-MM-dd
        日期+时间：年月日时分秒 19位字符串
            yyyy-MM-dd HH:mm:ss
     */
    private String expireTime;  // 失效时间
    private String lockState;   // 锁定状态 0 1
    private String deptno;  // 部门编号
    private String allowIps;    // 允许访问ip地址
    private String createTime;  // 创建时间
    private String createBy;    // 创建者
    private String editTime;    // 修改时间
    private String editBy;  // 修改者

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginAct() {
        return loginAct;
    }

    public void setLoginAct(String loginAct) {
        this.loginAct = loginAct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getLockState() {
        return lockState;
    }

    public void setLockState(String lockState) {
        this.lockState = lockState;
    }

    public String getDeptno() {
        return deptno;
    }

    public void setDeptno(String deptno) {
        this.deptno = deptno;
    }

    public String getAllowIps() {
        return allowIps;
    }

    public void setAllowIps(String allowIps) {
        this.allowIps = allowIps;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public String getEditBy() {
        return editBy;
    }

    public void setEditBy(String editBy) {
        this.editBy = editBy;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", loginAct='" + loginAct + '\'' +
                ", name='" + name + '\'' +
                ", loginPwd='" + loginPwd + '\'' +
                ", email='" + email + '\'' +
                ", expireTime='" + expireTime + '\'' +
                ", lockState='" + lockState + '\'' +
                ", deptno='" + deptno + '\'' +
                ", allowIps='" + allowIps + '\'' +
                ", createTime='" + createTime + '\'' +
                ", createBy='" + createBy + '\'' +
                ", editTime='" + editTime + '\'' +
                ", editBy='" + editBy + '\'' +
                '}';
    }
}
