//定义服务
app.service('specificationService', function ($http) {

    this.findAll = function () {
        return $http.get('../specification/findAll');
    }

    this.findPage = function (page, size) {
        return $http.get('../specification/findPage/' + page + '/' + size);
    };

    this.findOne = function (id) {
        return $http.get('../specification/findOne/' + id);
    };

    this.save = function (specification) {
        return $http.post('../specification/add', specification);
    };

    this.delete = function (selectIDs) {
        return $http.get('../specification/delete/' + selectIDs);
    };

});