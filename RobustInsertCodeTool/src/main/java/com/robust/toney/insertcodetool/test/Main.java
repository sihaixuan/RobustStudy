package com.robust.toney.insertcodetool.test;

import com.robust.toney.patch.IChangeQuickRedirect;
import com.robust.toney.patch.utils.PatchProxy;

import org.objectweb.asm.Type;

/**
 * Created by toney on 2017/2/13.
 */

public class Main {
    public static void main(String[] args){
         String REDIRECTCLASSNAME = Type.getDescriptor(IChangeQuickRedirect.class);
         String PROXYCLASSNAME = PatchProxy.class.getName().replace(".", "/");
         System.out.println("REDIRECTCLASSNAME = " + REDIRECTCLASSNAME);
         System.out.println("PROXYCLASSNAME = " + PROXYCLASSNAME);


    }
}
