package com.toney.robust.plugin.insertcode.util.leakcanary;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by toney on 2017/2/24.
 */

public class FragmentClassVisitor extends ClassVisitor {

    private String appDesc;
    private String fragmentDesc;
    private boolean hasOnDestroyMethod;
    public FragmentClassVisitor(ClassVisitor visitor, String appDesc, String fragmentDesc) {
        super(Opcodes.ASM5,visitor);
        this.appDesc = appDesc;
        this.fragmentDesc = fragmentDesc;

    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        System.out.println("name :" + name);
        MethodVisitor mv = cv.visitMethod(access,name,desc,signature,exceptions);
        if(name.equals("onDestroy")){
            hasOnDestroyMethod = true;
            return new FragmentClassVisitor.MethodAdapter(mv);
        }
        return mv;
    }

    @Override
    public void visitEnd() {
        if(!hasOnDestroyMethod){
            hasOnDestroyMethod = true;
//            addOnDestroyMethod();


        }
        super.visitEnd();
    }

    private void addOnDestroyMethod(){
        MethodVisitor mv = super.visitMethod(Opcodes.ACC_PUBLIC,"onDestroy","()V",null,null);
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "android/support/v4/app/Fragment", "onDestroy", "()V", false);
        Label l1 = new Label();
        mv.visitLabel(l1);
//                mv.visitLineNumber(22, l1);
        mv.visitFieldInsn(Opcodes.GETSTATIC, appDesc, "refWatcher", "Lcom/squareup/leakcanary/RefWatcher;");
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/squareup/leakcanary/RefWatcher", "watch", "(Ljava/lang/Object;)V", false);

        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitLocalVariable("this", fragmentDesc, null, l0, l2, 0);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitEnd();

    }

    private   class MethodAdapter extends MethodVisitor{

        public MethodAdapter(MethodVisitor methodVisitor) {
            super(Opcodes.ASM5,methodVisitor);
        }

        @Override
        public void visitInsn(int opcode) {
            if(opcode == Opcodes.RETURN){
                mv.visitCode();
                Label l1 = new Label();
                mv.visitLabel(l1);
//                mv.visitLineNumber(22, l1);
                mv.visitFieldInsn(Opcodes.GETSTATIC, appDesc, "refWatcher", "Lcom/squareup/leakcanary/RefWatcher;");
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/squareup/leakcanary/RefWatcher", "watch", "(Ljava/lang/Object;)V", false);

                Label l2 = new Label();
                mv.visitLabel(l2);
                mv.visitLocalVariable("this", fragmentDesc, null, l1, l2, 0);
                mv.visitEnd();
            }
            super.visitInsn(opcode);
        }
    }

    public static void main(String[] args) throws Exception {
        String source = "E:\\workspace\\android_studio\\me\\RobustStudy\\RobustHost\\build\\intermediates\\classes\\debug\\com\\robust\\toney\\MainFragment.class";
        String des = "E:\\workspace\\android_studio\\me\\RobustStudy\\RobustHost\\build\\intermediates\\classes\\debug\\com\\robust\\toney\\MainFragment1.class";
        ClassReader reader = new ClassReader(new FileInputStream(new File(source)));
        ClassWriter writer = new ClassWriter(reader,ClassWriter.COMPUTE_MAXS);
        ClassVisitor visitor = new FragmentClassVisitor(writer,"com/robust/toney/MainApp","Lcom/robust/toney/MainFragment;");
        reader.accept(visitor,ClassReader.SKIP_DEBUG);
        byte[] newClass = writer.toByteArray();
        File newFile = new File(des);reader.getItemCount();
        new FileOutputStream(newFile).write(newClass);

    }
}
