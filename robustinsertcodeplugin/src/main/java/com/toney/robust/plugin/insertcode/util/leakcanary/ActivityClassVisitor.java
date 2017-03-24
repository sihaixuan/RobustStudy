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

public class ActivityClassVisitor extends ClassVisitor {

    private String appDesc;
    private String activityDesc;
    private boolean hasOnDestroy;
    public ActivityClassVisitor(ClassVisitor visitor,String appDesc,String activityDesc) {
        super(Opcodes.ASM5,visitor);
        this.appDesc = appDesc;
        this.activityDesc = activityDesc;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access,name,desc,signature,exceptions);
        if(name.equals("onDestroy")){
            hasOnDestroy = true;
            return new ActivityClassVisitor.MethodAdapter(mv);
        }
        return mv;
    }

    @Override
    public void visitEnd() {
        if(!hasOnDestroy){
//            addOnDestoryMethod();
        }
        super.visitEnd();
    }

    private void addOnDestoryMethod(){

        MethodVisitor mv = super.visitMethod(Opcodes.ACC_PUBLIC,"onDestroy","()V",null,null);
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "android/support/v7/app/AppCompatActivity", "onDestroy", "()V", false);
        Label l1 = new Label();
        mv.visitLabel(l1);
//                mv.visitLineNumber(22, l1);
        mv.visitFieldInsn(Opcodes.GETSTATIC, appDesc, "refWatcher", "Lcom/squareup/leakcanary/RefWatcher;");
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/squareup/leakcanary/RefWatcher", "watch", "(Ljava/lang/Object;)V", false);

        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitLocalVariable("this", activityDesc, null, l0, l2, 0);
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
                mv.visitLocalVariable("this", activityDesc, null, l1, l2, 0);
                mv.visitEnd();
            }
            super.visitInsn(opcode);
        }
    }

    public static void main(String[] args) throws Exception {
        String source = "E:\\workspace\\android_studio\\me\\RobustStudy\\RobustHost\\build\\intermediates\\classes\\debug\\com\\robust\\toney\\MainActivity.class";
        String des = "E:\\workspace\\android_studio\\me\\RobustStudy\\RobustHost\\build\\intermediates\\classes\\debug\\com\\robust\\toney\\MainActivity1.class";
        ClassReader reader = new ClassReader(new FileInputStream(new File(source)));
        ClassWriter writer = new ClassWriter(reader,ClassWriter.COMPUTE_MAXS);
        ClassVisitor visitor = new ActivityClassVisitor(writer,"com/robust/toney/MainApp","L" + reader.getClassName() + ";");
        reader.accept(visitor,ClassReader.SKIP_DEBUG);
        byte[] newClass = writer.toByteArray();
        File newFile = new File(des);reader.getItemCount();
        new FileOutputStream(newFile).write(newClass);

    }
}
