package indexer.tzy.com.indexer.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import indexer.tzy.com.indexer.bean.Department;
import indexer.tzy.com.indexer.bean.Staff;

/**
 * Created by sf on 2017/3/17.
 */

public class DataBuilder {

    //为了简化情景，假设部门名称的首字母都不相同
    public static final String[] DNames = {"dpaprt1",
            "MathDepart",
            "TestDepart",
            "HRDepart",
            "MarketDepart",
            "SaleDepart",
            "devDeprt",
            "AdvanceDepart"
    };

    public static final String[] StaffNames = {"kobe",
            "james",
            "hardon",
            "jack",
            "mike",
            "lily",
            "blue",
            "blacket",
            "francle",
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
            "candy",
            "zook",
            "geogle"
    };

   public static List<Department> createDeaprtments(){

        List<Department> departments = new ArrayList<>(DNames.length);
        Long did = 100L;
        for(int i = 0;i < DNames.length;i++,did += 15 ){
            departments.add(new Department(DNames[i],did,System.currentTimeMillis()));
        }
        return departments;
    }

    public static List<Staff> createStaffs(int size){
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

    /**
     * 对员工按照姓名进行排序，并生成 索引列表
     * */
    public static Object[] sortByName(List<Staff> datas, Comparator<Staff> comparator){

        if(datas == null ){
            return null;
        }

        Collections.sort(datas,comparator);

        Set<String> sectionSet = new TreeSet<>(); //26个字母 + “#”
        for(int i=0, len = datas.size(); i<len; i++){
            Staff item = datas.get(i);
            if(item == null){
                continue;
            }

            String nameAcronym = item.getName();
            if(!TextUtils.isEmpty(nameAcronym)){
                char firstChar = nameAcronym.charAt(0);
                if(Character.isLetter(firstChar)){
                    sectionSet.add(nameAcronym.substring(0,1).toUpperCase());
                }
            }
        }

        List<String> objects = new ArrayList<>(sectionSet);
        return objects.toArray();
    }



    /**
     * 对员工按照部门进行排序，并生成 索引列表
     * */
    public static Object[] sortByDepart(List<Staff> datas, Comparator<Staff> comparator){

        if(datas == null ){
            return null;
        }

        Collections.sort(datas,comparator);


        Set<String> sectionSet = new TreeSet<>(); //26个字母 + “#”
        for(int i=0, len = datas.size(); i<len; i++){
            Staff item = datas.get(i);
            if(item == null){
                continue;
            }

            String dNmae = item.getDepartment().getdName();
            if(!TextUtils.isEmpty(dNmae)){
                char firstChar = dNmae.charAt(0);
                //除了字母外，其他的都匹配'#'
                if(Character.isLetter(firstChar)){
                    sectionSet.add(dNmae.substring(0,1).toUpperCase());
                }
            }
        }

        List<String> objects = new ArrayList<>(sectionSet);
        return objects.toArray();
    }

}
