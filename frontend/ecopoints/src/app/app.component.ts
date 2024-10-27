import { HttpClientModule } from '@angular/common/http';
import { Component, importProvidersFrom } from '@angular/core';
import { bootstrapApplication } from '@angular/platform-browser';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'ecopoints';
}


bootstrapApplication(AppComponent, {
  providers: [
    importProvidersFrom(HttpClientModule)
  ]
}).catch(err => console.error(err));