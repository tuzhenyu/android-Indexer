package indexer.tzy.com.indexer.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import indexer.tzy.com.indexer.bean.Department;
import indexer.tzy.com.indexer.bean.Staff;

/**
 * Created by sf on 2017/3/17.
 */

public class DataBuilder {

    public static final String[] DNames = {"dpaprt1",
            "depart2",
            "depart3",
            "ssDpart",
            "ScDepart",
            "MathDepart",
            "TestDepart",
            "HRDepart",
            "MarketDepart",
            "SaleDepart",
            "devDeprt"
    };

    public static final String[] StaffNames = {"kobe",
            "james",
            "hardon",
            "jack",
            "mike",
            "lily",
            "westbrook",
            "t-mac",
            "yaoMing",
            "ai",
            "lerborn",
            "aerston",
            "jaz",
            "ludy",
            "antoney",
            "lubeou",
            "bryant",
            "yi",
            "tom",
            "stepthen",
            "sharkp",
            "yohu",
            "candy"
    };

   public List<Department> createDeaprtments(){

        List<Department> departments = new ArrayList<>(DNames.length);
        Long did = 100L;
        for(int i = 0;i < DNames.length;i++,did += 15 ){
            departments.add(new Department(DNames[i],did,System.currentTimeMillis()));
        }
        return departments;
    }

    public List<Staff> createStaffs(int size){
        if(size <= 0 ){
            size = 15;
        }

        List<Department> departments = createDeaprtments();

        List<Staff> staffs = new ArrayList<>(size);

        Long pid = 1000L;
        Random random = new Random();
        for(int i = 0;i < size ; ++i,pid+=10){
            staffs.add(new Staff(pid,
                    StaffNames[random.nextInt(StaffNames.length)] + " " + StaffNames[random.nextInt(StaffNames.length)],
                    20 + random.nextInt(10),
                    random.nextBoolean() ? Staff.Enum_Gender.MALE : Staff.Enum_Gender.FEMALE,
                    departments.get(random.nextInt(departments.size()))));
        }

        return staffs;
    }


}
