package com.robust.toney.patch;

/**
 * Created by toney on 2017/2/9.
 * 保存修复类和旧类的类型
 */

public class PatchClassInfo {
    private String fixClassName; //需要修复类的名称
    private String patchClassName;//需要patch类名称

    public PatchClassInfo(String fixClassName,String patchClassName){
        this.fixClassName = fixClassName;
        this.patchClassName = patchClassName;
    }

    public String getFixClassName(){
        return  this.fixClassName;
    }

    public String getPatchClassName() {
        return patchClassName;
    }
}
