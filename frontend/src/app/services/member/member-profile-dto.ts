import { AdminDTO } from "../admin/admin-dto";

export interface MemberProfileDTO extends AdminDTO {
    biography: string;
    dateOfMembership: string;
    hours: string;
    minutes: string;
}
