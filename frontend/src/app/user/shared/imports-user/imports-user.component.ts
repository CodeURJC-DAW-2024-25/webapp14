import { Component } from '@angular/core';

@Component({
  selector: 'app-imports-user',
  templateUrl: './imports-user.component.html',
  styles: ``
})
export class ImportsUserComponent {
  ngOnInit(): void {
    this.loadScript('/assets/js/jquery-1.11.0.min.js');
    this.loadScript('https://cdn.jsdelivr.net/npm/swiper@9/swiper-bundle.min.js');
    this.loadScript('https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js');
  }

  ngOnLoad(): void {
    this.loadScript('/assets/js/plugins.js');
    this.loadScript('/assets/js/script.js');
    this.loadScript('/assets/js/app.js');
  }

  private loadScript(url: string): void {
    const script = document.createElement('script');
    script.src = url;
    script.defer = true;
    document.body.appendChild(script);
  }
}
