import { Component } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-imports-admin',
  templateUrl: './imports-admin.component.html',
  styles: ``
})
export class ImportsAdminComponent {

  fontAwesomeCssUrl: SafeResourceUrl = '';
  sbAdmin2CssUrl: SafeResourceUrl = '';
  customCssUrl: SafeResourceUrl = '';

  constructor(private sanitizer: DomSanitizer) { }

  ngOnInit(): void {
    this.fontAwesomeCssUrl = this.sanitizer.bypassSecurityTrustResourceUrl(`${environment.baseAssetUrl}assets/vendor/fontawesome-free/css/all.min.css`);
    this.sbAdmin2CssUrl = this.sanitizer.bypassSecurityTrustResourceUrl(`${environment.baseAssetUrl}assets/css/sb-admin-2.min.css`);
    this.customCssUrl = this.sanitizer.bypassSecurityTrustResourceUrl(`${environment.baseAssetUrl}assets/css/custom.css`);

    this.loadScript(`${environment.baseAssetUrl}assets/vendor/jquery/jquery.min.js`);
    this.loadScript(`${environment.baseAssetUrl}assets/vendor/bootstrap/js/bootstrap.bundle.min.js`);
    this.loadScript(`${environment.baseAssetUrl}assets/js/app.js`);
  }

  private loadScript(url: string): void {
    const script = document.createElement('script');
    script.src = url;
    script.defer = true;
    document.body.appendChild(script);
  }
}
