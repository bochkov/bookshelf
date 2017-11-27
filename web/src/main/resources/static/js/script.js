var app = angular.module('bookshelf.app', []);

app.controller('BookController', function ($scope, $http) {

    $scope.find = function (request) {
        $scope.data_not_found = null;
        $scope.data = null;
        $http({
            method: 'POST',
            url: '/api/search/',
            data: request
        }).success(
            function (response) {
                if (response.length === 0)
                    $scope.data_not_found = "Ничего не найдено";
                else
                    $scope.data = response;
            }
        ).error(
            function (response) {
                $scope.data = response.message;
            }
        )
    };

    $scope.register = function (user, pass) {
        $http({
                method: 'POST',
                url: '/api/register/',
                data: {username: user, password: pass}
            }
        ).success(
            function () {
                window.location.reload();
            }
        );
    };
});