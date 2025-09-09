import { Component, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-list',
  imports: [ReactiveFormsModule],
  templateUrl: './admin-list.html',
  styleUrl: './admin-list.css'
})
export class AdminList {

  private router = inject(Router);

  create() {
    this.router.navigate(['/administradores/crear']);
  }

}
