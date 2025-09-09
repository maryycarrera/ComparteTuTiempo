import { Observable } from "rxjs";
import { SignupRequest } from "./auth/payload/request/signup-request";
import { MessageResponse } from "./auth/payload/response/message-response";

export interface UserCreationService {
  create(request: SignupRequest): Observable<MessageResponse>;
}