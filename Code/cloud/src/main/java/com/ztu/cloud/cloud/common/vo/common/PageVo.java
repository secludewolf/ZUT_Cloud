package com.ztu.cloud.cloud.common.vo.common;

import com.github.pagehelper.PageInfo;
import com.ztu.cloud.cloud.common.bean.mysql.User;
import lombok.Data;

import java.util.List;

/**
 * @author Jager
 * @description 分页数据
 * @date 2020/8/13-16:17
 **/
@Data
public class PageVo<T> {
	private List<T> result;
	private int currentPageNumber;
	private int maxCurrentPageNumber;
	private int pageSize;
	private int currentSize;
	private String sortKey;
	private String sortType;
	private long total;

	public PageVo(List<T> result, int currentPageNumber, int pageSize, String sortKey, String sortType, long total) {
		this.result = result;
		this.currentPageNumber = currentPageNumber;
		this.maxCurrentPageNumber = (int) (total / pageSize) + ((total % pageSize) == 0 ? 0 : 1);
		this.pageSize = pageSize;
		this.currentSize = result.size();
		this.sortKey = sortKey;
		this.sortType = sortType;
		this.total = total;
	}

	public PageVo(PageInfo<T> pageInfo, String sortKey, String sortType) {
		this.result = pageInfo.getList();
		this.currentPageNumber = pageInfo.getPageNum();
		this.maxCurrentPageNumber = pageInfo.getPages();
		this.pageSize = pageInfo.getPageSize();
		this.currentSize = pageInfo.getSize();
		this.sortKey = sortKey;
		this.sortType = sortType;
		this.total = pageInfo.getTotal();
	}

	public PageVo(PageVo<User> pageVo, List<com.ztu.cloud.cloud.common.vo.admin.User> userList) {
		this.result = (List<T>) userList;
		this.currentPageNumber = pageVo.getCurrentPageNumber();
		this.maxCurrentPageNumber = pageVo.getMaxCurrentPageNumber();
		this.pageSize = pageVo.getPageSize();
		this.currentSize = pageVo.getCurrentSize();
		this.sortKey = pageVo.getSortKey();
		this.sortType = pageVo.getSortType();
		this.total = pageVo.getTotal();
	}

}
