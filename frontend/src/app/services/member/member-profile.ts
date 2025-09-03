import { AdminDTO } from "../admin/admin-dto";

export interface MemberProfile extends AdminDTO {
    biography: string;
    dateOfMembership: string;
    hours: string;
    minutes: string;
}
