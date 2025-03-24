package es.codeurjc.webapp14.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.codeurjc.webapp14.dto.BasicProductDTO;
import es.codeurjc.webapp14.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/search")
@Tag(name = "Search", description = "Endpoints for managing search bar")
public class SearchRestController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "Get search Products", description = "Return Products based on the search information")
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
