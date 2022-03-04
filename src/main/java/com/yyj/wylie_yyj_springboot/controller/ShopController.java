package com.yyj.wylie_yyj_springboot.controller;

import com.yyj.wylie_yyj_springboot.domain.entity.Product;
import com.yyj.wylie_yyj_springboot.domain.entity.ProductOption;
import com.yyj.wylie_yyj_springboot.service.ImageService;
import com.yyj.wylie_yyj_springboot.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ShopController {
    @Autowired
    ProductService productService;
    @Autowired
    ImageService imageService;

    @RequestMapping(value="/")
    public String mainPage(Model model, Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            if (auth.getName().equals("admin")) {
                return "redirect:/admin";
            }
            model.addAttribute("uid", auth.getName());
        }
        Page<Product> productPage = productService.getProductList(1);
        List<Product> productList = productPage.getContent();
        model.addAttribute("productList", productList);
        model.addAttribute("productPage", productPage);
        return "index";
    }

    @RequestMapping(value="/{page}")
    public String list(@PathVariable("page") int page, Model model, Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            model.addAttribute("uid", auth.getName());
        }
        Page<Product> productPage = productService.getProductList(page);
        List<Product> productList = productPage.getContent();
        model.addAttribute("productList", productList);
        model.addAttribute("productPage", productPage);
        return "index";
    }

    @RequestMapping("/category/{category}/{page}")
    public String filtering(@PathVariable("page") int page, @PathVariable("category") int category, Model model) {
        Page<Product> productPage = productService.filteringByCategory(category, page);
        List<Product> productList = productPage.getContent();
        model.addAttribute("productList", productList);
        model.addAttribute("productPage", productPage);
        model.addAttribute("category", category);
        return "index";
    }

    @RequestMapping("/search/{page}")
    public String search(@PageableDefault Pageable pageable, @PathVariable("page") int page, @RequestParam(value="keyword") String keyword, @RequestParam(value="category") int category, Model model) {
        Page<Product> productPage;
        if (category != 0) {
            productPage = productService.searchProductListWithCategory(keyword, category, page);
        }
        else {
            productPage = productService.searchProductList(keyword, page);
        }
        List<Product> productList = productPage.getContent();
        if (productList.isEmpty()) {
            model.addAttribute("noResult", true);
        }
        model.addAttribute("productList", productList);
        model.addAttribute("productPage", productPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        return "index";
    }

    @RequestMapping("/product/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        List<ProductOption> options = productService.getOptionList(id);
        List<String> thumbnails = imageService.findPathByPidAndThumb(id, 1);
        List<String> detailImages = imageService.findPathByPidAndThumb(id, 0);
        model.addAttribute("product", product);
        model.addAttribute("options", options);
        model.addAttribute("thumbnails", thumbnails);
        model.addAttribute("detailImages", detailImages);
        return "/shop/ProductDetail";
    }
}