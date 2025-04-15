import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProductDTO } from '../dtos/product.dto';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = 'https://localhost:8443/api/v1/products'; // Cambia esta URL por la de tu API REST

  constructor(private http: HttpClient) {}

  getProducts(): Observable<ProductDTO[]> {
    return this.http.get<ProductDTO[]>(this.apiUrl);
  }
}