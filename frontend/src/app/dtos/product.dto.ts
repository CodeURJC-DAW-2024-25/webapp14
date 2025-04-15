import { SizeDTO } from "./size.dto";
import { ReviewDTO } from "./review.dto";


export interface ProductDTO {
	id?: number,
	name: string,
	description: string,
    price: number,
    stock: number,
    outOfStock: boolean,
    sold: number,
    category: string,
    imageBool: boolean,
    imageUrl: string,
    reviews: ReviewDTO[]
	sizes: SizeDTO[],
    showDetails?: boolean
}


