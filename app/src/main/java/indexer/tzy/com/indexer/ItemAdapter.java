package indexer.tzy.com.indexer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import indexer.tzy.com.indexer.bean.Staff;
import indexer.tzy.com.indexer.index.RosterSectionIndexer;

/**
 * Created by sf on 2017/3/21.
 */

public class ItemAdapter extends BaseAdapter {

    List<Staff> list;

    Context context;

    RosterSectionIndexer indexer;

    public int sortType ;


    public ItemAdapter(List<Staff> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }

    public SectionIndexer getIndexer() {
        return indexer;
    }

    public void setIndexer(RosterSectionIndexer indexer) {
        this.indexer = indexer;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Staff getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null){
            view = View.inflate(context,R.layout.item,null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        Staff staff = list.get(position);
        viewHolder = (ViewHolder) view.getTag();

        String section = "";
        if(sortType == MainActivity.SORT_BY_STAFF_NAME){
            section = staff.getName().substring(0,1).toUpperCase();
        }else if(sortType == MainActivity.SORT_BY_DEAPRTMENT_NAME){
            section = staff.getDepartment().getdName().substring(0,1).toUpperCase();
        }

        int sectioIndex = indexer.getSectionIndex(section);
        int sectionPosition = indexer.getPositionForSection(sectioIndex);
        if(sectionPosition == position){
            viewHolder.txSection.setVisibility(View.VISIBLE);
            if(sortType == MainActivity.SORT_BY_STAFF_NAME){
                viewHolder.txSection.setText(section);
            }else if(sortType == MainActivity.SORT_BY_DEAPRTMENT_NAME){
                viewHolder.txSection.setText(context.getString(R.string.s_departname,staff.getDepartment().getdName()));
            }
        }else{
            viewHolder.txSection.setVisibility(View.GONE);
        }

        viewHolder.txStaffName.setText(staff.getName());
        viewHolder.txStaffId.setText("id：" + staff.getId());
        return view;
    }

    private static class ViewHolder{
        public TextView txSection;
        public TextView txStaffName;
        public TextView txStaffId;
        public ViewHolder(View view) {

            txSection = (TextView) view.findViewById(R.id.tx_depart);
            txStaffName = (TextView) view.findViewById(R.id.tx_staff_name);
            txStaffId = (TextView) view.findViewById(R.id.tx_staff_id);
        }
    }


}
