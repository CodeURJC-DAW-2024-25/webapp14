import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';


@Component({
  selector: 'app-profile-edit',
  templateUrl: './profile-edit.component.html'
})
export class ProfileEditComponent implements OnInit {

  profileForm: FormGroup;
  admin: any;
  selectedImage: any = null;
  previewImageUrl: any = null;
  noAdminImageUrl: string = '';
  editProfileUrl: string = '';
  hasImage: boolean = false;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private sanitizer: DomSanitizer,
    private router: Router
  ) {
    this.profileForm = this.fb.group({
      name: ['', Validators.required],
      surname: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      currentPassword: [''],
      password: [''],
      confirmPassword: ['']
    });
  }

  ngOnInit(): void {
    this.noAdminImageUrl = `${environment.baseAssetUrl}assets/images/noAdminImage.png`;
    this.editProfileUrl = `${environment.baseAssetUrl}admin/profile/edit`;
    this.loadAdminProfile();
  }

  loadAdminProfile(): void {
    this.userService.getAdminProfile().subscribe({
      next: (data) => {
        this.admin = data;
        this.profileForm.patchValue({
          name: this.admin.name,
          surname: this.admin.surname,
          email: this.admin.email
        });
        if (this.admin.profileImage) {
          this.previewImageUrl = this.admin.profileImage;
          this.hasImage = true;
        } else {
          this.previewImageUrl = this.noAdminImageUrl;
          this.hasImage = false;
        }
      },
      error: (err) => {
        console.error('Error loading admin profile:', err);
      }
    });
  }

  onImageSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedImage = file;
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.previewImageUrl = this.sanitizer.bypassSecurityTrustUrl(e.target.result);
        this.hasImage = true;
      };
      reader.readAsDataURL(file);
    }
  }

  onSubmit(): void {
    if (this.profileForm.invalid) {
      console.warn('Formulario inválido. No se enviará.');
      return;
    }

    const formData = new FormData();

    formData.append('name', this.profileForm.value.name);
    formData.append('surname', this.profileForm.value.surname);
    formData.append('email', this.profileForm.value.email);
    if (this.profileForm.value.currentPassword) {
      formData.append('currentPassword', this.profileForm.value.currentPassword);
    }
    if (this.profileForm.value.password) {
      formData.append('password', this.profileForm.value.password);
      formData.append('confirmPassword', this.profileForm.value.confirmPassword);
    }
    if (this.selectedImage) {
      formData.append('image', this.selectedImage);
    }

    this.userService.updateAdminProfile(formData).subscribe({
      next: (response) => {
        console.log('Perfil actualizado exitosamente:', response);
        this.router.navigate(['/admin/profile']);
      },
      error: (err) => {
        console.error('Error actualizando perfil:', err);
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/admin/profile']);
  }

}
