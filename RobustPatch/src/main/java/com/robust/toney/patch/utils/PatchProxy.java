package com.robust.toney.patch.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.robust.toney.patch.IChangeQuickRedirect;
import com.robust.toney.patch.IPatchInfo;
import com.robust.toney.patch.PatchClassInfo;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

import dalvik.system.DexClassLoader;

/**
 * Created by toney on 2017/2/9.
 */

public class PatchProxy {
    private DexClassLoader mLoader;
    public PatchProxy(String patchDexPath, Context context){
        super();
        try{
            File file = new File(patchDexPath);

            if(file != null && file.exists()){
                Log.e("PatchProxy","find patch.dex");
            }
            mLoader = new DexClassLoader(patchDexPath,context.getDir("libs",0).getAbsolutePath(),null,this.getClass().getClassLoader());
        }catch (Exception e){
            Log.e("PatchProxy","load patch dex fail :" + Log.getStackTraceString(e));
        }
    }

    public static boolean isSupport(Object[] argsObj, Object thisObj, IChangeQuickRedirect changeQuickRedirect,boolean isStatic){
        if(changeQuickRedirect == null){
            return false;
        }

        String classMethod = getClassMethod(isStatic);
        if(TextUtils.isEmpty(classMethod)){
            return  false;
        }

        return changeQuickRedirect.isSupport(classMethod,getObjects(argsObj));


    }

    public static Object accessDispatch(Object[] argsObj, Object thisObj, IChangeQuickRedirect changeQuickRedirect,boolean isStatic){
        if(changeQuickRedirect == null){
            return false;
        }

        String classMethod = getClassMethod(isStatic);
        if(TextUtils.isEmpty(classMethod)){
            return  false;
        }

        return changeQuickRedirect.accessDispatch(classMethod,getObjects(argsObj));
    }

    /**
     * 获取当前执行方法的信息
     * @param isStatic
     * @return
     */
    private static String getClassMethod(boolean isStatic){
        String str = "";
        try{
            StackTraceElement stackTraceElement = new Throwable().getStackTrace()[2];
            str = stackTraceElement.getClassName() + ":"+ stackTraceElement.getMethodName() + ":" + isStatic;
        }catch (Exception e){

        }

        return str;
    }

    private static Object[] getObjects(Object[] argsObj){
        return argsObj;
    }

    public void patch(){
        String className = "";
        try{
            Class<?> pathInfoClazz = mLoader.loadClass("com.robust.toney.patch.impl.PatchInfoImpl");
            List<PatchClassInfo> infoList = ((IPatchInfo)pathInfoClazz.newInstance()).getPatchedClassesInfo();
            for(PatchClassInfo info : infoList){
                IChangeQuickRedirect changeQuickRedirect = (IChangeQuickRedirect) mLoader.loadClass(info.getPatchClassName()).newInstance();
                Class<?> fixedClass = mLoader.loadClass(info.getFixClassName());
                className = info.getFixClassName();
                Field changeQuickRedirectField = fixedClass.getField("changeQuickRedirect");
                changeQuickRedirectField.set(null,changeQuickRedirect);
            }
            Log.d("toney","patch success ");
        }catch (Exception e){
            Log.d("toney","patch error : " + className +" " + Log.getStackTraceString(e));
        }
    }


}
