import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';


@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
})
export class ProfileComponent implements OnInit {

  admin: any;
  hasImage: boolean = false;

  constructor(private userService: UserService) { }

  ngOnInit(): void {
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
