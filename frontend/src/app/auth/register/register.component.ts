import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { UserService } from '../../services/user.service';
import { NewUserDTO } from '../../dtos/newUser.dto';

import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['../../../assets/css/loginRegisterStyle.css'],
})
export class RegisterComponent {

  registerForm: FormGroup;
  submitted = false;
  errors: any = {};

  loginUrl: string = '';

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router,
    private userService: UserService,
  ) {
    this.registerForm = this.fb.group({
      name: ['', Validators.required],
      surname: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      encodedPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required],
      agree: [false, Validators.requiredTrue]
    });
  }

  ngOnInit(): void {
    this.loginUrl = `${environment.baseUrl}login`;
  }

  onSubmit(): void {

    this.submitted = true;

    if (this.registerForm.invalid) {
      return;
    }

    const newUser: NewUserDTO = {
      name: this.registerForm.get('name')?.value,
      surname: this.registerForm.get('surname')?.value,
      email: this.registerForm.get('email')?.value,
      encodedPassword: this.registerForm.get('encodedPassword')?.value,
      confirmPassword: this.registerForm.get('confirmPassword')?.value
    };

    if (newUser.encodedPassword !== newUser.confirmPassword) {
      this.errors.confirmPassword = 'Las contraseñas no coinciden';
      return;
    }

    this.userService.registerUser(newUser).subscribe({
      next: (res) => {
        console.log('Registro exitoso:', res);
        this.router.navigate([`${environment.baseUrl}login`]);
      },
      error: (err) => {
        this.errors = err.error || {};
        console.error('Error durante el registro:', err);
      }
    });
  }

}
