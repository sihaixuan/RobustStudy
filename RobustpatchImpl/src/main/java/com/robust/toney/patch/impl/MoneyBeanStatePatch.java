package com.robust.toney.patch.impl;

import android.text.TextUtils;

import com.robust.toney.patch.IChangeQuickRedirect;

/**
 * Created by toney on 2017/2/9.
 */

public class MoneyBeanStatePatch implements IChangeQuickRedirect {
    @Override
    public boolean isSupport(String methodSignature, Object[] paramArray) {
        //校验合法性
        if(methodSignature == null || methodSignature.length() == 0){
            return false;
        }
        final String[] signatures =methodSignature.split(":");
        if(TextUtils.equals(signatures[1],"getMoneyValue")){
            return true;
        }

        if(TextUtils.equals(signatures[1],"desc")){
            return true;
        }

        return false;
    }

    @Override
    public Object accessDispatch(String methodSignature, Object[] paramsArray) {
        if(methodSignature == null || methodSignature.length() == 0){
            return false;
        }
        final String[] signatures =methodSignature.split(":");
        if(TextUtils.equals(signatures[1],"getMoneyValue")){
            return 10000;
        }

        if(TextUtils.equals(signatures[1],"desc")){
            return "Patch Desc";
        }
        return null;
    }
}
