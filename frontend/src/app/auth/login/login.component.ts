import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';

import { environment } from '../../../environments/environment';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['../../../assets/css/loginRegisterStyle.css'],
})
export class LoginComponent implements OnInit {

  loginForm!: FormGroup;
  errorMessage: string | null = null;
  showPassword: boolean = false;
  token: string = '';

  registerUrl: string = '';

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.registerUrl = `${environment.baseUrl}register`;

    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  get email() { return this.loginForm.get('email'); }
  get password() { return this.loginForm.get('password'); }

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  onSubmit(): void {
    const loginRequest = {
      email: this.email?.value,
      password: this.password?.value
    };

    this.userService.login(loginRequest).subscribe({
      next: (response) => {
        this.userService.getUser(loginRequest).subscribe({
          next: (user) => {
            console.log("Usuario logueado:", user);
            this.userService.setCurrentUser(user);
            if (user.roles.includes('ADMIN')) {
              this.router.navigate(['/admin/charts']);
            } else {
              this.router.navigate(['/index']);
            }
          },
          error: (error) => {
            console.error("No se pudo obtener la información del usuario", error);
            this.errorMessage = 'Error al obtener datos del usuario';
          }
        });
      },
      error: (error) => {
        console.error("Error en el login", error);
        this.errorMessage = 'Credenciales incorrectas';
      }
    });
  }

}
