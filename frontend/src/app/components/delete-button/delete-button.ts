import { Component, EventEmitter, Output } from '@angular/core';
import { BaseIconButton } from '../base-icon-button/base-icon-button';

@Component({
  selector: 'app-delete-button',
  imports: [BaseIconButton],
  templateUrl: './delete-button.html',
  styleUrl: './delete-button.css'
})
export class DeleteButton {

  @Output() clickDelete = new EventEmitter<void>();

  onClick() {
    this.clickDelete.emit();
  }

}
