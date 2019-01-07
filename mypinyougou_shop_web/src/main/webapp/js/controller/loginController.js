//定义控制器
app.controller("loginController", function($scope,loginService) {
	
	//获取用户登录名
	$scope.loadLoginInfo = function(){
		
		//调用服务层方法
		loginService.loadLoginInfo().success(
				function(res){
					$scope.loginName = res.loginName;
					/*$scope.loginTime = res.loginTime;*/
				}
		);
		
	};
	
});