package com.zxycloud.hzy_xg.bean.base;

import com.zxycloud.hzy_xg.base.BaseBean;

/**
 * 用户信息Bean
 *
 * @author leiming
 * @date 2017/10/11
 */
public class UserBean extends BaseBean {
    /**
     * userId                          : String   , 用户ID
     * username                     : String   , 用户名
     * phone                        : String   , 用户手机号
     * password                     : String   , 用户密码
     * allowLoginNum                : integer  , 允许登录数量
     * state                        : integer  , 用户状态
     * mail                         : String   , 用户邮箱
     * phoneVerificationState       : boolean  , 用户手机验证状态
     * mailVerificationState        : boolean  , 用户邮箱验证状态
     * randomCode                   : String   , 用户密码加密随机项
     * nickname                     : String   , 用户昵称
     * avatarUrl                    : String   , 用户头像地址
     */

    private String userId;
    private String username;
    private String phone;
    private String password;
    private int allowLoginNum;
    private int state;
    private String mail;
    private boolean phoneVerificationState;
    private boolean mailVerificationState;
    private String randomCode;
    private String nickname;
    private String avatarUrl;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAllowLoginNum() {
        return allowLoginNum;
    }

    public void setAllowLoginNum(int allowLoginNum) {
        this.allowLoginNum = allowLoginNum;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public boolean isPhoneVerificationState() {
        return phoneVerificationState;
    }

    public void setPhoneVerificationState(boolean phoneVerificationState) {
        this.phoneVerificationState = phoneVerificationState;
    }

    public boolean isMailVerificationState() {
        return mailVerificationState;
    }

    public void setMailVerificationState(boolean mailVerificationState) {
        this.mailVerificationState = mailVerificationState;
    }

    public String getRandomCode() {
        return randomCode;
    }

    public void setRandomCode(String randomCode) {
        this.randomCode = randomCode;
    }

    public String getNickName() {
        return nickname;
    }

    public void setNickName(String nickName) {
        this.nickname = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
