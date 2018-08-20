package com.zxycloud.hzy_xg.bean.GetBean;

import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.bean.base.UserBean;

/**
 * 获取用户信息Bean
 *
 * @author leiming
 * @date 2017/10/11
 */
public class GetUserInfoBean extends BaseBean {
    /**
     * 用户信息
     */
    private UserBean data;

    public UserBean getData() {
        return data;
    }

    public void setData(UserBean data) {
        this.data = data;
    }
}

