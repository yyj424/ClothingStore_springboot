package com.yyj.wylie_yyj_springboot.controller;

import com.yyj.wylie_yyj_springboot.domain.entity.Product;
import com.yyj.wylie_yyj_springboot.domain.entity.ProductOption;
import com.yyj.wylie_yyj_springboot.dto.ExcelData;
import com.yyj.wylie_yyj_springboot.service.ProductService;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class ExcelController {
    @Autowired
    ProductService productService;

    @RequestMapping(value="/admin/excel")
    public String excelMain() {
        return "/admin/excel/ExcelMain";
    }

    @GetMapping(value="/admin/excel/upload")
    public String uploadForm() {
        return "/admin/excel/UploadExcel";
    }

    @PostMapping(value="/admin/excel/upload")
    public String upload(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        List<ExcelData> dataList = new ArrayList<>();
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!extension.equals("xlsx") && !extension.equals("xls")) {
            throw new IOException("엑셀파일만 업로드 할 수 있습니다.");
        }
        Workbook workbook = null;
        if (extension.equals("xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());
        } else {
            workbook = new HSSFWorkbook(file.getInputStream());
        }
        Sheet worksheet = workbook.getSheetAt(0);
        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);
            ExcelData data = new ExcelData();
            data.setNum((int)row.getCell(0).getNumericCellValue());
            data.setName(row.getCell(1).getStringCellValue());
            data.setColor(row.getCell(2).getStringCellValue());
            data.setSize(row.getCell(3).getStringCellValue());
            data.setStock((int)row.getCell(4).getNumericCellValue());
            data.setSales((int)row.getCell(5).getNumericCellValue());
            dataList.add(data);
        }
        model.addAttribute("dataList", dataList);
        return "/admin/excel/ViewExcel";
    }

    @RequestMapping("admin/excel/download")
    public String downloadList() {
        return "/admin/excel/DownloadExcel";
    }

    @RequestMapping(value="/admin/excel/download/{month}/{week}")
    public String download(@PathVariable("month") int month, @PathVariable("week") int week, HttpServletResponse response) throws IOException {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet();
        Row row;
        Cell cell;
        int rowNum = 0;

        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("번호");
        cell = row.createCell(1);
        cell.setCellValue("상품명");
        cell = row.createCell(2);
        cell.setCellValue("색상");
        cell = row.createCell(3);
        cell.setCellValue("사이즈");
        cell = row.createCell(4);
        cell.setCellValue("재고");
        cell = row.createCell(5);
        cell.setCellValue("판매량");

        List<Product> productList = productService.getAllProducts();
        int num = 1;

        for(Product product : productList) {
            String name = product.getName();
            List<ProductOption> options = productService.getOptionList(product.getPid());
            for(ProductOption option : options) {
                row = sheet.createRow(rowNum++);
                cell = row.createCell(0);
                cell.setCellValue(num);
                cell = row.createCell(1);
                cell.setCellValue(name);
                cell = row.createCell(2);
                cell.setCellValue(option.getColor());
                cell = row.createCell(3);
                cell.setCellValue(option.getSize());
                cell = row.createCell(4);
                cell.setCellValue(option.getStock());
                cell = row.createCell(5);
                cell.setCellValue(0);
                num++;
            }
        }

        String value = "attachment;filename=" + month + "m" + week + "w_sales.xlsx";
        response.setContentType("ms-vnd/excel");
//        response.setHeader("Content-Disposition", "attachment;filename=example.xls");
        response.setHeader("Content-Disposition", value);

        wb.write(response.getOutputStream());
        wb.close();
        return "/admin/excel/ExcelMain";
    }
}
