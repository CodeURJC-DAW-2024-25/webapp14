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
  user = this.userService.getCurrentUserData();
  userId = this.userService.getCurrentUserId();
  logged: boolean = this.userId != null;
  isAdmin: boolean = this.userService.getIsAdminUser();
  imageFile?: File;
  errorEmail = '';
  errorCurrentPassword = ''; 
  errorNewPassword = '';
  errorImage = '';

  constructor(private fb: FormBuilder, private userService: UserService, public router: Router) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      name: [''],
      surname: [''],
      email: [''],
      address: [''],
      currentPassword: [''],
      newPassword: [''],
      confirmPassword: ['']
    });

    const user = this.userService.getCurrentUserData();
    if (user) {
      this.user = user;
      this.form.patchValue({
        name: user.name,
        surname: user.surname,
        email: user.email,
        address: user.address ?? ''
      });
    }
  }
  

  onFileChange(event: any): void {
    this.imageFile = event.target.files[0];
  }

  onSubmit(): void {
    const dto = this.form.value;
  
    this.userService.updateUserProfile(dto).subscribe({
      next: (updatedUser: UserDTO) => {
        
        this.userService.setCurrentUser(updatedUser); 
        this.user = updatedUser; 
        this.form.patchValue({
          name: updatedUser.name,
          surname: updatedUser.surname,
          email: updatedUser.email,
          address: updatedUser.address ?? ''
        });
  
        this.router.navigate(['/index']).then(() => {
          window.location.reload();
        });
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

