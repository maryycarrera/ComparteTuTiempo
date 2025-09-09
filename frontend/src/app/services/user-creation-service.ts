import { Observable } from "rxjs";
import { SignupRequest } from "./auth/payload/request/signup-request";

export interface UserCreationService {
  create(request: SignupRequest): Observable<any>;
}