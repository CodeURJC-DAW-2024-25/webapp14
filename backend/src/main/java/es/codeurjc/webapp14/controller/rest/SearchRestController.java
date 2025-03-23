package es.codeurjc.webapp14.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.codeurjc.webapp14.dto.BasicProductDTO;
import es.codeurjc.webapp14.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
public class SearchRestController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getSearchData(HttpServletRequest request,
            @RequestParam(value = "query", required = false) String query) {
        Map<String, Object> data = new HashMap<>();

        if (query != null && !query.isEmpty()) {
            List<BasicProductDTO> products = productService.searchProductsByName(query);
            data.put("productsSearch", products);

            return ResponseEntity.ok(data);
        }

        return ResponseEntity.notFound().build();
    }
}
