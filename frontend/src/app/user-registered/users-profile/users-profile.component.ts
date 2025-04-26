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
    const dto = this.form.value;
  
    this.userService.updateUserProfile(dto).subscribe({
      next: () => {
        if (this.imageFile) {
          this.userService.updateUserImage(this.imageFile).subscribe({
            next: () => this.router.navigate(['/user/profile']),
            error: (err) => console.error('Error subiendo imagen', err)
          });
        } else {
          this.router.navigate(['/user/profile']);
        }
      },
      error: (err) => {
        console.error('Error actualizando perfil:', err);
      }
    });
  }
  

  deleteAccount(): void {
    this.userService.deleteCurrentUser().subscribe(() => {
      this.router.navigate(['/index']);
    });
  }
}

