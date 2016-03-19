package com.autonavi.mapart.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.autonavi.mapart.entity.Facilityachieve;
import com.autonavi.mapart.orm.AnShapeDao;

public class MyPredicate<T> implements Predicate<T>{

	 Facilityachieve var1;
	  public boolean test(T var){
	  if(var1.equals(var)){
	   return true;
	  }
	  return false;
	  }
	  
	  
//		hashCode()方法被用来获取给定对象的唯一整数。这个整数被用来确定对象被存储在HashTable类似的结构中的位置。
//		默认的，Object类的hashCode()方法返回这个对象存储的内存地址的编号。
//		如果重写equals()方法必须要重写hashCode()方法
//		  1.如果两个对象相同，那么它们的hashCode值一定要相同；
//		  2.如果两个对象的hashCode相同，它们并不一定相同（这里说的对象相同指的是用eqauls方法比较）。  
//		        如不按要求去做了，会发现相同的对象可以出现在Set集合中，同时，增加新元素的效率会大大下降。
//		  3.equals()相等的两个对象，hashcode()一定相等；equals()不相等的两个对象，却并不能证明他们的hashcode()不相等。
//		@Override
//		public int hashCode() {
//			final int prime = 31;
//			int result = 1;
//			result = prime * result + getOuterType().hashCode();
//			result = prime * result + ((area == null) ? 0 : area.hashCode());
//			result = prime * result + ((polygon == null) ? 0 : polygon.hashCode());
//			return result;
//		}
//		private Facilityachieve getOuterType() {
//			return Facilityachieve.this;
//		}
//		@Override
//		public boolean equals(Object obj) {
//			if (this == obj)
//				return true;
//			if (obj == null)
//				return false;
//			if (getClass() != obj.getClass())
//				return false;
//			Facilityachieve other = (Facilityachieve) obj;
//			if (!getOuterType().equals(other.getOuterType()))
//				return false;
//			if (area == null) {
//				if (other.area != null)
//					return false;
//			} else if (!area.equals(other.area))
//				return false;
//			if (polygon == null) {
//				if (other.polygon != null)
//					return false;
//			} else if (!polygon.equals(other.polygon))
//				return false;
//			return true;
//		}

	public static void main(String[] args) throws Exception {
//		 BeanFactory factory = new ClassPathXmlApplicationContext("service-context.xml");
//		 AnShapeDao anShapedao = (AnShapeDao)factory.getBean("anShapeDao");
//		 List<Facilityachieve> updateList = new ArrayList<Facilityachieve>();
//		 updateList.add(new Facilityachieve("name_chn","F47F002034",1,3310,0,0,0,"MULTIPOLYGON (((360669.657329667 86100,360563.20299948 86100,360559.54133 86107.043409,360571.5982 86114.674499,360584.73822 86117.569739,360593.64662 86112.447389,360603.81709 86109.255179,360627.27605 86105.097929,360669.1458 86101.534569,360669.657329667 86100)))",
//				 "a",0,"",""));
//		 anShapedao.updateDataByImport(updateList);
		 
		List<Facilityachieve> list1 = new ArrayList<Facilityachieve>();
//		Set<Facilityachieve> set = new HashSet<Facilityachieve>();
//		long startTime=System.currentTimeMillis();   //获取开始时间  
		for(int i = 0;i<10;i++){
			list1.add(new Facilityachieve(i,"mesh",1321+i));
//			set.add(new Facilityachieve(i,"mesh",1321+i));
		}
		Facilityachieve fa1 = new Facilityachieve("name_chn","mesh",1325,3310,0,0,0,"MULTIPOLYGON (((360669.657329667 86100,360563.20299948 86100,360559.54133 86107.043409,360571.5982 86114.674499,360584.73822 86117.569739,360593.64662 86112.447389,360603.81709 86109.255179,360627.27605 86105.097929,360669.1458 86101.534569,360669.657329667 86100)))",
				 "a",0,"","");
		System.out.println(list1.indexOf(fa1));
//		long pp = System.currentTimeMillis();
//		System.out.println(pp-startTime);
//		Facilityachieve fa1 = new Facilityachieve("mesh",132);
//		System.out.println(list1.size());
//		System.out.println(set.size());
//		System.out.println(list1.contains(fa1));
//		set.contains(fa1);
//		System.out.println(System.currentTimeMillis()-pp);
//		for(int i=0;i<list1.size();i++){
//			if(list1.contains(fa1)){
//				System.out.println(list1.contains(fa1));
//				break;
//			}
//		}
//		System.out.println(System.currentTimeMillis()-pp);
		
//		System.out.println(set.size());
//		
//		System.out.println(fa2.hashCode());
//		System.out.println(fa1.hashCode());
//		System.out.println(fa3.hashCode());
////		System.out.println(fa1.equals(fa2));
////		System.out.println(fa2.equals(fa3));
////		System.out.println(fa2.equals(fa1));
//		System.out.println();
//		System.out.println(list1.contains(fa2));
//		System.out.println();
		
		System.out.println(Math.ceil(200000/10000.0));
		
	}
}
