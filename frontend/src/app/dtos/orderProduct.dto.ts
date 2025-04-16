
import { BasicProductSizeDTO } from "./basicProductSize.dto";

export interface OrderProductDTO {
	id?: number,
    product: BasicProductSizeDTO,
	productName: string,
    quantity: number,
	size: string,
    subtotalPrice: number,
}