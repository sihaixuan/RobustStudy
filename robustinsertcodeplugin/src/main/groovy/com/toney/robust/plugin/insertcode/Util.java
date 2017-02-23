package com.toney.robust.plugin.insertcode;

import org.gradle.api.Project;

/**
 * Created by toney on 2017/2/13.
 */

public class Util {

    private Util(){
        super();
    }

    public static void log(Project project,String content){
        project.getLogger().error(content);
    }
}
