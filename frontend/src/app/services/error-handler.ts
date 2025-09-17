import { HttpErrorResponse } from "@angular/common/http";
import { throwError } from "rxjs";

export class ErrorHandler {
    public handleError(error: HttpErrorResponse, defaultMessage: string) {
        let errorMsg = defaultMessage;
        if (error.error && error.error.message) {
            errorMsg = error.error.message;
        } else if (error.error) {
            errorMsg = error.error;
        }
        return throwError(() => new Error(errorMsg));
    }
}