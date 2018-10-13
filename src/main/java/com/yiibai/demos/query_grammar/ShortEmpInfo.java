package com.yiibai.demos.query_grammar;

public class ShortEmpInfo {
    private Long empId;
    private String empNo;
    private String empName;

    public ShortEmpInfo(Long empId, String empNo, String empName) {
        this.empId = empId;
        this.empName = empName;
        this.empNo = empNo;
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }
}
