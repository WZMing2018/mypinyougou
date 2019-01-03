app.controller('baseController', function ($scope) {
    // 分页控件配置
    // currentPage：当前页；totalItems：总记录数；itemsPerPage：每页记录数；perPageOptions：分页选项；onChange：当页码变更后自动触发的方法
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function () {
            $scope.reloadList();//重新加载
        }
    };

    //重新加载列表 数据
    $scope.reloadList = function () {
        //切换页码
        $scope.findPage($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    };


    //初始化选中的id的集合
    $scope.selectIDs = [];
    //更新复选
    $scope.updateSelection = function ($event, id) {
        if ($event.target.checked) {//如果是被选中,添加
            $scope.selectIDs.push(id);
        } else {//否则,删除
            var idx = $scope.selectIDs.indexOf(id);
            $scope.selectIDs.splice(idx, 1);//删除
        }
    };

});