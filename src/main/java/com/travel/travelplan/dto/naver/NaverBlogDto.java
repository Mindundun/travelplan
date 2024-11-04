package com.travel.travelplan.dto.naver;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NaverBlogDto {
    private String lastBuildDate;
    private Integer total;
    private Integer start;
    private Integer display;
    private List<BlogPost> items;
}