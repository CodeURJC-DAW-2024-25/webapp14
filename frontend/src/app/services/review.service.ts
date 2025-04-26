import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { NewReviewRequestDTO } from '../dtos/newReviewRequest.dto';

import { environment } from '../environment';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  private baseUrl = `${environment.apiUrl}/products/reviews`;

  constructor(private http: HttpClient) { }

  getReviewReported(): Observable<any> {

    return this.http.get<any>(this.baseUrl,{
      withCredentials: true
    });
  }

  getReview(reviewId: number, productId: number): Observable<any> {
    const url = `${environment.apiUrl}/products/${productId}/reviews/${reviewId}`;
    return this.http.get<any>(url);
  }

  acceptReview(reviewId: number, productId: number, body: NewReviewRequestDTO): Observable<void>  {
    const url = `${environment.apiUrl}/products/${productId}/reviews/${reviewId}`;
    return this.http.put<void>(url,body,{
      withCredentials: true
    });
  }

  deleteReview(reviewId: number, productId: number, userId: number): Observable<void>  {
    const url = `${environment.apiUrl}/products/${productId}/reviews/${reviewId}?userId=${userId}`;
    return this.http.delete<void>(url,{
      withCredentials: true
    });
  }

  reportReview(reviewId: number, productId: number): Observable<void>  {
    const url = `${environment.apiUrl}/products/${productId}/reviews/${reviewId}`;
    return this.http.patch<void>(url,null,{
      withCredentials: true
    });
  }

  addReview(body: NewReviewRequestDTO, userId: number, productId: number) {
    const url = `${environment.apiUrl}/products/${productId}/reviews?userId=${userId}`;
    return this.http.post<void>(url,body,{
      withCredentials: true
    });
  }

  editReview(body: NewReviewRequestDTO, userId: number, productId: number, reviewId: number): Observable<void> {
    const url = `${environment.apiUrl}/products/${productId}/reviews/${reviewId}?userId=${userId}`;
    return this.http.put<void>(url, body,{
      withCredentials: true
    });
  }
  

}
