// header-user.component.ts
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-header-user',
  templateUrl: './header-user.component.html'
})
export class HeaderUserComponent implements OnInit {
  user = this.userService.getCurrentUserData();
  userId = this.userService.getCurrentUserId();
  query: string = '';
  productsSearch: any[] = [];
  logged: boolean = this.userId != null;
  userName: String = this.user?.name || '';
  csrfToken: string = '';
  open: boolean = false;

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit(): void {
    this.loadInitialData();
  }

  loadInitialData() {
    this.userService.loadUserData().subscribe((response) => {
    });
  }

  onSearch() {
    if (this.query) {
      this.userService.searchProducts(this.query).subscribe((response) => {
        this.productsSearch = response.productsSearch || [];
      });
    }
  }

  logout() {
    this.userService.logout().subscribe(() => {
      this.router.navigate(['/index']);
    });
  }
}
