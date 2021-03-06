package com.spring.mvc.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.mvc.board.commons.PageCreator;
import com.spring.mvc.board.commons.PageVO;
import com.spring.mvc.board.commons.SearchVO;
import com.spring.mvc.board.model.BoardVO;
import com.spring.mvc.board.service.IBoardService;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	private IBoardService service;
	
	//서치 기능 및 페이징 구현한 list
	@GetMapping("/list")
	public String list(PageVO paging, Model model, SearchVO search) {
		List<BoardVO> list = service.getArticleList(paging, search);
		
		System.out.println("URL: /board/list GET -> result: " + list.size());
		System.out.println("페이지 번호: " + paging.getPage());
		
		PageCreator pc = new PageCreator();
		pc.setPaging(paging);
		pc.setArticleTotalCount(service.countArticles(search));
		
		model.addAttribute("articles", list);
		model.addAttribute("pc", pc);
		model.addAttribute("se", search);
		
		return "board/list";
		
	}
	
	
	//페이징 처리 이후 게시글 목록 불러오기 요청.
	/*@GetMapping("/list")
	public String list(PageVO paging, Model model) {
		List<BoardVO> list = service.getArticleList(paging);
		
		System.out.println("URL: /board/list GET -> result: " + list.size());
		System.out.println("페이지 번호: " + paging.getPage());
		
		PageCreator pc = new PageCreator();
		pc.setPaging(paging);
		pc.setArticleTotalCount(service.countArticles());
		
		model.addAttribute("articles", list);
		model.addAttribute("pc", pc);
		
		return "board/list";
		
	}*/
	
	/*
	//게시글 목록 불러오기 요청
	@GetMapping("/list")
	public String list(Model model) {
		List<BoardVO> list = service.getArticleList();
		
		System.out.println("URL: /board/list GET -> result: " + list.size());
		model.addAttribute("articles", list);
		
		return "board/list";
	}
	*/
	
	//글 작성 페이지 요청
	@GetMapping("/write")
	public void write() {
		System.out.println("URL: /board/write -> GET");
	}
	
	//게시글 DB 등록 요청
	@PostMapping("/write")
	public String write(BoardVO article, RedirectAttributes ra) {
		
		System.out.println("URL: /board/write -> POST");
		service.insert(article);
		
		ra.addFlashAttribute("msg", "regSuccess");
		return "redirect:/board/list";
	}
	
	//게시물 상세 조회 요청
	@GetMapping("/content/{boardNo}")
	//@PathVariable은 URL 경로에 변수를 포함시켜 주는 방식.
	public String content(@PathVariable int boardNo, Model model, PageVO page, SearchVO search, Integer replynum) {
		System.out.println("URL: /board/content -> GET");
		
		System.out.println("parameter(글 번호): " + replynum);
		
		
		BoardVO vo = service.getArticle(boardNo);	
		
		System.out.println("result data: " + vo);
		model.addAttribute("article", vo);
		model.addAttribute("page",page);
		model.addAttribute("se",search);
		model.addAttribute("replynum",replynum);
		
		System.out.println("CONTENT-> GET ==="+search.getKeyword()+search.getCondition());
		return "board/content";
	}
	
	//게시물 삭제 처리 요청
	@PostMapping("/delete")
	public String remove(int boardNo, RedirectAttributes ra, PageVO page, SearchVO search) {
		System.out.println("URL: /board/delete -> POST");
		System.out.println("parameter(글 번호): " + boardNo);
		service.delete(boardNo);
		ra.addFlashAttribute("msg", "delSuccess");
		
		return "redirect:/board/list?page="+ page.getPage()+"&countPerPage="+page.getCountPerPage()
		+"&keyword="+search.getKeyword()+"&condition="+search.getCondition();
	}
	
	//게시글 수정 페이지 이동 요청
	@GetMapping("/modify")
	public String modify(int boardNo, Model model, PageVO page, SearchVO search) {
		System.out.println("URL: /board/modify -> GET");
		System.out.println("parameter(글 번호): " + boardNo);
		System.out.println("modify-> GET ==="+search.getKeyword()+search.getCondition());
		model.addAttribute("article", service.getArticle(boardNo));
		model.addAttribute("page", page);
		model.addAttribute("se1", search);
		return "board/modify";
	}
	
	//게시물 수정 요청
	@PostMapping("/modify")
	public String modify(BoardVO article, RedirectAttributes ra, PageVO page) {
		System.out.println("URL: /board/modify -> POST");
		service.update(article);
		
		ra.addFlashAttribute("msg", "modSuccess");
	
		return "redirect:/board/content/"+article.getBoardNo()+"?page="+ page.getPage()+"&countPerPage="+page.getCountPerPage();
	}
	
	

}































