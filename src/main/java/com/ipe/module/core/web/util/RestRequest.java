package com.ipe.module.core.web.util;

import com.ipe.common.util.PageModel;

/**
 * Created with IntegerelliJ IDEA.
 * User: tangdu
 * Date: 13-9-7
 * Time: 下午10:43
 * To change this template use File | Settings | File Templates.
 */
public class RestRequest {
    private Integer start = 0;
    private Integer limit = 20;
    private String sort;
    private String queryParams;
    private PageModel pageModel=new PageModel();

    public PageModel getPageModel() {
        return pageModel;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        if(start!=null){
            this.start=start;
        }
        pageModel.setStartRow(this.start);
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        if(limit!=null){
            this.limit=limit;
        }
        pageModel.setEndRow(this.limit);
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(String queryParams) {
        this.queryParams = queryParams;
    }


}
