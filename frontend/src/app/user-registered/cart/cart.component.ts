import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { OrderProductDTO } from '../../dtos/orderProduct.dto';
import { OrderProductService } from '../../services/orderProduct.service';
import { UserService } from '../../services/user.service';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html'
})
export class CartComponent {


  user = this.userService.getCurrentUserData();
  userId = this.userService.getCurrentUserId();
  logged: boolean = this.userId != null;
  isAdmin: boolean = this.userService.getIsAdminUser();
  orderProducts: OrderProductDTO[] = [];
  orderProductsEmpty = true;
  orderNotProcessed = false;

  subtotal = 0;
  shipping = 0;
  total = 0;

  constructor(private router: Router, private orderProductService: OrderProductService, private userService: UserService) { }

  ngOnInit(): void {
    if (!this.logged) {
      this.router.navigate(["/login"]);
    }
    this.loadCart();
  }


  loadCart(): void {
    if (this.userId != null) {
      this.orderProductService.getCart(this.userId).subscribe({
        next: (data) => {

          console.log(data);

          this.orderProducts = data.orderProducts;
          this.orderProductsEmpty = this.orderProducts.length === 0;
          this.total = data.totalPrice;
          this.shipping = data.totalPrice > 100 ? 0 : 10;
          this.subtotal = this.total - this.shipping;
        },
        error: (err) => {
          console.error('Error al cargar el carrito:', err);
        }
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/index']);
  }

  deleteOrderProduct(id: number): void {
    if (this.userId != null) {
      this.orderProductService.deleteOrderProduct(id, this.userId).subscribe({
        next: (data) => {

          console.log('OrderProduct eliminado con éxito');
          this.loadCart();


        },
        error: (err) => {
          console.error('Error al eiminar el orderProduct', err);
          this.loadCart();

        }
      });
    }
  }

  processOrder(): void {
    if (this.userId == null) {
      return;
    }
    this.orderProductService.processOrder(this.userId).subscribe({
      next: (data) => {
        console.log('Order procesado con éxito');
        this.router.navigate(['/orders']);
      },
      error: (err) => {
        console.error('Error al procesar el order', err);
        this.loadCart();
      }
    });
  }

  getProductImageUrl(productId: number): string {
    if (productId) {
      return `${environment.apiUrl}/products/${productId}/image`;
    } else {
      return '';
    }
  }
}
