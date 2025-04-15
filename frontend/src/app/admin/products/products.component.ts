import { Component, OnInit } from '@angular/core';
import { ProductDTO } from '../../dtos/product.dto';

import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  //styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {

  newProduct = {
    name: '',
    description: '',
    price: 0,
    category: '',
    image: null,
    stock_S: 0,
    stock_M: 0,
    stock_L: 0,
    stock_XL: 0
  };

  currentPage: number = 0;
  totalPages: number = 0;
  pageSize: number = 10;

  categories: String[] = ["Abrigos", "Camisetas", "Pantalones", "Jerséis"]
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
    this.productService.getProducts(this.currentPage, this.pageSize).subscribe({
      next: (data) => {

        console.log(data);

        this.totalPages = data.products.totalPages;

        this.products = data.products.content
        this.productCount = data.totalProducts;
        this.totalStock = data.totalStock;
        this.totalOutStock = data.totalOutStock;
        this.categoriesCount = data.categoriesCount;
        this.loading = false;

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

  toggleDetails(product: ProductDTO): void {
    console.log('Open togle modal for product:', product);
  }

 

  deleteProduct(productId: number): void {
    this.productService.deleteProduct(productId).subscribe(
      () => {
        this.products = this.products.filter(product => product.id !== productId);
        console.log('Producto eliminado con éxito');
        this.deleteTry = false;
        this.loadProducts();
      },
      (error) => {
        console.error('Error al eliminar el producto', error);
        if (error.status === 500) {
          this.deleteTry = true;
          this.loadProducts();
        }
      }
      
    );
  }

  loadMoreProducts(): void {
    console.log('Load more products');
  }

  editProduct(product: any): void {
    this.productService.editProduct(product).subscribe({
      next: response => {
        console.log("Producto editado correctamente:", response);
      },
      error: err => {
        console.error("Error al editar producto:", err);
      }
    });
  }
  


  onImageSelected(event: Event, product: any): void {
    const fileInput = event.target as HTMLInputElement;
    if (fileInput.files && fileInput.files[0]) {
      const file = fileInput.files[0];
      product.selectedImage = file;
  
      const reader = new FileReader();
      reader.onload = (e: any) => {
        product.newImagePreviewUrl = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }
  

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.loadProducts();
    }
  }

  previousPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadProducts();
    }
  }



  createProduct(): void {
    const body = {
      name: this.newProduct.name,
      description: this.newProduct.description,
      price: this.newProduct.price,
      stock: 0, // o puedes eliminarlo si no se usa
      category: this.newProduct.category,
      imageBool: !!this.newProduct.image
    };
  
    const params = {
      stock_S: this.newProduct.stock_S || 0,
      stock_M: this.newProduct.stock_M || 0,
      stock_L: this.newProduct.stock_L || 0,
      stock_XL: this.newProduct.stock_XL || 0,
    };
  
    this.productService.createProduct(body, params).subscribe({
      next: (response) => {
        console.log('Producto creado correctamente', response);
        this.loadProducts();
      },
      error: (error) => {
        console.error('Error al crear el producto:', error);
      }
    });
  }
  
  

}