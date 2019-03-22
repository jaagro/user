package com.jaagro.user.api.constant;

/**
 * @author tony
 */
public final class UserType {

    /**
     * 客户，签约客户 全局使用
     */
    public static final String CUSTOMER = "customer";

    /**
     * 装货地客户，物流业务使用
     */
    public static final String LOAD_SITE = "loadSite";

    /**
     * 卸货地客户，物流业务使用
     */
    public static final String UNLOAD_SITE = "unloadSite";

    /**
     * 员工 全局使用
     */
    public static final String EMPLOYEE = "employee";

    /**
     * 司机，物流业务使用
     */
    public static final String DRIVER = "driver";

    /**
     * 养殖农户，养殖业务使用
     */
    public static final String FARMERS = "farmers";

    /**
     * 游客身份司机(仅填写手机号)
     */
    public static final String VISITOR_DRIVER_P = "visitorDriver_p";
    /**
     * 游客身份司机(已上传基本信息)
     */
    public static final String VISITOR_DRIVER_U = "visitorDriver_u";

    /**
     * 游客身份客户(仅填写手机号)
     */
    public static final String VISITOR_CUSTOMER_P = "visitorCustomer_p";
    /**
     * 游客身份客户(已上传基本信息)
     */
    public static final String VISITOR_CUSTOMER_U = "visitorCustomer_u";
}
