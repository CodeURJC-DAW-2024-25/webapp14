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
  logged: boolean = this.userId != null;
  userName: String = this.user?.name || '';
  query: string = '';
  productsSearch: any[] = [];
  csrfToken: string = '';
  open: boolean = false; // Estado del dropdown

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
    this.user = null;
    this.logged = false;
    this.userId = null;

    this.userService.logout().subscribe(() => {
      this.router.navigate(['/index']);
    });
  }

  // Método para alternar el estado de `open` y evitar la propagación del evento
  toggleDropdown(event: Event) {
    event.stopPropagation();  // Evita que el evento se propague
    this.open = !this.open;    // Cambia el estado de apertura del dropdown
  }
}
