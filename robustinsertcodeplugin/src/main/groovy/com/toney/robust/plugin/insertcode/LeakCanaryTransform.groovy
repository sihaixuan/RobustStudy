package com.toney.robust.plugin.insertcode

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import com.intellij.openapi.util.text.StringUtil

import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.Project
import org.objectweb.asm.ClassReader;



/**
 * Created by toney on 2017/2/23.
 * 插桩 开发期 检测内存泄露
 */

public class LeakCanaryTransform extends Transform {

    private Project project;
    private String appDesc;

    public LeakCanaryTransform(Project project){
        this.project = project;
    }

    @Override
    public String getName() {
        return "LeakCanaryTransform";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        Collection<TransformInput> inputs = transformInvocation.inputs

        inputs.each { TransformInput input ->


            for(DirectoryInput transformInput in  input.directoryInputs){
                getAppDesc(transformInput.file)
                if(!StringUtil.isEmpty(appDesc)){
                    Util.log(project,"appDesc = " + appDesc)
                    break
                }
            }

        }

        if(StringUtil.isEmpty(appDesc)){
            Util.log(project,"insert LeakCanary for no finding application class")
            return
        }


        Util.log(project,"insert LeakCanary start")
        inputs.each { TransformInput input ->

            input.directoryInputs.each {    DirectoryInput directoryInput ->

                File dest = transformInvocation.getOutputProvider().getContentLocation(directoryInput.name,
                        directoryInput.contentTypes, directoryInput.scopes,
                        Format.DIRECTORY)

                insertCode(directoryInput.file)
                FileUtils.copyDirectory(directoryInput.file, dest)

            }



            //遍历jar
            /**
             * 遍历jar
             */
            input.jarInputs.each { JarInput jarInput ->
                String destName = jarInput.name;
                /**
                 * 重名名输出文件,因为可能同名,会覆盖
                 */
                def hexName = DigestUtils.md5Hex(jarInput.file.absolutePath);
                if (destName.endsWith(".jar")) {
                    destName = destName.substring(0, destName.length() - 4);
                }
                /**
                 * 获得输出文件
                 */
                File dest = transformInvocation.getOutputProvider().getContentLocation(destName + "_"+hexName, jarInput.contentTypes, jarInput.scopes, Format.JAR);

                if(!dest.parentFile.exists()){
                    dest.parentFile.mkdirs()
                }

                if(!dest.exists()){
                    dest.createNewFile()
                }

                FileUtils.copyFile(jarInput.file, dest);
                project.logger.error "Copying ${jarInput.file.absolutePath} to ${dest.absolutePath}"
            }
        }

        Util.log(project,"insert LeakCanary end")

    }

    private void insertCode(File dir){
        List<File> files = dir.listFiles()
        for(File file : files){
            if(file.isDirectory()){
                insertCode(file)
                continue
            }

            ClassType type = getClassType(file)
            switch (type){

                case ClassType.APP:
                    insertCodeInApplicationClass(file)
                    break
                case ClassType.ACTIVITY:
                    insertCodeInActivityClass(file)
                    break
                case ClassType.FRAGMENT:
                    insertCodeInFragmentClass(file)
                    break

                case ClassType.OTHER:
                default:break
            }
        }


    }

    private void insertCodeInApplicationClass(File src){
        Util.log(project,"insertCodeInApplicationClass")
        File temp = new File(src.absolutePath + "_temp")
        if(temp.exists()){
            temp.delete()
        }

        temp.createNewFile()
        boolean isSuccess = false;

        try{
            isSuccess = LeakCanaryAsmUtil.insertAppClass(src,temp)

        }catch (Exception e){

            Util.log(project,e.getMessage())
        }finally{
            if(isSuccess){
                src.delete()
                temp.renameTo(src)
                Util.log(project,src.getAbsolutePath() + " insert Code success")
            }else{
                temp.delete()
                Util.log(project,src.getAbsolutePath() + " insert Code failed")
            }

        }
    }

    private void insertCodeInActivityClass(File src){
        File temp = new File(src.absolutePath + "_temp")
        if(temp.exists()){
            temp.delete()
        }

        temp.createNewFile()
        boolean isSuccess = false;

        try{
            isSuccess = LeakCanaryAsmUtil.insertActivityClass(src,temp,appDesc)

        }catch (Exception e){

            Util.log(project,e.getMessage())
        }finally{
            if(isSuccess){
                src.delete()
                temp.renameTo(src)
            }else{
                temp.delete()
                Util.log(project,src.getAbsolutePath() + " insert Code failed")
            }

        }
    }

    private void insertCodeInFragmentClass(File src){
        File temp = new File(src.absolutePath + "_temp")
        if(temp.exists()){
            temp.delete()
        }

        temp.createNewFile()
        boolean isSuccess = false;

        try{
            isSuccess = LeakCanaryAsmUtil.insertFragmentClass(src,temp,appDesc)

        }catch (Exception e){

            Util.log(project,e.getMessage())
        }finally{
            if(isSuccess){
                src.delete()
                temp.renameTo(src)
            }else{
                temp.delete()
                Util.log(project,src.getAbsolutePath() + " insert Code failed")
            }

        }
    }


    private ClassType getClassType(File src){
//        Util.log(project,"getClassType src = " + src.absolutePath)
        FileInputStream inputStream = null;
        try{
            inputStream = new FileInputStream(src);
            ClassReader reader = new ClassReader(inputStream);
            String superName = reader.getSuperName()
            if(superName.endsWith("Activity")){
                return ClassType.ACTIVITY
            }else if(superName.endsWith("Application")){
                return ClassType.APP
            }else if(superName.endsWith("Fragment")){
                return ClassType.FRAGMENT
            }
        }catch (Exception e){

        }finally{
            if(inputStream != null){
                try{
                    inputStream.close()
                }catch (Exception ex){

                }

            }
        }

        return ClassType.OTHER
    }

    private void getAppDesc(File file){
        String applicationDesc ;
        if(file.isDirectory()){
            List<File> files = file.listFiles()
            for(File subFile : files){
                if(subFile.isDirectory()){
                    getAppDesc(subFile)
                }else{

                    ClassType type = getClassType(subFile)
                    if(type == ClassType.APP){
                        applicationDesc = getClassName(subFile)
                        if(!StringUtil.isEmpty(applicationDesc)){
                            appDesc = applicationDesc
                            return
                        }
                    }

                }
            }
        }else{
            ClassType type = getClassType(subFile)
            if(type == ClassType.APP){
                applicationDesc = getClassName(file)
                if(!StringUtil.isEmpty(applicationDesc)){
                    appDesc = applicationDesc
                }
            }

        }
    }

    private String getClassName(File source){
        FileInputStream inputStream = null;
        try{
            inputStream = new FileInputStream(source)
            ClassReader reader = new ClassReader(inputStream)
            return reader.getClassName()
        }catch (Exception e){
            Util.log(project,e.getMessage())
        }finally{
            if(inputStream != null){
                try{
                    inputStream.close()
                }catch (Exception e){

                }
            }
        }


    }

    enum ClassType{
        OTHER,
        APP,
        ACTIVITY,
        FRAGMENT
    }


    public static void main(String[] args){
        LeakCanaryTransform transform = new LeakCanaryTransform();
        transform.getAppDesc(new File("E:\\workspace\\android_studio\\me\\RobustStudy\\RobustHost\\build\\intermediates\\classes\\debug"))
        System.out.println("appDesc = " + transform.appDesc)
    }

}
