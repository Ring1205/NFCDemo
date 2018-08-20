package com.zxycloud.hzy_xg.bean.PostBean;

import java.util.List;

public class PostDeleteLabelBean {
    /**
     * 所要删除的标签Id
     */
    private List<String> ids;

    public PostDeleteLabelBean(List<String> ids) {
        this.ids = ids;
    }
}
