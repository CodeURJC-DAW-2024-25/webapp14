const NUM_RESULTS = 4;
let currentPage = 0;
const NUM_RESULT_REVIEWS = 2;
let loadMoreRequests = 0;


//load more products
async function loadMore(category) {
    currentPage++;

    const response = await fetch(`/index/moreProducts?category=${category}&page=${currentPage}`);
    const data = await response.text();

    document.getElementById("products").innerHTML += data;

    if (data.includes("<!-- true -->")) {
        document.getElementById('loadMoreButton').style.display = 'none';
    }
}


//load more reviews
async function loadMoreReviews(productId) {
    const from = (loadMoreRequests + 1) * NUM_RESULT_REVIEWS;
    const to = from + NUM_RESULT_REVIEWS;

    const response = await fetch(`/index/moreReviews?id=${productId}&from=${from}&to=${to}`);
    const data = await response.text();

    document.getElementById("reviews").innerHTML += data;

    if (data.includes("<!-- true -->")) {
        document.getElementById('loadMoreReviewsButton').style.display = 'none';
    }

    loadMoreRequests++;
}



