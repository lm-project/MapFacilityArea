define(function (){
	console.log("加载polygonList , 包含新增，删除，修改，查询polygon功能");
   
	var _this = {}, first = null;
    	_this.length = 0;
    //添加
    _this.add = function(id, val) { 
    	if(id==0){
    		return;
    	}
    	console.log("新增polygonxxxxxxxx任务项为："+id);
        first = {id:id, data:val, next: first || null};
        _this.length++;
    };
    
    //删除
    _this.del = function(id){
    	console.log("删除polygon根据"+id);
        var ptemp = temp = first;
        for( ; temp; ptemp = temp ,temp= temp.next){
            if(temp.id == id){
                ptemp.next = temp.next;
                _this.length--;
                if ( _this.length == 0 ) {
                    first = null;
                }
                return true;
            }
        }
        return false;
    };
    
    //获取
    _this.get = function(id){
    	console.log("获取polygon根据"+id);
        for( var temp = first ; temp; temp= temp.next){
            if(temp.id == id){
            	return temp.data;
            }
        }
    };
    
    //替换
    _this.replace = function(id, val) { 
    	console.log("修改polygon 根据"+id);
    	for( var temp = first ; temp; temp= temp.next){
            if(temp.id == id){
             	temp.data = val;
            }
        }
    };
    
    _this.getAll = function() {
    	var rs = [];
    	console.log("获取所有的polygon");
		for( var temp = first ; temp ; temp= temp.next){
            rs.push(temp.data);
        }
        return rs;
    }
    return _this;
});
