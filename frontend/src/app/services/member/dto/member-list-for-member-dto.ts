import { MemberListDTO } from "./member-list-dto";

export interface MemberListForMemberDTO extends MemberListDTO {
    email: string;
}