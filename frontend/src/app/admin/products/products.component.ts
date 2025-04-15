import { Component, OnInit } from '@angular/core';
import { ProductDTO } from '../../dtos/product.dto';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  //styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {
  productCount: number = 0;
  categoriesCount: number = 0;
  totalStock: number = 0;
  totalOutStock: number = 0;
  deleteTry: boolean = false;
  loading: boolean = false;
  products: ProductDTO[] = [];
  summaryCards = [
    { title: 'Número de Productos', value: this.productCount, color: 'primary', icon: 'fas fa-tshirt' },
    { title: 'Número de Categorias', value: this.categoriesCount, color: 'success', icon: 'fas fa-th-list' },
    { title: 'Productos en Stock', value: this.totalStock, color: 'warning', icon: 'fas fa-boxes' },
    { title: 'Productos sin Stock', value: this.totalOutStock, color: 'danger', icon: 'fas fa-exclamation-circle' }
  ];

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    console.log('ngOnInit ejecutado');

    this.loadProducts();
  }

  loadProducts(): void {
    this.loading = true;
    console.log('DENTRO');
    this.productService.getProducts().subscribe({
      next: (data) => {
        console.log('DENTRO');
        console.log(data.products.content);

        this.products = data.products.content;
        this.productCount = data.totalProducts;
        this.totalStock = data.totalStock;
        this.totalOutStock = data.totalOutStock;
        this.categoriesCount = data.categoriesCount;
        this.loading = false;
        console.log('ACTUALIZADO');

        this.summaryCards = [
          { title: 'Número de Productos', value: this.productCount, color: 'primary', icon: 'fas fa-tshirt' },
          { title: 'Número de Categorias', value: this.categoriesCount, color: 'success', icon: 'fas fa-th-list' },
          { title: 'Productos en Stock', value: this.totalStock, color: 'warning', icon: 'fas fa-boxes' },
          { title: 'Productos sin Stock', value: this.totalOutStock, color: 'danger', icon: 'fas fa-exclamation-circle' }
        ];
      },
      error: (err) => {
        console.error('Error al cargar los productos:', err);
        this.loading = false;
      }
    });
  }

  openCreateModal(): void {
    console.log('Open create modal');
  }


  toggleDetails(product: ProductDTO): void {
    console.log('Open togle modal for product:', product);
  }

  openEditModal(product: ProductDTO): void {
    console.log('Open edit modal for product:', product);
  }

  deleteProduct(productId: number): void {
    console.log('Delete product with ID:', productId);
  }

  loadMoreProducts(): void {
    console.log('Load more products');
  }
}