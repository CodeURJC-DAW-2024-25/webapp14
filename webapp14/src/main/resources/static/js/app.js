const NUM_RESULTS = 4;
let currentPage = 0;
const NUM_RESULT_REVIEWS = 2;
let loadMoreRequests = 0;
const NUM_PRODUCTS = 10;
let currentPage2 = 0;
let page = 0;

async function loadMoreAdminProducts(button) {
    currentPage2++;

    const response = await fetch(`/admin/moreProductsAdmin?page=${currentPage2}&size=${NUM_PRODUCTS}`);
    const data = await response.text();

    document.getElementById("products").innerHTML += data;

    if (data.includes("<!-- true -->")) {
        document.getElementById('load-more').style.display = 'none';
    }
}


const NUM_USERS = 5;
let currentPage3 = 0;

async function loadMoreAdminUsers(button) {
    currentPage3++;

    const response = await fetch(`/admin/moreUsersAdmin?page=${currentPage3}&size=${NUM_USERS}`);
    const data = await response.text();

    document.getElementById("users").innerHTML += data;

    if (data.includes("<!-- true -->")) {
        document.getElementById('load-more').style.display = 'none';
    }
}

const NUM_USERSREVIEWS = 5;
let currentPage4 = 0;

async function loadMoreAdminUsersReviews(button) {
    currentPage4++;

    const response = await fetch(`/admin/moreUsersReviewsAdmin?reportedPage=${currentPage4}&size=${NUM_USERSREVIEWS}`);
    const data = await response.text();

    document.getElementById("usersWithReportedReviews").innerHTML += data;

    if (data.includes("<!-- true -->")) {
        document.getElementById('load-more-user-review').style.display = 'none';
    }
}

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


//load more recommended products
async function loadMoreRecommendedProducts() {
    page++;

    const response = await fetch(`/index/moreRecProducts?page=${page}&size=${NUM_RESULT_REVIEWS}`);
    const data = await response.text();

    document.getElementById("recProducts").innerHTML += data;

    if (data.includes("<!-- true -->")) {
        document.getElementById('loadMoreRecommendedProductsButton').style.display = 'none';
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
