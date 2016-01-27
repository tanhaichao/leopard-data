package io.leopard.lang;

import java.util.ArrayList;
import java.util.List;

public class PagingImpl<E> implements Paging<E> {

	private List<E> list = null;

	private Boolean nextPage;
	private Integer totalCount;
	private Integer pageCount;
	private Integer pageSize;

	public PagingImpl() {

	}

	public PagingImpl(Paging<?> paging) {
		if (paging.isNextPage() != null) {
			this.setNextPage(paging.isNextPage());
		}
		if (paging.getTotalCount() != null) {
			this.setTotalCount(paging.getTotalCount());
		}
		if (paging.getPageCount() != null) {
			this.setPageCount(paging.getPageCount());
		}
		if (paging.getPageSize() != null) {
			this.setPageSize(paging.getPageSize());
		}
	}

	public PagingImpl(List<?> list, int size) {
		boolean nextPage;
		if (list == null) {
			nextPage = false;
		}
		else {
			nextPage = list.size() >= size;
		}
		this.setNextPage(nextPage);

		this.list = new ArrayList<E>();
	}

	// public PagingImpl(List<E> list) {
	// this.list = list;
	// }
	//
	// public PagingImpl(List<E> list, int count) {
	// this.list = list;
	// this.totalCount = count;
	// }
	//
	// public PagingImpl(List<E> list, boolean nextPage) {
	// this.list = list;
	// this.nextPage = nextPage;
	// }

	@Override
	public List<E> getList() {
		return this.list;
	}

	public void setList(List<E> list) {
		this.list = list;
	}

	@Override
	public void add(E element) {
		if (this.list == null) {
			this.list = new ArrayList<E>();
		}
		this.list.add(element);
	}

	@Override
	public void add(int index, E element) {
		if (this.list == null) {
			this.list = new ArrayList<E>();
		}
		this.list.add(index, element);
	}

	@Override
	public Integer getTotalCount() {
		return this.totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	@Override
	public Integer getPageCount() {
		return this.pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	@Override
	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public Boolean isNextPage() {
		return nextPage;
	}

	public void setNextPage(Boolean nextPage) {
		this.nextPage = nextPage;
	}

	@Override
	public E get(int index) {
		return list.get(index);
	}

	@Override
	public int size() {
		if (list == null) {
			return 0;
		}
		return list.size();
	}

}
