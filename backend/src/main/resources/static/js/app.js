const NUM_RESULTS = 4;
let currentPage = 0;
const NUM_RESULT_REVIEWS = 2;
let loadMoreRequests = 0;
const NUM_PRODUCTS = 10;

let currentPage2 = 0;
let page = 0;

async function loadMoreAdminProducts(button) {
    currentPage2++;

    document.getElementById("loading-spinner").classList.remove('d-none');

    const response = await fetch(`/admin/moreProductsAdmin?page=${currentPage2}&size=${NUM_PRODUCTS}`);
    const data = await response.text();

    document.getElementById("products").innerHTML += data;

    document.getElementById("loading-spinner").classList.add('d-none');

    if (data.includes("<!-- true -->")) {
        document.getElementById('load-more').classList.add('d-none');
    }
}

const NUM_USERS = 5;
let currentPage3 = 0;

async function loadMoreAdminUsers(button) {
    currentPage3++;

    document.getElementById("loading-spinner-1").classList.remove('d-none');

    const response = await fetch(`/admin/moreUsersAdmin?page=${currentPage3}&size=${NUM_USERS}`);
    const data = await response.text();

    document.getElementById("users").innerHTML += data;

    document.getElementById("loading-spinner-1").classList.add('d-none');

    if (data.includes("<!-- true -->")) {
        document.getElementById('load-more').classList.add('d-none');
    }
}

const NUM_USERSREVIEWS = 5;
let currentPage4 = 0;

async function loadMoreAdminUsersReviews(button) {
    currentPage4++;

    document.getElementById("loading-spinner-2").classList.remove('d-none');

    const response = await fetch(`/admin/moreUsersReviewsAdmin?reportedPage=${currentPage4}&size=${NUM_USERSREVIEWS}`);
    const data = await response.text();

    document.getElementById("usersWithReportedReviews").innerHTML += data;

    document.getElementById("loading-spinner-2").classList.add('d-none');

    if (data.includes("<!-- true -->")) {
        document.getElementById('load-more-user-review').classList.add('d-none');
    }
}

const NUM_ORDERS = 10;
let currentPage5 = 0;

async function loadMoreAdminOrders(button) {
    currentPage5++;

    document.getElementById("loading-spinner").classList.remove('d-none');

    const response = await fetch(`/admin/moreOrdersAdmin?page=${currentPage5}&size=${NUM_ORDERS}`);
    const data = await response.text();

    const ordersList = document.getElementById("orders");
    ordersList.innerHTML += data;

    document.getElementById("loading-spinner").classList.add('d-none');

    if (data.includes("<!-- true -->")) {
        document.getElementById('load-more').classList.add('d-none');
    }
}

async function loadMore(category) {
    currentPage++;

    document.getElementById("loading-spinner").classList.remove('d-none');

    const response = await fetch(`/index/moreProducts?category=${category}&page=${currentPage}`);
    const data = await response.text();

    document.getElementById("products").innerHTML += data;

    document.getElementById("loading-spinner").classList.add('d-none');

    if (data.includes("<!-- true -->")) {
        document.getElementById('loadMoreButton').classList.add('d-none');
    }
}

async function loadMoreRecommendedProducts() {
    page++;

    document.getElementById("loading-spinner").classList.remove('d-none');

    const response = await fetch(`/index/moreRecProducts?page=${page}&size=${NUM_RESULT_REVIEWS}`);
    const data = await response.text();

    document.getElementById("recProducts").innerHTML += data;

    document.getElementById("loading-spinner").classList.add('d-none');

    if (data.includes("<!-- true -->")) {
        document.getElementById('loadMoreRecommendedProductsButton').classList.add('d-none');
    }
}

async function loadMoreReviews(productId) {
    const from = (loadMoreRequests + 1) * NUM_RESULT_REVIEWS;
    const to = from + NUM_RESULT_REVIEWS;

    document.getElementById("loading-spinner").classList.remove('d-none');

    const response = await fetch(`/index/moreReviews?id=${productId}&from=${from}&to=${to}`);
    const data = await response.text();

    document.getElementById("reviews").innerHTML += data;

    document.getElementById("loading-spinner").classList.add('d-none');

    if (data.includes("<!-- true -->")) {
        document.getElementById('loadMoreReviewsButton').classList.add('d-none');
    }

    loadMoreRequests++;
}
