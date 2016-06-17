package czd.com.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页模型类
 *
 */
public class Page<T> implements Serializable {

	private static final long serialVersionUID = 8885846635024671405L;
	
	private static long DEFAULT_PAGE_SIZE = 20;
	private long pageSize = DEFAULT_PAGE_SIZE;//每页的记录数
	private long start; // 当前页第一条数据在List中的位置,从0开始
	private List<T> data = Collections.emptyList(); // 当前页中存放的记录
	private long totalCount = 0;//总记录数
	
	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 只构造空页
	 */
	public Page(){
		this(01,00,DEFAULT_PAGE_SIZE,Collections.<T>emptyList());
	}
	
	public Page(long start,long totalSize,long pageSize, List<T> data){
		this.pageSize = pageSize;
		this.start = start;
		this.totalCount = totalSize;
		this.data = data;
	}
	
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 取总记录数
	 * @return
	 */
	public long getTotalCount(){
		return this.totalCount;
	}
	
	/**
	 * 取总页数
	 * @return
	 */
	public long getTotalPageCount(){
		if(totalCount % pageSize == 0)
			return totalCount / pageSize;
		else
			return totalCount / pageSize + 1;
	}
	
	/**
	 * 取得每页的数据容量
	 * @return
	 */
	public long getPageSize(){
		return pageSize;
	}
	
	/**
	 * 取得当前页中的记录
	 * @return
	 */
	public List<T> getResult(){
		return data;
	}
	
	public void setResult(List<T> data){
		this.data=data;
	}
	
	/**
	 * 取得当前页码,页码从1开始
	 * @return
	 */
	public long getCurrentPageNo(){
		return start / pageSize + 1;
	}
	
	/**
	 * 该页是否有下一页
	 * @return
	 */
	public boolean hasNextPage(){
		return this.getCurrentPageNo() < this.getTotalPageCount() - 1;
	}
	
	/**
	 * 该页是否有上一页
	 * @return
	 */
	public boolean hasPreviousPage(){
		return this.getCurrentPageNo() > 1;
	}
	
	/**
	 * 获取任一页第一条数据在数据集的位置,每页条数使用默认值
	 * @param pageNo
	 * @return
	 */
	protected static long getStartOfPage(long pageNo){
		return getStartOfPage(pageNo, DEFAULT_PAGE_SIZE);
	}
	
	/**
	 * 获取任一页第一条数据在数据集的位置.
	 * @param pageNo	从1开始的页号
	 * @param pageSize	每页的记录数
	 * @return			该页第一条记录
	 */
	public static long getStartOfPage(long pageNo,long pageSize){
		return (pageNo - 1) * pageSize;
	}
	
	
}
