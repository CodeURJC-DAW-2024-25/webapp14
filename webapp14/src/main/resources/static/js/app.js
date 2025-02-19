const NUM_RESULTS = 4;
let currentPage = 0;

async function loadMore(category) {
    currentPage++;

    const response = await fetch(`/index/moreProducts?category=${category}&page=${currentPage}`);
    const data = await response.text();

    document.getElementById("products").innerHTML += data;

    if (data.includes("<!-- true -->")) {
        document.getElementById('loadMoreButton').style.display = 'none';
    }
}
