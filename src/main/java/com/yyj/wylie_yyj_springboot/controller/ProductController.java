package com.yyj.wylie_yyj_springboot.controller;

import com.yyj.wylie_yyj_springboot.domain.entity.Image;
import com.yyj.wylie_yyj_springboot.domain.entity.Product;
import com.yyj.wylie_yyj_springboot.domain.entity.ProductOption;
import com.yyj.wylie_yyj_springboot.service.ImageService;
import com.yyj.wylie_yyj_springboot.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    ImageService imageService;

    @RequestMapping(value="/admin/product/list/{page}")
    public String list(@PathVariable("page") int page, Model model) {
        Page<Product> productPage = productService.getProductListForAdmin(page);
        List<Product> productList = productPage.getContent();
        model.addAttribute("productList", productList);
        model.addAttribute("productPage", productPage);
        return "/admin/product/ProductList";
    }

    @RequestMapping("/admin/product/category/{category}/{page}")
    public String filtering(@PathVariable("page") int page, @PathVariable("category") int category, Model model) {
        Page<Product> productPage = productService.filteringByCategoryForAdmin(category, page);
        List<Product> productList = productPage.getContent();
        model.addAttribute("productList", productList);
        model.addAttribute("productPage", productPage);
        model.addAttribute("category", category);
        return "/admin/product/ProductList";
    }

    @RequestMapping("/admin/product/search/{page}")
    public String search(@PageableDefault Pageable pageable, @PathVariable("page") int page, @RequestParam(value="keyword") String keyword, @RequestParam(value="category") int category, Model model) {
        Page<Product> productPage;
        if (category != 0) {
            productPage = productService.searchProductListWithCategoryForAdmin(keyword, category, page);
        }
        else {
            productPage = productService.searchProductListForAdmin(keyword, page);
        }
        List<Product> productList = productPage.getContent();
        if (productList.isEmpty()) {
            model.addAttribute("noResult", true);
        }
        model.addAttribute("productList", productList);
        model.addAttribute("productPage", productPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        return "/admin/product/ProductList";
    }

    @GetMapping(value="/admin/product/enroll")
    public String enrollForm() {
        return "/admin/product/ProductForm";
    }

    @PostMapping(value="/admin/product/enroll")
    public String enroll(@ModelAttribute(value="ProductCommand") ProductCommand cmd) throws Exception {
        //save product with main thumb
        Product product = new Product(cmd.getCategory(),cmd.getName(), cmd.getPrice(), cmd.getDesc(), cmd.getDiscount(), "N");
        String mainThumb = "/images/" + cmd.getMainThumb().getOriginalFilename();
        product.setThumb(mainThumb);
        productService.saveProduct(product);
        Long pid = productService.findLastPid();
        //save all thumbnails and detailImages
        imageService.saveMainThumbNail(cmd.getMainThumb(), pid);
        if (!cmd.getThumb().get(0).isEmpty()) {
            imageService.saveThumbNail(cmd.getThumb(), pid);
        }
        if (!cmd.getImage().get(0).isEmpty()) {
            imageService.saveDetailImage(cmd.getImage(), pid);
        }
        //save options
        for (ProductOption option : cmd.options) {
            if (option.getColor() != null) {
                ProductOption productOption = new ProductOption(option.getColor(), option.getStock(), option.getSize(), pid);
                productService.saveProductOption(productOption);
            }
        }
        return "redirect:/admin/product/list/1";
    }

    @GetMapping("/admin/product/update/{id}")
    public String updateForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        List<ProductOption> options = productService.getOptionList(id);
        List<String> thumbnails = imageService.findPathByPidAndThumb(id, 1);
        List<String> detailImages = imageService.findPathByPidAndThumb(id, 0);
        model.addAttribute("product", product);
        model.addAttribute("options", options);
        model.addAttribute("count", options.size());
        model.addAttribute("thumbnails", thumbnails);
        model.addAttribute("detailImages", detailImages);
        return "/admin/product/ProductUpdate";
    }

    @PostMapping("/admin/product/update/{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute(value="ProductCommand") ProductCommand cmd) throws Exception {
        System.out.println(cmd.getMainThumb()+"***********************************8");
        Product product = productService.getProductById(id);
        product.setCategory(cmd.getCategory());
        product.setName(cmd.getName());
        product.setPrice(cmd.getPrice());
        product.setDesc(cmd.getDesc());
        if (!cmd.getMainThumb().isEmpty()) {
            String mainThumb = "/images/" + cmd.getMainThumb().getOriginalFilename();
            product.setThumb(mainThumb);
            imageService.deleteAllByPidAndThumb(id, 2);
            imageService.saveMainThumbNail(cmd.getMainThumb(), id);
        }
        product.setDiscount(cmd.getDiscount());
        productService.saveProduct(product);
        //update all thumbnails and detailImages
        if (!cmd.getThumb().get(0).isEmpty()) {
            imageService.deleteAllByPidAndThumb(id, 1);
            imageService.saveThumbNail(cmd.getThumb(), id);
        }
        if (!cmd.getImage().get(0).isEmpty()) {
            imageService.deleteAllByPidAndThumb(id, 0);
            imageService.saveDetailImage(cmd.getImage(), id);
        }
        //update options
        productService.deleteAllByPid(id);
        for (ProductOption option : cmd.options) {
            if (option.getColor() != null) {
                ProductOption productOption = new ProductOption(option.getColor(), option.getStock(), option.getSize(), id);
                productService.saveProductOption(productOption);
            }
        }
        return "redirect:/admin/product/list/1";
    }

    @RequestMapping("/admin/product/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/product/list/1";
    }
}
