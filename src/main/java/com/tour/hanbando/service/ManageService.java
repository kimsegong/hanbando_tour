package com.tour.hanbando.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

public interface ManageService {

    /* 기존 회원 목록 */
    public void loadUserList(HttpServletRequest request, Model model);
    
    /* 기존 회원 검색 */
    public void loadSearchUserList(HttpServletRequest request, Model model);
   
}
