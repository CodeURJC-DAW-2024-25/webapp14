import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductDTO } from '../../dtos/product.dto';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html'
})
export class CategoryComponent implements OnInit {

  category!: string;
  products: ProductDTO[] = [];
  loading = false;
  totalPages: number = 1;

  page = 0;
  readonly size = 4;

  constructor(private route: ActivatedRoute, private productService: ProductService) {}

  ngOnInit(): void {
    this.category = this.route.snapshot.paramMap.get('category')?.toUpperCase() ?? '';
    this.loadProducts();
  }

  loadProducts(): void {
    this.productService.getProductsByCategory(this.category, this.page).subscribe({
      next: data => {
        const paginated = data.products as any;
        this.products.push(...(paginated.content ?? []));
        this.totalPages = paginated.totalPages ?? 1; // Captura totalPages
        this.loading = false;
      },
      error: err => {
        console.error('Error al cargar productos por categoría:', err);
        this.loading = false;
      }
    });
  }
  
  loadMore(): void {
    this.page++;
    this.productService.getProductsByCategory(this.category, this.page).subscribe({
      next: (data) => {
        const paginated = data.products as any;
        this.products.push(...(paginated.content ?? []));
        this.totalPages = paginated.totalPages ?? this.totalPages;
      },
      error: (err) => console.error('Error al cargar más productos:', err)
    });
  }
  
  

  goBack(): void {
    history.back();
  }
}
