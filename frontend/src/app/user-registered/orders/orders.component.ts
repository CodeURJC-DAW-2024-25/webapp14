import { Component, OnInit } from '@angular/core';

import { OrderDTO } from '../../dtos/order.dto';
import { OrderService } from '../../services/order.service';
import { Router } from '@angular/router';




@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html'
})
export class OrdersComponent {

  exists: boolean = false;
  orders: OrderDTO[] = [];

  private userId = 2;


  constructor(private orderService: OrderService, private router: Router) {}
  

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
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

  goBack(): void {
    this.router.navigate(['/index']);
  }

  viewOrderDetails(orderId: number): void {
    this.router.navigate(['/order', orderId]);
  }

}
