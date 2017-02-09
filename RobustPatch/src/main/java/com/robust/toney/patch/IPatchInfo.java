package com.robust.toney.patch;

import java.util.List;

/**
 * Created by toney on 2017/2/9.
 * 修复包中所有需要修复的类型
 */

public interface IPatchInfo {
    List<PatchClassInfo> getPatchedClassesInfo();
}
