import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';


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

  constructor(private router: Router, private userService: UserService) { }

  ngOnInit(): void {
    if(!this.isAdmin){
      this.router.navigate(["/access-error"]);
    }
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

}
