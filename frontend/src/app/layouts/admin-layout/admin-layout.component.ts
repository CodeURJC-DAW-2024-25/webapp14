import { Component } from '@angular/core';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-admin-layout',
  templateUrl: './admin-layout.component.html',
  styles: ``
})
export class AdminLayoutComponent {
  chartsUrl: string = '';
  usersUrl: string = '';
  ordersUrl: string = '';
  productsUrl: string = '';
  loginUrl: string = '';

  ngOnInit(): void {
    this.chartsUrl = `${environment.baseUrl}admin/charts`;
    this.usersUrl = `${environment.baseUrl}admin/users`;
    this.ordersUrl = `${environment.baseUrl}admin/orders`;
    this.productsUrl = `${environment.baseUrl}admin/products`;
    this.loginUrl = `${environment.baseUrl}login`;
  }
}
