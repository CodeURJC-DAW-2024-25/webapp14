import { SizeDTO } from "./size.dto";


export interface BasicProductSizeDTO {
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
    sizes: SizeDTO[]
}