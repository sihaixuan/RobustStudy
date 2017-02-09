package com.robust.toney.patch.impl;

import com.robust.toney.patch.IPatchInfo;
import com.robust.toney.patch.PatchClassInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toney on 2017/2/9.
 */

public class PatchInfoImpl implements IPatchInfo {
    @Override
    public List<PatchClassInfo> getPatchedClassesInfo() {
        List<PatchClassInfo> infos = new ArrayList<>();
        PatchClassInfo moneyPatchInfo = new PatchClassInfo("com.robust.toney.MoneyBean","com.robust.toney.patch.impl.MoneyBeanStatePatch");
        infos.add(moneyPatchInfo);
        return infos;
    }
}
