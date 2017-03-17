package indexer.tzy.com.indexer.index;

/**
 * Created by sf on 2017/3/17.
 */

public interface SectionCompartor<T> {
    /**
     * 比较 一个item 和 一个section的大小
     * @return item > section ,返回 1；item == section ，返回 0 ，item < section,返回 -1
     */
    int compare(T item, String section);
}
