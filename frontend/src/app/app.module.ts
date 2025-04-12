import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { IndexComponent } from './user/index/index.component';
import { CategoryComponent } from './user/category/category.component';
import { ElemDetailComponent } from './user/elem-detail/elem-detail.component';
import { MoreProductComponent } from './user/more-product/more-product.component';
import { MoreRecProductsComponent } from './user/more-rec-products/more-rec-products.component';
import { MoreReviewsComponent } from './user/more-reviews/more-reviews.component';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { NavigationBarComponent } from './auth/shared/navigation-bar/navigation-bar.component';
import { ChartsComponent } from './admin/charts/charts.component';
import { OrdersAdminComponent } from './admin/orders/orders.component';
import { ProductsComponent } from './admin/products/products.component';
import { ProfileComponent } from './admin/profile/profile.component';
import { ProfileEditComponent } from './admin/profile-edit/profile-edit.component';
import { UsersComponent } from './admin/users/users.component';
import { MoreOrdersComponent } from './admin/more-orders/more-orders.component';
import { MoreUsersComponent } from './admin/more-users/more-users.component';
import { MoreUsersReviewsComponent } from './admin/more-users-reviews/more-users-reviews.component';
import { FooterUserComponent } from './user/shared/footer-user/footer-user.component';
import { HeaderUserComponent } from './user/shared/header-user/header-user.component';
import { AccessErrorComponent } from './access-error/access-error.component';
import { NoPageErrorComponent } from './no-page-error/no-page-error.component';
import { CartComponent } from './user-registered/cart/cart.component';
import { OrdersDetailComponent } from './user-registered/orders-detail/orders-detail.component';
import { UsersProfileComponent } from './user-registered/users-profile/users-profile.component';
import { FooterAdminComponent } from './admin/shared/footer-admin/footer-admin.component';
import { HeaderAdminComponent } from './admin/shared/header-admin/header-admin.component';

@NgModule({
  declarations: [
    AppComponent,
    IndexComponent,
    CategoryComponent,
    ElemDetailComponent,
    MoreProductComponent,
    MoreRecProductsComponent,
    MoreReviewsComponent,
    LoginComponent,
    RegisterComponent,
    FooterUserComponent,
    HeaderUserComponent,
    NavigationBarComponent,
    ChartsComponent,
    OrdersAdminComponent,
    ProductsComponent,
    ProfileComponent,
    ProfileEditComponent,
    UsersComponent,
    MoreOrdersComponent,
    MoreUsersComponent,
    MoreUsersReviewsComponent,
    AccessErrorComponent,
    NoPageErrorComponent,
    CartComponent,
    OrdersDetailComponent,
    UsersProfileComponent,
    FooterAdminComponent,
    HeaderAdminComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
