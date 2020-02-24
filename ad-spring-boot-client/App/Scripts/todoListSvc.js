'use strict';
angular.module('todoApp')
    .factory('todoListSvc', ['$http', function ($http) {
        return {
            getItems: function () {
                return $http.get('http://localhost:9999/myapp/api/todolist/');
            },
            getItem: function (id) {
                return $http.get('http://localhost:9999/myapp/api/todolist/' + id);
            },
            postItem: function (item) {
                return $http.post('http://localhost:9999/myapp/api/todolist/', item);
            },
            putItem: function (item) {
                return $http.put('http://localhost:9999/myapp/api/todolist/', item);
            },
            deleteItem: function (id) {
                return $http({
                    method: 'DELETE',
                    url: '/api/todolist/' + id
                });
            }
        };
    }]);
