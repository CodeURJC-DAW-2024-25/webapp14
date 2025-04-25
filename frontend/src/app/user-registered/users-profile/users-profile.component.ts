import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { UserDTO } from '../../dtos/user.dto';
import { Router } from '@angular/router';

@Component({
  selector: 'app-users-profile',
  templateUrl: './users-profile.component.html'
})
export class UsersProfileComponent implements OnInit {
  form!: FormGroup;
  user!: UserDTO;
  imageFile?: File;
  errorEmail = '';
  errorCurrentPassword = '';
  errorNewPassword = '';
  errorImage = '';

  constructor(private fb: FormBuilder, private userService: UserService, public router: Router) {}

  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe(user => {
      this.user = user;
      this.form = this.fb.group({
        name: [user.name],
        surname: [user.surname],
        email: [user.email],
        address: [user.address ?? ''],
        currentPassword: [''],
        newPassword: [''],
        confirmPassword: ['']
      });
    });
  }

  onFileChange(event: any): void {
    this.imageFile = event.target.files[0];
  }

  onSubmit(): void {
    const formData = new FormData();
    const data = this.form.value;

    formData.append('name', data.name);
    formData.append('surname', data.surname);
    formData.append('email', data.email);
    formData.append('address', data.address);
    formData.append('currentPassword', data.currentPassword);
    formData.append('newPassword', data.newPassword);
    formData.append('confirmPassword', data.confirmPassword);
    if (this.imageFile) {
      formData.append('newImage', this.imageFile);
    }

    this.userService.updateUserProfile(formData).subscribe({
      next: () => this.router.navigate(['/user/profile']),
      error: (err) => {
        console.error(err);
        // Aquí podrías mapear errores específicos
      }
    });
  }

  deleteAccount(): void {
    this.userService.deleteCurrentUser().subscribe(() => {
      this.router.navigate(['/index']);
    });
  }
}

