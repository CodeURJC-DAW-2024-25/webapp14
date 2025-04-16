import { ReviewDTO } from "./review.dto";




export interface UserDTO {
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
    reportedReviews?: ReviewDTO[],
}