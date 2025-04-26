import { Component } from '@angular/core';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-navigation-bar',
  templateUrl: './navigation-bar.component.html'
})
export class NavigationBarComponent {
  indexUrl: string = '';

  ngOnInit(): void {
    this.indexUrl = `${environment.baseUrl}index`;
  }
}
