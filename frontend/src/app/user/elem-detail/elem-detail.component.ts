import { Component } from '@angular/core';
import { ReviewDTO } from '../../dtos/review.dto';
import { ProductService } from '../../services/product.service';
import { ActivatedRoute, Router } from '@angular/router';
import { SizeDTO } from '../../dtos/size.dto';
import { ReviewService } from '../../services/review.service';
import { UserService } from '../../services/user.service';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-elem-detail',
  templateUrl: './elem-detail.component.html'
})
export class ElemDetailComponent {

  user = this.userService.getCurrentUserData();
  userId = this.userService.getCurrentUserId();
  logged: boolean = this.userId != null;
  isAdmin: boolean = this.userService.getIsAdminUser();
  name: String = "";
  description: String = "";
  price: number = 0;
  stock: number = 0;
  outOfStock: boolean = false;
  imageBool: boolean = false;
  sizes: SizeDTO[] = [];
  quantity: number = 1;
  selectedSize: string = "";

  reviews: ReviewDTO[] = [];

  productId!: number;

  banned: boolean = false;

  newReview = {
    rating: 0,
    reviewText: ''
  };

  constructor(private router: Router, private productService: ProductService, private route: ActivatedRoute, private reviewService: ReviewService, private userService: UserService) { }

  ngOnInit(): void {
    this.productId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadProduct();
  }

  loadProduct(): void {
    this.productService.getProduct(this.productId).subscribe({
      next: (data) => {
        console.log(data);
        this.logged = this.userId != null;
        this.name = data.name;
        this.description = data.description;
        this.price = data.price;
        this.outOfStock = data.outOfStock;
        this.imageBool = data.imageBool;
        this.sizes = data.sizes;
        this.reviews = data.reviews;

      },
      error: (err) => {
        console.error('Error al cargar el producto:', err);

      }
    });
  }

  goBack(): void {
    this.router.navigate(['/index']);
  }


  reportReview(reviewId: number, productId: number): void {
    this.reviewService.reportReview(reviewId, productId).subscribe(
      () => {
        console.log('Review reportada con éxito');
        this.loadProduct();
      },
      (error) => {
        console.error('Error al reportar la review', error);
        if (error.status === 500) {
          this.loadProduct();
        }
      }

    );
  }

  deleteReview(reviewId: number, productId: number): void {
    if (this.userId == null) {
      return;
    }
    this.reviewService.deleteReview(reviewId, productId, this.userId).subscribe(
      () => {
        console.log('Review eliminada con éxito');
        this.loadProduct();
      },
      (error) => {
        console.error('Error al eliminar la review', error);
        if (error.status === 500) {
          this.loadProduct();
        }
      }

    );
  }

  submitReview() {
    if (this.userId == null) {
      return;
    }
    if (this.newReview.rating > 0 && this.newReview.reviewText.trim() !== '') {
      const body = {
        rating: this.newReview.rating,
        reviewText: this.newReview.reviewText
      };

      this.reviewService.addReview(body, this.userId, this.productId).subscribe({
        next: () => {
          this.newReview = { rating: 0, reviewText: '' };
          this.loadProduct();
        },
        error: (err) => console.error('Error al enviar reseña', err)
      });
    }
  }

  submitEditReview(reviewId: number): void {
    if (this.userId == null) {
      return;
    }
    const review = this.reviews.find(r => r.id === reviewId);
    if (review) {
      const body = {
        rating: review.rating,
        reviewText: review.reviewText
      };

      this.reviewService.editReview(body, this.userId, this.productId, reviewId).subscribe({
        next: () => {
          console.log('Reseña editada');
          this.loadProduct();
        },
        error: err => console.error('Error al editar reseña', err)
      });
    }
  }

  addToCart(productId: number, selectedSize: String, quantity: number): void {
    if (this.userId == null) {
      this.router.navigate(['/login']);
      return;
    }
    if (quantity > 0) {
      this.productService.addToCart(productId, this.userId, selectedSize, quantity).subscribe(
        () => {
          console.log('Producto añadido con éxito');
          this.router.navigate(['/cart']);
        },
        (error) => {
          console.error('Error al añadir el producto', error);
          if (error.status === 500) {
            this.loadProduct();
          }
        }

      );
    } else {
      alert("La cantidad debe ser mayor a 0.")
    }
  }

  getProductImageUrl(productId: number): string {
    return `${environment.apiUrl}/products/${productId}/image`;
  }
}
