﻿'use strict';
angular.module('todoApp', ['ngRoute', 'AdalAngular'])
    .config(['$routeProvider', '$httpProvider', 'adalAuthenticationServiceProvider', function ($routeProvider, $httpProvider, adalProvider) {

        $routeProvider.when("/", {
            controller: "homeCtrl",
            templateUrl: "/App/Views/Home.html",
        }).when("/TodoList", {
            controller: "todoListCtrl",
            templateUrl: "/App/Views/TodoList.html",
            requireADLogin: true,
        }).when("/UserData", {
            controller: "userDataCtrl",
            templateUrl: "/App/Views/UserData.html",
        }).otherwise({redirectTo: "/Home"});

        adalProvider.init(
            {
                instance: 'https://login.microsoftonline.com/',
                tenant: 'cirrusplcs.onmicrosoft.com',
                clientId: '86cdbd02-dba5-4920-ba3a-120b97fc948b',
                extraQueryParameter: 'nux=1',
                cacheLocation: 'localStorage',
                postLogoutRedirectUri: 'http://localhost:8080/logout',
            },
            $httpProvider
        );

    }]);
