import { Component } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-imports-user',
  templateUrl: './imports-user.component.html',
  styles: ``
})
export class ImportsUserComponent {

  noirAndBlancCssUrl: SafeResourceUrl = '';

  constructor(private sanitizer: DomSanitizer) { }

  ngOnInit(): void {
    this.noirAndBlancCssUrl = this.sanitizer.bypassSecurityTrustResourceUrl(`${environment.baseAssetUrl}assets/css/noirandblanc.css`);

    this.loadScript(`${environment.baseAssetUrl}assets/js/jquery-1.11.0.min.js`);
    this.loadScript('https://cdn.jsdelivr.net/npm/swiper@9/swiper-bundle.min.js');
    this.loadScript('https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js');
  }

  ngOnLoad(): void {
    this.loadScript(`${environment.baseAssetUrl}assets/js/plugins.js`);
    this.loadScript(`${environment.baseAssetUrl}assets/js/script.js`);
    this.loadScript(`${environment.baseAssetUrl}assets/js/app.js`);
  }

  private loadScript(url: string): void {
    const script = document.createElement('script');
    script.src = url;
    script.defer = true;
    document.body.appendChild(script);
  }
}
