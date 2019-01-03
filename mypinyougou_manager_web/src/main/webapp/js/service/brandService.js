//定义服务
app.service('brandService', function ($http) {

    this.findPage = function (page, size) {
        return $http.get('../brand/findPage/' + page + '/' + size);
    };

    this.save = function (brand) {
        return $http.post('../brand/add', brand);
    };

    this.edit = function (id) {
        return $http.get('../brand/edit/' + id);
    };

    this.delete = function (selectIDs) {
        return $http.get('../brand/delete/' + selectIDs);
    };

});