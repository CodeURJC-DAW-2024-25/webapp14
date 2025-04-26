import { Component, OnInit } from '@angular/core';

import { OrderDTO } from '../../dtos/order.dto';
import { OrderService } from '../../services/order.service';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';




@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html'
})
export class OrdersComponent {

  user = this.userService.getCurrentUserData();
  userId = this.userService.getCurrentUserId();
  logged: boolean = this.userId != null;
  isAdmin: boolean = this.userService.getIsAdminUser();

  exists: boolean = false;
  orders: OrderDTO[] = [];


  constructor(private orderService: OrderService, private router: Router, private userService: UserService) {}
  

  ngOnInit(): void {
    if(!this.logged){
      this.router.navigate(["/login"]);
    }
    this.loadOrders();
  }

  loadOrders(): void {
    if(this.userId == null){
      return;
    }
    else{
      this.orderService.getOrders(this.userId).subscribe({
        next: (data) => {

          console.log(data);

          this.orders = data;
          this.exists = data.length > 0;


        },
        error: (err) => {
          console.error('Error al cargar los pedidos:', err);
        }
      });
    }
  } 

  goBack(): void {
    this.router.navigate(['/index']);
  }

  viewOrderDetails(orderId: number): void {
    this.router.navigate(['/orders', orderId]);
  }

}
