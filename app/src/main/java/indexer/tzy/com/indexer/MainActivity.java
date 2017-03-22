package indexer.tzy.com.indexer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import indexer.tzy.com.indexer.bean.Staff;
import indexer.tzy.com.indexer.index.RosterSectionIndexer;
import indexer.tzy.com.indexer.index.SectionCompartor;
import indexer.tzy.com.indexer.index.SideBar;
import indexer.tzy.com.indexer.utils.DataBuilder;

/**
 * 实现索引定位的步骤：
 * 1.对数据源进行排序；
 * 2.根据数据源及其排序规则，生成有序的索引列表，显示到SideBar上；
 * 3.定义 每一个 section 与 每一个item项的比较规则， 用于二分查找，判断每个section的位置;
 * 4.在getView()中，判断当前位置是否是一个Section的位置，如果是，则显示Section,否则不显示；
 * 5.监听 SideBar 的索引列表的点击事件，当某个索引被点击了，就定位到SildeBar的位置。
 *
 * */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int SORT_BY_STAFF_NAME = 1;
    public static final int SORT_BY_DEAPRTMENT_NAME = 2;
    Button bn1;

    Button bn2;

    ListView listView;

    SideBar sideBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bn1 = (Button) findViewById(R.id.bn1);
        bn2 = (Button) findViewById(R.id.bn2);
        bn1.setOnClickListener(this);
        bn2.setOnClickListener(this);
        sideBar = (SideBar) findViewById(R.id.sideBar);
        listView = (ListView) findViewById(R.id.list_view);
        sideBar.setListView(listView);
        sortByName();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bn1:
                sortByName();
                break;
            case R.id.bn2:
                sortByDeaprtName();
                break;
            default:
        }
    }

    public void showSideBar(boolean isShow){
        if(isShow){
            sideBar.setVisibility(View.VISIBLE);
        }else{
            sideBar.setVisibility(View.INVISIBLE);
        }
    }

    protected void sortByName(){
        //构建数据
        List<Staff> staffs = DataBuilder.createStaffs(40);

        //对数据进行排序，并返回section列表,这里按姓名排序
        final Collator mCollator = java.text.Collator.getInstance(Locale.ENGLISH);
        mCollator.setStrength(java.text.Collator.PRIMARY);
        final Object[] sectiions = DataBuilder.sortByName(staffs, new Comparator<Staff>() {
            @Override
            public int compare(Staff left, Staff right) {
                if(left == null ){
                    if(right == null){
                        return 0;
                    }
                    return -1;
                }

                if(right == null){
                    return 1;
                }

                return mCollator.compare(left.getName(),right.getName());
            }
        });

        //生成索引器，要告诉索引器两个信息：有序的数据列表 + 索引列表
        RosterSectionIndexer<Staff> indexer = new RosterSectionIndexer<>(sectiions,staffs);
        //核心代码：的告诉索引器 该如何比较section和每个item的大小，要不然无法进行正确的二分查找
        indexer.setCompartor(new SectionCompartor<Staff>() {
            @Override
            public int compare(Staff item, Object section) {
                //这里的section索引是字母，代表员工姓名首字母

                String name = item.getName();
                String letter = String.valueOf(name.charAt(0));
                return letter.compareToIgnoreCase(section.toString());
            }
        });

        ItemAdapter itemAdapter = new ItemAdapter(staffs,this);
        itemAdapter.setIndexer(indexer);
        itemAdapter.setSortType(SORT_BY_STAFF_NAME);
        listView.setAdapter(itemAdapter);
        sideBar.setSectionIndexer(indexer);
    }

    /*
    * 按部门名称分组，部门内部员工按照员工id由小到大排序
    * */
    protected void sortByDeaprtName(){
        //构建数据
        List<Staff> staffs = DataBuilder.createStaffs(40);

        //对数据进行排序，并返回section列表,这里按姓名排序
        final Collator mCollator = java.text.Collator.getInstance(Locale.ENGLISH);
        mCollator.setStrength(java.text.Collator.PRIMARY);
        final Object[] sectiions = DataBuilder.sortByDepart(staffs, new Comparator<Staff>() {
            @Override
            public int compare(Staff left, Staff right) {
                if(left == null ){
                    if(right == null){
                        return 0;
                    }
                    return -1;
                }

                if(right == null){
                    return 1;
                }

                int compareDepart = mCollator.compare(left.getDepartment().getdName(),right.getDepartment().getdName());
                if(compareDepart == 0){
                    return (int) (left.getId() - right.getId());
                }
                return compareDepart;
            }
        });

        //生成索引器，要告诉索引器两个信息：有序的数据列表 + 索引列表
        RosterSectionIndexer<Staff> indexer = new RosterSectionIndexer<>(sectiions,staffs);
        //核心代码：的告诉索引器 该如何比较section和每个item的大小，要不然无法进行正确的二分查找
        indexer.setCompartor(new SectionCompartor<Staff>() {
            @Override
            public int compare(Staff item, Object section) {
                 //这里的section索引也是字母， 代表部门名称首字母
                String dName = item.getDepartment().getdName();
                String letter = String.valueOf(dName.charAt(0));
                return letter.compareToIgnoreCase(section.toString());
            }
        });

        ItemAdapter itemAdapter = new ItemAdapter(staffs,this);
        itemAdapter.setIndexer(indexer);
        itemAdapter.setSortType(SORT_BY_DEAPRTMENT_NAME);
        listView.setAdapter(itemAdapter);
        sideBar.setSectionIndexer(indexer);
    }
}
