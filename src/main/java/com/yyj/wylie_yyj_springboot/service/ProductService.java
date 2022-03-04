package com.yyj.wylie_yyj_springboot.service;

import com.yyj.wylie_yyj_springboot.domain.entity.Board;
import com.yyj.wylie_yyj_springboot.domain.entity.Product;
import com.yyj.wylie_yyj_springboot.domain.entity.ProductOption;
import com.yyj.wylie_yyj_springboot.domain.repository.ProductOptionRepository;
import com.yyj.wylie_yyj_springboot.domain.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductOptionRepository productOptionRepository;

    public Page<Product> getProductList(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 12, Sort.by("date").descending());
        return productRepository.findAll(pageable);
    }
    public Page<Product> getProductListForAdmin(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 5, Sort.by("date").descending());
        return productRepository.findAll(pageable);
    }
    public void saveProduct(Product product) {
        productRepository.saveAndFlush(product);
    }
    public Product getProductById(Long id) {
        return productRepository.getById(id);
    }
    public ProductOption getOptionById(Long id) { return productOptionRepository.getById(id); }
    public Long findLastPid() {
        return productRepository.findLastPid();
    }
    public void saveProductOption(ProductOption productOption) {
        productOptionRepository.saveAndFlush(productOption);
    }
    public Page<Product> filteringByCategory(int category, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 12, Sort.by("date").descending());
        return productRepository.findAllByCategory(pageable, category);
    }
    public Page<Product> searchProductList(String keyword, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 12, Sort.by("date").descending());
        return productRepository.findByNameContainingIgnoreCase(pageable, keyword);
    }
    public Page<Product> searchProductListWithCategory(String keyword, int category, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 12, Sort.by("date").descending());
        return productRepository.findByNameContainingIgnoreCaseAndCategory(pageable, keyword, category);
    }
    public Page<Product> filteringByCategoryForAdmin(int category, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 5, Sort.by("date").descending());
        return productRepository.findAllByCategory(pageable, category);
    }
    public Page<Product> searchProductListForAdmin(String keyword, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 5, Sort.by("date").descending());
        return productRepository.findByNameContainingIgnoreCase(pageable, keyword);
    }
    public Page<Product> searchProductListWithCategoryForAdmin(String keyword, int category, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 5, Sort.by("date").descending());
        return productRepository.findByNameContainingIgnoreCaseAndCategory(pageable, keyword, category);
    }
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    public List<ProductOption> getOptionList(Long pid) {
        return productOptionRepository.findAllByPid(pid);
    }
    public void deleteAllByPid(Long id) {
        productOptionRepository.deleteAllByPid(id);
    }
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
