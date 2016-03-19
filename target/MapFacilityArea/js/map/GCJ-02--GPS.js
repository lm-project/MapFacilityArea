define(function(){
	
	function getData(){
		 $.ajax({
				type : "GET",
				url : path+"/js/template/Mars2Wgs.txt",
				success : function(data) {
					console.log(data);
					convertLnglatToGPS(data);
				}
			});
	}
	
	function convertLnglatToGPS(data){
		var datas = data.split(",");
		var TableY = [660 * 450]; 
		var TableX = [];
	    for(var i= 0; i<datas.length;i++){  
            if (i % 2 == 0){  
                TableX[i / 2] = parseFloat(datas[i])/100000.0;  
            }else {  
                TableY[(i - 1) / 2] =  parseFloat(datas[i]) / 100000.0;  
            }  
        }  
    }   
	
    function GetID(I,J){  
    	return I + 660 * J;  
    }  
  
        /// <summary>  
        /// x是117左右，y是31左右  
        /// </summary>  
        /// <param name="xMars"></param>  
        /// <param name="yMars"></param>  
        /// <param name="xWgs"></param>  
        /// <param name="yWgs"></param>  
      function Parse(xMars, yMars){  
            var i, j, k;  
            var  x1, y1, x2, y2, x3, y3, x4, y4, dx, dy;  
            var t, u;  
            var xtry = xMars;  
            var ytry = yMars;  
            for (var k = 0; k < 10; ++k){  
                // 只对中国国境内数据转换  
                if (xtry < 72 || xtry > 137.9 || ytry < 10 || ytry > 54.9)return;   
                i = parseInt((xtry - 72.0) * 10.0);  
                j = parseInt((ytry - 10.0) * 10.0);  
                x1 = TableX[GetID(i, j)];  
                y1 = TableY[GetID(i, j)];  
                x2 = TableX[GetID(i + 1, j)];  
                y2 = TableY[GetID(i + 1, j)];  
                x3 = TableX[GetID(i + 1, j + 1)];  
                y3 = TableY[GetID(i + 1, j + 1)];  
                x4 = TableX[GetID(i, j + 1)];  
                y4 = TableY[GetID(i, j + 1)];  
  
                t = (xtry - 72.0 - 0.1 * i) * 10.0;  
                u = (ytry - 10.0 - 0.1 * j) * 10.0;  
  
                dx = (1.0 - t) * (1.0 - u) * x1 + t * (1.0 - u) * x2 + t * u * x3 + (1.0 - t) * u * x4 - xtry;  
                dy = (1.0 - t) * (1.0 - u) * y1 + t * (1.0 - u) * y2 + t * u * y3 + (1.0 - t) * u * y4 - ytry;  
  
                xtry = (xtry + xMars - dx) / 2.0;  
                ytry = (ytry + yMars - dy) / 2.0;  
            }  
  
           var xWgs = xtry;  
           var yWgs = ytry;  
  
        }  
  
      function button1_Click(object sender, EventArgs e){  
            var x = Convert.ToDouble(txbX.Text);  
            var y = Convert.ToDouble(txbY.Text);  
            Parse(x, y);  
      }  
      
      return getData();
});
