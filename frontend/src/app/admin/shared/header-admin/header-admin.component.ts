import { Component, OnInit } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { environment } from '../../../../environments/environment';


@Component({
  selector: 'app-header-admin',
  templateUrl: './header-admin.component.html'
})
export class HeaderAdminComponent implements OnInit {

  admin: any;
  hasImage: boolean = false;

  adminProfileUrl: string = '';
  noAdminImageUrl: string = '';

  isDropdownVisible: boolean = false;

  user = this.userService.getCurrentUserData();

  constructor(
    private userService: UserService,
  ) { }

  ngOnInit(): void {

    this.noAdminImageUrl = `${environment.baseAssetUrl}assets/images/noAdminImage.png`;

    this.userService.getAdminProfile().subscribe({
      next: (data) => {
        this.admin = data;
        this.hasImage = !!data?.hasImage;
      },
      error: (err) => {
        console.error('Error loading profile:', err);
      }
    });
  }

  onLogout(event: MouseEvent): void {
    event.preventDefault();
    this.toggleDropdown();
    this.userService.logout().subscribe({
      next: (response) => {
        console.log('Logout exitoso:', response);
        this.router.navigate(['/index']);
      },
      error: (err) => {
        console.error('Error al hacer logout:', err);
      }
    });
  }

  getAdminImageUrl(): string {
    return `${environment.apiUrl}/admin/profile/image`;
  }

  toggleDropdown() {
    this.isDropdownVisible = !this.isDropdownVisible;
  }
}
