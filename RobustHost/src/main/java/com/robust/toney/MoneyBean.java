package com.robust.toney;

import com.robust.toney.patch.IChangeQuickRedirect;
import com.robust.toney.utils.PatchProxy;

/**
 * Created by toney on 2017/2/9.
 */

public class MoneyBean {
    public static IChangeQuickRedirect changeQuickRedirect;

    public static String desc(){
        if(changeQuickRedirect != null){
            if(PatchProxy.isSupport(new Object[0],null,changeQuickRedirect,true)){
                return (String) PatchProxy.accessDispatch(new Object[0],null,changeQuickRedirect,true);
            }
        }
        return "MoneyBean";
    }

    public int getMoneyValue(){
        if(changeQuickRedirect != null){
            if(PatchProxy.isSupport(new Object[0],this,changeQuickRedirect,false)){
                return (Integer) PatchProxy.accessDispatch(new Object[0],this,changeQuickRedirect,false);
            }
        }
        return 10;
    }
}
