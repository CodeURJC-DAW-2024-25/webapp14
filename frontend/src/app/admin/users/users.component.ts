import { Component } from '@angular/core';
import { UserService } from '../../services/user.service';
import { UserDTO } from '../../dtos/user.dto';
import { ReviewDTO } from '../../dtos/review.dto';
import { ReviewService } from '../../services/review.service';




@Component({
  selector: 'app-users',
  templateUrl: './users.component.html'
})
export class UsersComponent {


  userCount: number = 0;
  totalReportedReviews: number = 0;
  bannedUserCont: number = 0;

  currentUsersPage: number = 0;
  totalUsersPages: number = 0;
  pageUsersSize: number = 5;
  users: UserDTO[] = []
  usersReviews: UserDTO[] = [];
  usersBanned: UserDTO[] = [];
  summaryCards = [
    { title: 'Usuarios totales', value: this.userCount, color: 'primary', icon: 'fas fa-tshirt' },
    { title: 'Usuarios baneados', value: this.bannedUserCont, color: 'warning', icon: 'fas fa-boxes' },
  ];

  constructor(private userService: UserService, private reviewService: ReviewService) {}
  

  ngOnInit(): void {
    this.loadData();
  }


  loadData(): void {
    this.userService.getUsers(this.currentUsersPage, this.pageUsersSize).subscribe({
      next: (data) => {

        console.log(data);

        this.totalUsersPages = data.users.totalPages;

        this.users = data.users.content;
        this.usersReviews = this.users.filter(user => user.reports > 0);
        this.usersBanned = this.users.filter(user => user.banned);
        this.userCount = data.totalUsers;

        this.reviewService.getReviewReported().subscribe({
          next: (reviewsData) => {
            const reportedReviews = reviewsData;
  
            this.users.forEach(user => {
              user.reportedReviews = reportedReviews.filter((review: ReviewDTO) => review.user.id === user.id && review.reported);
            });
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


  nextUsersPage(): void {
    if (this.currentUsersPage < this.totalUsersPages - 1) {
      this.currentUsersPage++;
      this.loadData();
    }
  }

  previousUsersPage(): void {
    if (this.currentUsersPage > 0) {
      this.currentUsersPage--;
      this.loadData();
    }
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
    this.reviewService.acceptReview(reviewId, productId).subscribe(
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
  }

  deleteReview(reviewId: number, productId: number): void {
    this.reviewService.deleteReview(reviewId, productId).subscribe(
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
    this.userService.unbanUser(userId).subscribe(
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
