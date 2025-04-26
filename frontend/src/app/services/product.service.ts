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
  getProductsByCategory(category: string, page: number): Observable<{ products: ProductDTO[], category: string }> {
    const params = new HttpParams().set('page', page.toString());

    return this.http.get<{ products: ProductDTO[], category: string }>(
      `${environment.apiUrl}/${category}`,
      { params }
    );
  } 

  constructor(private http: HttpClient) {}

  private baseUrl = `${environment.apiUrl}/products`;


  deleteProduct(productId: number): Observable<void> {
    const url = `${environment.apiUrl}/products/${productId}`;
    return this.http.delete<void>(url,{
      withCredentials: true
    });
  }

  editProduct(product: any, stocks: any): Observable<any> {
    const url = `${environment.apiUrl}/products/${product.id}`;
    
    const params = new HttpParams()
    .set('stock_S', stocks.stock_S || 0)
    .set('stock_M', stocks.stock_M || 0)
    .set('stock_L', stocks.stock_L || 0)
    .set('stock_XL', stocks.stock_XL || 0);
  
    const productDTO = {
      name: product.name,
      description: product.description,
      price: product.price,
      category: product.category,
      stock: product.stock,
      outOfStock: product.outOfStock,
    };
  
    return this.http.put<any>(url, productDTO, { params, withCredentials: true });
  }

  createProduct(body: NewProductRequestDTO, params: any): Observable<ProductDTO> {
    const url = `${environment.apiUrl}/products`;
  
    return this.http.post<ProductDTO>(url, body, {
      params: params,
      headers: { 'Content-Type': 'application/json', }, withCredentials: true
    });
  }
  
  uploadProductImage(productId: number, formData: FormData): Observable<any> {
    return this.http.post(`${environment.apiUrl}/products/${productId}/image`, formData, {
      withCredentials: true
    });
  }

  updateProductImage(productId: number, formData: FormData): Observable<any> {
    return this.http.put(`${environment.apiUrl}/products/${productId}/image`, formData, {
      withCredentials: true
    });
  }


  getProducts(page: number, pageSize: number): Observable<any> {
    const url = `${environment.apiUrl}/products?page=${page}&size=${pageSize}`;
    return this.http.get<any>(url, { 
      withCredentials: true
    });
  }

  getIndexProducts(page: number, size: number): Observable<any> {
    const url = `${environment.apiUrl}?page=${page}&size=${size}`;
    return this.http.get<any>(url);
  }

  getIndexProductsLogged(page: number, size: number): Observable<any> {
    const url = `${environment.apiUrl}?page=${page}&size=${size}`;
    return this.http.get<any>(url, { 
      withCredentials: true
    });
  }

  getProduct(productId: number): Observable<any> {
    const url = `${environment.apiUrl}/products/${productId}`;
    return this.http.get<any>(url);
  }

  addToCart(productId: number, userId: number, selectedSize: String, quantity: number): Observable<any> {
  const url = `${environment.apiUrl}/cart/${productId}?userId=${userId}&size=${selectedSize}&quantity=${quantity}`;
  return this.http.post<any>(url, null, {
    withCredentials: true
  });
}

}