import { Component } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { BaseUserForm } from '../../components/base-user-form/base-user-form';

@Component({
  selector: 'app-create-admin',
  imports: [ReactiveFormsModule, BaseUserForm],
  templateUrl: './create-admin.html',
  styleUrl: './create-admin.css'
})
export class CreateAdmin {

}
