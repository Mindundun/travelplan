package com.travel.travelplan.dto;

import java.util.List;

import lombok.Data;

@Data
public class PagingDto <T> {

    private int totalCount;

    private List<T> list;

}
