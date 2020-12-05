package com.krenog.myf.dto;

import io.swagger.annotations.ApiParam;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class FilterParameters {
    @Max(value = 100, message = "limit should be less than 100")
    @ApiParam(value = "Кол-во событий в ответе")
    private Integer limit = 10;

    @Min(value = 0, message = "limit should be more than 0")
    @ApiParam(value = "Номер страницы")
    private Integer offset = 0;

    public FilterParameters() {
    }

    public FilterParameters(@Max(value = 100, message = "limit should be less than 100") Integer limit, @Min(value = 1, message = "limit should be more than 0") Integer offset) {
        this.limit = limit;
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
