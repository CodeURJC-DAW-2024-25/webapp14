import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../services/product.service';
import { ProductDTO } from '../../dtos/product.dto';

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html'
})
export class IndexComponent implements OnInit {

  page: number = 0;
  readonly size: number = 4;

  products: ProductDTO[] = [];
  recommendedProducts: ProductDTO[] = [];
  loggedIn: boolean = false; //hasta que sepa como se usa auth jeje
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

  constructor(private productService: ProductService) { }

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.productService.getIndexProducts(0, 4).subscribe({
      next: data => {
        this.products = data.bestSellingProducts ?? [];
      },
      error: err => console.error('Error al cargar tendencias:', err)
    });
  }

  loadMoreRecommendedProducts(): void {
    this.page++;
    this.productService.getProducts(this.page, this.size).subscribe({
      next: data => {
        this.products.push(...data.products.content);
      },
      error: err => {
        console.error('Error al cargar más recomendados:', err);
      }
    });
  }
}
