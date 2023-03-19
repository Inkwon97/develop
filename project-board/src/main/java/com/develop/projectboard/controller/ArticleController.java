package com.develop.projectboard.controller;

import com.develop.projectboard.domain.type.SearchType;
import com.develop.projectboard.dto.response.ArticleResponse;
import com.develop.projectboard.dto.response.ArticleWithCommentResponse;
import com.develop.projectboard.service.ArticleService;
import com.develop.projectboard.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/articles")
@Controller
public class ArticleController {

    private final ArticleService articleService;
    private final PaginationService paginationService;

    @GetMapping
    public String articles(
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map
    ) {
        Page<ArticleResponse> articles = articleService.searchArticles(searchType, searchValue, pageable).map(ArticleResponse::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), articles.getTotalPages());
        map.addAttribute("articles", articles);
        map.addAttribute("paginationBarNumbers", barNumbers);

        return "articles/index";
    }

    @GetMapping("/{articleId}")
    public String article(@PathVariable Long articleId, ModelMap map) {

        ArticleWithCommentResponse article = ArticleWithCommentResponse.from(articleService.getArticle(articleId));
        map.addAttribute("article", article); // TODO: 구현할 때 여기에 실제 데이터를 넣어줘야 한다
        map.addAttribute("articleComments", article.articleCommentsResponse());

        return "articles/detail";
    }

}
