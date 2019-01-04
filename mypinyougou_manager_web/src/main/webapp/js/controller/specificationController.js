//定义控制器
app.controller('specificationController', function ($scope, specificationService, $controller){

    //引用父控制器
    $controller('baseController', {$scope: $scope});

    // 分页查询
    $scope.findPage = function (page, size) {
        specificationService.findPage(page, size).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };

    //初始化specification对象
    $scope.specification = {
        spec:{},
        optionList:[]
    };

    //新增一行
    $scope.addRows = function () {
        $scope.specification.optionList.push({});
    };

    //移除一行
    $scope.removeRows = function (index) {
        $scope.specification.optionList.splice(index, 1);
    };

    //修改(回显)
    $scope.findOne = function (id) {
        specificationService.findOne(id).success(function (res) {
            $scope.specification = res;
        });
    };

    // 保存
    $scope.save = function () {
        specificationService.save($scope.specification).success(function (res) {
            alert(res.message);
            if(res.success) {
                //重新查询,刷界面
                $scope.reloadList();
            }
        });
    };

    //批量删除
    $scope.delete = function () {
        specificationService.delete($scope.selectIDs).success(function (res) {
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