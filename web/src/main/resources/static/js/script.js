var app = angular.module('bookshelf.app', []);

app.controller('BookController', function ($scope, $http) {

    $scope.find = function(request) {
        $scope.data_not_found = null;
        $scope.data = null;
        $http.post('/api/search/', {'request': request})
            .success(function(response) {
                if (response.length == 0)
                    $scope.data_not_found = "Ничего не найдено";
                else
                    $scope.data = response;
            })
            .error(function(response) {
                $scope.data = response.message;
            })
    }
});