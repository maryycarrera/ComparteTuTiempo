import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AdminService } from '../../services/admin/admin.service';
import { Subscription } from 'rxjs';
import { AdminForListDTO } from '../../services/admin/dto/admin-for-list-dto';
import { BaseIconButton } from '../../components/base-icon-button/base-icon-button';
import { LoginService } from '../../services/auth/login.service';

@Component({
  selector: 'app-admin-list',
  imports: [ReactiveFormsModule, BaseIconButton],
  templateUrl: './admin-list.html',
  styleUrl: './admin-list.css'
})
export class AdminList implements OnInit, OnDestroy {

  private router = inject(Router);
  private adminService = inject(AdminService);
  private loginService = inject(LoginService);
  private subscription: Subscription = new Subscription();
  private isCurrentUserMap: Map<string, boolean> = new Map();

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
          if (this.admins) {
            this.admins.forEach(admin => this.checkIfCurrentUser(admin.id));
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
        this.isCurrentUserMap.delete(adminId); // limpiar cache
      },
      error: (error) => {
        this.errorMessage = error && error.message ? error.message : String(error);
      }
    });
  }

  // Llama a la API solo si no está cacheado
  private checkIfCurrentUser(adminId: string): void {
    if (this.isCurrentUserMap.has(adminId)) return;
    this.loginService.isCurrentUserPersonId(adminId).subscribe({
      next: (response) => {
        this.isCurrentUserMap.set(adminId, !!response.object);
      },
      error: (error) => {
        this.isCurrentUserMap.set(adminId, false);
        this.errorMessage = error && error.message ? error.message : String(error);
      }
    });
  }

  isCurrentUserThisAdminId(adminId: string): boolean {
    return this.isCurrentUserMap.get(adminId) || false;
  }
}
