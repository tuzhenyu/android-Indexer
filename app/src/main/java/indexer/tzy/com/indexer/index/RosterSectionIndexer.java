package indexer.tzy.com.indexer.index;

import android.database.DataSetObserver;
import android.util.SparseIntArray;
import android.widget.SectionIndexer;

import java.util.List;
import java.util.Locale;

/**
 * 类描述：同时对部门和成员排序并能得到每个元素对应section的索引器
 * 该类使用二分查找法和索引缓存，相比之前使用的 顺序查找法， 效率高很多;
 * 数据源最好使用{@link java.util.Comparator }定义排序规则。
 * {@link RosterSectionIndexer#getPositionForSection(int)}可直接判断是否可以在Item中显示section
 * <p/>
 * Created by tzy on 2016/11/12.
 */
public class RosterSectionIndexer<T> extends DataSetObserver implements SectionIndexer {

    //记住，数据源一定要经过排序的！
    List<T> datas;

    /**
     * Cached length of the alphabet array.
     */
    private int mAlphabetLength;

    /**
     * This contains a cache of the computed indices so far. It will get reset whenever
     * the dataset changes or the cursor changes.
     */
    private SparseIntArray mAlphaMap;

    /**
     * Use a collator to compare strings in a localized manner.
     */
    //private ComparatorManager.RosterComparator comparator;

    /**
     * The section array converted from the alphabet string.
     */
    private Object[] mAlphabetArray;

    /**
     * Use a collator to compare strings in a localized manner.
     */
    private java.text.Collator mCollator;

    SectionCompartor<T> compartor;

    String sectionOfDepart = "";//

    public RosterSectionIndexer(String sectionOfDepart, Object[] sectionsArrays, List<T> datas) {
        this.sectionOfDepart = sectionOfDepart;
        mAlphabetLength = sectionsArrays.length;
        mAlphabetArray = sectionsArrays;
        mAlphaMap = new SparseIntArray(mAlphabetLength);
        //comparator = new ComparatorManager.RosterComparator();
        // Get a Collator for the current locale for string comparisons.
        mCollator = java.text.Collator.getInstance(Locale.ENGLISH);
        mCollator.setStrength(java.text.Collator.PRIMARY);
        getMemberSectionIndex();
        this.datas = datas;
    }


    /**
     * Returns the section array constructed from the alphabet provided in the constructor.
     * @return the section array
     */
    public Object[] getSections() {
        return mAlphabetArray;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
        mAlphaMap.clear();
    }

    /**
     * Default implementation compares the first character of word with letter.
     */
    protected int compare(T item, String section) {
          if(compartor != null){
              return compare(item, section);
          }
        return 0;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        final SparseIntArray alphaMap = mAlphaMap;

        if (datas == null ) {
            return 0;
        }

        // Check bounds
        if (sectionIndex <= 0) {
            return 0;
        }
        if (sectionIndex >= mAlphabetLength) {
            sectionIndex = mAlphabetLength - 1;
        }


        int count = datas.size();
        int start = 0;
        int end = count;
        int pos;

        String targetLetter = mAlphabetArray[sectionIndex].toString();
        int key = targetLetter.charAt(0);
        // Check map
        if (Integer.MIN_VALUE != (pos = alphaMap.get(key, Integer.MIN_VALUE))) {
            // Is it approximate? Using negative value to indicate that it's
            // an approximation and positive value when it is the accurate
            // position.
            if (pos < 0) {
                pos = -pos;
                end = pos;
            } else {
                // Not approximate, this is the confirmed start of section, return it
                return pos;
            }
        }

        // Do we have the position of the previous section?
        if (sectionIndex > 0) {
            int prevLetter =
                    mAlphabetArray[sectionIndex - 1].toString().charAt(0);
            int prevLetterPos = alphaMap.get(prevLetter, Integer.MIN_VALUE);
            if (prevLetterPos != Integer.MIN_VALUE) {
                start = Math.abs(prevLetterPos);
            }
        }

        // Now that we have a possibly optimized start and end, let's binary search

        pos = (end + start) / 2;

        while (pos < end) {
            // Get letter at pos
            T item =  datas.get(pos);
            if (item == null) {
                if (pos == 0) {
                    break;
                } else {
                    pos--;
                    continue;
                }
            }
            int diff = compare(item, targetLetter);
            if (diff != 0) {

                if (diff < 0) {
                    start = pos + 1;
                    if (start >= count) {
                        pos = count;
                        break;
                    }
                } else {
                    end = pos;
                }
            } else {
                // They're the same, but that doesn't mean it's the start
                if (start == pos) {
                    // This is it
                    break;
                } else {
                    // Need to go further lower to find the starting row
                    end = pos;
                }
            }
            pos = (start + end) / 2;
        }
        alphaMap.put(key, pos);
        return pos;
    }

