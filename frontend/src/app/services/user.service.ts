import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { environment } from '../environment';
import { UserDTO } from '../dtos/user.dto';
import { HttpErrorResponse } from '@angular/common/http';
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { NewUserDTO } from '../dtos/newUser.dto';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private currentUser: UserDTO | null = null;
  private userId: number | null = null;
  private token: string | null = null;
  private isLoggedIn: boolean = false;
  private isAdmin: boolean = false;

  constructor(private http: HttpClient, ) {  this.loadCurrentUser();
  }

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

  private isUserLoggedIn(): boolean {
    // Buscar la cookie 'SESSION_ID' (reemplaza con el nombre real de tu cookie)
    const cookies = document.cookie;
    return cookies.includes('SESSION_ID');
  }

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

  registerUser(newUser: NewUserDTO): Observable<any> {
    return this.http.post<any>(`${environment.apiUrl}/user/register`, newUser).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    console.error('Error desde backend:', error);
    return throwError(() => error);
  }

  updateUserProfile(editUserDto: any): Observable<any> {
    return this.http.put(`${environment.apiUrl}/users`, editUserDto, {
      withCredentials: true
    });
  }

  updateUserImage(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('imageFile', file);
    return this.http.put(`${environment.apiUrl}/users/image`, formData, {
      withCredentials: true
    });
  }

  deleteCurrentUser(): Observable<any> {
    return this.http.delete(`${environment.apiUrl}/users`, {
      withCredentials: true
    });
  }

  getCurrentUser(): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${environment.apiUrl}/users/me`, {
      withCredentials: true
    });
  }

  getAdminProfile(): Observable<any> {
    return this.http.get<any>(`${environment.apiUrl}/users/admin`, {
      withCredentials: true
    });
  }


  setCurrentUser(user: UserDTO): UserDTO {
    this.currentUser = user;
    this.userId = user.id || null;
    this.isLoggedIn = true;
    this.isAdmin = user.roles.includes('ADMIN');

    localStorage.setItem('currentUser', JSON.stringify(user));

    return user;
  }

  loadCurrentUser(): void {
    const userJson = localStorage.getItem('currentUser');
    if (userJson) {
      const user = JSON.parse(userJson) as UserDTO;
      this.currentUser = user;
      this.userId = user.id || null;
      this.isLoggedIn = true;
      this.isAdmin = user.roles.includes('ADMIN');
    }
  }



  getCurrentUserId(): number | null {
    return this.userId;
  }

  getCurrentUserData(): UserDTO | null {
    return this.currentUser;
  }

  getIsLoggedInUser(): boolean {
    return this.isLoggedIn;
  }

  getIsAdminUser(): boolean {
    return this.isAdmin;
  }

  logout(): Observable<any> {
    this.currentUser = null;
    this.isLoggedIn = false;
    this.isAdmin = false;
    this.userId = null;
    return this.http.post(`${environment.apiUrl}/auth/logout`, {});
    
  }

}
