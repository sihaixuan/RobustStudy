package com.robust.toney.patch;

/**
 * Created by toney on 2017/2/9.
 */

public interface IChangeQuickRedirect {
    /**
     * 判断当前方法是否支持修复 方法所属类全称:方法名:方法是否为static类型，注意中间使用冒号进行连接的
     * @param methodSignature
     * @param paramArray
     * @return
     */
    boolean isSupport(String methodSignature, Object[] paramArray);

    /**
     * 具体修复逻辑
     * @param methodSignature
     * @param paramsArray
     * @return
     */
    Object accessDispatch(String methodSignature, Object[] paramsArray);

}
