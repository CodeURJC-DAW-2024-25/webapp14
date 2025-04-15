

export interface ReviewDTO {
    id?: number,
    username: string,
    //user: UserDTO,
    rating: number,
    reviewText: String,
    reported: boolean,
    own: boolean,
    ratingStars: boolean[],
    emptyStars: boolean[]
}
