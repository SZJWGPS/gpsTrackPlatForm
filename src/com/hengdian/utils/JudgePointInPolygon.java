package com.hengdian.utils;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import com.hengdian.entity.LoLatitude;

public class JudgePointInPolygon {

	/**
	 * 判断当前位置是否在围栏内
	 * 
	 * @param mobilelocationEntity
	 * @param enclosureList
	 * @return
	 */
	public static boolean isInPolygon(LoLatitude point, List<LoLatitude> enclosureList) {
		double p_x = point.longitude;
		double p_y = point.latitude;
		Point2D.Double point0 = new Point2D.Double(p_x, p_y);

		List<Point2D.Double> pointList = new ArrayList<Point2D.Double>();

		for (LoLatitude enclosure : enclosureList) {
			double polygonPoint_x = enclosure.longitude;
			double polygonPoint_y = enclosure.latitude;
			Point2D.Double polygonPoint = new Point2D.Double(polygonPoint_x, polygonPoint_y);
			pointList.add(polygonPoint);
		}
		JudgePointInPolygon test = new JudgePointInPolygon();
		return test.checkWithJdkGeneralPath(point0, pointList);
	}

	/**
	 * 返回一个点是否在一个多边形区域内
	 * 
	 * @param point
	 * @param polygon
	 * @return
	 */
	// 方法一
	public boolean checkWithJdkGeneralPath(Point2D.Double point, List<Point2D.Double> polygon) {
		GeneralPath p = new GeneralPath();

		Point2D.Double first = polygon.get(0);
		p.moveTo(first.x, first.y);
		polygon.remove(0);

		for (Point2D.Double d : polygon) {
			p.lineTo(d.x, d.y);
		}
		p.lineTo(first.x, first.y);
		p.closePath();

		return p.contains(point);

	}

	// 方法二
	public boolean checkWithJdkPolygon(Point2D.Double point, List<Point2D.Double> polygon) {
		Polygon p = new Polygon();
		final int TIMES = 1000000;
		for (Point2D.Double d : polygon) {
			int x = (int) d.x * TIMES;
			int y = (int) d.y * TIMES;
			p.addPoint(x, y);
		}
		int x = (int) point.x * TIMES;
		int y = (int) point.y * TIMES;
		return p.contains(x, y);
	}

	// ============================================================
	/**
	 * 
	 * @param px
	 * @param py
	 * @param polygonXA
	 * @param polygonYA
	 * @return
	 */
	public boolean isPointInPolygon(double px, double py, ArrayList<Double> polygonXA, ArrayList<Double> polygonYA) {
		boolean isInside = false;
		double ESP = 1e-9;
		int count = 0;
		double linePoint1x;
		double linePoint1y;
		double linePoint2x = 180;
		double linePoint2y;

		linePoint1x = px;
		linePoint1y = py;
		linePoint2y = py;

		for (int i = 0; i < polygonXA.size() - 1; i++) {
			double cx1 = polygonXA.get(i);
			double cy1 = polygonYA.get(i);
			double cx2 = polygonXA.get(i + 1);
			double cy2 = polygonYA.get(i + 1);
			if (isPointOnLine(px, py, cx1, cy1, cx2, cy2)) {
				return true;
			}
			if (Math.abs(cy2 - cy1) < ESP) {
				continue;
			}

			if (isPointOnLine(cx1, cy1, linePoint1x, linePoint1y, linePoint2x, linePoint2y)) {
				if (cy1 > cy2)
					count++;
			} else if (isPointOnLine(cx2, cy2, linePoint1x, linePoint1y, linePoint2x, linePoint2y)) {
				if (cy2 > cy1)
					count++;
			} else if (isIntersect(cx1, cy1, cx2, cy2, linePoint1x, linePoint1y, linePoint2x, linePoint2y)) {
				count++;
			}
		}
		System.out.println(count);
		if (count % 2 == 1) {
			isInside = true;
		}
		return isInside;
	}

	public double Multiply(double px0, double py0, double px1, double py1, double px2, double py2) {
		return ((px1 - px0) * (py2 - py0) - (px2 - px0) * (py1 - py0));
	}

	// 判断点是否在边上
	public boolean isPointOnLine(double px0, double py0, double px1, double py1, double px2, double py2) {
		boolean flag = false;
		double ESP = 1e-9;
		if ((Math.abs(Multiply(px0, py0, px1, py1, px2, py2)) < ESP) && ((px0 - px1) * (px0 - px2) <= 0)
				&& ((py0 - py1) * (py0 - py2) <= 0)) {
			flag = true;
		}
		return flag;
	}

	// 判断是否相交
	public boolean isIntersect(double px1, double py1, double px2, double py2, double px3, double py3, double px4,
			double py4) {
		boolean flag = false;
		double d = (px2 - px1) * (py4 - py3) - (py2 - py1) * (px4 - px3);
		if (d != 0) {
			double r = ((py1 - py3) * (px4 - px3) - (px1 - px3) * (py4 - py3)) / d;
			double s = ((py1 - py3) * (px2 - px1) - (px1 - px3) * (py2 - py1)) / d;
			if ((r >= 0) && (r <= 1) && (s >= 0) && (s <= 1)) {
				flag = true;
			}
		}
		return flag;
	}

	// =============================================================================
	/**
	 * 求解通过该点的水平线与多边形各边的交点，单边交点为奇数，则成立。其实就是需要看这个点的单边射线与多边形的交点
	 * 
	 * @param point
	 * @param points
	 * @param verticesCount
	 *            顶点数
	 * @return
	 */
	public boolean isInPolygon(Point point, Point[] points, int verticesCount) {
		int nCross = 0;
		for (int i = 0; i < verticesCount; i++) {
			Point p1 = points[i];
			Point p2 = points[(i + 1) % verticesCount];
			// 求解 y=p.y 与 p1 p2 的交点
			// p1p2 与 y=p0.y平行
			if (p1.y == p2.y)
				continue;
			// 交点在p1p2延长线上
			if (point.y < Math.min(p1.y, p2.y))
				continue;
			// 交点在p1p2延长线上
			if (point.y >= Math.max(p1.y, p2.y))
				continue;
			// 求交点的 X 坐标
			double x = (double) (point.y - p1.y) * (double) (p2.x - p1.x) / (double) (p2.y - p1.y) + p1.x;
			// 只统计单边交点
			if (x > point.x)
				nCross++;
		}
		return (nCross % 2 == 1);
	}

}
