// user.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { environment } from '../environment';
import { UserDTO } from '../dtos/user.dto';


@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  loadUserData(): Observable<any> {
    // Comprobar si la cookie de sesión está presente
    if (this.isUserLoggedIn()) {
      const userName = this.getUserNameFromCookie(); // Si lo deseas, puedes extraer el nombre de usuario desde la cookie
      return of({
        logged: true,
        userName,
        csrfToken: '' // Puedes también extraer el CSRF token si está en la cookie o realizar otra lógica
      });
    } else {
      return of({
        logged: false,
        userName: null,
        csrfToken: ''
      });
    }
  }

  // Método para comprobar si el usuario está logueado
  private isUserLoggedIn(): boolean {
    // Buscar la cookie 'SESSION_ID' (reemplaza con el nombre real de tu cookie)
    const cookies = document.cookie;
    return cookies.includes('SESSION_ID');
  }

  // Método para obtener el nombre de usuario desde la cookie
  private getUserNameFromCookie(): string | null {
    const cookies = document.cookie.split(';');
    for (const cookie of cookies) {
      const [name, value] = cookie.trim().split('=');
      if (name === 'USER_NAME') {  // Reemplaza 'USER_NAME' con el nombre de tu cookie de nombre de usuario
        return value;
      }
    }
    return null;
  }

  searchProducts(query: string): Observable<any> {
    return this.http.get<any>(`${environment.apiUrl}/search?query=${query}`);
  }

  // Método para hacer logout
  logout(): Observable<any> {
    return this.http.post(`${environment.apiUrl}/logout`, {});
  }

  getUsers(page: number, pageSize: number): Observable<any> {
    const url = `${environment.apiUrl}/users?page=${page}&size=${pageSize}`;

    return this.http.get<any>(url);
  }

  deleteUser(userId: number): Observable<void> {
    const url = `${environment.apiUrl}/users/${userId}`;
    return this.http.delete<void>(url);
  }

  banUser(userId: number): Observable<void> {
    const url = `${environment.apiUrl}/users/${userId}?ban=true`;
    return this.http.patch<void>(url, null);
  }

  unbanUser(userId: number): Observable<void> {
    const url = `${environment.apiUrl}/users/${userId}?ban=false`;
    return this.http.patch<void>(url, null);
  }

  getUsersReported(page: number, pageSize: number): Observable<any> {
    const url = `${environment.apiUrl}/users/reports?page=${page}&size=${pageSize}`;
    return this.http.get<any>(url);
  }

  login(loginRequest: { email: string; password: string }): Observable<any> {
    return this.http.post<any>(`${environment.apiUrl}/auth/login`, loginRequest, {
      withCredentials: true // To make sure the cookie is being sent with the token
    });
  }

  getUser(loginRequest: { email: string; password: string }): Observable<UserDTO> {
    return this.http.post<any>(`${environment.apiUrl}/users/me`, loginRequest, {
      withCredentials: true
    });
  }

}
