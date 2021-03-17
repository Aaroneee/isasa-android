package com.ivory.isasa.entity;

import java.util.Date;

public class Emp {

    private Integer id;

    private String empName;

    private String empPwd;

    private Integer shopId;

    private String a2sk;

    private String empPhone;

    private String empMail;

    private String empRemark;

    private Integer isValid;

    private Date createDate;

    private Date updateDate;

    private String tenantCrop;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpPwd() {
        return empPwd;
    }

    public void setEmpPwd(String empPwd) {
        this.empPwd = empPwd;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getA2sk() {
        return a2sk;
    }

    public void setA2sk(String a2sk) {
        this.a2sk = a2sk;
    }

    public String getEmpPhone() {
        return empPhone;
    }

    public void setEmpPhone(String empPhone) {
        this.empPhone = empPhone;
    }

    public String getEmpMail() {
        return empMail;
    }

    public void setEmpMail(String empMail) {
        this.empMail = empMail;
    }

    public String getEmpRemark() {
        return empRemark;
    }

    public void setEmpRemark(String empRemark) {
        this.empRemark = empRemark;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getTenantCrop() {
        return tenantCrop;
    }

    public void setTenantCrop(String tenantCrop) {
        this.tenantCrop = tenantCrop;
    }

    @Override
    public String toString() {
        return "Emp{" +
                "id=" + id +
                ", empName=" + empName +
                ", empPwd=" + empPwd +
                ", shopId=" + shopId +
                ", a2sk=" + a2sk +
                ", empPhone=" + empPhone +
                ", empMail=" + empMail +
                ", empRemark=" + empRemark +
                ", isValid=" + isValid +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", tenant=" + tenantCrop +
                "}";
    }
}
