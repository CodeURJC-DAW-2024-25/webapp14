import { Component, OnInit } from '@angular/core';
import { ProductDTO } from '../../dtos/product.dto';
import { Router } from '@angular/router';
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

  imageFile: File | null = null;
  imagePreview: string | ArrayBuffer | null = null;
  selectedImageFile: File | null = null;
  fileName: string = 'Seleccionar Archivo...';


  constructor(private productService: ProductService, private router: Router) {}

  ngOnInit(): void {
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
    this.currentPage++;
    this.loading = true;
  
    this.productService.getProducts(this.currentPage, this.pageSize).subscribe({
      next: (data) => {
        const newProducts = data.products.content;
  
        this.products = this.products.concat(newProducts);
        this.totalPages = data.products.totalPages;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error al cargar más productos:', err);
        this.loading = false;
      }
    });
  }
  

  editProduct(product: any): void {
    this.productService.editProduct(product).subscribe({
      next: response => {
        console.log("Producto editado correctamente:", response);
        if (this.imageFile && response.id) {
          const formData = new FormData();
          formData.append('imageFile', this.imageFile);
          this.productService.updateProductImage(response.id, formData).subscribe({
            next: () => {
              console.log('Imagen actualizada correctamente');
            },
            error: (err) => console.error('Error al subir la imagen:', err)
          });
        } else {
        }
      },
      error: err => {
        console.error("Error al editar producto:", err);
      }
    });
  }
  
  

  onImageSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.imageFile = file;
  
      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result;
      };
      reader.readAsDataURL(file);
    }
  }
  

  createProduct(): void {
    const body = {
      name: this.newProduct.name,
      description: this.newProduct.description,
      price: this.newProduct.price,
      stock: 0,
      category: this.newProduct.category,
      imageBool: !!this.imageFile
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
        if (this.imageFile && response.id) {
          const formData = new FormData();
          formData.append('imageFile', this.imageFile);
          this.productService.uploadProductImage(response.id, formData).subscribe({
            next: () => {
              console.log('Imagen subida correctamente');
              this.loadProducts();
            },
            error: (err) => console.error('Error al subir la imagen:', err)
          });
        } else {
          this.loadProducts();
        }
      },
      error: (error) => {
        console.error('Error al crear el producto:', error);
      }
    });
  }

  saveChanges(event: MouseEvent,product: ProductDTO): void {
    event.preventDefault();
    event.stopPropagation();
  
    this.editProduct(product);
  }
  
  

}