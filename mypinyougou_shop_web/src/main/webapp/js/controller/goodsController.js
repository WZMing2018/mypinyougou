 //控制层 
app.controller('goodsController' ,
	function($scope,$controller,goodsService,itemCatService,typeTemplateService,uploadService,specificationService){
	
	$controller('baseController',{$scope:$scope});//继承

	//初始化页面复合类对象
    $scope.entity = {
        tbGoods: {},
        tbGoodsDesc: {
            itemImages: [],
            customAttributeItems:[],
            specificationItems:[]
        },
        tbItems: []
    };

    //读取列表数据绑定到表单中
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象
        //获取富文本数据
        $scope.entity.tbGoodsDesc.introduction = editor.html();
		if($scope.entity.tbGoods.id != null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加
		}				
		serviceObject.success(
			function(response){
                alert(response.message);
				if(response.success){
					//清空数据
                    //复合类数据
                    $scope.entity = {tbGoods:{}, tbGoodsDesc:{}, tbItems:[]};
                    //一级分类下拉单选输入框
                    $scope.itemCat1List = [];
                    //二级分类下拉单选输入框
                    $scope.itemCat2List = [];
                    //三级分类下拉单选输入框
                    $scope.itemCat3List = [];
                    //品牌下拉单选输入框
                    $scope.typeTemplate.brandIds = [];
                    //富文本输入框
                    editor.html('');
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
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
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

    $scope.itemCat1List = [];

	//查询商品一级分类列表
	$scope.selectItemCat1List = function () {
        itemCatService.findItemCatByParentId(0).success(function (res) {
        	$scope.itemCat1List = res;
        });
    }

    $scope.itemCat2List = [];

    //监控entity.tbGoods.category1Id值的变化
    $scope.$watch('entity.tbGoods.category1Id', function (newValue, oldValue) {
        if (newValue != undefined) {
            itemCatService.findItemCatByParentId(newValue).success(function (res) {
                $scope.itemCat2List = res;
                //注意entity.tbGoods.category1Id值变化时,必须初始化以下值
                $scope.itemCat3List = [];
                $scope.entity.tbGoods.typeTemplateId = '';
                $scope.typeTemplate.brandIds = [];
            });
        }
    })

    $scope.itemCat3List = [];

    //监控entity.tbGoods.category2Id值的变化
    $scope.$watch('entity.tbGoods.category2Id', function (newValue, oldValue) {
        if (newValue != undefined) {
            itemCatService.findItemCatByParentId(newValue).success(function (res) {
                $scope.itemCat3List = res;
                //注意entity.tbGoods.category2Id值变化时,必须初始化以下值
                $scope.entity.tbGoods.typeTemplateId = '';
                $scope.typeTemplate.brandIds = [];
            });
        }
    })

    //监控entity.tbGoods.category3Id值的变化
    $scope.$watch('entity.tbGoods.category3Id', function (newValue, oldValue) {
        if (newValue != undefined) {
            itemCatService.findOne(newValue).success(function (res) {
                $scope.entity.tbGoods.typeTemplateId = res.typeId;
            });
        }
    })

    $scope.typeTemplate = {brandIds:[]};
    $scope.specList = [];

    //监控entity.tbGoods.typeTemplateId值的变化
    $scope.$watch('entity.tbGoods.typeTemplateId', function (newValue, oldValue) {
        if (newValue != '' && newValue != undefined) {
            typeTemplateService.findOne(newValue).success(function (res) {
                $scope.typeTemplate = res;
                $scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);
                $scope.entity.tbGoodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);
            });

            specificationService.findSpecAndOption(newValue).success(function (res) {
                $scope.specList = res;
            });
        }
    })

    $scope.image = {url: '', color: ''};
	//图片上传
	$scope.uploadFile = function () {
        uploadService.uploadFile().success(function (res) {
            if (res.success) {
                $scope.image.url = res.message;
            } else {
                alert(res.message);
            }
        });
    }

    //向$scope.entity.tbGoodsDesc.itemImages数组中添加一个对象
	$scope.addImage = function () {
        $scope.entity.tbGoodsDesc.itemImages.push($scope.image);
    }

    $scope.removeImage = function (index) {
        $scope.entity.tbGoodsDesc.itemImages.splice(index, 1);
    }

    //{"attributeName":"网络制式","attributeValue":["移动3G","移动4G"]}
    $scope.spec = {attributeName:'', attributeValue:[]};
	//$scope.entity.tbGoodsDesc.specificationItems
    $scope.updateSpec = function ($event, specName, optionName) {
        //$scope.entity.tbGoodsDesc.specificationItems数组中是否已存在$scope.spec对象
        var spec = $scope.searchObjectByKey($scope.entity.tbGoodsDesc.specificationItems, 'attributeName', specName);
        if ($event.target.checked) {//选中
            if (spec!=null) {//数组中已存在该规格对象
                spec.attributeValue.push(optionName);
            } else {//数组中不存在该规格对象
                $scope.entity.tbGoodsDesc.specificationItems.push({attributeName:specName, attributeValue:[optionName]});
            }
        } else {//未选中
            var index = spec.attributeValue.indexOf(optionName);
            spec.attributeValue.splice(index, 1);
            //判断$scope.spec.attributeValue数组是否为空
            if (spec.attributeValue.length <= 0) {
                index = $scope.entity.tbGoodsDesc.specificationItems.indexOf(spec);
                $scope.entity.tbGoodsDesc.specificationItems.splice(index, 1);
            }
        }
        alert(JSON.stringify($scope.entity.tbGoodsDesc.specificationItems));
    }


});	
