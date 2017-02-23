package com.robust.toney.insertcodetool;

import java.util.*;

import org.objectweb.asm.*;

public class PersonDump implements Opcodes {

    public static byte[] dump() throws Exception {

        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;

        cw.visit(V1_7, ACC_PUBLIC + ACC_SUPER, "com/robust/toney/insertcodetool/bean/Person", null, "java/lang/Object", null);

        cw.visitSource("Person.java", null);

        {
            fv = cw.visitField(ACC_PUBLIC + ACC_STATIC, "changeQuickRedirect", "Lcom/robust/toney/patch/IChangeQuickRedirect;", null, null);
            fv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(11, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitInsn(RETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "Lcom/robust/toney/insertcodetool/bean/Person;", null, l0, l1, 0);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "toStr", "(Ljava/lang/String;)Ljava/lang/String;", null, null);
            mv.visitCode();

            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(15, l0);
            mv.visitFieldInsn(GETSTATIC, "com/robust/toney/insertcodetool/bean/Person", "changeQuickRedirect", "Lcom/robust/toney/patch/IChangeQuickRedirect;");

            Label l1 = new Label();
            mv.visitJumpInsn(IFNULL, l1);

            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLineNumber(16, l2);
            mv.visitInsn(ICONST_1);
            mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
            mv.visitInsn(DUP);
            mv.visitInsn(ICONST_0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitInsn(AASTORE);
            mv.visitInsn(ACONST_NULL);
            mv.visitFieldInsn(GETSTATIC, "com/robust/toney/insertcodetool/bean/Person", "changeQuickRedirect", "Lcom/robust/toney/patch/IChangeQuickRedirect;");
            mv.visitInsn(ICONST_1);
            mv.visitMethodInsn(INVOKESTATIC, "com/robust/toney/patch/utils/PatchProxy", "isSupport", "([Ljava/lang/Object;Ljava/lang/Object;Lcom/robust/toney/patch/IChangeQuickRedirect;Z)Z", false);
            mv.visitJumpInsn(IFEQ, l1);

            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitLineNumber(17, l3);
            mv.visitInsn(ICONST_1);
            mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
            mv.visitInsn(DUP);
            mv.visitInsn(ICONST_0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitInsn(AASTORE);
            mv.visitInsn(ACONST_NULL);
            mv.visitFieldInsn(GETSTATIC, "com/robust/toney/insertcodetool/bean/Person", "changeQuickRedirect", "Lcom/robust/toney/patch/IChangeQuickRedirect;");
            mv.visitInsn(ICONST_1);
            mv.visitMethodInsn(INVOKESTATIC, "com/robust/toney/patch/utils/PatchProxy", "accessDispatch", "([Ljava/lang/Object;Ljava/lang/Object;Lcom/robust/toney/patch/IChangeQuickRedirect;Z)Ljava/lang/Object;", false);
            mv.visitTypeInsn(CHECKCAST, "java/lang/String");
            mv.visitInsn(ARETURN);
            mv.visitLabel(l1);
            mv.visitLineNumber(21, l1);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            mv.visitLdcInsn("aaa");
            mv.visitInsn(ARETURN);
            Label l4 = new Label();
            mv.visitLabel(l4);
            mv.visitLocalVariable("str", "Ljava/lang/String;", null, l0, l4, 0);
            mv.visitMaxs(4, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(12, l0);
            mv.visitTypeInsn(NEW, "com/robust/toney/insertcodetool/MoneyBeanStatePatch");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "com/robust/toney/insertcodetool/MoneyBeanStatePatch", "<init>", "()V", false);
            mv.visitFieldInsn(PUTSTATIC, "com/robust/toney/insertcodetool/bean/Person", "changeQuickRedirect", "Lcom/robust/toney/patch/IChangeQuickRedirect;");
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 0);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }
}
