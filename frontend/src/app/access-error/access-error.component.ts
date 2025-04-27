import { Component } from '@angular/core';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-access-error',
  templateUrl: './access-error.component.html',
  styleUrls: ['../../assets/css/loginRegisterStyle.css']
})
export class AccessErrorComponent {

  user = this.userService.getCurrentUserData();
  userId = this.userService.getCurrentUserId();
  logged: boolean = this.userId != null;
  isAdmin: boolean = this.userService.getIsAdminUser();

  constructor(private userService: UserService) { }
}
