package com.toney.robust.plugin.insertcode.util.leakcanary;

import com.toney.robust.plugin.insertcode.util.two.AbstractClassVisitor;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


/**
 * Created by toney on 2017/2/23.
 */

public class ApplicationClassVisitor extends AbstractClassVisitor {


    private final static int FIELD_ACCESS =  Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC;
    private boolean hasAddFiled;
    private String addFieldName = "refWatcher";
    private String addFieldDesc = "Lcom/squareup/leakcanary/RefWatcher;";
    private Object addFieldValue = null;
    private String appDesc ;

    public ApplicationClassVisitor(ClassVisitor delegate,String appDesc) {
        super(delegate);
        this.appDesc = appDesc;
    }

    @Override
    public FieldVisitor visitField(int access ,String name, String desc, String signature, Object value) {
        if(name.equals(addFieldName)){
            hasAddFiled = true;
        }
        return super.visitField(access, name, desc, signature, value);
    }

    @Override
    public void visitEnd() {

        if(!hasAddFiled){
            FieldVisitor fv = super.visitField(FIELD_ACCESS,addFieldName,addFieldDesc,null,addFieldValue);
            if(fv != null){
                fv.visitEnd();
            }
        }

        super.visitEnd();
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access,name,desc,signature,exceptions);
        if(name.equals("onCreate")){
            System.out.println("has application");
            return new MethodAdapter(mv);
        }
        return mv;
    }

    class MethodAdapter extends MethodVisitor{


        public MethodAdapter(MethodVisitor methodVisitor) {
            super(Opcodes.ASM5, methodVisitor);
        }


        @Override
        public void visitCode() {
            super.visitCode();



        }

        @Override
        public void visitEnd() {


            super.visitEnd();
        }

        @Override
        public void visitInsn(int opcode) {
            if(opcode == Opcodes.RETURN){
                mv.visitCode();
                Label label0 = new Label();
                mv.visitLabel(label0);
                mv.visitLineNumber(25,label0);
                mv.visitJumpInsn(0,label0);
                mv.visitVarInsn(Opcodes.ALOAD,0);
                mv.visitMethodInsn(Opcodes.INVOKESTATIC,"com/squareup/leakcanary/LeakCanary", "install", "(Landroid/app/Application;)Lcom/squareup/leakcanary/RefWatcher;", false);
                mv.visitFieldInsn(Opcodes.PUTSTATIC,appDesc,"refWatcher","Lcom/squareup/leakcanary/RefWatcher;");


                Label label2 = new Label();
                mv.visitLabel(label2);
                mv.visitLocalVariable("this", "Lcom/robust/toney/MainApp;", null, label0, label2, 0);
                mv.visitEnd();
            }
            super.visitInsn(opcode);
        }
    }

    public static void main(String[] args) throws Exception {

        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        String source = "E:\\workspace\\android_studio\\me\\RobustStudy\\RobustHost\\build\\intermediates\\classes\\debug\\com\\robust\\toney\\MainApp.class";
        String des = "E:\\workspace\\android_studio\\me\\RobustStudy\\RobustHost\\build\\intermediates\\classes\\debug\\com\\robust\\toney\\MainApp1.class";
        File old = new File(source);
        File newFile = new File(des);
        try{

            inputStream = new FileInputStream(old);
            ClassReader reader = new ClassReader(inputStream);
            ClassWriter writer = new ClassWriter(reader,ClassWriter.COMPUTE_MAXS);
            ClassVisitor visitor = new ApplicationClassVisitor(writer,reader.getClassName());
            reader.accept(visitor,ClassReader.SKIP_DEBUG);
            byte[] newClass = writer.toByteArray();
            System.out.println("className : " + reader.getClassName());
            outputStream.write(newClass);


        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            try {
                if(inputStream != null){
                    inputStream.close();
                }

                if(outputStream != null){
                    outputStream.close();
                }
            }catch (Exception e){}
            boolean delFlag = old.delete();
            boolean renameFlag = newFile.renameTo(old);
            newFile.delete();
            System.out.println("delFlag = " + Boolean.toString(delFlag) + ",renameFlag :" + Boolean.toString(renameFlag));
        }

    }


}
