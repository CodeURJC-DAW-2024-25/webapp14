import { ReviewReportedDTO } from "./reviewReported.dto";




export interface UserReportedDTO {
    id?: number,
    name: string,
    surname: string,
    email: string,
    address: string,
    banned: boolean,
    reports: number,
    imageUrl: String,
    roles: String[],
    showDetails?: boolean,
    showReports?: boolean,
    reviews: ReviewReportedDTO[],
}