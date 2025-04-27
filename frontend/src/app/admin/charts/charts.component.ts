import { Component, OnInit } from '@angular/core';
import {
  Chart,
  registerables
} from 'chart.js';
import { ChartService } from '../../services/chart.service';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-charts',
  templateUrl: './charts.component.html'
})
export class ChartsComponent implements OnInit {

  user = this.userService.getCurrentUserData();
  userId = this.userService.getCurrentUserId();
  logged: boolean = this.userId != null;
  isAdmin: boolean = this.userService.getIsAdminUser();

  todaySales: number = 0;
  totalSales: number = 0;
  totalOrders: number = 0;
  totalUsers: number = 0;

  productNames: string[] = [];
  productSales: number[] = [];
  orderDates: string[] = [];
  orderCounts: number[] = [];

  constructor(private chartService: ChartService, private router: Router, private userService: UserService) {
    Chart.register(...registerables);
  }

  ngOnInit(): void {
    if (!this.userId) {
      this.router.navigate(["/login"]);
      return;
    }
    if (!this.isAdmin) {
      this.router.navigate(["/access-error"]);
    }

    this.chartService.getChartsData().subscribe(data => {
      this.todaySales = data.todaySales;
      this.totalSales = data.totalSales;
      this.totalOrders = data.totalOrders;
      this.totalUsers = data.totalUsers;

      this.productNames = data.top5BestSellingProducts.map((p: any) => p.name);
      this.productSales = data.top5BestSellingProducts.map((p: any) => p.sold);

      this.orderDates = data.ordersLast30Days.dates;
      this.orderCounts = data.ordersLast30Days.counts;

      this.renderCharts();
    });
  }

  renderCharts(): void {
    new Chart('topProductsChart', {
      type: 'bar',
      data: {
        labels: this.productNames,
        datasets: [{
          label: 'Ventas',
          data: this.productSales,
          backgroundColor: 'rgba(54, 162, 235, 0.6)',
          borderColor: 'rgba(54, 162, 235, 1)',
          borderWidth: 1
        }]
      },
      options: {
        responsive: true,
        scales: {
          y: {
            beginAtZero: true
          }
        }
      }
    });

    new Chart('weeklyOrdersChart', {
      type: 'line',
      data: {
        labels: this.orderDates,
        datasets: [{
          label: 'Pedidos',
          data: this.orderCounts,
          fill: false,
          borderColor: 'rgba(255, 99, 132, 1)',
          tension: 0.1
        }]
      },
      options: {
        responsive: true,
        scales: {
          y: {
            beginAtZero: true
          }
        }
      }
    });
  }
}
