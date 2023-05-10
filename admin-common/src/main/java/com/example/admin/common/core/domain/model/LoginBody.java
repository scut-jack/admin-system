package com.example.admin.common.core.domain.model;

/**
 * 用户登录对象
 */
public class LoginBody {
    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 唯一标识
     * 生成验证码时传给前端，用户登录时/注册时会带上用于做验证码的校验：
     * 校验过程为生成验证码会以 uuid -> 验证码正确结果 形式保存到redis，
     * 而后uuid会发送到前端用户填写验证码结果提交时，会拿uuid去redis取正确验证码结果与用户填写的验证码结果对比。
     */
    private String uuid;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