    @Override
    public int getSectionForPosition(int position) {
        T item =  datas.get(position);
        // Linear search, as there are only a few items in the section index
        // Could speed this up later if it actually gets used.
        for (int i = 0; i < mAlphabetLength; i++) {
            String section = mAlphabetArray[i].toString();
            if (compare(item, section) == 0) {
                return i;
            }
        }
        return 0; // Don't recognize the letter - falls under zero'th section
    }

    /*
    * @hide
    */
    @Override
    public void onChanged() {
        super.onChanged();
        mAlphaMap.clear();
    }

    /*
     * @hide
     */
    @Override
    public void onInvalidated() {
        super.onInvalidated();
        mAlphaMap.clear();
    }

    /**
     * 通过section的在{@link RosterSectionIndexer#mAlphabetArray}的索引值找到要显示在列表的section字段
     * @param
     * @return
     */
   /* public String getSectionName(int sectionIndex){
        // Check bounds
        if (sectionIndex <= 0) {
            sectionIndex = 0;
        }
        if (sectionIndex >= mAlphabetLength) {
            sectionIndex = mAlphabetLength - 1;
        }
        String section = mAlphabetArray[sectionIndex].toString();

        if(section.equals(sectionOfDepart)){
            return titleSectionDepart;
        }else{
            return section;
        }
    }*/

    public int getSectionIndex(String section){
        int index = 0;
        for(;index < mAlphabetLength;++index){
            if(section.equals(mAlphabetArray[index].toString())){
                return index;
            }
        }
        return mAlphabetLength - 1;
    }

    protected int memberSectionIndex = -1;

    public int getMemberSectionIndex(){
        if(memberSectionIndex >= 0){
            return memberSectionIndex;
        }
        int index = 0;
        for(;index < mAlphabetLength;++index){
            if(!sectionOfDepart.equals(mAlphabetArray[index].toString())){
                return memberSectionIndex = index;
            }
        }
        return memberSectionIndex = 0;
    }

    /*public static Object[] getSections(List<T> datas,String sectionDepart){
        if(datas == null ){
            return null;
        }

        boolean hasDepart = false;
        boolean hasUnKnow = false;

        Set<String> sectionSet = new TreeSet<>(); //26个字母 + “部” + “#”
        for(int i=0, len=datas.size(); i<len; i++){
            T item = datas.get(i);
            if(item == null){
                continue;
            }

            if(item.getType() == RosterItem.TYPE_GROUP){
                hasDepart = true;
                continue;
            }


            String nameAcronym = item.getName();
            if(TextUtils.isEmpty(nameAcronym)){
                hasUnKnow = true;
            }else{
                char firstChar = nameAcronym.charAt(0);
                //除了字母外，其他的都匹配'#'
                if(Character.isLetter(firstChar)){
                    sectionSet.add(nameAcronym.substring(0,1).toUpperCase());
                }else{
                    hasUnKnow = true;
                }
            }
        }

        List<String> objects = new LinkedList<>(sectionSet);


        if(hasUnKnow){
            objects.add(0,"#");
        }

        if(hasDepart){
            objects.add(0,sectionDepart);
        }


        return objects.toArray();
    }*/
}
