import { Component } from '@angular/core';
import { OrderService } from '../../services/order.service';
import { OrderProductDTO } from '../../dtos/orderProduct.dto';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user.service';  



@Component({
  selector: 'app-orders-detail',
  templateUrl: './orders-detail.component.html'
})
export class OrdersDetailComponent {

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

  private orderId!: number;

  constructor(private router: Router, private orderService: OrderService, private route: ActivatedRoute, private userService: UserService) {}

  ngOnInit(): void {
    if(!this.logged){
      this.router.navigate(["/access-error"]);
    }
    this.orderId = Number(this.route.snapshot.paramMap.get('orderId'));
    this.loadOrder();
  }

  loadOrder(): void {
    if(this.userId == null){
      
    }
    else{
      this.orderService.getOrder(this.userId, this.orderId).subscribe({
        next: (data) => {
          console.log(data);
          this.orderProducts = data.orderProducts;  
          this.orderProductsEmpty = this.orderProducts.length === 0;
          this.total= data.totalPrice;    
          this.shipping = data.totalPrice > 100 ? 0 : 10;    
          this.subtotal = this.total - this.shipping;
        },
        error: (err) => {
          console.error('Error al cargar el pedido:', err);
        }
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/orders']);
  }

}
