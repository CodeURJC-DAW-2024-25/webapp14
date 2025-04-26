import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../environment';

@Injectable({
  providedIn: 'root'
})
export class OrderProductService {

  private baseUrl = `${environment.apiUrl}/cart`;

  constructor(private http: HttpClient) { }

  getCart(userId: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}?userId=${userId}`, {
      withCredentials: true
    });
  }
  
  deleteOrderProduct(orderProductId: number, userId: number): Observable<any> {
    const url = `${environment.apiUrl}/cart/${orderProductId}`;
    return this.http.patch<any>(`${url}?userId=${userId}`, null, {
      withCredentials: true
    });
  }

  processOrder(userId: number): Observable<any> {
    return this.http.patch<any>(`${this.baseUrl}?userId=${userId}`, null, {
      withCredentials: true
    });
  }
}
