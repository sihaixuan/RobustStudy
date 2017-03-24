package asm.com.robust.toney;

import java.util.*;

import org.objectweb.asm.*;

public class MainAppDump implements Opcodes {

    public static byte[] dump() throws Exception {

        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;

        cw.visit(V1_7, ACC_PUBLIC + ACC_SUPER, "com/robust/toney/MainActivity", null, "android/support/v7/app/AppCompatActivity", null);

        cw.visitSource("MainActivity.java", null);

        cw.visitInnerClass("com/robust/toney/R$layout", "com/robust/toney/R", "layout", ACC_PUBLIC + ACC_FINAL + ACC_STATIC);

        cw.visitInnerClass("com/robust/toney/R$id", "com/robust/toney/R", "id", ACC_PUBLIC + ACC_FINAL + ACC_STATIC);

        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(7, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "android/support/v7/app/AppCompatActivity", "<init>", "()V", false);
            mv.visitInsn(RETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "Lcom/robust/toney/MainActivity;", null, l0, l1, 0);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PROTECTED, "onCreate", "(Landroid/os/Bundle;)V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(11, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESPECIAL, "android/support/v7/app/AppCompatActivity", "onCreate", "(Landroid/os/Bundle;)V", false);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(12, l1);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitLdcInsn(new Integer(2130968602));
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/robust/toney/MainActivity", "setContentView", "(I)V", false);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLineNumber(13, l2);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitLdcInsn(new Integer(2131492949));
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/robust/toney/MainActivity", "findViewById", "(I)Landroid/view/View;", false);
            mv.visitTypeInsn(CHECKCAST, "android/widget/TextView");
            mv.visitVarInsn(ASTORE, 2);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitLineNumber(14, l3);
            mv.visitTypeInsn(NEW, "com/robust/toney/MoneyBean");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "com/robust/toney/MoneyBean", "<init>", "()V", false);
            mv.visitVarInsn(ASTORE, 3);
            Label l4 = new Label();
            mv.visitLabel(l4);
            mv.visitLineNumber(15, l4);
            mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
            mv.visitLdcInsn("MoneyValue : ");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/robust/toney/MoneyBean", "getMoneyValue", "()I", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;", false);
            mv.visitLdcInsn(",desc : ");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitMethodInsn(INVOKESTATIC, "com/robust/toney/MoneyBean", "desc", "()Ljava/lang/String;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            mv.visitVarInsn(ASTORE, 4);
            Label l5 = new Label();
            mv.visitLabel(l5);
            mv.visitLineNumber(16, l5);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitMethodInsn(INVOKEVIRTUAL, "android/widget/TextView", "setText", "(Ljava/lang/CharSequence;)V", false);
            Label l6 = new Label();
            mv.visitLabel(l6);
            mv.visitLineNumber(17, l6);
            mv.visitInsn(RETURN);
            Label l7 = new Label();
            mv.visitLabel(l7);
            mv.visitLocalVariable("this", "Lcom/robust/toney/MainActivity;", null, l0, l7, 0);
            mv.visitLocalVariable("savedInstanceState", "Landroid/os/Bundle;", null, l0, l7, 1);
            mv.visitLocalVariable("textView", "Landroid/widget/TextView;", null, l3, l7, 2);
            mv.visitLocalVariable("bean", "Lcom/robust/toney/MoneyBean;", null, l4, l7, 3);
            mv.visitLocalVariable("content", "Ljava/lang/String;", null, l5, l7, 4);
            mv.visitMaxs(2, 5);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PROTECTED, "onDestroy", "()V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(21, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "android/support/v7/app/AppCompatActivity", "onDestroy", "()V", false);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(22, l1);
            mv.visitFieldInsn(GETSTATIC, "com/robust/toney/MainApp", "refWatcher", "Lcom/squareup/leakcanary/RefWatcher;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/squareup/leakcanary/RefWatcher", "watch", "(Ljava/lang/Object;)V", false);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLineNumber(23, l2);
            mv.visitInsn(RETURN);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitLocalVariable("this", "Lcom/robust/toney/MainActivity;", null, l0, l3, 0);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }
}
