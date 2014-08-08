package com.lqk.framework.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Region;

/** 
 * @Company: Dilitech
 * @author longqiankun
 * @email qiankun.long@dilitech.com
 * @Title: MathUtils.java
 * @Description: 数学工具类
 * @version 1.0  
 * @created 2014-1-15 上午9:50:51 
 */

public class MathUtils {
	static double DEF_PI = 3.14159265359; // PI
	static double DEF_2PI= 6.28318530712; // 2*PI
	static double DEF_PI180= 0.01745329252; // PI/180.0
	static double DEF_R =6370693.5; // radius of earth
	/**
	 * 获取两点之间的距离，公式：(p2.x-p1.x)2+(p2.y-p1.y)2=len2
	 * @param x1 第一个点的x坐标
	 * @param y1 第一个点的y坐标
	 * @param x2 第二个点的x坐标
	 * @param y2 第二个点的y坐标
	 * @return 两点之间的距离
	 */
	
	public static double getTwoPointDis(int x1,int y1,int x2,int y2){
		int disLen=(x1-x2)*(x1-x2)+(y1-y2)*(y1-y2);
		return Math.sqrt(disLen);
	}
	/**
	 * 获取地图上两点距离
	 * @param lon1  开始点的精度
	 * @param lat1 开始点的纬度
	 * @param lon2 结束点精度
	 * @param lat2 结束点纬度
	 * @return
	 */
		public static double GetShortDistance(double lon1, double lat1, double lon2, double lat2)
		{
		double ew1, ns1, ew2, ns2;
			double dx, dy, dew;
			double distance;
		// 角度转换为弧度
			ew1 = lon1 * DEF_PI180;
		ns1 = lat1 * DEF_PI180;
		ew2 = lon2 * DEF_PI180;
			ns2 = lat2 * DEF_PI180;
		// 经度差
		dew = ew1 - ew2;
		// 若跨东经和西经180 度，进行调整
			if (dew > DEF_PI)
		dew = DEF_2PI - dew;
		else if (dew < -DEF_PI)
		dew = DEF_2PI + dew;
		dx = DEF_R * Math.cos(ns1) * dew; // 东西方向长度(在纬度圈上的投影长度)
		dy = DEF_R * (ns1 - ns2); // 南北方向长度(在经度圈上的投影长度)
		// 勾股定理求斜边长
		distance = Math.sqrt(dx * dx + dy * dy);
		return distance;
		}
		/**
		 * 获取地图上两点距离
		 * @param lon1  开始点的精度
		 * @param lat1 开始点的纬度
		 * @param lon2 结束点精度
		 * @param lat2 结束点纬度
		 * @return
		 */
		public  static double GetLongDistance(double lon1, double lat1, double lon2, double lat2)
		{
		double ew1, ns1, ew2, ns2;
			double distance;
		// 角度转换为弧度
		ew1 = lon1 * DEF_PI180;
		ns1 = lat1 * DEF_PI180;
			ew2 = lon2 * DEF_PI180;
			ns2 = lat2 * DEF_PI180;
			// 求大圆劣弧与球心所夹的角(弧度)
			distance = Math.sin(ns1) * Math.sin(ns2) + Math.cos(ns1) * Math.cos(ns2) * Math.cos(ew1 - ew2);
		// 调整到[-1..1]范围内，避免溢出
		if (distance > 1.0)
		     distance = 1.0;
			else if (distance < -1.0)
			      distance = -1.0;
		// 求大圆劣弧长度
		distance = DEF_R * Math.acos(distance);
		return distance;
		}
	/**
	 * 获取两点之间的距离，公式：(Math.atan((p2.y - p1.y) / (p2.x - p1.x)) * 180 / Math.PI;
	 * @param x1 第一个点的x坐标
	 * @param y1 第一个点的y坐标
	 * @param x2 第二个点的x坐标
	 * @param y2 第二个点的y坐标
	 * @return 两点之间的角度
	 */
	public static double getTwoPointAngle(int x1,int y1,int x2,int y2){
		 //两点的x、y值
       int x = x2-x1;
       int y = y2-y1;
      double  hypotenuse = Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));
        //斜边长度
       double cos = x/hypotenuse;
       double radian = Math.acos(cos);
        //求出弧度
       double angle = 180/(Math.PI/radian);
        //用弧度算出角度        
        if (y<0) {
                angle = -angle;
        } else if ((y == 0) && (x<0)) {
                angle = 180;
        }
        return angle;
