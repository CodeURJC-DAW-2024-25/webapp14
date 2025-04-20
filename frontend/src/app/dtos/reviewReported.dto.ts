import { ProductDTO } from "./product.dto";
import { UserDTO } from "./user.dto";


export interface ReviewReportedDTO {
    id?: number,
    username: string,
    user: UserDTO,
    product: ProductDTO,
    rating: number,
    reviewText: String,
    reported: boolean,
    own: boolean,
    ratingStars: boolean[],
    emptyStars: boolean[]
}
