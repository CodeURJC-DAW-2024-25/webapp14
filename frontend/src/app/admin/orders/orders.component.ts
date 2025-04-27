import { Component, OnInit } from '@angular/core';
import { OrderService } from '../../services/order.service';
import { OrderDTO } from '../../dtos/order.dto';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html'
})
export class OrdersAdminComponent implements OnInit {

  user = this.userService.getCurrentUserData();
  userId = this.userService.getCurrentUserId();
  logged: boolean = this.userId != null;
  isAdmin: boolean = this.userService.getIsAdminUser();

  orders: { id: number, [key: string]: any }[] = [];

  orderCount: number = 0;
  loading: boolean = false;
  noOrders: boolean = false;

  currentPage: number = 0;
  totalPages: number = 0;
  pageSize: number = 10;

  constructor(private orderService: OrderService, private router: Router, private userService: UserService) { }

  ngOnInit(): void {
    if (!this.isAdmin) {
      this.router.navigate(["/access-error"]);
    }

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

  getProductImageUrl(productId: number): string {
    return `${environment.apiUrl}/products/${productId}/image`;
  }
}
