 //控制层 
app.controller('goodsController' ,
	function($scope,$controller,goodsService,itemCatService,brandService){
	
	$controller('baseController',{$scope:$scope});//继承

	//初始化页面复合类对象
    $scope.entity = {
        tbGoods: {},
        tbGoodsDesc: {
            itemImages: [],
            customAttributeItems:[],
            specificationItems:[]
        },
        tbItems: [{spec:{}, price:0, num:99999, status:'0', isDefault:'0'}] //默认SKU列表中初始化一个对象
    };

    //初始化数据
    $scope.itemCat1List = [];
    $scope.itemCat2List = [];
    $scope.itemCat3List = [];
    $scope.typeTemplate = {brandIds:[]};
    $scope.specList = [];
    $scope.image = {url: '', color: ''};
    //{"attributeName":"网络制式","attributeValue":["移动3G","移动4G"]}
    // 存入$scope.entity.tbGoodsDesc.specificationItems数组中
    $scope.spec = {attributeName:'', attributeValue:[]};

    //翻译数组
    $scope.auditStatus = ['未审核','审核通过','审核未通过','关闭'];
    $scope.catList = [];
    $scope.brandList = [];
    $scope.status = ['否', '是'];
    //查询分类列表初始化翻译数组
    $scope.findCatList = function () {
        itemCatService.findAll().success(function (res) {
            for (var i=0; i<res.length; i++) {
                $scope.catList[res[i]['id']] = res[i]['name'];
            }
        });
    }

    //查询品牌列表初始化翻译数组
    $scope.findBrand = function () {
        brandService.findAll().success(function (res) {
            for (var i=0; i<res.length; i++) {
                $scope.brandList[res[i]['id']] = res[i]['name'];
            }
        });
    }

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
				$scope.entity.tbGoodsDesc.itemImages = JSON.parse($scope.entity.tbGoodsDesc.itemImages);
				$scope.entity.tbGoodsDesc.customAttributeItems = JSON.parse($scope.entity.tbGoodsDesc.customAttributeItems);
				$scope.entity.tbGoodsDesc.specificationItems = JSON.parse($scope.entity.tbGoodsDesc.specificationItems);
                for (var i=0; i<$scope.entity.tbItems.length; i++) {
                    $scope.entity.tbItems[i].spec = JSON.parse($scope.entity.tbItems[i].spec);
                }

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
                    $scope.entity = {
                        tbGoods: {},
                        tbGoodsDesc: {
                            itemImages: [],
                            customAttributeItems:[],
                            specificationItems:[]
                        },
                        tbItems: [{spec:{}, price:0, num:99999, status:'0', isDefault:'0'}] //默认SKU列表中初始化一个对象
                    };
                    //一级分类下拉单选输入框
                    // $scope.itemCat1List = [];
                    //二级分类下拉单选输入框
                    $scope.itemCat2List = [];
                    //三级分类下拉单选输入框
                    $scope.itemCat3List = [];
                    //品牌下拉单选输入框
                    $scope.typeTemplate.brandIds = [];
                    //富文本输入框
                    editor.html('');
                    //规格列表
                    $scope.specList = [];
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

	//查询商品一级分类列表
	$scope.selectItemCat1List = function () {
        itemCatService.findItemCatByParentId(0).success(function (res) {
        	$scope.itemCat1List = res;
            $scope.entity.tbGoods.category1Id = '';
        });
    }

    //监控entity.tbGoods.category1Id值的变化
    $scope.$watch('entity.tbGoods.category1Id', function (newValue, oldValue) {
        if (newValue != undefined) {
            itemCatService.findItemCatByParentId(newValue).success(function (res) {
                $scope.itemCat2List = res;
                //注意entity.tbGoods.category1Id值变化时,必须初始化以下值
                $scope.entity.tbGoods.category2Id = '';
                $scope.itemCat3List = [];
                $scope.entity.tbGoods.typeTemplateId = '';
                //品牌
                $scope.typeTemplate.brandIds = [];
                //自定义属性
                $scope.entity.tbGoodsDesc.customAttributeItems = [];
                //初始化规格选项卡
                $scope.specList = [];
                $scope.entity.tbGoodsDesc.specificationItems = [];
                $scope.entity.tbItems = [{spec:{}, price:0, num:99999, status:'0', isDefault:'0'}];
            });
        }
    })

    //监控entity.tbGoods.category2Id值的变化
    $scope.$watch('entity.tbGoods.category2Id', function (newValue, oldValue) {
        if (newValue != undefined) {
            itemCatService.findItemCatByParentId(newValue).success(function (res) {
                $scope.itemCat3List = res;
                //注意entity.tbGoods.category2Id值变化时,必须初始化以下值
                $scope.entity.tbGoods.category3Id = '';
                $scope.entity.tbGoods.typeTemplateId = '';
                //品牌
                $scope.typeTemplate.brandIds = [];
                //自定义属性
                $scope.entity.tbGoodsDesc.customAttributeItems = [];
                //初始化规格选项卡
                $scope.specList = [];
                $scope.entity.tbGoodsDesc.specificationItems = [];
                $scope.entity.tbItems = [{spec:{}, price:0, num:99999, status:'0', isDefault:'0'}];
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
                //初始化规格选项卡
                $scope.entity.tbGoodsDesc.specificationItems = [];
                $scope.entity.tbItems = [{spec:{}, price:0, num:99999, status:'0', isDefault:'0'}];
            });
        }
    })

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

	//更新商品规格
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
    }

    //生成SKU列表
    $scope.createItemList = function () {
        //每次点击都重新生产SKU列表
        $scope.entity.tbItems = [{spec:{}, price:0, num:99999, status:'0', isDefault:'0'}];
        //拿到该商品最新的规格
        //[{"attributeName":"网络制式","attributeValue":["移动3G","移动4G"]},{"attributeName":"屏幕尺寸","attributeValue":["6寸","5寸"]}]
        var specs = $scope.entity.tbGoodsDesc.specificationItems;
        for(var i=0; i<specs.length; i++) {
            $scope.entity.tbItems =
                $scope.addColumn($scope.entity.tbItems, specs[i]['attributeName'], specs[i]['attributeValue']);
        }
        // alert(JSON.stringify($scope.entity.tbItems));
    }

    //增加列
    $scope.addColumn = function (list, specName, optionName) {
        var newItemList = [];
        for (var i=0; i<list.length; i++) {
            var oldItem = list[i];
            for (var j=0; j<optionName.length; j++) {
                var newItem = JSON.parse(JSON.stringify(oldItem)); // 深克隆,拿到一份模板
                newItem.spec[specName] = optionName[j];
                newItemList.push(newItem);
            }
        }
        return newItemList;
    }

    //初始化SpecListAndTbItems
    $scope.initSpecItemsAndTbItems = function () {
        // $scope.specList = [];
        $scope.entity.tbGoodsDesc.specificationItems = [];
        $scope.entity.tbItems = [{spec:{}, price:0, num:99999, status:'0', isDefault:'0'}];
    }

    //更新商品审核状态
    $scope.updateAuditStatus = function (status) {
        goodsService.updateAuditStatus($scope.selectIDs, status).success(function (res) {
            alert(res.message);
            if (res.success) {
                $scope.reloadList();
                $scope.selectIDs = [];
            }
        });
    }

    //更新商品的删除状态
    $scope.updateDeleteStatus = function () {
        goodsService.updateDeleteStatus($scope.selectIDs).success(function (res) {
            alert(res.message);
            if (res.success) {
                $scope.reloadList();
                $scope.selectIDs = [];
            }
        })
    }
});	
