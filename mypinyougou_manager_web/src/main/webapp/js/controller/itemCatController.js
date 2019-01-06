 //控制层 
app.controller('itemCatController' ,function($scope,$controller,itemCatService,typeTemplateService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		itemCatService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		itemCatService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		itemCatService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=itemCatService.update( $scope.entity ); //修改  
		}else{
            $scope.entity.parentId = $scope.parentId;
            $scope.entity.typeId = $scope.pojo.typeTemplate.id;
			serviceObject=itemCatService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
                alert(response.message);
				if(response.success){
					//重新查询 
		        	$scope.findItemByParentId($scope.parentId);//重新加载
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		itemCatService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		itemCatService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

    $scope.grade = 1;//记录当前显示页面的分类级别
    $scope.item_01 = {}; //保存选中的一级分类对象
    $scope.item_02 = {}; //保存选中的二级分类对象
    $scope.parentItemShow = '顶级分类列表'; //商品分类编辑,保存上级分类名称,(回显上级分类名称使用)
    $scope.parentId = 0; // 保存上级分类的id


	//更新当前分类级别
	$scope.setGrade = function (grade) {
        $scope.grade = grade;
    }

	//查询一级分类
	$scope.findItemByParentId = function (parentId) {
        itemCatService.findItemByParentId(parentId).success(function (res) {
            $scope.list = res;
        })
    }

    //查询下一级分类
	$scope.selectNext = function (item) {//item为点击的当前分类,以当前分类的id作为parentId查询下一级分类
		if ($scope.grade==1) {
            $scope.item_01 = {};
            $scope.item_02 = {};
            $scope.parentItemShow = '顶级分类列表';
            $scope.parentId = 0;
        } else if ($scope.grade==2) {
            $scope.item_01 = item;
            $scope.item_02 = {};
            $scope.parentItemShow = item.name;
            $scope.parentId = item.id;
        } else if ($scope.grade==3) {
            $scope.item_02 = item;
            $scope.parentItemShow = $scope.item_01.name + '>>' + item.name;
            $scope.parentId = item.id;
        }

        itemCatService.findItemByParentId(item.id).success(function (res) {
            $scope.list = res;
        });
    }

    //初始化select2下拉框数据
    $scope.typeTemplateList = {data: []};

    //查询商品类型模板id
	$scope.findTypeTemplateList = function () {
        typeTemplateService.findAll().success(function (res) {
        	$scope.typeTemplateList.data = res;
        });
    }

    //处理新建按钮,上次数据显示bug
	$scope.initHandler = function () {
        $scope.pojo = {};
        $scope.entity = {};
    }

});
