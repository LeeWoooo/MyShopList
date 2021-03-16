package com.shop.util;

import lombok.Getter;

@Getter
public class PagiNation {

    //한 페이지에 보여질 게시글
    private int pageSige;
    //view에서 보여질 페이지의 수
    private int totalPage;
    //DB조회 시작범위
    private int startSearch;
    //DB조회 끝 범위
    private int endSearch;

    public PagiNation(int count,int pageNumber) {
        //한 페이지에 6개씩 보여진다.
        this.pageSige = 6;
        //view에 보여질 총 페이지 수 구하기
        this.totalPage = (int)Math.ceil(count/(double)pageSige);
        //범위구하기
        this.startSearch = (pageNumber*pageSige)-pageSige;
        this.endSearch = startSearch + pageSige +1 ;
    }
}
