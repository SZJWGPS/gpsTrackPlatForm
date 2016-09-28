package com.hengdian.utils;

public class GadgetUtils {	
	
	/**
	 * 对一个整型数组的元素从大到小进行排序，并返回根据现在位置顺序排序的原来脚标的数组
	 * 
	 * @param arr
	 * 
	 */
	public static int[] sortIntArrAndGetOldIndex(int[] arr) {

		int i, j, index, tmp, arrLength = arr.length;
		int[] oldIndexArr = new int[arrLength];
		for(i = 0; i < arrLength; i++){
			oldIndexArr[i] = i;
		}
		
		for (i = 0; i < arrLength - 1; i++) {
			index = i;
			for (j = i + 1; j < arrLength; j++) {
				if (arr[index] > arr[j]) {
					index = j;
				}
			}			
			if (i != index) {
				tmp = arr[i];
				arr[i] = arr[index];
				arr[index] = tmp;
				
				tmp = oldIndexArr[i];
				oldIndexArr[i] = oldIndexArr[index];
				oldIndexArr[index] = tmp;
			}
		}
		return oldIndexArr;
	}
	
	/**
	 * 对图表的横坐标名称集合和纵坐标值数组，按照纵坐标的值，从大到小排序
	 * 
	 * @param arr
	 * @param arrStr
	 */
	public static void sortIntArrAndStringArr(int[] arr, String[] arrStr) {

		int i, j, index, tmp, arrLength = arr.length;
		for (i = 0; i < arrLength - 1; i++) {
			index = i;
			for (j = i + 1; j < arrLength; j++) {
				if (arr[index] > arr[j]) {
					index = j;
				}
			}
			if (i != index) {
				tmp = arr[i];
				arr[i] = arr[index];
				arr[index] = tmp;
				String strTmp = arrStr[i];
				arrStr[i] = arrStr[index];
				arrStr[index] = strTmp;
			}
		}
	}
}
