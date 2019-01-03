//定义控制器
app.controller('brandController', function ($scope, brandService, $controller){

    //引用父控制器
    $controller('baseController', {$scope: $scope});

    // 分页查询
    $scope.findPage = function (page, size) {
        brandService.findPage(page, size).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };

    // 新建
    $scope.save = function () {
        brandService.save($scope.brand).success(function (res) {
            if(res.success) {
                //重新查询,刷界面
                $scope.reloadList();
            } else {
                alert(res.message);
            }
        });
    };

    //修改
    $scope.edit = function (id) {
        brandService.edit(id).success(function (res) {
            $scope.brand = res;
        });
    };

    //批量删除
    $scope.delete = function () {
        brandService.delete($scope.selectIDs).success(function (res) {
            alert(res.message);
            if(res.success) {
                //重新查询,刷界面
                $scope.reloadList();
                //重新初始化
                $scope.selectIDs = [];
            }
        });
    };

});