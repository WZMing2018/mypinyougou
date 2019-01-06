//服务层
app.service('loginService',function($http){

	this.showLoginName = function () {
        return $http.get('../login/name');
    }

});
