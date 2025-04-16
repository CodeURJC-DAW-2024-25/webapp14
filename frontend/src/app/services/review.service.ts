import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../environment';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  private baseUrl = `${environment.apiUrl}/products/reviews`;

  constructor(private http: HttpClient) { }

  getReviewReported(): Observable<any> {

    return this.http.get<any>(this.baseUrl);
  }


  //HACER EL REST DE ACEPTAR
  acceptReview(reviewId: number, productId: number): Observable<void>  {
    const url = `${environment.apiUrl}/products/${productId}/reviews/${reviewId}`;
    return this.http.delete<void>(url);
  }

  deleteReview(reviewId: number, productId: number): Observable<void>  {
    const url = `${environment.apiUrl}/products/${productId}/reviews/${reviewId}`;
    return this.http.delete<void>(url);
  }

}
