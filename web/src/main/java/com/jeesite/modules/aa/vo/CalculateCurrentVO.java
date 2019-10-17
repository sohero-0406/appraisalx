package com.jeesite.modules.aa.vo;

import com.jeesite.modules.aa.entity.CalculateCurrent;
import com.jeesite.modules.aa.entity.Reference;
import com.jeesite.modules.sys.entity.DictData;

import java.util.List;

public class CalculateCurrentVO {

    private Reference reference1;       //参照物1
    private Reference reference2;       //参照物2
    private List<Reference> referenceList;      //参照物列表
    private CalculateCurrent calculateCurrent;      //被评估车辆
    private List<DictData> vehicleConfigTypeList;       //车辆配置类型列表
    private List<DictData> engineTypeList;       //发动机类别列表
    private List<DictData> gearboxTypeList;       //变速箱类型列表
    private List<DictData> paymentMethodList;       //付款方式列表
    private List<DictData> exhaustEmissionStandardList; //尾气排放标准

    public List<DictData> getExhaustEmissionStandardList() {
        return exhaustEmissionStandardList;
    }

    public void setExhaustEmissionStandardList(List<DictData> exhaustEmissionStandardList) {
        this.exhaustEmissionStandardList = exhaustEmissionStandardList;
    }

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

    public List<DictData> getVehicleConfigTypeList() {
        return vehicleConfigTypeList;
    }

    public void setVehicleConfigTypeList(List<DictData> vehicleConfigTypeList) {
        this.vehicleConfigTypeList = vehicleConfigTypeList;
    }

    public List<DictData> getEngineTypeList() {
        return engineTypeList;
    }

    public void setEngineTypeList(List<DictData> engineTypeList) {
        this.engineTypeList = engineTypeList;
    }

    public List<DictData> getGearboxTypeList() {
        return gearboxTypeList;
    }

    public void setGearboxTypeList(List<DictData> gearboxTypeList) {
        this.gearboxTypeList = gearboxTypeList;
    }

    public List<DictData> getPaymentMethodList() {
        return paymentMethodList;
    }

    public void setPaymentMethodList(List<DictData> paymentMethodList) {
        this.paymentMethodList = paymentMethodList;
    }
}
