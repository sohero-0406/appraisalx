package com.jeesite.modules.aa.vo;

import com.jeesite.modules.aa.entity.CalculateCurrent;
import com.jeesite.modules.aa.entity.Reference;

import java.util.List;

public class CalculateCurrentVO {

    private Reference reference1;       //参照物1
    private Reference reference2;       //参照物2
    private List<Reference> referenceList;      //参照物列表
    private CalculateCurrent calculateCurrent;      //被评估车辆

    public Reference getReference1() {
        return reference1;
    }

    public void setReference1(Reference reference1) {
        this.reference1 = reference1;
    }

    public Reference getReference2() {
        return reference2;
    }

    public void setReference2(Reference reference2) {
        this.reference2 = reference2;
    }

    public List<Reference> getReferenceList() {
        return referenceList;
    }

    public void setReferenceList(List<Reference> referenceList) {
        this.referenceList = referenceList;
    }

    public CalculateCurrent getCalculateCurrent() {
        return calculateCurrent;
    }

    public void setCalculateCurrent(CalculateCurrent calculateCurrent) {
        this.calculateCurrent = calculateCurrent;
    }
}
