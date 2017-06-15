import {Component} from '@angular/core';

@Component( {

    selector: 'navbarComponent',

    template: `

        <div class="navbar-wrapper">
      <div class="container">

        <nav class="navbar navbar-inverse navbar-static-top">
          <div class="container">
            <div class="navbar-header">
              <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
              </button>
              <a class="navbar-brand" href="#">
                       <img src="http://res.cloudinary.com/emfs/image/upload/v1491570457/logo-emfs_h4hrpj.svg" class="img-responsive" alt="Responsive image">
                     </a>
            </div>
            <div id="navbar" class="navbar-collapse collapse">
              <ul class="nav navbar-nav">
                <li class="active"><a href="#">Главная</a></li>
                <li><a href="#about">О нас</a></li>
                <li><a href="#contact">Контакты</a></li>
                <li class="dropdown">
                         <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Варианты оплаты<span class="caret"></span></a>
                         <ul class="dropdown-menu">
                           <li><a href="#">ЕРИП</a></li>
                           <li><a href="#">PAYPAL</a></li>
                           <li role="separator" class="divider"></li>
                           <li><a href="#">Безналичный расчет</a></li>
                           <li role="separator" class="divider"></li>
                           <li><a href="#">Оплата наличными</a></li>
                         </ul>
                       </li>
              </ul>
              
                                            <form class="navbar-form navbar-left">
                       <div class="form-group">
                         <input type="text" class="form-control" placeholder="Поиск по сайту">
                       </div>
                         <button type="submit" class="btn btn-default">Искать</button>
                     </form>

                     <ul class="nav navbar-nav navbar-right">

                       <li class="dropdown">
                         <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Регистрация<span class="caret"></span></a>
                           <ul class="dropdown-menu">
                             <li><a href="#">Facebook</a></li>
                             <li><a href="#">Google+</a></li>
                             <li><a href="#">LinkedIn</a></li>
                             <li role="separator" class="divider"></li>
                             <li><a href="#">Забыли пароль?</a></li>
                           </ul>
                       </li>
                       <button type="button" class="btn btn-primary navbar-btn">Sign in</button>
                       <button type="button" class="btn btn-warning navbar-btn">Пробный расчет</button>
                      </ul>
              
              
              
              
            </div>
          </div>
        </nav>

      </div>
    </div>
               
               
               
               
              `,


    styles: [
             `

              .navbar-brand>img {
               height: 100%;
               padding: 0px;
               width: auto;
               }
              .navbar-right {
               margin-right: 0px;
               }

            /* GLOBAL STYLES
-------------------------------------------------- */
/* Padding below the footer and lighter body text */

body {
  padding-bottom: 40px;
  color: #5a5a5a;
}


/* CUSTOMIZE THE NAVBAR
-------------------------------------------------- */

/* Special class on .container surrounding .navbar, used for positioning it into place. */
.navbar-wrapper {
  position: absolute;
  top: 0;
  right: 0;
  left: 0;
  z-index: 20;
}

/* Flip around the padding for proper display in narrow viewports */
.navbar-wrapper > .container {
  padding-right: 0;
  padding-left: 0;
}
.navbar-wrapper .navbar {
  padding-right: 15px;
  padding-left: 15px;
}
.navbar-wrapper .navbar .container {
  width: auto;
}


/* CUSTOMIZE THE CAROUSEL
-------------------------------------------------- */

/* Carousel base class */
.carousel {
  height: 500px;
  margin-bottom: 60px;
}
/* Since positioning the image, we need to help out the caption */
.carousel-caption {
  z-index: 10;
}

/* Declare heights because of positioning of img element */
.carousel .item {
  height: 500px;
  background-color: #777;
}
.carousel-inner > .item > img {
  position: absolute;
  top: 0;
  left: 0;
  min-width: 100%;
  height: 500px;
}


/* MARKETING CONTENT
-------------------------------------------------- */

/* Center align the text within the three columns below the carousel */
.marketing .col-lg-4 {
  margin-bottom: 20px;
  text-align: center;
}
.marketing h2 {
  font-weight: normal;
}
.marketing .col-lg-4 p {
  margin-right: 10px;
  margin-left: 10px;
}


/* Featurettes
------------------------- */

.featurette-divider {
  margin: 80px 0; /* Space out the Bootstrap <hr> more */
}

/* Thin out the marketing headings */
.featurette-heading {
  font-weight: 300;
  line-height: 1;
  letter-spacing: -1px;
}


/* RESPONSIVE CSS
-------------------------------------------------- */

@media (min-width: 768px) {
  /* Navbar positioning foo */
  .navbar-wrapper {
    margin-top: 20px;
  }
  .navbar-wrapper .container {
    padding-right: 15px;
    padding-left: 15px;
  }
  .navbar-wrapper .navbar {
    padding-right: 0;
    padding-left: 0;
  }

  /* The navbar becomes detached from the top, so we round the corners */
  .navbar-wrapper .navbar {
    border-radius: 4px;
  }

  /* Bump up size of carousel content */
  .carousel-caption p {
    margin-bottom: 20px;
    font-size: 21px;
    line-height: 1.4;
  }

  .featurette-heading {
    font-size: 50px;
  }
}

@media (min-width: 992px) {
  .featurette-heading {
    margin-top: 120px;
  }
}
             `
            ]
         } )

export class NavbarComponent { }