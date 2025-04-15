import { Component } from '@angular/core';

@Component({
  selector: 'app-imports-admin',
  templateUrl: './imports-admin.component.html',
  styles: ``
})
export class ImportsAdminComponent {
  ngOnInit(): void {
    this.loadScript('/assets/vendor/jquery/jquery.min.js');
    this.loadScript('/assets/vendor/bootstrap/js/bootstrap.bundle.min.js');
    this.loadScript('/assets/js/app.js');
  }

  private loadScript(url: string): void {
    const script = document.createElement('script');
    script.src = url;
    script.defer = true;
    document.body.appendChild(script);
  }
}
