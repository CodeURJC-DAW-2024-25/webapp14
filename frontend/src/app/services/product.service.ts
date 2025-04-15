import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProductDTO } from '../dtos/product.dto';
import { NewProductRequestDTO } from '../dtos/newProductRequest.dto';

import { environment } from '../environment';


@Injectable({
  providedIn: 'root'
})
export class ProductService {
  

  constructor(private http: HttpClient) {}

  private baseUrl = `${environment.apiUrl}/products`;


  deleteProduct(productId: number): Observable<void> {
    const url = `${environment.apiUrl}/products/${productId}`;
    return this.http.delete<void>(url);
  }

  editProduct(product: any): Observable<any> {
    const url = `${environment.apiUrl}/products/${product.id}`;
    
    const params = new HttpParams()
      .set('stock_S', product.stock_S || 0)
      .set('stock_M', product.stock_M || 0)
      .set('stock_L', product.stock_L || 0)
      .set('stock_XL', product.stock_XL || 0);
  
    const productDTO = {
      name: product.name,
      description: product.description,
      price: product.price,
      category: product.category,
      stock: product.stock,
      outOfStock: product.outOfStock,
    };
  
    return this.http.put<any>(url, productDTO, { params });
  }

  createProduct(body: NewProductRequestDTO, params: any): Observable<ProductDTO> {
    const url = `${environment.apiUrl}/products`;
  
    return this.http.post<ProductDTO>(url, body, {
      params: params,
      headers: { 'Content-Type': 'application/json' }
    });
  }
  


  getProducts(page: number, pageSize: number): Observable<any> {
    const url = `${environment.apiUrl}/products?page=${page}&size=${pageSize}`;
    return this.http.get<any>(url);
  }
}