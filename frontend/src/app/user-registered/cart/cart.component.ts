import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { OrderProductDTO } from '../../dtos/orderProduct.dto';
import { orderProductService } from '../../services/orderProduct.service';


@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html'
})
export class CartComponent {
  orderProducts: OrderProductDTO[] = [];
  orderProductsEmpty = true;
  orderNotProcessed = false;

  subtotal = 0;
  shipping = 0;
  total = 0;

  constructor(private router: Router, private orderProductService: orderProductService) {}

  ngOnInit(): void {
    this.loadCart();
  }


  loadCart(): void {
    this.orderProductService.getCart(2).subscribe({
      next: (data) => {

        console.log(data);

        this.orderProducts = data.orderProducts;  
        this.orderProductsEmpty = this.orderProducts.length === 0;
        this.total= data.totalPrice;    
        this.shipping = data.totalPrice > 100 ? 0 : 10;    
        this.subtotal = this.total - this.shipping;
      },
      error: (err) => {
        console.error('Error al cargar el carrito:', err);
      }
    });
  } 

  goBack(): void {
    this.router.navigate(['/index']);
  }

  deleteOrderProduct(id: number): void {
    this.orderProductService.deleteOrderProduct(id,2).subscribe({
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

  processOrder(): void {
    this.orderProductService.processOrder(2).subscribe({
      next: (data) => {

        console.log('Order procesado con éxito');
        this.loadCart();


      },
      error: (err) => {
        console.error('Error al procesar el order', err);
        this.loadCart();

      }
    });
  }
}
