package com.toney.robust.plugin.insertcode;


import com.toney.robust.plugin.insertcode.util.leakcanary.ActivityClassVisitor;
import com.toney.robust.plugin.insertcode.util.leakcanary.ApplicationClassVisitor;
import com.toney.robust.plugin.insertcode.util.leakcanary.FragmentClassVisitor;

import org.gradle.api.Project;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by toney on 2017/2/24.
 */

public class LeakCanaryAsmUtil {

    public static Project project;
    private LeakCanaryAsmUtil(){
        super();

    }



    public static boolean insertAppClass(File classFile,File newClassFile) {
        FileOutputStream fos = null;
        FileInputStream fis = null;

        try{
            fis = new FileInputStream(classFile);
            fos = new FileOutputStream(newClassFile);


            // 使用全限定名，创建一个ClassReader对象
            ClassReader classReader = new ClassReader(fis);
            // 构建一个ClassWriter对象，并设置让系统自动计算栈和本地变量大小
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);

            ClassVisitor classAdapter = new ApplicationClassVisitor(classWriter,classReader.getClassName());
            classReader.accept(classAdapter, ClassReader.SKIP_DEBUG);

            byte[] newAry = classWriter.toByteArray();
            if(newAry == null){
                return false;
            }
            fos.write(newAry);
            newAry = null;
            return true;
        }catch(Exception e){
            Util.log(project,"add code to classfile error:"+e.toString());
        }finally{
            try{
                fos.close();
                fis.close();
            }catch(Exception e){
            }
        }
        return false;
    }

    public static boolean insertActivityClass(File classFile,File newClassFile,String appDesc){
        FileOutputStream fos = null;
        FileInputStream fis = null;

        try{
            fis = new FileInputStream(classFile);
            fos = new FileOutputStream(newClassFile);


            // 使用全限定名，创建一个ClassReader对象
            ClassReader classReader = new ClassReader(fis);
            // 构建一个ClassWriter对象，并设置让系统自动计算栈和本地变量大小
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            ClassVisitor classAdapter = new ActivityClassVisitor(classWriter,appDesc,"L"+ classReader.getClassName() +";");
            classReader.accept(classAdapter, ClassReader.SKIP_DEBUG);

            byte[] newAry = classWriter.toByteArray();
            if(newAry == null){
                return false;
            }
            fos.write(newAry);
            newAry = null;
            return true;
        }catch(Exception e){
            Util.log(project,"add code to classfile error:"+e.toString());
        }finally{
            try{
                fos.close();
                fis.close();
            }catch(Exception e){
            }
        }
        return false;
    }

    public static boolean insertFragmentClass(File src,File des,String appDesc){
        FileOutputStream fos = null;
        FileInputStream fis = null;

        try{
            fis = new FileInputStream(src);
            fos = new FileOutputStream(des);


            // 使用全限定名，创建一个ClassReader对象
            ClassReader classReader = new ClassReader(fis);
            // 构建一个ClassWriter对象，并设置让系统自动计算栈和本地变量大小
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            ClassVisitor classAdapter = new FragmentClassVisitor(classWriter,appDesc,"L" +classReader.getClassName() + ";");
            classReader.accept(classAdapter, ClassReader.SKIP_DEBUG);

            byte[] newAry = classWriter.toByteArray();
            if(newAry == null){
                return false;
            }
            fos.write(newAry);
            newAry = null;
            return true;
        }catch(Exception e){
            Util.log(project,"add code to classfile error:"+e.toString());
        }finally{
            try{
                fos.close();
                fis.close();
            }catch(Exception e){
            }
        }
        return false;
    }
}
