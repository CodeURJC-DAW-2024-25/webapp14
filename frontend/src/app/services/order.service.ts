import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environment';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private baseUrl = `${environment.apiUrl}/users/orders`;
  private url = `${environment.apiUrl}/orders`;

  constructor(private http: HttpClient) { }

  getOrders(userId: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}?userId=${userId}`, {
      withCredentials: true
    });
  }

  getOrder(userId: number, orderId: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/${orderId}?userId=${userId}`, {
      withCredentials: true
    });
  }

  getAllOrders(page: number, pageSize: number): Observable<any> {
    const url = `${environment.apiUrl}/orders?page=${page}&size=${pageSize}`;
    return this.http.get<any>(url, {
      withCredentials: true
    });
  }

  deleteOrder(orderId: number): Observable<any> {
    return this.http.delete(`${this.url}/${orderId}`, {
      withCredentials: true
    });
  }

  updateOrderState(orderId: number, newState: string): Observable<any> {
    return this.http.patch(`${this.url}/${orderId}?newState=${newState}`, {}, {
      withCredentials: true
    });
  }
}