//		return Math.atan(((y2 - y1) / (x2 - x1))*180 / Math.PI);
	}
	/**
	 *  根据已知第一个点的坐标，角度，距离，求出第二个点 公式：x2=x1+距离*cos(角度)  y2=y1+距离*sin (角度)
	 * @param x 第一个点的x坐标
	 * @param y 第一个点的y坐标
	 * @param angle 两点之间的角度
	 * @param dislen 两点之间的距离
	 * @return 第二个点的坐标
	 */
	public static Point getSecondPoint(int x,int y,float angle, int dislen){
		Point point =new Point();
		double x1=(x+dislen*Math.cos(angle));
		double	y2=y+dislen*Math.sin(angle);
		point.x=(int) x1;
		point.y=(int) y2;
		return point;
	}
	/**
	 * 
	* @Title: getSecondPoint
	* @Description: 获取第三个点
	* @param @param x1 第一个点的x坐标
	* @param @param y1 第一个点的y坐标
	* @param @param x2 第二个点的x坐标
	* @param @param y2 第二个点的y坐标
	* @param @param dislen 距离
	* @param @return
	* @return Point
	* @throws
	 */
	public static Point getSecondPoint(int x1,int y1,int x2,int y2, int dislen){
		int angle=(int) getTwoPointAngle(x1, y1, x2, y2);
		Point point =new Point();
		double x=(x1+dislen*Math.cos(angle));
		double	y=y1+dislen*Math.sin(angle);
		point.x=(int) x;
		point.y=(int) y;
		return point;
	}
	/**
	 * 判断点是否在path绘制的区域内
	 * @param path
	 * @param p 判断的点
	 * @param screen_width
	 * @param screen_height
	 * @return
	 */
	public static boolean checkPointInPath(Path path,Point p,int screen_width,int screen_height)
    {
            Bitmap lookup = Bitmap.createBitmap(screen_width, screen_height, Bitmap.Config.ARGB_8888); 
            //do this so that regions outside any path have a default 
            //path index of 255 
//            lookup.eraseColor(0xFF000000); 
             
            Canvas canvas = new Canvas(lookup); 
            Paint paint = new Paint(); 
             
            //these are defaults, you only need them if reusing a Paint 
            paint.setAntiAlias(false); 
//            paint.setStyle(Paint.Style.FILL); 
            paint.setColor(Color.BLUE); // use only alpha value for color 0xXX000000 
            canvas.drawPath(path, paint);  
            if(p.y>=0&&p.y<(p.y+lookup.getHeight())){
            	   int pixel = lookup.getPixel(p.x, p.y); 
                   lookup.recycle();
                   return pixel==Color.BLUE; 
            }else{
            	lookup.recycle();
            	return false;
            }
         
    }
	/**
	 * 
	* @Title: checkPointInPath
	* @Description: 检查点是否在路径上
	* @param @param path 路径
	* @param @param p 点
	* @param @param lookup
	* @param @return
	* @return boolean
	* @throws
	 */
	public static boolean checkPointInPath(Path path,Point p,Bitmap lookup)
    {

            Canvas canvas = new Canvas(lookup); 
            Paint paint = new Paint(); 
            paint.setAntiAlias(false); 
            paint.setColor(Color.BLUE); // use only alpha value for color 0xXX000000 
            canvas.drawPath(path, paint);  
            if(p.y>=0&&p.y<(p.y+lookup.getHeight())){
            	   int pixel = lookup.getPixel(p.x, p.y); 
//                   lookup.recycle();
                   return pixel==Color.BLUE; 
            }else{
//            	lookup.recycle();
            	return false;
            }
         
    }
	/**
	 * 
	* @Title: dip2px
	* @Description: 将dip转换成像素px
	* @param @param context 上下文
	* @param @param dipValue dip的值
	* @param @return
	* @return int
	* @throws
	 */
	public static int dip2px(Context context, float dipValue){ 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int)(dipValue * scale + 0.5f); 
} 
/**
 * 
* @Title: px2dip
* @Description: 将像素px转换成dip
* @param @param context
* @param @param pxValue
* @param @return
* @return int
* @throws
 */
