import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// LAYOUTS
import { StoreLayoutComponent } from './layouts/store-layout/store-layout.component';
import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';

// USER
import { IndexComponent } from './user/index/index.component';
import { CategoryComponent } from './user/category/category.component';
import { ElemDetailComponent } from './user/elem-detail/elem-detail.component';
import { CartComponent } from './user-registered/cart/cart.component';
import { OrdersDetailComponent } from './user-registered/orders-detail/orders-detail.component';
import { OrdersComponent } from './user-registered/orders/orders.component';
import { UsersProfileComponent } from './user-registered/users-profile/users-profile.component';

// AUTH
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';

// ADMIN
import { ChartsComponent } from './admin/charts/charts.component';
import { OrdersAdminComponent } from './admin/orders/orders.component';
import { ProductsComponent } from './admin/products/products.component';
import { ProfileComponent } from './admin/profile/profile.component';
import { ProfileEditComponent } from './admin/profile-edit/profile-edit.component';
import { UsersComponent } from './admin/users/users.component';

const routes: Routes = [
  // LAYOUT TIENDA
  {
    path: '',
    component: StoreLayoutComponent,
    children: [
      { path: '', component: IndexComponent },
      { path: 'index', component: IndexComponent },
      { path: 'category', component: CategoryComponent },
      { path: 'product/:id', component: ElemDetailComponent },
      { path: 'cart', component: CartComponent },
      { path: 'orders', component: OrdersComponent },
      { path: 'orders-detail', component: OrdersDetailComponent },
      { path: 'users-profile', component: UsersProfileComponent },
    ]
  },

  // LOGIN y REGISTER (sin layout, o con otro si querés)
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  // LAYOUT ADMIN
  {
    path: 'admin',
    component: AdminLayoutComponent,
    children: [
      { path: 'charts', component: ChartsComponent },
      { path: 'orders', component: OrdersAdminComponent },
      { path: 'products', component: ProductsComponent },
      { path: 'profile', component: ProfileComponent },
      { path: 'profile/edit', component: ProfileEditComponent },
      { path: 'users', component: UsersComponent },
    ]
  },

  // WILDCARD
  { path: '**', redirectTo: 'error/404' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
