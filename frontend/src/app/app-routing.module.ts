import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// LAYOUTS
import { StoreLayoutComponent } from './layouts/store-layout/store-layout.component';
import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';
import { LoginRegisterLayoutComponent } from './layouts/login-register-layout/login-register-layout.component';

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


import { NoPageErrorComponent } from './no-page-error/no-page-error.component';
import { AccessErrorComponent } from './access-error/access-error.component';


const routes: Routes = [

  // LAYOUT STORE
  {
    path: '',
    component: StoreLayoutComponent,
    children: [
      { path: '', component: IndexComponent },
      { path: 'index', component: IndexComponent },
      { path: 'index/category/:category', component: CategoryComponent },
      { path: 'index/elem_detail/:id', component: ElemDetailComponent },
      { path: 'cart', component: CartComponent },
      { path: 'orders', component: OrdersComponent },
      { path: 'order/:orderId', component: OrdersDetailComponent },
      { path: 'user_registered/users_profile', component: UsersProfileComponent },
    ]
  },

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

  // LAYOUT LOGIN AND REGISTER
  {
    path: '',
    component: LoginRegisterLayoutComponent,
    children: [
      { path: 'login', component: LoginComponent },
      { path: 'register', component: RegisterComponent },
    ]
  },

  // WILDCARD
  { path: 'access-error', component: AccessErrorComponent },
  { path: 'no-page-error', component: NoPageErrorComponent },
  { path: '**', redirectTo: 'no-page-error' }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
