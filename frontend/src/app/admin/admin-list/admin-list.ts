import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AdminService } from '../../services/admin/admin.service';
import { Subscription } from 'rxjs';
import { AdminForListDTO } from '../../services/admin/dto/admin-for-list-dto';
import { BaseIconButton } from '../../components/base-icon-button/base-icon-button';

@Component({
  selector: 'app-admin-list',
  imports: [ReactiveFormsModule, BaseIconButton],
  templateUrl: './admin-list.html',
  styleUrl: './admin-list.css'
})
export class AdminList implements OnInit, OnDestroy {

  private router = inject(Router);
  private adminService = inject(AdminService);
  private subscription: Subscription = new Subscription();

  errorMessage?: string;
  successMessage?: string;
  admins?: AdminForListDTO[];
  timeout = 3000; // 3 segundos

  ngOnInit(): void {
    const navigation = window.history.state;
    if (navigation && navigation.successMsg) {
      this.successMessage = navigation.successMsg;
      setTimeout(() => this.successMessage = undefined, this.timeout);
    }
    this.subscription.add(
      this.adminService.getAllAdmins().subscribe({
        next: (response) => {
          this.admins = response.objects;
          if (!this.admins || this.admins.length === 0) {
            this.errorMessage = response.message;
          }
        },
        error: (error) => {
          this.errorMessage = error && error.message ? error.message : String(error);
        }
      })
    );
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  create() {
    this.router.navigate(['/administradores/crear']);
  }

  viewAdminDetails(adminId: string) {
    // not implemented yet
  }

  deleteAdmin(adminId: string) {
    this.adminService.deleteAdmin(adminId).subscribe({
      next: (msg) => {
        this.admins = this.admins?.filter(a => a.id !== adminId);
        this.successMessage = typeof msg === 'string' ? msg : 'Administrador eliminado con éxito.';
        setTimeout(() => this.successMessage = undefined, this.timeout);
      },
      error: (error) => {
        this.errorMessage = error && error.message ? error.message : String(error);
      }
    });
  }

}
