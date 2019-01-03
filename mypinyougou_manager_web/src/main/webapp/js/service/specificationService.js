//定义服务
app.service('specificationService', function ($http) {

    this.findPage = function (page, size) {
        return $http.get('../specification/findPage/' + page + '/' + size);
    };

    this.save = function (specification) {
        return $http.post('../specification/add', specification);
    };

    this.edit = function (id) {
        return $http.get('../specification/edit/' + id);
    };

    this.delete = function (selectIDs) {
        return $http.get('../specification/delete/' + selectIDs);
    };

});