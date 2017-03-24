package com.toney.robust.plugin.insertcode;


import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.Project




/**
 * Created by toney on 2017/2/22.
 */

public class RobustTransform extends Transform{

    private Project project;

    public RobustTransform(Project project){
        this.project = project;
    }

    @Override
    public String getName() {
        return "RobustTransform";
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
        Collection<TransformInput> inputs = transformInvocation.getInputs();
        //遍历
        inputs.each { TransformInput input ->

            //遍历目录
            input.directoryInputs.each { DirectoryInput directoryInput ->
                File dest = transformInvocation.getOutputProvider().getContentLocation(directoryInput.name,
                        directoryInput.contentTypes, directoryInput.scopes,
                        Format.DIRECTORY)

                //这里进行我们的处理 TODO
                project.logger.error "Copying 遍历目录 ${directoryInput.name} to ${dest.absolutePath}"
                /**
                 * 处理完后拷到目标文件
                 */
//                MyInject.injectDir(directoryInput.file.absolutePath,"com\\robust\\toney")

                FileUtils.copyDirectory(directoryInput.file, dest)
                insertCode(dest)

            }

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

                //处理jar进行字节码注入处理TODO

                FileUtils.copyFile(jarInput.file, dest);
//                project.logger.error "Copying ${jarInput.file.absolutePath} to ${dest.absolutePath}"
            }


        }

        
    }

    private void insertCode(File dir){
        List<File> files = dir.listFiles()

        for(File file : files){
            if(file.isDirectory()){
                insertCode(file)
                continue
            }

            if(!file.absolutePath.endsWith("MoneyBean.class")){
                continue
            }
            File temp = new File(file.absolutePath + "_temp")
            if(temp.exists()){
                temp.delete()
            }

            temp.createNewFile()
            boolean isSuccess = false;

            try{
                isSuccess = InsertCodeUtils.operateClassByteCode(file,temp)

            }catch (Exception e){

                Util.log(project,e.getMessage())
            }finally{
               if(isSuccess){
                   file.delete()
                   temp.renameTo(file)
               }else{
                   temp.delete()
                   Util.log(project,file.getAbsolutePath() + " insert Code failed")
               }

            }

        }

    }
}