public static int px2dip(Context context, float pxValue){ 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int)(pxValue / scale + 0.5f); 
} 
/**
 * 获取两点到第三点的夹角。
 * 
 * @param x 第一个点 x
 * @param y 第一个点 y
 * @param x1 中心点x
 * @param y1 中心点y
 * @param x2 第二个点 x
 * @param y2 第二个点 y
 * @return
 */
public double getActionDegrees(float x, float y, float x1, float y1,
		float x2, float y2) {

	double a = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	double b = Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2));
	double c = Math.sqrt((x1 - x) * (x1 - x) + (y1 - y) * (y1 - y));
	// 余弦定理
	double cosA = (b * b + c * c - a * a) / (2 * b * c);
	// 返回余弦值为指定数字的角度，Math函数为我们提供的方法
	double arcA = Math.acos(cosA);
	double degree = arcA * 180 / Math.PI;

	// 接下来我们要讨论正负值的关系了，也就是求出是顺时针还是逆时针。
	// 第1、2象限
	if (y1 < y && y2 < y) {
		if (x1 < x && x2 > x) {// 由2象限向1象限滑动
			return degree;
		}
		// 由1象限向2象限滑动
		else if (x1 >= x && x2 <= x) {
			return -degree;
		}
	}
	// 第3、4象限
	if (y1 > y && y2 > y) {
		// 由3象限向4象限滑动
		if (x1 < x && x2 > x) {
			return -degree;
		}
		// 由4象限向3象限滑动
		else if (x1 > x && x2 < x) {
			return degree;
		}

	}
	// 第2、3象限
	if (x1 < x && x2 < x) {
		// 由2象限向3象限滑动
		if (y1 < y && y2 > y) {
			return -degree;
		}
		// 由3象限向2象限滑动
		else if (y1 > y && y2 < y) {
			return degree;
		}
	}
	// 第1、4象限
	if (x1 > x && x2 > x) {
		// 由4向1滑动
		if (y1 > y && y2 < y) {
			return -degree;
		}
		// 由1向4滑动
		else if (y1 < y && y2 > y) {
			return degree;
		}
	}

	// 在特定的象限内
	float tanB = (y1 - y) / (x1 - x);
	float tanC = (y2 - y) / (x2 - x);
	if ((x1 > x && y1 > y && x2 > x && y2 > y && tanB > tanC)// 第一象限
			|| (x1 > x && y1 < y && x2 > x && y2 < y && tanB > tanC)// 第四象限
			|| (x1 < x && y1 < y && x2 < x && y2 < y && tanB > tanC)// 第三象限
			|| (x1 < x && y1 > y && x2 < x && y2 > y && tanB > tanC))// 第二象限
		return -degree;
	return degree;
}
/**
 * 两点间的距离
 * 
 * @param x1
 * @param y1
 * @param x2
 * @param y2
 * @return
 */
public static double distance(double x1, double y1, double x2, double y2) {
	return Math.sqrt(Math.abs(x1 - x2) * Math.abs(x1 - x2)
			+ Math.abs(y1 - y2) * Math.abs(y1 - y2));
}

/**
 * 计算点a(x,y)的角度
 * 
 * @param x
 * @param y
 * @return
 */
public static double pointTotoDegrees(double x, double y) {
	return Math.toDegrees(Math.atan2(x, y));
}
/**
 * 1=30度 2=45度 4=60度
 * 
 * @param tan
 * @return
 */
private static float switchDegrees(float x, float y) {
	return (float)pointTotoDegrees(x, y);
}
/**
 * 两点间角度
 * @param a
 * @param b
 * @return
 */
