import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../../services/user.service';


@Component({
  selector: 'app-header-admin',
  templateUrl: './header-admin.component.html'
})
export class HeaderAdminComponent implements OnInit {

  admin: any;
  hasImage: boolean = false;

  constructor(
    private userService: UserService,
    private router: Router
  ) { }

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

  logout(): void {
    this.userService.logout().subscribe({
      next: (response) => {
        console.log('Logout exitoso:', response);
      },
      error: (err) => {
        console.error('Error al hacer logout:', err);
      }
    });
  }

}
