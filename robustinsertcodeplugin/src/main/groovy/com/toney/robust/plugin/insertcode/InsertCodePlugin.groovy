package com.toney.robust.plugin.insertcode

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin


import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.compile.JavaCompile;

/**
 * Created by toney on 2017/2/13.
 * 动态植入代码插件
 */

public class InsertCodePlugin implements Plugin<Project>{

    private Project project;
    private List<String> whiteLists;

    @Override
    void apply(Project project) {
        this.project = project;

//        if(!project.plugins.hasPlugin(AppPlugin.class)){
//            throw  new ProjectConfigurationException("the android plugin must be applied!",null)
//        }

        applyExtension();
        initData()


        if(project.plugins.hasPlugin(AppPlugin.class)){
            def android = project.extensions.getByType(AppExtension.class)
//            android.registerTransform(new RobustTransform(project))
            android.registerTransform(new LeakCanaryTransform(project))
        }
        project.afterEvaluate {
            project.android.applicationVariants.each { variant ->
//                def proguardTask = project.tasks.findByName("transformClassesAndResourcesWithProguardFor${variant.name.capitalize()}")
//                if (proguardTask) {
//                    project.logger.error "proguard=>${variant.name.capitalize()}"
//
//                    proguardTask.inputs.files.files.each { File file->
//                        project.logger.error "file inputs=>${file.absolutePath}"
//                    }
//
//                    proguardTask.outputs.files.files.each { File file->
//                        project.logger.error "file outputs=>${file.absolutePath}"
//                    }
//                }
//
//                def dexTask = project.tasks.findByName("transformClassesWithDexFor${variant.name.capitalize()}")
//                if (dexTask) {
//                    project.logger.error "dex=>${variant.name.capitalize()}"
//
//                    dexTask.inputs.files.files.each { File file->
//                        project.logger.error "file inputs=>${file.absolutePath}"
//                    }
//
//                    dexTask.outputs.files.files.each { File file->
//                        project.logger.error "file outputs=>${file.absolutePath}"
//                    }
//                }
//
//                def testTask = project.tasks.findByName("transformClassesWithRobustTransformFor${variant.name.capitalize()}")
//
//                if (testTask) {
//
//                    Set<File> testTaskInputFiles = testTask.inputs.files.files
//                    Set<File> testTaskOutputFiles = testTask.inputs.files.files
//
//                    project.logger.error "Name:transformClassesWithTransformImpl=====>${testTask.name} input"
//                    testTaskInputFiles.each { inputFile ->
//                        def path = inputFile.absolutePath
//                        project.logger.error "file inputs=>" +path
//                    }
//
//                    project.logger.error "Name:transformClassesWithTransformImpl=====>${testTask.name} output"
//                    testTaskOutputFiles.each { inputFile ->
//                        def path = inputFile.absolutePath
//                        project.logger.error "file outputs=>" + path
//                    }
//                }


            }
        }


//        insertCodeAction();
    }

    void initData(){
        whiteLists = new ArrayList<>();
        whiteLists.addAll("android.**")
        whiteLists.addAll("**.R.class")
    }


    void  applyExtension(){

    }

    void insertCodeAction(){

        Util.log(project,"insertCodeAction")
//        project.afterEvaluate{
//
//            project.android.applicationVariants.all {  BaseVariant variant ->
//                //javaCompile task 仅有代码发生变化后，才会执行
//                variant.javaCompile.doLast{
//                    Util.log(project,"do last")
//                    insertCodes(variant.javaCompile);
//                }
//
//            }
//        }


    }

    void insertCodes(JavaCompile javaCompile){
        Util.log(project,"insertCode start")
        FileCollection files = javaCompile.outputs.files;

        Util.log(project, "------source-------")
//        for(File file : javaCompile.source) {
//
//
//            Util.log(project, file.getAbsolutePath())
//
//        }


        for(File file : files){
            Util.log(project,file.getAbsolutePath())
            if(file.getAbsolutePath().endsWith("classes\\debug") || file.getAbsolutePath().endsWith("classes\\release")){
                Util.log(project,"insertCodeInHostProject")
                insertCodeInHostProject(file)
            }
        }


        Util.log(project,"---------classpath---------")

//        for(File file : javaCompile.classpath){
//            Util.log( project,"file name = " + file.getName() + "," +file.getAbsolutePath() )
////            temp = new File(file.getAbsolutePath() + "_temp")
////            temp.delete()
////            temp.createNewFile()
////            boolean isSuccess = false;
////            try{
////                isSuccess = InsertCodeUtils.operateClassByteCode(file, temp)
////
////            }catch (Exception e){
////                Util.log(project,e.getMessage())
////            }finally{
////                if(isSuccess){
////                    file.delete()
////                    temp.renameTo(file)
////                }else{
////                    temp.delete()
////                }
////            }
//        }

        Util.log(project,"insertCode end")

    }

    void insertCodeInHostProject(File classedPath){

        if(!classedPath.exists()){
            return;
        }
        Util.log(project, "insertCodeInHostProject classedPath = " + classedPath.absolutePath )
        List<File> subFiles = classedPath.listFiles()
        for(File file : subFiles){
            Util.log(project, "file = " + file.absolutePath )
            if(file.isDirectory() ){
                Util.log(project,"isDirectory")
                if(!file.absolutePath.endsWith("android")){
                    insertCodeInHostProject(file)
                    Util.log(project,"isDirectory insertCodeInHostProject")
                }

                continue
            }

            if(!file.absolutePath.endsWith("MoneyBean.class")){
                continue;
            }

            File temp = new File(file.getAbsolutePath() + "_temp")
            if(temp.exists()){
                temp.delete()
            }

            temp.createNewFile()
            boolean isSuccess = false;
//            Util.log(project,"file : " + file.absolutePath + ",temp : " + temp.absolutePath)
            try{
                isSuccess = InsertCodeUtils.operateClassByteCode(file, temp)

            }catch (Exception e){
                Util.log(project,"insert code execption:" + e.getLocalizedMessage())
                System.out.println(e.getLocalizedMessage())
            }finally{
                if(isSuccess){
                    file.delete()
                    temp.renameTo(file)
                }else{
                    temp.delete()
                    Util.log(project,file.getAbsolutePath() + " insert code failed !")
                }
            }

        }

    }

   private FilenameFilter filenameFilter = new FilenameFilter() {
       @Override
       boolean accept(File dir, String name) {
//           Util.log(project, "dir = " + dir.absolutePath + ", name = " + name)
           if(whiteLists == null || whiteLists.size() == 0){
                return  true;
           }

          if(name.endsWith("android")){
              return false;
          }

           return true
       }
   }

}
