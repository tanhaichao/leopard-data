package io.leopard.lang2;

import java.util.List;

public interface Paging<E> {

	void add(E element);

	/**
	 * 数据列表.
	 * 
	 * @return
	 */
	List<E> getList();

	E get(int index);

	/**
	 * 记录总条数.
	 * 
	 * @return
	 */
	Integer getTotalCount();

	/**
	 * 总页数.
	 * 
	 * @return
	 */
	Integer getPageCount();

	/**
	 * 每页记录条数.
	 * 
	 * @return
	 */
	Integer getPageSize();

	/**
	 * 是否有下一页.
	 * 
	 * @return
	 */
	Boolean isNextPage();

}
