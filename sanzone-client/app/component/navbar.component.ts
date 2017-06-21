import {Component} from '@angular/core';

@Component( {

    selector: 'navbarComponent',

    template: `
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
                        <ul class="nav navbar-nav ">
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
              `,


    styles: [
             `.navbar-brand>img {
               height: 120%;
               padding: 0px;
               width: auto;
               }
             `
            ]
         } )

export class NavbarComponent { }