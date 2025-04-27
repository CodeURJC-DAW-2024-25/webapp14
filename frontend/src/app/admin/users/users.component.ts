import { Component } from '@angular/core';
import { UserService } from '../../services/user.service';
import { UserDTO } from '../../dtos/user.dto';
import { UserReportedDTO } from '../../dtos/userReported.dto';
import { ReviewService } from '../../services/review.service';
import { Router } from '@angular/router';



@Component({
  selector: 'app-users',
  templateUrl: './users.component.html'
})
export class UsersComponent {

  user = this.userService.getCurrentUserData();
  userId = this.userService.getCurrentUserId();
  logged: boolean = this.userId != null;
  isAdmin: boolean = this.userService.getIsAdminUser();

  newReview = {
    rating: 0,
    reviewText: '',
  };


  userCount: number = 0;
  totalReportedReviews: number = 0;
  bannedUserCont: number = 0;

  currentUsersReportedPage: number = 0;
  pageUsersReportedSize: number = 5;


  currentUsersPage: number = 0;
  totalUsersPages: number = 0;
  pageUsersSize: number = 5;
  users: UserDTO[] = []
  usersReported: UserReportedDTO[] = [];
  usersBanned: UserDTO[] = [];
  summaryCards = [
    { title: 'Usuarios totales', value: this.userCount, color: 'primary', icon: 'fas fa-tshirt' },
    { title: 'Usuarios baneados', value: this.bannedUserCont, color: 'warning', icon: 'fas fa-boxes' },
  ];

  constructor(private router: Router, private userService: UserService, private reviewService: ReviewService) {}
  

  ngOnInit(): void {
    if (!this.logged) {
      this.router.navigate(["/login"]);
      return;
    }
    if(!this.isAdmin){
      this.router.navigate(["/access-error"]);
    }
    this.loadData();
  }


  loadData(): void {
    this.userService.getUsers(this.currentUsersPage, this.pageUsersSize).subscribe({
      next: (data) => {

        console.log(data);

        this.totalUsersPages = data.users.totalPages;

        this.users = data.users.content;
        this.usersBanned = data.bannedUsers;
        this.userCount = data.totalUsers;

        this.userService.getUsersReported(this.currentUsersReportedPage, this.pageUsersReportedSize).subscribe({
          next: (reportedData) => {
              console.log(reportedData);
              this.usersReported = reportedData.users.content.map((user: UserReportedDTO) => ({
                ...user,
                reviews: user.reviews.filter((review: any) => review.reported === true)
              }));    
          },
          error: (err) => {
            console.error('Error al cargar las reviews reportadas:', err);
          }
        });

        this.summaryCards = [
          { title: 'Usuarios totales', value: this.userCount, color: 'primary', icon: 'fas fa-tshirt' },
          { title: 'Usuarios baneados', value: this.bannedUserCont, color: 'warning', icon: 'fas fa-boxes' },
        ];
      },
      error: (err) => {
        console.error('Error al cargar los datos:', err);
      }
    });

  }

  loadMoreUsers(): void {
    this.currentUsersPage++;
  
    this.userService.getUsers(this.currentUsersPage, this.pageUsersSize).subscribe({
      next: (data) => {
        const newUsers = data.users.content;

        console.log(newUsers);

  
        this.users = this.users.concat(newUsers);
        this.totalUsersPages = data.users.totalPages;
      },
      error: (err) => {
        console.error('Error al cargar más usuarios:', err);
      }
    });
  }

  deleteUser(userId: number): void {
    this.userService.deleteUser(userId).subscribe(
      () => {

        console.log('Usuario eliminado con éxito');
        this.loadData();
      },
      (error) => {
        console.error('Error al eliminar el usuario', error);
        if (error.status === 500) {
          this.loadData();
        }
      }
      
    );
  }


  acceptReview(reviewId: number, productId: number): void {

    const body = {
      rating: 0,
      reviewText: ""
    };

    this.reviewService.getReview(reviewId, productId).subscribe(
      (data) => {
        console.log('Review obtenida con éxito');
        console.log(data);
        
        const body = {
          rating: data.rating,
          reviewText: data.reviewText
        };
    
        // Ahora llamamos a acceptReview dentro del mismo bloque
        this.reviewService.acceptReview(reviewId, productId, body).subscribe(
          () => {
            console.log('Review aceptada con éxito');
            this.loadData();
          },
          (error) => {
            console.error('Error al aceptar la review', error);
            if (error.status === 500) {
              this.loadData();
            }
          }
        );
      },
      (error) => {
        console.error('Error al obtener la review', error);
        if (error.status === 500) {
          this.loadData();
        }
      }
    );
  }

  deleteReview(reviewId: number, productId: number): void {
    this.reviewService.deleteReview(reviewId, productId, 1).subscribe(
      () => {
        console.log('Review eliminada con éxito');
        this.loadData();
      },
      (error) => {
        console.error('Error al eliminar la review', error);
        if (error.status === 500) {
          this.loadData();
        }
      }
      
    );
  }

  banUser(userId: number): void {
    this.userService.banUser(userId).subscribe(
      () => {

        console.log('Usuario baneado con éxito');
        this.loadData();
      },
      (error) => {
        console.error('Error al banear el usuario', error);
        if (error.status === 500) {
          this.loadData();
        }
      }
      
    );
  }

  unbanUser(userId: number): void {
    this.userService.unbanUser(userId).subscribe(
      () => {

        console.log('Usuario desbaneado con éxito');
        this.loadData();
      },
      (error) => {
        console.error('Error al desbanear el usuario', error);
        if (error.status === 500) {
          this.loadData();
        }
      }
      
    );
  }

}
