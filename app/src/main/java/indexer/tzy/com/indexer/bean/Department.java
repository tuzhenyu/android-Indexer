package indexer.tzy.com.indexer.bean;

/**
 * 部门
 * Created by tzy on 2017/3/17.
 */

public class Department {

    String dName;//姓名

    Long did;//部门id

    Long createTime;//创建时间

    public Department(String dName, Long did, Long createTime) {
        this.dName = dName;
        this.did = did;
        this.createTime = createTime;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
