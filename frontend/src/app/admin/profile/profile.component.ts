import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
})
export class ProfileComponent implements OnInit {

  user = this.userService.getCurrentUserData();
  userId = this.userService.getCurrentUserId();
  logged: boolean = this.userId != null;
  isAdmin: boolean = this.userService.getIsAdminUser();

  admin: any;
  hasImage: boolean = false;

  editProfileUrl: string = '';
  noAdminImageUrl: string = '';

  constructor(private router: Router, private userService: UserService) { }

  ngOnInit(): void {
    if (!this.userId) {
      this.router.navigate(["/login"]);
      return;
    }
    if (!this.isAdmin) {
      this.router.navigate(["/access-error"]);
    }

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

  getAdminImageUrl(): string {
    return `${environment.apiUrl}/admin/profile/image`;
  }

}