public static float getDegrees(Point a, Point b) {
	float ax = a.x;// a.index % 3;
	float ay = a.y;// a.index / 3;
	float bx = b.x;// b.index % 3;
	float by = b.y;// b.index / 3;
	float degrees = 0;
	if (bx == ax) // y轴相等 90度或270
	{
		if (by > ay) // 在y轴的下边 90
		{
			degrees = 90;
		} else if (by < ay) // 在y轴的上边 270
		{
			degrees = 270;
		}
	} else if (by == ay) // y轴相等 0度或180
	{
		if (bx > ax) // 在y轴的下边 90
		{
			degrees = 0;
		} else if (bx < ax) // 在y轴的上边 270
		{
			degrees = 180;
		}
	} else {
		if (bx > ax) // 在y轴的右边 270~90
		{
			if (by > ay) // 在y轴的下边 0 - 90
			{
				degrees = 0;
				degrees = degrees
						+ switchDegrees(Math.abs(by - ay),
								Math.abs(bx - ax));
			} else if (by < ay) // 在y轴的上边 270~0
			{
				degrees = 360;
				degrees = degrees
						- switchDegrees(Math.abs(by - ay),
								Math.abs(bx - ax));
			}

		} else if (bx < ax) // 在y轴的左边 90~270
		{
			if (by > ay) // 在y轴的下边 180 ~ 270
			{
				degrees = 90;
				degrees = degrees
						+ switchDegrees(Math.abs(bx - ax),
								Math.abs(by - ay));
			} else if (by < ay) // 在y轴的上边 90 ~ 180
			{
				degrees = 270;
				degrees = degrees
						- switchDegrees(Math.abs(bx - ax),
								Math.abs(by - ay));
			}

		}

	}
	return degrees;
}
/**
 * 点在圆肉
 * 
 * @param sx
 * @param sy
 * @param r
 * @param x
 * @param y
 * @return
 */
public static boolean checkInRound(float sx, float sy, float r, float x,
		float y) {
	return Math.sqrt((sx - x) * (sx - x) + (sy - y) * (sy - y)) < r;
}
/**
 * 两点间距离
 * @param p1x
 * @param p1y
 * @param p2x
 * @param p2y
 * @return
 */
private static Double getLenWithPoints(double p1x, double p1y, double p2x, double p2y) {
	Double length = null;
	length = Math.sqrt(Math.pow(p2x - p1x, 2) + Math.pow(p2y - p1y, 2));
	return length;
}
/**
 * 获取点到线的长度
 * @param lx1
 * @param ly1
 * @param lx2
 * @param ly2
 * @param px
 * @param py
 * @return
 */
public static Double getPoint2LineLength(double lx1, double ly1, double lx2,
		double ly2, double px, double py) {
	Double length = null;
	double b = getLenWithPoints(lx1, ly1, px, py);
	double c = getLenWithPoints(lx2, ly2, px, py);
	double a = getLenWithPoints(lx1, ly1, lx2, ly2);

	if (c + b == a) {// 点在线段上
		length = (double) 0;
	} else if (c * c >= a * a + b * b) { // 组成直角三角形或钝角三角形，投影在point1延长线上，
		length = b;
	} else if (b * b >= a * a + c * c) {// 组成直角三角形或钝角三角形，投影在point2延长线上，
		length = c;
	} else {
		// 组成锐角三角形，则求三角形的高
		double p = (a + b + c) / 2;// 半周长
		double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));// 海伦公式求面积
		length = 2 * s / c;// 返回点到线的距离（利用三角形面积公式求高）
	}
	return length;
}
/**
 * 判断点是否在path范围内
 * @param path
 * @param p
 * @return
 */
public static boolean isPointInPath(Path path,Point p){
	if(path==null||p==null){return false;}
	Region re=new Region();
	RectF r=new RectF();
	//计算控制点的边界
	path.computeBounds(r, true);
	//设置区域路径和剪辑描述的区域
	re.setPath(path, new Region((int)r.left,(int)r.top,(int)r.right,(int)r.bottom));
	if(re.contains(p.x, p.y)){
		return true;
	}else{
		return false;
	}
}
/**
 * 判断两个点组成的先是水平线、垂直线、斜线
 * @param x1
 * @param y1
 * @param x2
 * @param y2
 * @return 0：两个点重合，1：垂直线，2：水平线，3：斜线
 */
public static int checkLineOri(int x1,int y1,int x2,int y2){
	if((x1==x2)&&(y1==y2)){
		return 0;
	}else if(x1==x2){
		return 1;
	}else if(y1==y2){
		return 2;
	}else{
		return 3;
	}
}

/**
* @Title: getCirclePoint
* @Description: 已知圆的中点，半径、角度，求圆上的点
* @param CircleCenterX 中点x坐标
* @param CircleCenterY 中点y坐标
* @param r 圆的半径
* @param angle 圆心点到圆的线角度
* @return Point 圆上的点
* @throws
 */
public static Point getCirclePoint(int CircleCenterX,int CircleCenterY,int r,float angle){
	double x1   =   CircleCenterX   +   r   *   Math.cos(angle   *   3.14   /180) ;
	double y1   =   CircleCenterY   +   r   *   Math.sin(angle   *   3.14   /180) ;
	return new Point((int)x1,(int)y1);
}

}
