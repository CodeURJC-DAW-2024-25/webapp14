import { Component, OnInit } from '@angular/core';
import { OrderService } from '../../services/order.service';
import { OrderDTO } from '../../dtos/order.dto';


@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html'
})
export class OrdersAdminComponent implements OnInit {

  orders: { id: number, [key: string]: any }[] = [];

  orderCount: number = 0;
  loading: boolean = false;
  noOrders: boolean = false;

  currentPage: number = 0;
  totalPages: number = 0;
  pageSize: number = 10;

  constructor(private orderService: OrderService) { }

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.loading = true;
    this.orderService.getAllOrders(this.currentPage, this.pageSize).subscribe({
      next: (data) => {
        this.totalPages = data.orders.totalPages;
        this.orders = data.orders.content.map((order: OrderDTO) => ({
          ...order,
          showDetails: false
        }));
        this.orderCount = data.totalOrders;
        this.loading = false;
        this.noOrders = this.orders.length === 0;
      },
      error: (err) => {
        console.error('Error al cargar los pedidos:', err);
        this.loading = false;
      }
    });
  }

  deleteOrder(orderId: number): void {
    this.orderService.deleteOrder(orderId).subscribe(() => {
      this.orders = this.orders.filter(order => order.id !== orderId);
      this.loadOrders();
    });
  }

  updateOrderState(order: any): void {
    this.orderService.updateOrderState(order.id, order.state).subscribe(() => {
      console.log('order.state');
      console.log('Estado del pedido actualizado');
    });
  }

  loadMoreOrders(): void {
    this.currentPage++;
    this.loading = true;
    this.orderService.getAllOrders(this.currentPage, this.pageSize).subscribe({
      next: (data) => {
        const newOrders = data.orders.content;
        this.orders = this.orders.concat(newOrders);
        this.totalPages = data.orders.totalPages;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error al cargar más pedidos:', err);
        this.loading = false;
      }
    });
  }

}