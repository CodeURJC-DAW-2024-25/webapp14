import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../environment';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

    private baseUrl = `${environment.apiUrl}/users/orders`;

    constructor(private http: HttpClient) {}


    getOrders(userId: number): Observable<any> {
        return this.http.get<any>(`${this.baseUrl}?userId=${userId}`);
    }

    getOrder(userId: number, orderId: number): Observable<any> {
        return this.http.get<any>(`${this.baseUrl}/${orderId}?userId=${userId}`);
    }
      
}