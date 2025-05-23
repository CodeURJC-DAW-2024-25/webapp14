import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpErrorInterceptor } from './interceptors/http-error.interceptor';

import { AppComponent } from './app.component';
import { IndexComponent } from './user/index/index.component';
import { CategoryComponent } from './user/category/category.component';
import { ElemDetailComponent } from './user/elem-detail/elem-detail.component';

import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { NavigationBarComponent } from './auth/shared/navigation-bar/navigation-bar.component';
import { ChartsComponent } from './admin/charts/charts.component';
import { OrdersAdminComponent } from './admin/orders/orders.component';
import { ProductsComponent } from './admin/products/products.component';
import { ProfileComponent } from './admin/profile/profile.component';
import { ProfileEditComponent } from './admin/profile-edit/profile-edit.component';
import { UsersComponent } from './admin/users/users.component';

import { FooterUserComponent } from './user/shared/footer-user/footer-user.component';
import { FooterLoginRegisterComponent } from './auth/shared/footer-user/footer-login-register.component';
import { HeaderUserComponent } from './user/shared/header-user/header-user.component';
import { AccessErrorComponent } from './access-error/access-error.component';
import { NoPageErrorComponent } from './no-page-error/no-page-error.component';
import { CartComponent } from './user-registered/cart/cart.component';
import { OrdersComponent } from './user-registered/orders/orders.component';

import { OrdersDetailComponent } from './user-registered/orders-detail/orders-detail.component';
import { UsersProfileComponent } from './user-registered/users-profile/users-profile.component';
import { FooterAdminComponent } from './admin/shared/footer-admin/footer-admin.component';
import { HeaderAdminComponent } from './admin/shared/header-admin/header-admin.component';
import { StoreLayoutComponent } from './layouts/store-layout/store-layout.component';
import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';
import { LoginRegisterLayoutComponent } from './layouts/login-register-layout/login-register-layout.component';
import { ImportsUserComponent } from './user/shared/imports-user/imports-user.component';
import { ImportsAdminComponent } from './admin/shared/imports-admin/imports-admin.component';

@NgModule({
  declarations: [
    AppComponent,
    IndexComponent,
    CategoryComponent,
    ElemDetailComponent,

    LoginComponent,
    RegisterComponent,
    FooterUserComponent,
    HeaderUserComponent,
    NavigationBarComponent,
    ChartsComponent,
    OrdersComponent,
    OrdersAdminComponent,
    ProductsComponent,
    ProfileComponent,
    ProfileEditComponent,
    UsersComponent,

    AccessErrorComponent,
    NoPageErrorComponent,
    CartComponent,
    OrdersDetailComponent,
    UsersProfileComponent,
    FooterAdminComponent,
    FooterLoginRegisterComponent,
    HeaderAdminComponent,
    StoreLayoutComponent,
    AdminLayoutComponent,
    LoginRegisterLayoutComponent,
    ImportsUserComponent,
    ImportsAdminComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: HttpErrorInterceptor, multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule { }
