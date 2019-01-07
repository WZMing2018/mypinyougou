//服务层
app.service('sellerService',function($http){

    //增加
    this.add=function(entity){
        return  $http.post('../seller/add',entity );
    }

    //分页请求
    this.findPage = function (page, size) {
        return $http.get('../seller/findPage/' + page + '/' + size);
    };

    //查询单个实体
    this.findOne = function (id) {
        return $http.get('../seller/findOne/' + id);
    };

    //更新商家状态
    this.updateStatus = function (id, status) {
        return $http.get('../seller/updateStatus/' + id + '/' + status);
    };
	    	
});
