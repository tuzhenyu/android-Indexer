package indexer.tzy.com.indexer.index;

import android.database.DataSetObserver;
import android.widget.SectionIndexer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述：section的索引器
 * 该类使用二分查找法和索引缓存，相比之前使用的 顺序查找法， 效率高很多;
 * <p>
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
    private Map<Object,Integer> mAlphaMap;

    /**
     * Use a collator to compare strings in a localized manner.
     */
    //private ComparatorManager.RosterComparator comparator;

    /**
     * The section array converted from the alphabet string.
     */
    private Object[] mSectionArray;



    SectionCompartor<T> compartor;

    public RosterSectionIndexer( Object[] sectionsArrays, List<T> datas) {
        mAlphabetLength = sectionsArrays.length;
        mSectionArray = sectionsArrays;
        mAlphaMap = new HashMap<>(mAlphabetLength);
        //comparator = new ComparatorManager.RosterComparator();
        // Get a Collator for the current locale for string comparisons.

       // getMemberSectionIndex();
        this.datas = datas;
    }


    /**
     * Returns the section array constructed from the alphabet provided in the constructor.
     * @return the section array
     */
    public Object[] getSections() {
        return mSectionArray;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
        mAlphaMap.clear();
    }

    /**
     * Default implementation compares the first character of word with letter.
     */
    protected int compare(T item, Object section) {
          if(compartor != null){
              return compartor.compare(item, section);
          }
        return 0;
    }

    /**
     * 获取 一个 section 在列表中的位置，这里采用用二分查找
     * @param sectionIndex 该section在索引列表中的索引值
     * */
    @Override
    public int getPositionForSection(int sectionIndex) {
        final Map<Object,Integer> alphaMap = mAlphaMap;

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

        Object section = mSectionArray[sectionIndex];
        // Check map，之前有做过缓存，这里就直接返回结果
        Integer tempPos = alphaMap.get(section);
        pos = tempPos == null ? Integer.MIN_VALUE : tempPos;

        if (Integer.MIN_VALUE != pos ) {
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

        // 检查下是否有前一个 section 的位置缓存，如果有，就可以缩小查找范围
        if (sectionIndex > 0) {
            Object prevSection =
                    mSectionArray[sectionIndex - 1];
            Integer tempPrePos = alphaMap.get(prevSection);
            int prevLetterPos = tempPrePos == null ? Integer.MIN_VALUE : tempPrePos;

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
            int diff = compare(item, section);
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
        alphaMap.put(section, pos);
        return pos;
    }

    @Override
    public int getSectionForPosition(int position) {
        T item =  datas.get(position);
        // Linear search, as there are only a few items in the section index
        // Could speed this up later if it actually gets used.
        for (int i = 0; i < mAlphabetLength; i++) {
            String section = mSectionArray[i].toString();
            if (compare(item, section) == 0) {
                return i;
            }
        }
        return 0; // Don't recognize the letter - falls under zero'th section
    }


    @Override
    public void onChanged() {
        super.onChanged();
        mAlphaMap.clear();
    }


    @Override
    public void onInvalidated() {
        super.onInvalidated();
        mAlphaMap.clear();
    }

    public int getSectionIndex(String section){
        int index = 0;
        for(;index < mAlphabetLength;++index){
            if(section.equalsIgnoreCase(mSectionArray[index].toString())){
                return index;
            }
        }
        return mAlphabetLength - 1;
    }


    public SectionCompartor<T> getCompartor() {
        return compartor;
    }

    public void setCompartor(SectionCompartor<T> compartor) {
        this.compartor = compartor;
    }
}
