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

  createSuccess: string = '';

  constructor() {
    const navigation = window.history.state;
    if (navigation && navigation.successMsg) {
      this.createSuccess = navigation.successMsg;
    }
  }

  create() {
    this.router.navigate(['/administradores/crear']);
  }

}
