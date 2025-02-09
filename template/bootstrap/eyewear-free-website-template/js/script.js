(function ($) {

  "use strict";

  var initPreloader = function () {
    $(document).ready(function ($) {
      var Body = $('body');
      Body.addClass('preloader-site');
    });
    $(window).load(function () {
      $('.preloader-wrapper').fadeOut();
      $('body').removeClass('preloader-site');
    });
  }


// Función para cambiar estilos al hacer scroll o si no es la página principal
var initScrollNav = function () {
  var scroll = $(window).scrollTop();
  var isIndex = window.location.pathname.endsWith("index.html") || window.location.pathname === "/";

  if (!isIndex || scroll >= 200) {
    $('.navbar.fixed-top').addClass("scrolled");
  } else {
    $('.navbar.fixed-top').removeClass("scrolled");
  }
};

// Ejecutar la función al cargar la página y al hacer scroll
$(document).ready(function () {
  initScrollNav();
  $(window).scroll(function () {
    initScrollNav();
  });
});


  // init Chocolat light box
  var initChocolat = function () {
    Chocolat(document.querySelectorAll('.image-link'), {
      imageSize: 'contain',
      loop: true,
    })
  }


  var initProductQty = function () {

    $('.product-qty').each(function () {

      var $el_product = $(this);
      var quantity = 0;

      $el_product.find('.quantity-right-plus').click(function (e) {
        e.preventDefault();
        var quantity = parseInt($el_product.find('#quantity').val());
        $el_product.find('#quantity').val(quantity + 1);
      });

      $el_product.find('.quantity-left-minus').click(function (e) {
        e.preventDefault();
        var quantity = parseInt($el_product.find('#quantity').val());
        if (quantity > 0) {
          $el_product.find('#quantity').val(quantity - 1);
        }
      });

    });

  }

  // document ready
  $(document).ready(function () {

    AOS.init({
      duration: 1200,
      once: true,
    })

    var swiper = new Swiper(".slideshow", {
      slidesPerView: 1,
      spaceBetween: 0,
      speed: 1000,
      loop: true,
      pagination: {
          el: ".swiper-pagination",
          clickable: true,
      },
      navigation: {
          nextEl: ".swiper-button-next",
          prevEl: ".swiper-button-prev",
      }
  });
    // product single page
    var thumb_slider = new Swiper(".product-thumbnail-slider", {
      loop: true,
      slidesPerView: 3,
      autoplay: true,
      direction: "vertical",
      spaceBetween: 30,
    });

    var large_slider = new Swiper(".product-large-slider", {
      loop: true,
      slidesPerView: 1,
      autoplay: true,
      effect: 'fade',
      thumbs: {
        swiper: thumb_slider,
      },
    });

    window.addEventListener("load", (event) => {

      var $grid = $('.entry-container').isotope({
        itemSelector: '.entry-item',
        layoutMode: 'masonry'
      });

    });



    initPreloader();
    initChocolat();
    initProductQty();




  }); // End of a document

})(jQuery);