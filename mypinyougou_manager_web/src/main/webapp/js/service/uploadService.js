//服务层
app.service('uploadService',function($http){

    //图片上传
    this.uploadFile = function () {
        //提交表单对象
        var formData = new FormData();
        //添加数据->提交图片
        formData.append('file', file.files[0]);

        return $http({
            method:'post',
            url:'../file/upload',
            data:formData,
            headers:{'Content-type':undefined},
            transformRequest: angular.identity
        });
    }

	    	
});
