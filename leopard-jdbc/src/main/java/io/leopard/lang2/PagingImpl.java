package io.leopard.lang2;

import java.util.ArrayList;
import java.util.List;

public class PagingImpl<E> implements Paging<E> {

	private List<E> list = null;

	private Boolean nextPage;
	private Integer totalCount;
	private Integer pageCount;
	private Integer pageSize;

	@Override
	public List<E> getList() {
		return this.list;
	}

	@Override
	public void add(E element) {
		if (this.list == null) {
			this.list = new ArrayList<E>();
		}
		this.list.add(element);
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
		return null;
	}

	public void setNextPage(Boolean nextPage) {
		this.nextPage = nextPage;
	}

}
