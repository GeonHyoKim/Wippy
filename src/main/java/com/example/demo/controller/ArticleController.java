package com.example.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.ArticleService;
import com.example.demo.service.MemberService;
import com.example.demo.service.PicService;

import dto.Article;
import dto.ArticlePic;
import dto.Member;
import jakarta.servlet.http.HttpSession;

@Controller
public class ArticleController {
	private ArticleService articleService;
	private HttpSession session;
	private PicService picService;
	private MemberService memberService;

	public ArticleController(ArticleService articleService, PicService picService, MemberService memberService,
			HttpSession session) {
		this.articleService = articleService;
		this.picService = picService;
		this.session = session;
		this.memberService = memberService;
	}

	@GetMapping("/usr/article/write")
	public String write(Model model) {
		Member loginedMember = (Member) session.getAttribute("loginedMember");
		
		if (loginedMember == null) {
			model.addAttribute("message", "로그인 먼저 해주세요.");
			return "/usr/home/fail";
		}
		return "/usr/article/write";
	}

	@PostMapping("/usr/article/doWrite")
	public String doWrite(Model model, String title, String body, @RequestParam("articlePics") MultipartFile[] articlePics) throws IOException {
		Member loginedMember = (Member) session.getAttribute("loginedMember");

		if (loginedMember == null) {
			model.addAttribute("message", "로그인 먼저 해주세요.");
			return "/usr/home/fail";
		}
		
		int articleId = articleService.writeArticle(loginedMember.getId(), title, body);
		
	    picService.saveArticlePic(articleId, articlePics);

		return "redirect:/usr/article/list";
	}

	@GetMapping("/usr/article/list")
    public String list(Model model) {
        List<Article> articles = articleService.getArticles();

        for (Article article : articles) {
        	ArticlePic articlePic = picService.getPicByArticleId(article.getId());
            article.setArticlePic(articlePic);
            
            // 작성자 정보 추가
            Member writer = memberService.getMemberById(article.getMemberId());
            if (writer != null) {
                article.setWriterName(writer.getName());
            }
        }
        
        model.addAttribute("articles", articles);
        return "usr/article/list";
    }
	
	
	@GetMapping("/usr/article/detail")
	public String detail(Model model, int id) {
		
		Article article = articleService.getArticleById(id);
		
		articleService.increaseView(id);
			
		List<ArticlePic> articlePics = picService.getArticlePicById(article.getId());
		model.addAttribute("articlePics", articlePics);
		model.addAttribute("article", article);
		
		return "/usr/article/detail";
	}
	

}
