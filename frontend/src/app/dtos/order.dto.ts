
import { OrderProductDTO } from "./orderProduct.dto";

export interface OrderDTO {
    id?: number,
    userId: number,
    createdAtFormatted: string,
    isPaid: boolean,
    totalPrice: number,
    state: string,
    orderProducts: OrderProductDTO[];
}