package indexer.tzy.com.indexer.bean;

import android.support.annotation.Nullable;

/**
 * 员工
 * Created by tzy on 2017/3/17.
 */

public class Staff {

  public enum Enum_Gender{MALE,FEMALE;}

    Long id;//员工id

    String name;//姓名

    Integer age;//年龄

    Enum_Gender gender = Enum_Gender.MALE;//性别

    Department department;//所属部门

    public Staff(Long id, String name, Integer age, Enum_Gender gender) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    public Staff(Long id, String name, Integer age, Enum_Gender gender, Department department) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Enum_Gender getGender() {
        return gender;
    }

    public void setGender(Enum_Gender gender) {
        this.gender = gender;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
