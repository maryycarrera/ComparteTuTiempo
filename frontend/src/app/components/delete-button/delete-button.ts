import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-delete-button',
  imports: [],
  templateUrl: './delete-button.html',
  styleUrl: './delete-button.css'
})
export class DeleteButton {

  @Output() clickDelete = new EventEmitter<void>();

  onClick() {
    this.clickDelete.emit();
  }

}
