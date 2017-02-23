package com.robust.toney.insertcodetool.util;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * Created by toney on 2017/2/10.
 */

public class AsmUtil implements Opcodes{
    private AsmUtil(){
        super();
    }

    /**
     *
     * @param classVisitor
     * @param fieldName  字段名称
     * @param fieldClass  字段类型完整描述 如 Lcom/robust/toney/insertcodetool/bean/Person
     */
    public final static void addClassField(ClassVisitor classVisitor,String fieldName,String fieldClass){
        classVisitor.visitField(ACC_PUBLIC | ACC_STATIC,fieldName,fieldClass,null,null );
    }

    /**
     *
     * @param classVisitor
     * @param fieldName 字段名称
     * @param fieldClass 字段类
     */
    public final static void addClassField(ClassVisitor classVisitor,String fieldName,Class<?> fieldClass){
        classVisitor.visitField(ACC_PUBLIC | ACC_STATIC,fieldName, Type.getDescriptor(fieldClass),null,null );
    }


    /**
     *
     * @param methodVisitor
     * @param className 字段所在类名
     * @param fieldName 字段名称
     * @param fieldClass  字段类型完整描述 如 Lcom/robust/toney/insertcodetool/bean/Person
     */
    public final static void addClassFieldValue(MethodVisitor methodVisitor,String className, String fieldName, String fieldClass){
        methodVisitor.visitFieldInsn(PUTSTATIC,className,fieldName,fieldClass);
    }

    /**
     *
     * @param methodVisitor
     * @param className 字段所在类完整名称
     * @param fieldName 字段名称
     * @param fieldClass 字段类
     */
    public final static void addClassFieldValue(MethodVisitor methodVisitor,String className, String fieldName, Class<?> fieldClass){
        methodVisitor.visitFieldInsn(PUTSTATIC,className,fieldName,Type.getDescriptor(fieldClass));
    }



    public static void insertCodeInMethod(MethodVisitor mv){

        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitFieldInsn(GETSTATIC,null,null,null);

        Label l1 = new Label();
        mv.visitJumpInsn(IFNULL,l1);


    }

}
