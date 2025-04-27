import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../services/product.service';
import { ProductDTO } from '../../dtos/product.dto';
import { UserService } from '../../services/user.service';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html'
})
export class IndexComponent implements OnInit {

  user = this.userService.getCurrentUserData();
  userId = this.userService.getCurrentUserId();
  logged: boolean = this.userId != null;
  isAdmin: boolean = this.userService.getIsAdminUser();

  page: number = 0;
  readonly size: number = 2;

  products: ProductDTO[] = [];
  recommendedProducts: ProductDTO[] = [];
  loggedIn: boolean = false;
  loading: boolean = false;

  categoryBlocks = [
    { name: 'PANTALONES', image: 'assets/images/Pantalones.webp' },
    { name: 'CAMISETAS', image: 'assets/images/chaquetas.webp' },
    { name: 'ABRIGOS', image: 'assets/images/ABRIGO.webp' },
    { name: 'JERSEYS', image: 'assets/images/LANDING-punto.webp' }
  ];

  services = [
    {
      title: 'Envíos Gratis',
      description: 'En toda la península Ibérica',
      icon: 'ci:shopping-cart-02'
    },
    {
      title: 'Pago Seguro 100%',
      description: 'Tus compras están protegidas con métodos cifrados.',
      icon: 'tdesign:secured'
    },
    {
      title: 'Garantía de Calidad',
      description: 'Productos con los más altos estándares de calidad.',
      icon: 'la:award'
    },
    {
      title: 'Ofertas Diarias',
      description: 'Descubre descuentos exclusivos y promociones.',
      icon: 'solar:dollar-outline'
    }
  ];

  constructor(private productService: ProductService, private userService: UserService) { }

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    if (this.logged) {
      this.productService.getIndexProductsLogged(0, 2).subscribe({
        next: data => {
          this.products = data.recommendedProducts.content ?? [];
        },
        error: err => console.error('Error al cargar recomendados:', err)
      });
    }
    else {
      this.productService.getIndexProducts(0, 4).subscribe({
        next: data => {
          this.products = data.bestSellingProducts ?? [];
        },
        error: err => console.error('Error al cargar tendencias:', err)
      });

    }
  }

  loadMoreRecommendedProducts(): void {
    this.page++;
    this.productService.getIndexProductsLogged(this.page, this.size).subscribe({
      next: data => {
        this.products.push(...data.recommendedProducts.content);
      },
      error: err => {
        console.error('Error al cargar más recomendados:', err);
      }
    });
  }

  getProductImageUrl(productId: number): string {
    return `${environment.apiUrl}/products/${productId}/image`;
  }
}
